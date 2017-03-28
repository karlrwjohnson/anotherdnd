package anotherdnd.model.race;

import anotherdnd.model.mechanics.Ability;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(AbilityBonuses.class)

@interface AbilityBonus {
    Ability ability();
    int bonus();
}
