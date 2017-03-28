package anotherdnd.model.util;

import anotherdnd.view.util.UnsafeFunctional.*;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/** Like Optional, but holds an error or a value instead of just a value. **/
public class Maybe<R> {
    public static <R> Maybe<R> of(UnsafeSupplier<R> supplier) {
        try {
            return new Maybe<>(supplier.get());
        } catch (Exception error) {
            return new Maybe<>(error);
        }
    }

    public void with(Consumer<R> withValue, Consumer<Exception> withError) {
        if (value != null) {
            withValue.accept(value);
        } else {
            withError.accept(error);
        }
    }

    public Maybe<R> or(UnsafeSupplier<R> unsafeSupplier) {
        if (value != null) {
            return this;
        } else {
            //noinspection unchecked
            return of(unsafeSupplier);
        }
    }

    public R orElse(Supplier<R> supplier) {
        if (value != null) {
            return this.value;
        } else {
            //noinspection unchecked
            return supplier.get();
        }
    }

    public <T> Maybe<T> mapValue(UnsafeFunction<R, T> unsafeFunction) {
        if (value != null) {
            try {
                return new Maybe<>(unsafeFunction.get(this.value));
            } catch (Exception error) {
                return new Maybe<>(error);
            }
        } else {
            return new Maybe<>(error);
        }
    }

    public R orElseThrow() {
        if (value != null) {
            return value;
        } else {
            throw new RuntimeException(error);
        }
    }

    public R orElseThrow(Function<Exception, ? extends RuntimeException> toRuntimeException) {
        if (value != null) {
            return value;
        } else {
            throw toRuntimeException.apply(error);
        }
    }

    public Optional<R> toOptional() {
        return Optional.ofNullable(value);
    }

    //// Private /////

    private final R value;
    private final Exception error;

    private Maybe(R value) {
        this.value = value;
        this.error = null;
    }

    private Maybe(Exception error) {
        this.value = null;
        this.error = error;
    }
}
