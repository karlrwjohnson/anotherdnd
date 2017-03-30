package anotherdnd.view.util;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Problem: Observable is a class because only classes can store data.
 * Solution: Store data in the interface??!?
 * 
 * @param <T> Class of the observable object
 * @param <E> Class of the notifying event
 * 
 * NOTE that as will ModelSync, the observer MAY NOT hold a reference to the observed object:
 * Please use the injected reference from the lambda; otherwise, the observed object will never be garbage-collected.
 */
public interface IObservable<T extends IObservable, E> {

    /**
     * Callback storage.
     * - WeakHashMap so that the garbage collector can still clean things up.
     * - Implicitly static so IntelliJ will shut up about unnecessary keywords
     * - Name starts with an underscore because interfaces cannot have real "private" fields or methods
     *   so I'm pretending it's Python
     */
    WeakHashMap<IObservable, Set<Object>> _observers = new WeakHashMap<>();

    default void observe(BiConsumer<T, E> observer) {
        _observers.computeIfAbsent(this, x -> new HashSet<>())
                .add(observer);
    }

    default void observe(Consumer<T> observer) {
        _observers.computeIfAbsent(this, x -> new HashSet<>())
                .add(observer);
    }

    default void observe(Runnable observer) {
        _observers.computeIfAbsent(this, x -> new HashSet<>())
                .add(observer);
    }

    default void unObserve(BiConsumer<IObservable, Object> observer) {
        _observers.getOrDefault(this, Collections.emptySet())
                .remove(observer);
    }

    default void unObserveAll() {
        _observers.remove(this);
    }

    default void countObservers() {
        _observers.getOrDefault(this, Collections.emptySet())
                .size();
    }

    default void notifyObservers(E arg) {
        _observers.getOrDefault(this, Collections.emptySet())
                .forEach(observer -> {
                    if (observer instanceof BiConsumer) {
                        //noinspection unchecked
                        ((BiConsumer<IObservable, E>) observer).accept(this, arg);
                    } else if (observer instanceof Consumer) {
                        //noinspection unchecked
                        ((Consumer<IObservable>) observer).accept(this);
                    } else if (observer instanceof Runnable) {
                        ((Runnable) observer).run();
                    }
                });
    }

    default void notifyObservers() {
        _observers.getOrDefault(this, Collections.emptySet())
                .forEach(observer -> {
                    if (observer instanceof BiConsumer) {
                        //noinspection unchecked
                        ((BiConsumer<IObservable, E>) observer).accept(this, null);
                    } else if (observer instanceof Consumer) {
                        //noinspection unchecked
                        ((Consumer<IObservable>) observer).accept(this);
                    } else if (observer instanceof Runnable) {
                        ((Runnable) observer).run();
                    }
                });
    }
}
