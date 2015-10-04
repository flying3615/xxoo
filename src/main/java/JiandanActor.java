import akka.actor.*;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static akka.actor.SupervisorStrategy.*;

/**
 * Created by centling on 2015/10/3.
 */
public class JiandanActor extends UntypedActor {

    int count = 0;

    int userAgentIndex = 0;

    String[] userAgents = {
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
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(2, Duration.create(5,
                TimeUnit.SECONDS),
                t -> {
                    if(t instanceof IOException){
                        System.out.println("********IOException occurred, restart all child actors*********");
                        int a = (userAgentIndex < userAgents.length - 1) ? (userAgentIndex += 1) : (userAgentIndex = 0);
                        System.out.println("re-pick a user agent from list "+userAgents[userAgentIndex]);
                        return restart();
                    }
                    System.out.printf("********cannot handle %s escalate it*********\n",t);
                    return escalate();
                },
                true);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if("start".equals(message)){
            ActorRef html = getContext().actorOf(Props.create(HtmlActor.class).withDispatcher("my-dispatcher"), "html");
            Message msg = new Message("http://jandan.net/ooxx","start",userAgents[userAgentIndex]);
            html.tell(msg,getSelf());
        }else if("save".equals(message)){
            System.out.println("download " + (++count) + " images");
        }else {
            Message msg = (Message) message;
            ActorRef html = getContext().actorOf(Props.create(HtmlActor.class));
            String url = msg.url;
            Message msg_rebuild = new Message(url, "restart", userAgents[userAgentIndex]);
            html.tell(msg_rebuild, getSelf());
        }
    }

}
