package future;

import akka.dispatch.OnFailure;

/**
 * Created by centling on 2015/10/13.
 */
public class PrintException extends OnFailure {
    @Override
    public void onFailure(Throwable failure) throws Throwable {
        System.out.println("in on failure handler");
        System.out.println(failure);
    }

}
