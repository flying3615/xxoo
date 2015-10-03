import akka.actor.UntypedActor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;

/**
 * Created by centling on 2015/10/3.
 */
public class PicActor extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String){
            boolean flag = download((String)message,"./img/");
            if(flag){
                System.out.printf("------download %s successfully------\n",message);
            }else{
                System.out.printf("------download %s failed------\n",message);
            }
        }
//        getSelf().tell();

    }

    private boolean download(String url,String dir) {
        try {
            URL httpurl = new URL(url);
            String fileName = getFileNameFromUrl(url);
            System.out.println(fileName);
            File f = new File(dir + fileName);
            FileUtils.copyURLToFile(httpurl, f);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String getFileNameFromUrl(String url){
        String name = new Long(System.currentTimeMillis()).toString() + ".X";
        int index = url.lastIndexOf("/");
        if(index > 0){
            name = url.substring(index + 1);
            if(name.trim().length()>0){
                return name;
            }
        }
        return name;
    }

//    public static void main(String[] args){
//        String url = "http://ww2.sinaimg.cn/mw600/792e1c7ajw1ew3ggc4hmsj20hs0qogo8.jpg";
//        download(url,"./img/");
//    }

}
