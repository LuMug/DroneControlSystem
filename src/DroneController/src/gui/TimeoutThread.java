package gui;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * This class allows the user to start a task with a timeout.
 * @author Luca Di Bello
 */
public class TimeoutThread {
    
    /**
     * ExecutorService object that manage the task execution into the thread.
     */
    private ExecutorService executor = Executors.newFixedThreadPool(4);
    
    /**
     * Thread timeout.
     */
    private final int TIMEOUT;
    
    /**
     * TimeUnit that specify the timeout time unit.
     */
    private final TimeUnit UNIT;
    
    /**
     * Default constructor.
     * 
     * @param timeout Timeout of the thread.
     * @param unit Timeout time unit.
     */
    public TimeoutThread(int timeout, TimeUnit unit) {
        this.TIMEOUT = timeout;
        this.UNIT = unit;
    }
    
    /**
     * This method executes send task in another thread with a specified timeout.
     * @param task SendTask to execute.
     */
    public void execute(SendTask task) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(String.format("[Executor] Started thread with %s %s timeout", TIMEOUT, UNIT));
                    List<Future<String>> future = executor.invokeAll(Arrays.asList(task), TIMEOUT, TimeUnit.SECONDS);
                    System.out.println("[Info] Ended " + future.get(0) + " task");
                } catch (InterruptedException ex) {
                    System.err.println("[Executor] Error detected. Message: " + ex.getMessage());
                }
            }
        }).start();
    }
}
