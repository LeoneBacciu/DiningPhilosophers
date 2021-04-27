import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public class Fork {
    private final Callable<Void> callback;
    private final AtomicBoolean _grabbed;

    public Fork(Callable<Void> callback) {
        this._grabbed = new AtomicBoolean();
        this.callback = callback;
    }

    public boolean grabbed() {
        return _grabbed.get();
    }

    public boolean grab() {
        boolean success = _grabbed.compareAndSet(false, true);
        if (success) print();
        return success;
    }

    public void drop() {
        _grabbed.set(false);
        print();
    }

    private void print() {
        try {
            callback.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
