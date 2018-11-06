import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

/**
 * @author John Berkley
 * CPP Class: CS 3700
 * Date Created: Nov 03, 2018
 */
public class PrimeActor extends AbstractActor {

    private boolean[] isPrime;
    private int localPrime;
    private int N;
    private ActorRef manager;

    private PrimeActor(boolean[] isPrime, int localPrime, int N, ActorRef manager) {
        this.isPrime = isPrime;
        this.localPrime = localPrime;
        this.N = N;
        this.manager = manager;
    }

    static Props props(boolean[] isPrime, int localPrime, int N, ActorRef manager) {
        return Props.create(PrimeActor.class, () -> new PrimeActor(isPrime, localPrime, N, manager));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Begin.class, num -> {
                    boolean beginNextActor = false;

                    if (localPrime > Math.sqrt(N)) {
                        for (int i = localPrime; i <= N; i++) {
                            if (isPrime[i]) {
                                System.out.println(i);
                            }
                        }
                        manager.tell(new PrimeActor.End(), ActorRef.noSender());

                    } else if (localPrime != -1) {
                        for (int j = localPrime; localPrime * j <= N; j++) {
                            isPrime[localPrime * j] = false;

                            if (!beginNextActor && localPrime * j >= N / 2) {
                                ActorRef nextActor = getContext().actorOf(PrimeActor.props(isPrime, getNextLocalPrime(localPrime), N, manager));
                                nextActor.tell(new Begin(), ActorRef.noSender());
                                beginNextActor = true;
                            }
                        }
                    }
                }).build();
    }

    private int getNextLocalPrime(int currentLocalPrime) {
        for (int i = currentLocalPrime + 1; i <= N; i++) {
            if (isPrime[i]) {
                return i;
            }
        }

        return -1;
    }

    static class Begin {
    }

    private static class End {
    }
}