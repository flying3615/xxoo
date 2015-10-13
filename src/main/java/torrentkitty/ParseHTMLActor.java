package torrentkitty;

import akka.actor.UntypedActor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by centling on 2015/10/13.
 */
public class ParseHTMLActor extends UntypedActor {

    LinkedBlockingQueue<Result> queue = new LinkedBlockingQueue<>();

    public ParseHTMLActor(LinkedBlockingQueue<Result> queue) {
        this.queue = queue;
    }

    @Override
    public void onReceive(Object message) throws Exception {

        DownloadMessage target = (DownloadMessage) message;
        System.out.println("##########" + target.pageNum + "###########");
        Document html = Jsoup.connect(target.downloadURL + "/" + target.pageNum)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .timeout(0)
                .get();

        Element body = html.body();
        Elements as = body.getElementsByAttributeValue("rel", "magnet");
        for (int k = 0; k < as.size(); k++) {
            String targetURL = as.get(k).attr("href");
            String title = as.get(k).attr("title");
            queue.put(new Result(targetURL, title));
        }
        getContext().stop(getSelf());
    }
}
