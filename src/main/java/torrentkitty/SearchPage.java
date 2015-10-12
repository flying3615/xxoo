package torrentkitty;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by centling on 2015/10/5.
 */
public class SearchPage {


    public static void main(String[] args) throws IOException {
        //第一会所
        String url = "http://www.torrentkitty.net/search/%E7%AC%AC%E4%B8%80%E4%BC%9A%E6%89%80/";
        Document html = Jsoup.connect(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36")
                .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .timeout(0)
                .get();
        Element body = html.body();
        Elements as = body.getElementsByAttributeValue("rel","magnet");
        for(int k=0;k<as.size();k++){
            if("Open".equals(as.get(k).text())){
                String targetURL = as.get(k).attr("href");
                String title = as.get(k).attr("title");
                System.out.println(targetURL);
                if(k%15==0&&k!=0){
                    System.out.println("----------------------------------------------");
                }
            }
        }
    }
}
