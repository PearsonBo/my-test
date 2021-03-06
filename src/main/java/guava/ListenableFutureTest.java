package guava;

import com.google.common.util.concurrent.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * 添加回调
 * Created by hujianbo on 2018/io1/25.
 */
public class ListenableFutureTest {

    public static void main(String[] args) {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
        ListenableFuture<String> future = service.submit(() -> {
            System.out.println("call exe..");
            return doSubmit();
        });
        future.addListener(() -> {
            try {
                System.out.println(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }, service);

        Futures.addCallback(future, new FutureCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("success");
            }

            @Override
            public void onFailure(Throwable t) {
                try {
                    String s = future.get();
                    System.out.println("-----" + s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                System.out.println("failure:" + t.getMessage());
            }
        }, service);

    }

    private static String doSubmit() {
        int i = 1/0;
        return "task success!";
    }
}
