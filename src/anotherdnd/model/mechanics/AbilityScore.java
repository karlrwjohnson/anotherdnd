package anotherdnd.model.mechanics;

import anotherdnd.model.Character;
import anotherdnd.model.mechanics.BonusSet.*;

public class AbilityScore {

    public interface AddsAbilityScoreBonus extends Hooks.Hook {
        Bonus getAbilityScoreBonus(Ability ability);
    }

    public interface AddsAbilityScorePenalty extends Hooks.Hook {
        Penalty getAbilityScorePenalty(Ability ability);
    }



    public final Ability ability;
    public final Character character;

    private int baseScore;

    public int getBaseScore() { return this.baseScore; }
    public void setBaseScore(int base) {
        this.baseScore = base;
    }

    public AbilityScore(Ability ability, Character character, int baseScore) {
        this.ability = ability;
        this.character = character;
        this.baseScore = baseScore;
    }

    public int getScore() {
        BonusSet bonusSet = new BonusSet();
        bonusSet.addBonus(BonusType.Untyped, character, baseScore);

//        System.out.println("Calculating " + ability);
//        System.out.println("  Base score: " + getBaseScore());
//        System.out.println("  Number of hooks giving a bonus: " + character.getHooks(AddsAbilityScoreBonus.class))

        character.getHooks(AddsAbilityScoreBonus.class)
            .forEachOrdered(source -> bonusSet.add(source.getAbilityScoreBonus(ability)));

        character.getHooks(AddsAbilityScorePenalty.class)
            .forEachOrdered(source -> bonusSet.add(source.getAbilityScorePenalty(ability)));

        return bonusSet.total();
    }

    public int getBonus() {
        return this.getScore() / 2 - 5;
    }
}
