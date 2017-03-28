package anotherdnd.model.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static anotherdnd.model.util.MoreStreams.filterInstancesOf;

/** Stuff that doesn't fit anywhere else and is too small to warrant its own file **/
public interface Util {

    static <T> boolean instanceExists(Class<? extends T> classes, Collection<? extends T> instances) {
        return instances.stream()
            .flatMap(filterInstancesOf(classes))
            .findAny()
            .isPresent();
    }

    /**
     * Stopgap until Java 9's Optional::or
     *
     * In Java 9, replace `optionalOr(() -> optional1, () -> optional2)` with `optional1.or(() -> optional2)`
     */
    @SafeVarargs
    static <T> Optional<T> optionalOr(Supplier<Optional<T>>... optionals) {
        return Arrays.stream(optionals)
            .flatMap(supplier -> supplier.get().map(Stream::of).orElse(Stream.empty()))
            .findFirst();
    }

    /** Insert spaces into CamelCasedString **/
    static String unCamelCase(String src) {
        StringBuilder sb = new StringBuilder();

        final char firstLetter = src.charAt(0);
        final boolean shouldCapitalize = (firstLetter >= 'A' && firstLetter <= 'Z');

        final int toLowercase = 'a' - 'A';

        // Substring range selectors so that StringBuilder can blit entire ranges of characters at a time.
        // (I made this way too complicated, I think)
        int begin = 0, end = 1;
        while (end < src.length()) {
            char chr = src.charAt(end);
            if (chr >= 'A' && chr <= 'Z') {

                // Subsequent words
                if (begin != 0) {
                    sb.append(' ');
                }

                if (shouldCapitalize) {
                    sb.append(src, begin, end);
                } else {
                    sb.append(src.charAt(begin) + toLowercase);
                    sb.append(src, begin+1, end);
                }

                begin = end;
            }
            end++;
        }

        if (shouldCapitalize) {
            sb.append(src, begin, end);
        } else {
            sb.append(src.charAt(begin) + toLowercase);
            sb.append(src, begin+1, end);
        }

        return sb.toString();
    }

    /** Title Case a MACRO_CASED_STRING **/
    static String unMacroCase(String src) {
        StringBuilder sb = new StringBuilder();

        final int toLowercase = 'a' - 'A';

        boolean startOfWord = true;
        for (char chr : src.toCharArray()) {
            if (chr == '_') {
                // Print whitespace, but only once
                if (!startOfWord) {
                    sb.append(' ');
                }

                startOfWord = true;
            }
            else if (chr >= 'A' && chr <= 'Z') {
                if (startOfWord) {
                    sb.append(chr);
                    startOfWord = false;
                } else {
                    sb.append((char) (chr + toLowercase));
                }
            }
            else {
                sb.append(chr);
            }
        }

        return sb.toString();
    }

    static int randInt(int max) {
        return (int) (Math.random() * max);
    }

    static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> keyClass, Function<K, V> transform) {
        EnumMap<K, V> ret = new EnumMap<K, V>(keyClass);
        for (K key : keyClass.getEnumConstants()) {
            ret.put(key, transform.apply(key));
        }
        return ret;
    }
}
