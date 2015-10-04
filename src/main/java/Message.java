import java.io.Serializable;

/**
 * Created by centling on 2015/10/4.
 */
public class Message implements Serializable{

    final String url;
    final String status;
    final String userAgent;


    public Message(String url, String status, String userAgent) {
        this.url = url;
        this.status = status;
        this.userAgent = userAgent;
    }
}
