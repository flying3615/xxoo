package torrentkitty;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by centling on 2015/10/13.
 */
public class SupervisorActor extends UntypedActor {

    LinkedBlockingQueue<Result> queue = new LinkedBlockingQueue<>();

    public SupervisorActor(LinkedBlockingQueue<Result> queue){
        this.queue = queue;
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if(message instanceof DownloadMessage){
            DownloadMessage target = (DownloadMessage)message;
            Document html = Jsoup.connect(((DownloadMessage) message).downloadURL+"/"+target.pageNum)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36")
                    .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .timeout(0)
                    .get();


            Element body = html.body();
            Element divPagination = body.getElementsByClass("pagination").get(0);
            Elements as = divPagination.getElementsByTag("a");
            int totalPage = as.size()-2;
            Element totalPageTag = as.get(totalPage);
            String totalPageStr = totalPageTag.attr("href");
            if(totalPageStr.matches("\\d+")){
                Integer totalPageNum = Integer.parseInt(totalPageStr);
                for(int i=1;i<=totalPageNum;i++){
                    ActorRef ParseHTMLActor = getContext().actorOf(Props.create(ParseHTMLActor.class,queue), "nextpage"+i);
                    ParseHTMLActor.tell(new DownloadMessage(target.downloadURL,i),getSelf());
                }
            }

        }else{
            unhandled(message);
        }

    }
}
