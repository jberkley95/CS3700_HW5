import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author John Berkley
 * CPP Class: CS3700
 * Date Created: Nov 05, 2018
 */
public class PCLocks {
    public static void main(String[] args) {
        final ReentrantLock lock = new ReentrantLock();
        final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

        Runnable producer = () -> {
            try {
                for (int i = 0; i < 100; i++) {
                    lock.lock();
                    queue.put(i);
                    System.out.println(Thread.currentThread().getName() + " Produced " + i);
                    lock.unlock();
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
                    } else if (!lock.isLocked() && queue.peek() == null) {
                        return;
                    }
                }
            } catch (InterruptedException ignored) {
            }
        };

        final int NUM_PRODUCERS = 2, NUM_CONSUMERS = 5;
        for (int i = 0; i < NUM_PRODUCERS; i++) {
            new Thread(producer).start();
        }
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            new Thread(consumer).start();
        }
    }
}
