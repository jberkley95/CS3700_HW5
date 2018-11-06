/**
 * @author John Berkley
 * CPP Class: CS3700
 * Date Created: Nov 05, 2018
 */
public class PrimeSieve {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        int n = 1_000_000;

        boolean[] isPrime = new boolean[n + 1];
        for (int i = 2; i <= n; i++) {
            isPrime[i] = true;
        }

        for (int i = 2; i * i <= n; i++) {
            if (isPrime[i]) {
                for (int j = i; i * j <= n; j++) {
                    isPrime[i * j] = false;
                }
            }
        }

        for (int i = 2; i <= n; i++) {
            if (isPrime[i]) System.out.println(i);
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total Runtime: " + totalTime + " milliseconds.");
    }
}
