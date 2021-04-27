import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class DiningTable {
    private static final int n = 5;
    private static final Philosopher[] philosophers = new Philosopher[n];
    private static final Fork[] forks = new Fork[n];
    private static final Thread[] threads = new Thread[n];

    private static final AtomicInteger counter = new AtomicInteger(0);


    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(n / 2);

        for (int i = 0; i < n; i++) {
            forks[i] = new Fork(DiningTable::print);
        }

        for (int i = 0; i < n; i++) {
            philosophers[i] = new Philosopher(semaphore, forks[i], forks[(i + 1) % n]);
        }

        for (int i = 0; i < n; i++) {
            threads[i] = new Thread(philosophers[i]);
        }

        for (int i = 0; i < n; i++) {
            threads[i].start();
        }
        for (int i = 0; i < n; i++) {
            threads[i].join();
        }

    }

    private static Void print() {
        StringBuilder sb = new StringBuilder(String.format("%03d. ", counter.getAndIncrement()));
        for (int i = 0; i < n; i++) {
            sb.append(forks[i].grabbed() ? "▮" : "▯");
            switch (philosophers[i].getEatState()) {
                case EATING:
                    sb.append("◍");
                    break;
                case EATEN:
                    sb.append("⬤");
                    break;
                default:
                    sb.append("○");
            }
        }
        sb.append(forks[0].grabbed() ? "▮" : "▯");
        System.out.println(sb);
        return null;
    }
}
