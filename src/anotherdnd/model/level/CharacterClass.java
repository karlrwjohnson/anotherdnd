package anotherdnd.model.level;

import anotherdnd.model.util.Util;

import java.lang.annotation.*;
import java.util.*;

public interface CharacterClass {

    default List<Class<? extends ClassLevel>> getClassLevels () {
        ClassLevels classLevelAnnotation = this.getClass().getAnnotation(ClassLevels.class);
        if (classLevelAnnotation == null) {
            throw new RuntimeException("Class level list is not defined for " + this.getClass().getSimpleName());
        }

        return Arrays.asList(classLevelAnnotation.value());
    }

    default Optional<Class<? extends ClassLevel>> nextLevel (Collection<? extends ClassLevel> takenLevels) {
        Iterator<Class<? extends ClassLevel>> itr = getClassLevels().iterator();
        while (itr.hasNext()) {
            Class<? extends ClassLevel> availableLevel = itr.next();

            if (!Util.instanceExists(availableLevel, takenLevels)) {
                return Optional.ofNullable(itr.next());
            }
        }

        return Optional.of(getClassLevels().get(0));
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface ClassLevels {
        Class<? extends ClassLevel>[] value();
    }
}
