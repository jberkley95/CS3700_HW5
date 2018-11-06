import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import java.io.IOException;

/**
 * @author John Berkley
 * CPP Class: CS 3700
 * Date Created: Nov 03, 2018
 */
public class SieveActor {
    static long endTime;

    public static void main(String[] args) {
        final ActorSystem sieveSystem = ActorSystem.create("Sieve");
        final int N = 1_000_000;

        ActorRef sManager = sieveSystem.actorOf(SieveManager.props(), "Manager");

        try {
            long startTime = System.nanoTime();
            sManager.tell(new Start(N), ActorRef.noSender());
            System.in.read();
            long totalTime = endTime - startTime;
            System.out.printf("Total Runtime: %.2f milliseconds.", totalTime * 1e-6);
        } catch (IOException ignored) {
        } finally {
            sieveSystem.terminate();
        }
    }

    static class Start {
        int num;

        Start(int num) {
            this.num = num;
        }
    }
}