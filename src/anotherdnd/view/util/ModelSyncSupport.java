package anotherdnd.view.util;

import anotherdnd.model.util.Maybe;

import javax.swing.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static anotherdnd.model.util.Util.optionalOr;
import static anotherdnd.view.util.UnsafeFunctional.carefully;
import static anotherdnd.view.util.UnsafeFunctional.carefullyConsume;

public class ModelSyncSupport {
    private ModelSyncSupport(){} // No instantiations!

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Getter {
        String value();
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Setter {
        String value();
    }

//    public static <T> Supplier<? extends T> getGetter(Object object, String fieldName, Class<T> fieldType) {
//        // Given object=foo, fieldname="bar", try these things:
//        //  0) Look for field foo.bar
//        //       Look for annotation @Getter. If present, try to use foo`foo.bar@Getter.value`() as the method name
//        //         If it doesn't work, throw an exception
//        //  1) foo.getBar()
//        //  2) foo.isBar()
//        //  3) Look for field foo.bar
//        //       Return its value
//        //  X) Throw exception
//
//        Class<?> objectClass = object.getClass();
//
//        // In Java 9, replace `optionalOr(() -> optional1, () -> optional2)` with `optional1.or(() -> optional2)`
//        return optionalOr(
//            () -> getGetterFromAnnotation (object, objectClass, fieldName),
//            () -> findGetter              (object, objectClass, fieldName),
//            () -> generateFieldGetter     (object, objectClass, fieldName)
//        ).orElseThrow(() -> new RuntimeException(String.format(
//            "Cannot find a suitable getter for field %s.%s, and the field itself doesn't appear to exist",
//            objectClass.getName(), fieldName))
//        );
//    }
//
//    public static <T> Consumer<T> getSetter(Object object, String fieldName, Class<T> fieldType) {
//        Class<?> objectClass = object.getClass();
//
//    }
//
//    ///// Support /////
//
//    private static <T> Function<T, T> assertIsAssignable (Method)
//
//    private static Maybe<Field> getAccessibleField(Class<?> objectClass, String fieldName) {
//        return Maybe
//            // Try a public field first
//            .of(() -> objectClass.getField(fieldName))
//            // Look for a private field
//            .or(() -> {
//                // Class::getDeclaredField finds private fields, but only in the child class --
//                // it doesn't look in super-classes
//                Field field = objectClass.getDeclaredField(fieldName);
//
//                // Pry it open with a screwdriver
//                field.setAccessible(true);
//                return field;
//            });
//    }
//
//    private static String capitalize(String lowercase) {
//        if (lowercase.length() < 1) {
//            return lowercase;
//        } else {
//            return lowercase.substring(0, 1).toUpperCase() + lowercase.substring(1);
//        }
//    }
//
//    ///// Getter finders /////
//
//    /** Look for a getter using a @Getter annotation on the field **/
//    private static Optional<Supplier<Object>> getGetterFromAnnotation(Object object, Class<?> objectClass, String fieldName) {
//        return Maybe
//            // Search a matching field and an annotation on it
//            .of(() -> objectClass.getField(fieldName))
//            .mapValue(field -> field.getAnnotation(Getter.class).value())
//
//            // Downgrade to Optional so thrown exceptions propagate
//            .toOptional()
//
//            .map(getterName -> Maybe
//                // Look for the referenced getter
//                .of(() -> objectClass.getMethod(getterName))
//                .mapValue(getter -> carefully(() -> getter.invoke(object)))
//
//                // If it isn't there, throw an exception instead of failing silently because the programmer made a mistake
//                .orElseThrow(err -> new RuntimeException(String.format(
//                    "@Getter annotation for field %s.%s references method \"%s\", which doesn't exist",
//                    objectClass.getName(), fieldName, getterName
//                )))
//            );
//    }
//
//    /** Look for a getter using standard Java bean naming convensions **/
//    private static <T> Optional<Supplier<T>> findGetter(Object object, Class<?> objectClass, String fieldName, Class<T> fieldType) {
//        String capitalizedFieldName = capitalize(fieldName);
//        String objectGetterName = "get" + capitalizedFieldName;
//        String booleanGetterName = "is" + capitalizedFieldName;
//
//        return Maybe
//            .of(() -> objectClass.getMethod(objectGetterName))
//            .or(() -> objectClass.getMethod(booleanGetterName))
//            .mapValue(getter -> {
//                if (fieldType.isAssignableFrom(getter.getReturnType())) {
//                    //noinspection unchecked
//                    return carefully(() -> (T) getter.invoke(object));
//                } else {
//                    //TODO: this message will get lost
//                    throw new RuntimeException(String.format(
//                        "Method returns a %s, but a field type of %s is requested."
//                    ));
//                }
//            })
//            .toOptional();
//    }
//
//    /** Create our own "getter" by directly accessing the field through reflection **/
//    private static Optional<Supplier<Object>> generateFieldGetter(Object object, Class<?> objectClass, String fieldName) {
//        return getAccessibleField(objectClass, fieldName)
//            .mapValue(field -> carefully(() -> field.get(object)))
//            .toOptional();
//    }
//
//    ///// Setter finders /////
//
//    /** Look for a getter using a @Getter annotation on the field **/
//    private static Optional<Supplier<Object>> getSetterFromAnnotation(Object object, Class<?> objectClass, String fieldName) {
//        return Maybe
//            // Search a matching field and an annotation on it
//            .of(() -> objectClass.getField(fieldName))
//            .mapValue(field -> field.getAnnotation(Getter.class).value())
//
//            // Downgrade to Optional so thrown exceptions propagate
//            .toOptional()
//
//            .map(getterName -> Maybe
//                // Look for the referenced getter
//                .of(() -> objectClass.getMethod(getterName))
//                .mapValue(getter -> carefully(() -> getter.invoke(object)))
//
//                // If it isn't there, throw an exception instead of failing silently because the programmer made a mistake
//                .orElseThrow(err -> new RuntimeException(String.format(
//                    "@Getter annotation for field %s.%s references method \"%s\", which doesn't exist",
//                    objectClass.getName(), fieldName, getterName
//                )))
//            );
//    }
//
//    /** Look for a getter using standard Java bean naming conventions **/
//    private static <T> Optional<Consumer<T>> findGetter(Object object, Class<?> objectClass, String fieldName, Class<T> fieldType) {
//        String setterName = "set" + capitalize(fieldName);
//        return Maybe.of(() -> objectClass.getMethod(setterName, fieldType))
//            .mapValue(setter -> carefullyConsume((T value) -> setter.invoke(object, value)))
//            .toOptional();
//    }
//
//    /** Create our own "setter" by directly accessing the field through reflection **/
//    private static Optional<Consumer<Object>> generateFieldSetter(Object object, Class<?> objectClass, String fieldName) {
//        return getAccessibleField(objectClass, fieldName)
//            .mapValue(field -> carefullyConsume(value -> field.set(object, value)))
//            .toOptional();
//    }
}
