package google;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URLEncoder;

/**
 * Created by centling on 2015/12/23.
 */
public class FirstTry {

    public static final String GOOGLE_PRFIX = "https://www.google.com";

    public static BufferedWriter out;

    public static void main(String[] args) throws Exception {

        System.setProperty("https.proxyHost", "localhost");
        System.setProperty("https.proxyPort", "1080");
        if(args.length==0){
            System.out.println("pls input a search word...");
            return;
        }
        out = new BufferedWriter(new FileWriter(args[0]+"_result"));
        String encodeRes = URLEncoder.encode(args[0], "UTF-8");
        String url = GOOGLE_PRFIX+"/search?q=" + encodeRes + "/";


        Document doc = Jsoup.connect(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36")
                .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .timeout(0)
                .get();

        Element table = doc.getElementById("nav");
        Elements pages = table.getElementsByClass("fl");

        for (Element a : pages) {
            System.out.println(a.attr("href"));
            getPage(GOOGLE_PRFIX + a.attr("href"));
            out.write("-----------------------------\n");
        }

        out.close();
    }

    public static void getPage(String url) throws IOException {
        Document doc = Jsoup.connect(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .timeout(0)
                .get();
        Elements results = doc.getElementsByAttributeValue("class", "g");

        for (Element result : results) {
            Element a_result = result.getElementsByTag("a").get(0);
            String result_url = a_result.attr("href");
            String result_title = a_result.text();
            System.out.println(result_title + " " + result_url);
            out.write(result_title + " " + result_url+"\n");
        }

    }


}
