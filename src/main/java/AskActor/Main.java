package AskActor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;


/**
 * Created by centling on 2015/10/13.
 */
public class Main {

    public static void main(String[] args){

        ActorSystem actorSystem = ActorSystem.create();
        ActorRef answerActor = actorSystem.actorOf(Props.create(AnswerActor.class), "AnswerActor");

        Timeout timeout = new Timeout(Duration.create(5,"seconds"));
        String message = "Hello";
        Future<Object> future = Patterns.ask(answerActor, message, timeout);
        try {
            String result = (String)Await.result(future,Duration.Inf());
            System.out.println("SwapperMain: get result " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            answerActor.tell(PoisonPill.getInstance(), null);
            System.out.printf("SwapperMain: get actor %s again after being killed\n", actorSystem.actorSelection("/user/AnswerActor"));
            actorSystem.shutdown();
        }
    }
}
