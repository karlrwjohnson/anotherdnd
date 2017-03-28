package anotherdnd.view.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/** Functional interfaces and functions which are capable of handling checked exceptions **/
public interface UnsafeFunctional {
    /**
     * Wrapper around checked exception-throwing lambdas when you know they couldn't possibly throw a checked exception.
     * Theoretically irresponsible, but practically beneficial.
     */
    static <T, R> Function<T, R> carefully(UnsafeFunction<T, R> unsafeFunction) {
        return param -> {
            try {
                return unsafeFunction.get(param);
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * Wrapper around checked exception-throwing lambdas when you know they couldn't possibly throw a checked exception.
     * Theoretically irresponsible, but practically beneficial.
     */
    static <R> Supplier<R> carefully(UnsafeSupplier<R> unsafeSupplier) {
        return () -> {
            try {
                return unsafeSupplier.get();
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * Wrapper around checked exception-throwing lambdas when you know they couldn't possibly throw a checked exception.
     * Theoretically irresponsible, but practically beneficial.
     */
    static <T> Consumer<T> carefully(UnsafeConsumer<T> unsafeConsumer) {
        return value -> {
            try {
                unsafeConsumer.accept(value);
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    /** Overload of consume() because Consumer and Function lambdas are syntactically ambiguous **/
    static <T> Consumer<T> carefullyConsume(UnsafeConsumer<T> unsafeConsumer) {
        return carefully(unsafeConsumer);
    }

    @FunctionalInterface
    interface UnsafeFunction<T, R> {
        R get(T param) throws Exception;
    }

    @FunctionalInterface
    interface UnsafeSupplier<R> {
        R get() throws Exception;
    }

    @FunctionalInterface
    interface UnsafeConsumer<T> {
        void accept(T value) throws Exception;
    }
}
