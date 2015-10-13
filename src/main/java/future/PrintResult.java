package future;

import akka.dispatch.OnSuccess;

/**
 * Created by centling on 2015/10/13.
 */
public class PrintResult extends OnSuccess {
    @Override
    public void onSuccess(Object result) throws Throwable {
        System.out.println(result);
    }
}
