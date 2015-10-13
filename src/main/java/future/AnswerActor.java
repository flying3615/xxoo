package future;

import akka.actor.UntypedActor;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Futures;
import akka.pattern.Patterns;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by centling on 2015/10/13.
 */
public class AnswerActor extends UntypedActor {

    ExecutorService poll = Executors.newCachedThreadPool();
    ExecutionContext ec = ExecutionContexts.fromExecutorService(poll);

    @Override
    public void onReceive(Object message) throws Exception {
        String messageStr = (String)message;
        if("Hello".equals(messageStr)){
            Future<String> f1 = akka.dispatch.Futures.future(() -> {
                System.out.println("begin sleep 3 seconds");
                TimeUnit.SECONDS.sleep(3);
                System.out.println("after sleep 3 seconds");
                return "Hello from Future";
            },ec);

            //send futures back to sender
            Patterns.pipe(f1, ec).to(getSender());

        }
    }


    @Override
    public void postStop() throws Exception {
        poll.shutdown();
    }
}
