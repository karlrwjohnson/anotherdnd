package anotherdnd.model.util;

import java.util.HashSet;
import java.util.function.Consumer;

public class SimpleObservable {
    private final HashSet<Object> callbacks = new HashSet<>();
    private boolean firingObservers = false;

    public void observe(Runnable callback) {
        callbacks.add(callback);
    }

    public void observe(Consumer<SimpleObservable> callback) {
        callbacks.add(callback);
    }

    @SuppressWarnings("unchecked")
    public void fireObservers() {
        if (!firingObservers) try {
            firingObservers  = true;
            for (Object callback : callbacks) {
                if (callback instanceof Runnable) ((Runnable) callback).run(); else
                if (callback instanceof Consumer) ((Consumer) callback).accept(this);
            }
        } finally {
            firingObservers = false;
        }
    }
}