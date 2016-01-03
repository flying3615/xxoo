package google;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by centling on 2016/1/3.
 */
public class streamTest {
    public static void main(String[] args) throws IOException {

        File input = new File("E:\\project\\xxoo\\src\\main\\java\\google\\googleResult.html");
        Document doc = Jsoup.parse(input, "UTF-8");
        Elements results = doc.getElementsByAttributeValue("class", "g");

        results.stream()
                .map(result -> {
                            String[] res = {
                                    result.getElementsByTag("a").get(0).attr("href"),
                                    result.getElementsByTag("a").get(0).text()
                            };
                            return res;
                        }
                )
                .forEach(res -> System.out.println(res));
//
//        List<Integer> aa = Arrays.asList(1,2,3);
//
//        aa.stream().map(a -> a + 1).peek(System.out::println);

    }
}
