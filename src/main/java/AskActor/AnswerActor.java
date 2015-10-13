package AskActor;

import akka.actor.UntypedActor;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Futures;
import akka.pattern.Patterns;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by centling on 2015/10/13.
 */
public class AnswerActor extends UntypedActor {

    public AnswerActor(){
        getContext().setReceiveTimeout(Duration.create(1,TimeUnit.MILLISECONDS));
    }

    @Override
    public void onReceive(Object message) throws Exception {
        String messageStr = (String)message;
        if("Hello".equals(messageStr)){

            getSender().tell("AnswerActor: greeting from answer actor",getSelf());
        }else
            unhandled(message);
    }

    @Override
    public void postStop() throws Exception {
        System.out.println("AnswerActor: invoke stop");
    }
}
