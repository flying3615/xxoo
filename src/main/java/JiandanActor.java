import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * Created by centling on 2015/10/3.
 */
public class JiandanActor extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception {
        System.out.println("JiandanActor receive a message "+message);
        if("start".equals(message)){
            ActorRef html = getContext().actorOf(Props.create(HtmlActor.class), "html");
            html.tell("http://jandan.net/ooxx",getSelf());
        }else{
            ActorRef html = getContext().actorOf(Props.create(HtmlActor.class));
            html.tell(message,getSelf());
        }
    }
}
