package torrentkitty;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by centling on 2015/10/5.
 */
public class Main {

    static LinkedBlockingQueue<Result> queue = new LinkedBlockingQueue<>();


    public static void main(String[] args) throws IOException, InterruptedException {
        //第一会所
        String encodeRes = URLEncoder.encode("anjelica", "UTF-8");
        String url = "http://www.torrentkitty.net/search/" + encodeRes + "/";
        ActorSystem system = ActorSystem.create("torrentKittySystem");
        final Inbox inbox = Inbox.create(system);

        ActorRef SupervisorActor = system.actorOf(Props.create(SupervisorActor.class, queue), "supervisor");
        DownloadMessage downloadMessage = new DownloadMessage(url, 1);
        inbox.send(SupervisorActor, downloadMessage);


        new Thread(() -> {
            String fileAll = "title_url.txt";
            String urls = "url.txt";
            BufferedWriter out = null;
            BufferedWriter out2 = null;
            try {
                out = new BufferedWriter(new FileWriter(fileAll));
                out2 = new BufferedWriter(new FileWriter(urls));
                long count = 0;
                while (true) {
                    if(queue.isEmpty()){
                        TimeUnit.SECONDS.sleep(5);
                        //wait for 5 seconds then double check
                        if(queue.isEmpty())break;
                    }
                    Result result = queue.take();
                    out.write(result.title+"\n");
                    out.write(result.url+"\n");
                    out.write("-------------------------------------------------------------------------------------\n");
                    out2.write(result.url+"\n");
                    if((++count)%15==0&&count!=0){
                        out2.write("-------------------------------------------------------------------------------------\n");
                    }
                }
                //shutdown akka
                System.out.println("get url "+count);
                system.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                    out2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();


    }
}
