import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * Created by centling on 2015/10/3.
 */
public class Main {

    public static void main(String[] args){
        ActorSystem system = ActorSystem.create("JanDanSystem");
        ActorRef JiandanActor = system.actorOf(Props.create(JiandanActor.class),"jiandan");
        JiandanActor.tell("start",JiandanActor);
    }
}
