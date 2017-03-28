package anotherdnd.model.util;

import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public interface MoreStreams {
    static IntStream intStream (Collection<Integer> ints) {
        return ints.stream().mapToInt(Integer::intValue);
    }

    static IntStream intStream (OptionalInt optionalInt) {
        return optionalInt.isPresent() ? IntStream.of(optionalInt.getAsInt()) : IntStream.empty();
    }

    static IntStream intStream (Optional<Integer> optionalInt) {
        return Streams.stream(optionalInt).mapToInt(Integer::intValue);
    }

    // Filter objects for a given type while casting them to that type
    static <T, R> Function<T, Stream<R>> filterInstancesOf(Class<R> type) {
        return item -> {
            if (type.isAssignableFrom(item.getClass())) {
                @SuppressWarnings("unchecked")
                R castedItem = (R) item;

                return Stream.of(castedItem);
            } else {
                return Stream.empty();
            }
        };
    }

    static <T> T getOnlyElement (Stream<T> stream) {
        // Guava is 90% of the way there
        return Iterables.getOnlyElement(stream.limit(2).collect(Collectors.toList()));
    }
}
