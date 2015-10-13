package future;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;


/**
 * Created by centling on 2015/10/13.
 */
public class Main {

    public static void main(String[] args){

        ActorSystem actorSystem = ActorSystem.create();
        ActorRef answerActor = actorSystem.actorOf(Props.create(AnswerActor.class), "AnswerActor");
        //time out need to be greater than answer actor' future process time
        //so that it can be successful
        Timeout timeout = new Timeout(Duration.create(2,"seconds"));
        String message = "Hello";
        Future<Object> future = Patterns.ask(answerActor, message, timeout);
        try {
            future.onSuccess(new PrintResult(), actorSystem.dispatcher());
            future.onFailure(new PrintException(), actorSystem.dispatcher());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
