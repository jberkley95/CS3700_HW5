import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.concurrent.BlockingQueue;

/**
 * @author John Berkley
 * CPP Class: CS 3700
 * Date Created: Nov 05, 2018
 */
public class ProducerActor extends AbstractActor {
    static public Props props(BlockingQueue<Integer> buffer, ActorRef bufferActor) {
        return Props.create(ProducerActor.class, () -> new ProducerActor(buffer, bufferActor));
    }

    static class Done {}
    static class Produced {}

    final BlockingQueue<Integer> buffer;
    final ActorRef bufferActor;

    public ProducerActor(BlockingQueue<Integer> buffer, ActorRef bufferActor) {
        this.buffer = buffer;
        this.bufferActor = bufferActor;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(BufferActor.RequestItems.class, p -> {
                    for (int i = 0; i < 100; i++) {
                        buffer.put(i);
                        System.out.println(Thread.currentThread().getName() + " Produced " + i);
                        bufferActor.tell(new Done(), ActorRef.noSender());

                    }
                    bufferActor.tell(new Done(), ActorRef.noSender());
                })
                .build();
    }
}
