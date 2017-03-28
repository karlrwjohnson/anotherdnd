package anotherdnd.model.feats;

import anotherdnd.model.Character;

import java.lang.annotation.Annotation;

public interface Feat {

    default boolean meetsRequirements(Character character) {
        for (Annotation annotation : getClass().getAnnotations()) {
            if (!Requirements.checkAny(annotation, character)) {
                return false;
            }
        }

        return true;
    }

    boolean isStateless();
}
