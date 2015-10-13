package future;

import akka.dispatch.OnFailure;

import java.util.concurrent.CountDownLatch;

/**
 * Created by centling on 2015/10/13.
 */
public class PrintException extends OnFailure {


    private CountDownLatch countDownLatch;

    public PrintException(CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void onFailure(Throwable failure) throws Throwable {
        System.out.println("PrintException:in on failure handler");
        System.out.println(failure);
        countDownLatch.countDown();
    }

}
