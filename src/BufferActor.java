import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author John Berkley
 * CPP Class: CS 3700
 * Date Created: Nov 05, 2018
 */
public class BufferActor extends AbstractActor {

    static public Props props(int MAX_CAPACITY, int numProd, int numCons, ActorRef[] consumerActor) {
        return Props.create(BufferActor.class, () -> new BufferActor(MAX_CAPACITY, numProd, numCons, consumerActor));
    }

    static class RequestItems {}
    static class AllFinished {}

    final BlockingQueue<Integer> buffer;
    final int numProd, numCons;
    int counter;
    ActorRef[] consumerActor;

    public BufferActor(int MAX_CAPACITY, int numProd, int numCons, ActorRef[] consumerActor) {
        this.buffer = new ArrayBlockingQueue<>(MAX_CAPACITY);
        this.numProd = numProd;
        this.numCons = numCons;
        this.consumerActor = consumerActor;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PCActors.Start.class, b -> {

                })
                .match(ProducerActor.Done.class, foo -> {
                    counter++;

                    if (counter == numProd) {
                        for (ActorRef actorRef: consumerActor) {
                            actorRef.tell(new AllFinished(), ActorRef.noSender());
                        }
                    }
                })
                .build();
    }
}
