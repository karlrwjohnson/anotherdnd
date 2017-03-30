package anotherdnd.model.race;

import anotherdnd.model.mechanics.Ability;

public interface FloatingPlusTwoRace extends Race {
    // Never gonna change, but at least it's defined somewhere
    int FLOATING_BONUS = 2;

    Ability getFloatingAbility();
    void setFloatingAbility(Ability ability);

    @Override
    default int getAbilityScoreModifier(Ability ability) {
        return Race.super.getAbilityScoreModifier(ability) + (getFloatingAbility() == ability ? FLOATING_BONUS : 0);
    }

    abstract class FloatingPlusTwoRaceImpl extends AbstractRace implements FloatingPlusTwoRace {
        private Ability floatingAbility = null; // I think this is okay?

        @Override public Ability getFloatingAbility() { return this.floatingAbility; }
        @Override public void setFloatingAbility(Ability ability) { this.floatingAbility = ability; }
    }
}
