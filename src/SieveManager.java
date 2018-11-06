import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

/**
 * @author John Berkley
 * CPP Class: CS 3700
 * Date Created: Nov 03, 2018
 */
public class SieveManager extends AbstractActor {

    private boolean[] isPrime;
    private int N;

    static Props props() {
        return Props.create(SieveManager.class, SieveManager::new);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SieveActor.Start.class, start -> {
                    N = start.num;

                    this.isPrime = new boolean[N + 1];

                    for (int i = 2; i <= N; i++) {
                        isPrime[i] = true;
                    }

                    ActorRef prime = getContext().actorOf(PrimeActor.props(isPrime, 2, N, self()));

                    prime.tell(new PrimeActor.Begin(), ActorRef.noSender());
                })
                .build();
    }
}
