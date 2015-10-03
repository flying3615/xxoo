import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by centling on 2015/10/3.
 */
public class HtmlActor extends UntypedActor {
    static String[] userAgents = {
            "Mozilla/5.0",
            "Mozilla/4.0",
            "Windows NT 5.1",
            "MSIE 7.0",
            "Opera/9.80",
            "Windows Phone OS 7.5",
            "Trident/5.0",
            "hpwOS/3.0.0",
            "Windows NT 6.1"
    };



    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            System.out.println("HtmlActor Actor:" + getSelf() + "|| parsing page" + message);
            int v = (int) (Math.random() * (userAgents.length));
            Document html = Jsoup.connect((String) message).userAgent(userAgents[v]).get();
            Element body = html.body();
            Element navi = body.select("a.previous-comment-page").first();
            String next = navi.attr("href");

            getSender().tell(next, getSelf());

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
}
