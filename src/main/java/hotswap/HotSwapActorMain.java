package hotswap;

import akka.actor.*;
import akka.japi.Procedure;

/**
 * Created by centling on 2015/10/13.
 */
public class HotSwapActorMain extends UntypedActor {

    Procedure<Object> angry = new Procedure<Object>() {
        @Override
        public void apply(Object message) {
            System.out.println("angry apply "+message);
            if (message.equals("bar")) {
                System.out.println("angry->bar->angry");
                getSender().tell("I am already angry?", getSelf());
            } else if (message.equals("foo")) {
                getContext().become(happy);
            }else{
                unhandled(message);
            }
        }
    };

    Procedure<Object> happy = new Procedure<Object>() {
        @Override
        public void apply(Object message) {
            System.out.println("happy apply "+message);
            if (message.equals("bar")) {
                System.out.println("happy->bar->happy");
                getSender().tell("I am already happy :-)", getSelf());
            } else if (message.equals("foo")) {
                getContext().become(angry);
            }else{
                unhandled(message);
            }
        }
    };

    @Override
    public void onReceive(Object message) throws Exception {
        if(message.equals("bar")){
            System.out.println("on receive bar");
            getContext().become(angry);
        }else if(message.equals("foo")){
            System.out.println("on receive foo");
            getContext().become(happy);
        }else{
            unhandled(message);
        }
    }


    public static void main(String[] args){
        ActorSystem actorSystem = ActorSystem.create();
        ActorRef HotSwapActor = actorSystem.actorOf(Props.create(HotSwapActorMain.class), "HotSwapActorMain");
        HotSwapActor.tell("bar",ActorRef.noSender());
        HotSwapActor.tell("bar", ActorRef.noSender());
    }
}
