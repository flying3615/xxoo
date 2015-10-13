package future;

import akka.dispatch.OnSuccess;

import java.util.concurrent.CountDownLatch;

/**
 * Created by centling on 2015/10/13.
 */
public class PrintResult extends OnSuccess {

    private CountDownLatch countDownLatch;

    public PrintResult(CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void onSuccess(Object result) throws Throwable {
        System.out.println("PrintResult:in on success handler");
        System.out.println(result);
        countDownLatch.countDown();
    }
}
