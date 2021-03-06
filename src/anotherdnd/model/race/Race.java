package anotherdnd.model.race;

import anotherdnd.model.mechanics.Ability;
import anotherdnd.model.mechanics.AbilityScore.AddsAbilityScoreBonus;
import anotherdnd.model.mechanics.AbilityScore.AddsAbilityScorePenalty;
import anotherdnd.model.mechanics.BonusSet.Bonus;
import anotherdnd.model.mechanics.BonusSet.BonusSource;
import anotherdnd.model.mechanics.BonusSet.BonusType;
import anotherdnd.model.mechanics.BonusSet.Penalty;

import java.util.*;

public interface Race extends
    AddsAbilityScoreBonus,
    AddsAbilityScorePenalty,
    BonusSource
{
    Set<Class<? extends Race>> races = new HashSet<>();

    default Map<Ability, Integer> getAbilityBonuses() {
        EnumMap<Ability, Integer> ret = new EnumMap<>(Ability.class);
        for (AbilityBonus bonus : this.getClass().getAnnotationsByType(AbilityBonus.class)) {
            ret.put(bonus.ability(), bonus.bonus());
        }
        for (Ability ability : Ability.values()) {
            ret.putIfAbsent(ability, 0);
        }
        return ret;
    }

    default int getAbilityScoreModifier(Ability ability) {
        return Arrays.stream(this.getClass().getAnnotationsByType(AbilityBonus.class))
                .filter(abilityBonus -> abilityBonus.ability() == ability)
                .mapToInt(AbilityBonus::bonus)
                .sum();
    }

    default Bonus getAbilityScoreBonus(Ability ability) {
        int bonus = getAbilityScoreModifier(ability);
        return new Bonus(BonusType.Racial, bonus > 0 ? bonus : 0, this);
    }

    default Penalty getAbilityScorePenalty(Ability ability) {
        int bonus = getAbilityScoreModifier(ability);
        return new Penalty(bonus < 0 ? -bonus : 0, this);
    }

    default String getDescription() {
        Description description = this.getClass().getAnnotation(Description.class);
        if (description == null) {
            throw new NullPointerException("Must implement getDescription()");
        } else {
            return description.value();
        }
    }
}
