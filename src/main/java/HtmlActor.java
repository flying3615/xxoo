import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by centling on 2015/10/3.
 */
public class HtmlActor extends UntypedActor {


    @Override
    public void postRestart(Throwable reason) throws Exception {

        super.postRestart(reason);
    }


    @Override
    public void onReceive(Object message) throws IOException {
        Message msg = (Message) message;
        System.out.println("HtmlActor Actor:" + getSelf() + "|| parsing page" + message);
        Document html = null;
        try {
            html = Jsoup.connect(msg.url).userAgent(msg.userAgent).get();
        } catch (IOException e) {
            System.out.println("exception message=" + msg.url);
            getSender().tell(new Message(msg.url, "restart", msg.userAgent), getSelf());
            throw e;
        }
        Element body = html.body();
        Element navi = body.select("a.previous-comment-page").first();
        String next = navi.attr("href");

        getSender().tell(new Message(next, "continue", msg.userAgent), getSelf());

        Elements comments = body.select("ol.commentlist li");
        for (int i = 0; i < comments.size(); i++) {
            Element e = comments.get(i);
            if (!e.attr("id").isEmpty()) {
                Elements p = e.select("p");
                if (!p.isEmpty()) {
                    Element img = p.first().select("img").first();
                    if (img != null) {
                        String imgsrc = img.attr("src");
                        ActorRef downActor = getContext().actorOf(Props.create(PicActor.class));
                        downActor.tell(imgsrc, getSelf());
                    }
                }
            }
        }
        System.out.printf("-------url %s-------\n", message);
    }
}
