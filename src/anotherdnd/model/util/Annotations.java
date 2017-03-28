package anotherdnd.model.util;

import java.lang.annotation.Annotation;
import java.util.function.Predicate;

public interface Annotations {
    static <T extends Annotation> boolean hasAnnotation(
        Object that,
        Class<T> annotationClass
    ) {
        T annotation = that.getClass().getAnnotation(annotationClass);
        return annotation != null;
    }

    static <T extends Annotation> boolean ifAnnotation(
        Object that,
        Class<T> annotationClass,
        Predicate<T> function
    ) {
        T annotation = that.getClass().getAnnotation(annotationClass);
        return annotation == null || function.test(annotation);
    }
}
