package anotherdnd.model.feats;

import anotherdnd.model.Character;
import anotherdnd.model.feats.category.CombatFeat;
import anotherdnd.model.mechanics.Ability;
import anotherdnd.model.mechanics.Attacks.*;
import anotherdnd.model.mechanics.BonusSet.*;
import anotherdnd.model.mechanics.BonusSet.Bonus;
import anotherdnd.model.weapons.Weapon;
import anotherdnd.model.weapons.WeaponMixins.*;

import java.util.Optional;

public interface FeatLibrary1 {


    /**
     * Subtract x=(BAB+1)/4 from melee attack rolls.
     * Add to damage:
     *    One-handed: +2*x
     *    Two-handed: +3*x
     *    Off-hand or secondary: +1*x
     */
    @Requirements.MinimumAbility(ability= Ability.STR, score=13)
    @Requirements.MinimumBaseAttackBonus(1)
    class PowerAttack implements CombatFeat, ImposesAttackPenalty, AddsDamageBonus {

        @Override
        public boolean isStateless() {
            return false;
        }

        @Override
        public Optional<Penalty> getAttackPenalty(Character attacker, Weapon weapon) {
            final int penalty = (attacker.getBaseAttackBonus() + 1) / 4;
            return Optional.of(new Penalty(penalty, this));
        }

        @Override
        public Bonus getBonus(Character attacker) {
            return null;
            //TODO
        }
    }

    class PointBlankShot extends StatelessFeat implements CombatFeat, AddsAttackBonus {
        // +1 attack and damage on targets within 30 feet
        @Override
        public Optional<Bonus> getAttackBonus(Character attacker, Character defender, Weapon weapon) {
            if (weapon instanceof RangedWeapon) {
                //TODO check for 30' range, range increment
                return Optional.of(new Bonus(BonusType.Untyped, +1, this));
            }
            return Optional.empty();
        }
    }

    @Requirements.RequireFeat(PointBlankShot.class)
    class PreciseShot extends StatelessFeat implements CombatFeat {
        // No penalty for shooting into melee.
    }


    class SimpleWeaponProficiency extends StatelessFeat implements CombatFeat {}
    class MartialWeaponProficiency extends StatelessFeat implements CombatFeat {}

    @Requirements.MinimumBaseAttackBonus(1)
    class ExoticWeaponProficiency extends ImmutableFeat implements CombatFeat {
        public ExoticWeaponProficiency(Weapon weapon) {

        }
    }

    class WeaponFocus extends ImmutableFeat implements CombatFeat {

        public final Class<? extends Weapon> weaponType;

        public WeaponFocus (Class<? extends Weapon> weaponType) {
            this.weaponType = weaponType;
        }

        @Override
        public boolean meetsRequirements(Character character) {
//            return CombatFeat.super.meetsRequirements(character) && character.hasFeat();
            return false;
        }
    }


    class WeaponFinesse extends ImmutableFeat implements CombatFeat, AddsAttackBonus {

        @Override
        public Optional<Bonus> getAttackBonus(Character attacker, Character defender, Weapon weapon) {
            if (weapon instanceof LightWeapon
                // || weapon instanceof ElvenCurveBlade
                // || weapon instanceof Rapier
                // || weapon instanceof Whip
                // || weapon instanceof SpikedChain
                // || weapon instanceof Rapier
            ) {
                int dexBonus = attacker.getAbilityBonus(Ability.DEX);
                return Optional.of(new Bonus(BonusType.Ability, dexBonus, this));
            } else {
                return Optional.empty();
            }
        }
    }

    class ArcaneSpellcasting extends StatelessFeat {} //TODO: Actually a class feature

    @Requirements.RequireFeat(ArcaneSpellcasting.class)
    class ArcaneStrike extends StatelessFeat implements CombatFeat, AddsDamageBonus {
        @Override public Bonus getBonus(Character attacker) {
            return null;
            //TODO
        }
    }
}

