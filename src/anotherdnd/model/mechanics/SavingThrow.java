package anotherdnd.model.mechanics;

import anotherdnd.model.Character;

import java.util.stream.Collectors;

public class SavingThrow {
    public interface AddsSaveBonus extends Hooks.Hook {
        BonusSet.Bonus getSaveBonus(Save save);
    }

    public interface AddsSavePenalty extends Hooks.Hook {
        BonusSet.Penalty getSavePenalty(Save save);
    }

    public final Save save;
    public final Character character;

    public SavingThrow(Save save, Character character) {
        this.save = save;
        this.character = character;
    }

    public int getBonus() {
        BonusSet bonusSet = new BonusSet();

        // Ability
        bonusSet.addBonus(BonusSet.BonusType.Ability, character, character.getAbilityBonus(save.ability));

        // Class levels
        bonusSet.addBonus(BonusSet.BonusType.Untyped, character, SaveProgression.getTotalSaveBonus(
            character.getClassLevels()
                .map(classLevel -> classLevel.getSaveProgression(save))
                .collect(Collectors.toList())
        ));

        character.getHooks(AddsSaveBonus.class)
            .forEachOrdered(source -> bonusSet.add(source.getSaveBonus(save)));

        character.getHooks(AddsSavePenalty.class)
            .forEachOrdered(source -> bonusSet.add(source.getSavePenalty(save)));

        return bonusSet.total();
    }
}
