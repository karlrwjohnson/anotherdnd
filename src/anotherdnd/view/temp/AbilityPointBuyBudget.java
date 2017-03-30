package anotherdnd.view.temp;

import anotherdnd.model.Character;
import anotherdnd.model.mechanics.Ability;
import anotherdnd.model.race.FloatingPlusTwoRace;
import anotherdnd.view.util.ModelSync;

import java.util.Arrays;

import static anotherdnd.model.race.FloatingPlusTwoRace.FLOATING_BONUS;
import static java.lang.Integer.max;
import static java.lang.Integer.min;

@SuppressWarnings("WeakerAccess")
public class AbilityPointBuyBudget {

    protected static final int[] POINTS_BY_SCORE = new int[]{ /*0*/ 0,0,0,0,0,0,0, /* 7 */ -4,-2,-1, /*10*/ 0,1,2,3,5,7,10,13, /*18*/ 17 };
    public static final int MINIMUM = 7;
    public static final int MAXIMUM = 18;
    public static final int DEFAULT_BUDGET = 25;

    public final Character character;
    protected int budget;

    public static AbilityPointBuyBudget of(Character character, int budget) {
        if (character.getRace() instanceof FloatingPlusTwoRace) {
            return new FloatingPlusTwoAbilityPointBuyBudget(character, budget);
        } else {
            return new AbilityPointBuyBudget(character, budget);
        }
    }
    public static AbilityPointBuyBudget of(Character character) {
        return of(character, DEFAULT_BUDGET);
    }

    private AbilityPointBuyBudget(Character character, int budget) {
        this.character = character;
        this.budget = budget;
    }
    
    public void setBudget(int budget) { this.budget = budget; }
    public int getBudget() { return budget; }
    
    public int getPointsSpentOn(Ability ability) {
        return POINTS_BY_SCORE[character.getAbilityBaseScore(ability)];
    }

    public int getTotalPointsSpent() {
        return Arrays.stream(Ability.values())
            .mapToInt(this::getPointsSpentOn)
            .sum();
    }

    public int getPointsRemaining() {
        return budget - getTotalPointsSpent();
    }

    public void incrementScore(Ability ability) {
        character.setAbilityBaseScore(ability, min(MAXIMUM, character.getAbilityBaseScore(ability) + 1));
        ModelSync.scheduleUpdate();
    }

    public void decrementScore(Ability ability) {
        character.setAbilityBaseScore(ability, max(MINIMUM, character.getAbilityBaseScore(ability) - 1));
        ModelSync.scheduleUpdate();
    }

    public boolean canIncrementScore(Ability ability) {
        return character.getAbilityBaseScore(ability) < MAXIMUM;
    }

    public boolean canDecrementScore(Ability ability) {
        return character.getAbilityBaseScore(ability) > MINIMUM;
    }

    private static class FloatingPlusTwoAbilityPointBuyBudget extends AbilityPointBuyBudget {
        private final FloatingPlusTwoRace characterRace;

        private FloatingPlusTwoAbilityPointBuyBudget(Character character, int budget) {
            super(character, budget);
            characterRace = (FloatingPlusTwoRace) character.getRace();
        }

        @Override
        public boolean canIncrementScore(Ability ability) {
            Ability floating = characterRace.getFloatingAbility();
            return character.getAbilityBaseScore(ability) < MAXIMUM ||
                floating != null && floating != ability && character.getAbilityBaseScore(floating) + 2 <= MAXIMUM;
        }

        @Override
        public void incrementScore(Ability ability) {
            Ability floating = characterRace.getFloatingAbility();

            // Re-allocate points to take maximum advantage of the floating +2
            if (floating == null) {
                // Floating bonus has not yet been claimed
                character.setAbilityBaseScore(ability, character.getAbilityBaseScore(ability) - FLOATING_BONUS);
                characterRace.setFloatingAbility(ability);
            } else if (ability != floating &&
                    character.getAbilityScore(ability) >= character.getAbilityScore(floating)) {
                character.setAbilityBaseScore(ability, character.getAbilityBaseScore(ability) - FLOATING_BONUS);
                character.setAbilityBaseScore(floating, character.getAbilityBaseScore(floating) + FLOATING_BONUS);
                characterRace.setFloatingAbility(ability);
            }

            super.incrementScore(ability);
        }

        @Override
        public void decrementScore(Ability ability) {
            super.decrementScore(ability);

            // Re-allocate points to take maximum advantage of the floating +2
            if (ability == characterRace.getFloatingAbility()) {

                // Find the highest ability score *after* the +2 bonus is applied.
                @SuppressWarnings("OptionalGetWithoutIsPresent")
                Ability highest = Arrays.stream(Ability.values())
                    .reduce((a, b) -> character.getAbilityScore(a) > character.getAbilityScore(b) ? a : b)
                    .get();

                // If the decremented score is no longer the highest with the bonus, transfer the bonus to the other
                // ability.
                if (highest != ability) {
                    character.setAbilityBaseScore(ability, character.getAbilityBaseScore(ability) + FLOATING_BONUS);
                    character.setAbilityBaseScore(highest, character.getAbilityBaseScore(highest) - FLOATING_BONUS);
                    characterRace.setFloatingAbility(highest);
                }
            }
        }
    }
}
