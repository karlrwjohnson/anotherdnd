package anotherdnd.model.race;

import static anotherdnd.model.mechanics.Ability.*;

@AbilityBonus(ability=DEX, bonus=+2)
@AbilityBonus(ability=INT, bonus=+2)
@AbilityBonus(ability=CON, bonus=-2)
@Description("Beautiful and long-lived, elves can seem proud or aloof to other races")
public class Elf extends AbstractRace {
    static { Race.races.add(Elf.class); }
}
