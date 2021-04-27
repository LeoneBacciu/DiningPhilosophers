import java.util.Random;
import java.util.concurrent.Semaphore;

public class Philosopher implements Runnable {
    public enum EatState {NO, EATING, EATEN};

    private final Random random = new Random();
    private final Semaphore semaphore;
    private final Fork leftFork, rightFork;
    private boolean leftGrabbed = false, rightGrabbed = false;
    private EatState eatState = EatState.NO;

    public Philosopher(Semaphore semaphore, Fork leftFork, Fork rightFork) {
        this.semaphore = semaphore;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(random.nextInt(1000));
            boolean success;
            do {

                semaphore.acquire();

                success = grabForks();

                if (success) eat();

                dropForks();

                semaphore.release();

            } while (!success);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public EatState getEatState() {
        return eatState;
    }

    private boolean grabForks() throws InterruptedException {
        boolean success;
        if (random.nextBoolean()) {
            success = grabLeft() && grabRight();
        } else {
            success = grabRight() && grabLeft();
        }
        return success;
    }


    private boolean grabLeft() throws InterruptedException {
        Thread.sleep(random.nextInt(125));
        boolean success = leftFork.grab();
        leftGrabbed = success;
        Thread.sleep(random.nextInt(125));
        return success;
    }

    private boolean grabRight() throws InterruptedException {
        Thread.sleep(random.nextInt(125));
        boolean success = rightFork.grab();
        rightGrabbed = success;
        Thread.sleep(random.nextInt(125));
        return success;
    }

    private void dropForks() {
        if (rightGrabbed) rightFork.drop();
        if (leftGrabbed) leftFork.drop();
        leftGrabbed = rightGrabbed = false;
    }

    private void eat() throws InterruptedException {
        eatState = EatState.EATING;
        Thread.sleep(random.nextInt(1000));
        eatState = EatState.EATEN;
    }
}
