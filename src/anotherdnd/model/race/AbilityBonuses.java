package anotherdnd.model.race;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)

@interface AbilityBonuses {
    AbilityBonus[] value();
}
