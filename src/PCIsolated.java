import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author John Berkley
 * CPP Class: CS3700
 * Date Created: Nov 05, 2018
 */
public class PCIsolated {
    public static void main(String[] args) {
        final int NUM_PRODUCERS = 2, NUM_CONSUMERS = 5;
        final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

        Runnable producer = () -> {
            try {
                for (int i = 0; i < 100; i++) {
                    queue.put(i);
                    System.out.println(Thread.currentThread().getName() + " Produced " + i);


                }
            } catch (InterruptedException ignored) {
            }
        };
        Runnable consumer = () -> {
            try {
                while (true) {
                    Integer queueElement = queue.poll(1, TimeUnit.SECONDS);
                    if (queueElement != null) {
                        System.out.println(Thread.currentThread().getName() + " Consumed : " + queueElement);
                        Thread.sleep(1000);
                    } else if (queue.peek() == null) {
                        return;
                    }
                }
            } catch (InterruptedException ignored) {
            }
        };

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < NUM_PRODUCERS; i++) {
            new Thread(producer).start();
        }
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            new Thread(consumer).start();
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total Runtime: " + totalTime + " milliseconds.");
    }
}
