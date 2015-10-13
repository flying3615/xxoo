package torrentkitty;

import org.jsoup.nodes.Document;

/**
 * Created by centling on 2015/10/13.
 */
public class DownloadMessage {
    final String downloadURL;
    final int pageNum;

    public DownloadMessage(String downloadURL,int pageNum) {
        this.downloadURL = downloadURL;
        this.pageNum = pageNum;
    }


}
