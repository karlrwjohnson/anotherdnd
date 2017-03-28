package anotherdnd.model.feats;

import anotherdnd.model.Character;
import anotherdnd.model.mechanics.Ability;

import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public interface Requirements {

    Map<Class<? extends Annotation>, Method> checkerIndex =
        new HashMap<Class<? extends Annotation>, Method>()
    {{
        for (Method method : Requirements.class.getMethods()) {
            if (!method.getName().equals("check")) {
                continue;
            }
            Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length == 2 &&
                Annotation.class.isAssignableFrom(parameters[0]) &&
                Character.class.isAssignableFrom(parameters[1])
            ) {
                Class<? extends Annotation> type = (Class<? extends Annotation>) parameters[0];
                put(type, method);
            }
        }
    }};

    static boolean checkAny(Annotation annotation, Character chr) {
        Method method = checkerIndex.get(annotation.getClass());
        if (method == null) {
            return true;
        } else try {
            return (Boolean) method.invoke(null, annotation, chr);
        } catch (
            IllegalAccessException |
            InvocationTargetException e
        ) {
            throw new RuntimeException(e);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface MinimumAbility {
        Ability ability();
        int score();
    }
    static boolean check(MinimumAbility req, Character chr) {
        return chr.getAbilityScore(req.ability()) >= req.score();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface MinimumBaseAttackBonus {
        int value();
    }
    static boolean check(MinimumBaseAttackBonus req, Character chr) {
        return chr.getBaseAttackBonus() >= req.value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface RequireFeat {
        Class<? extends Feat> value();
    }
    static boolean check(RequireFeat req, Character chr) {
        return chr.hasFeatOfType(req.value());
    }

}
