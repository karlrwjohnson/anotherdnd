package anotherdnd.model.util;

import java.io.PrintStream;
import java.util.function.Function;
import java.util.regex.Matcher;

import static anotherdnd.model.util.Regex.replaceAll;

public interface Log {

    static String _log_format(String str, Object[] args) {
        return replaceAll(str, "\\{([^}]*)\\}", new Function<Matcher, String>() {
            private int argIndex = 0;

            private String nextArg() {
                try {
                    return args[argIndex++].toString();
                } catch (IndexOutOfBoundsException e) {
                    System.err.printf("Logging stateement had at least %d automatic arguments, but only %d were provided", argIndex, args.length);
                    return "";
                }
            }

            private String getArg(String request) {
                int index = -1;
                try {
                    index = Integer.parseInt(request);
                    return args[index].toString();
                } catch (IndexOutOfBoundsException e) {
                    System.err.printf("Logging statement asked for argument #%d, but only %d were provided", index, args.length);
                    return "";
                } catch (NumberFormatException e) {
                    System.err.printf("Only numerical indices are supported; found \"%s\"", args.length, argIndex);
                    return "";
                }
            }

            @Override
            public String apply(Matcher match) {
                return match.group(0).length() == 0 ? nextArg() : getArg(match.group(0));
            }
        });
    }

    static void log(String str) {
        System.out.println(str);
    }

    static void log(String str, Object... args) {
        System.out.println(_log_format(str, args));
    }
}
