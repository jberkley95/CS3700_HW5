import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author John Berkley
 * CPP Class: CS 3700
 * Date Created: Nov 05, 2018
 */
public class ConsumerActor extends AbstractActor {
    static public Props props(BlockingQueue buffer, ActorRef bufferActor) {
        return Props.create(ConsumerActor.class, () -> new ConsumerActor(buffer, bufferActor));
    }

    static class Done {}
    static class Ready {}

    final BlockingQueue<Integer> buffer;
    final ActorRef bufferActor;

    public ConsumerActor(BlockingQueue<Integer> buffer, ActorRef bufferActor) {
        this.buffer = buffer;
        this.bufferActor = bufferActor;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ProducerActor.Produced.class, foo -> {
                    try {
                        while (!buffer.isEmpty()) {
                            Integer queueElement = buffer.poll(1, TimeUnit.SECONDS);
                            if (queueElement != null) {
                                System.out.println(Thread.currentThread().getName() + " Consumed : " + queueElement);
                                Thread.sleep(1000);
                            }
                        }
                    } catch (InterruptedException ignored) {}
                })
                .match(BufferActor.AllFinished.class, foo -> {
                    PCActors.endTime = System.currentTimeMillis();
                })
                .build();
    }
}
