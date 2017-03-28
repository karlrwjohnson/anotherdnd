package anotherdnd.model.util;


import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Regex {
    static String replaceAll (String haystack, String regex, Function<Matcher, String> replacer) {
        Matcher matcher = Pattern.compile(regex).matcher(haystack);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, replacer.apply(matcher));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
