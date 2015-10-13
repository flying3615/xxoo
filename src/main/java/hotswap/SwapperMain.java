package hotswap;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;


/**
 * Created by centling on 2015/10/13.
 */
public class SwapperMain {

    public static class Swap {

        public static Swap SWAP = new Swap();

        private Swap() {
        }
    }

    public static class Swapper extends UntypedActor {
        LoggingAdapter log = Logging.getLogger(getContext().system(), this);

        public void onReceive(Object message) {
            if (message == Swap.SWAP) {
                log.info("Hi");
                getContext().become(message1 -> {
                    log.info("Ho");
                    getContext().unbecome(); // resets the latest 'become'
                }, false); // this signals stacking of the new behavior
            } else {
                unhandled(message);
            }
        }
    }

    public static void main(String... args) {
        ActorSystem system = ActorSystem.create("MySystem");
        ActorRef swap = system.actorOf(Props.create(Swapper.class));
        swap.tell(Swap.SWAP, ActorRef.noSender()); // logs Hi
        swap.tell(Swap.SWAP, ActorRef.noSender()); // logs Ho
        swap.tell(Swap.SWAP, ActorRef.noSender()); // logs Hi
        swap.tell(Swap.SWAP, ActorRef.noSender()); // logs Ho
        swap.tell(Swap.SWAP, ActorRef.noSender()); // logs Hi
        swap.tell(Swap.SWAP, ActorRef.noSender()); // logs Ho
//        system.shutdown();
    }
}
