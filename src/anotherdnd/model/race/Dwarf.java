package anotherdnd.model.race;

import anotherdnd.model.mechanics.Ability;

@AbilityBonus(ability=Ability.CON, bonus=+2)
@AbilityBonus(ability=Ability.WIS, bonus=+2)
@AbilityBonus(ability=Ability.CHA, bonus=-2)
@Description("Though short in stature, Dwarves are tough and hard-working.")
public class Dwarf extends AbstractRace {
}
