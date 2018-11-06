import akka.actor.*;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author John Berkley
 * CPP Class: CS 3700
 * Date Created: Nov 05, 2018
 */
public class PCActors {
    static long endTime;

    static class Start {};

    public static void main(String[] args) throws IOException {
        final ActorSystem pcSystem = ActorSystem.create("PC");
        final int MAX_CAPACITY = 10;
        final int NUM_PRODUCERS = 5;
        final int NUM_CONSUMERS = 2;
        ActorRef[] producerActors = new ActorRef[NUM_PRODUCERS];
        ActorRef[] consumerActors = new ActorRef[NUM_CONSUMERS];
        final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

        long startTime = System.currentTimeMillis();

        ActorRef buffer = pcSystem.actorOf(BufferActor.props(MAX_CAPACITY, NUM_PRODUCERS, NUM_CONSUMERS, consumerActors));

        for (int i = 0; i < NUM_PRODUCERS; i++) {
            producerActors[i] = pcSystem.actorOf(ProducerActor.props(queue, buffer));
        }

        for (int i = 0; i < NUM_CONSUMERS; i++) {
            consumerActors[i] = pcSystem.actorOf(ConsumerActor.props(queue, buffer));
        }

        buffer.tell(new Start(), ActorRef.noSender());

        System.in.read();
        long totalTime = endTime - startTime;
        System.out.println("Total Runtime: " + totalTime + " milliseconds.");
    }
}
