package anotherdnd.model.weapons;

import anotherdnd.model.Character;
import anotherdnd.model.mechanics.Ability;
import anotherdnd.model.mechanics.Attacks.*;
import anotherdnd.model.mechanics.Damage;
import anotherdnd.model.mechanics.Dice;

import java.util.*;

import static anotherdnd.model.mechanics.Damage.DamageDescriptor.Bludgeoning;
import static anotherdnd.model.mechanics.Damage.DamageDescriptor.Piercing;
import static anotherdnd.model.mechanics.Damage.DamageDescriptor.Slashing;

public interface WeaponMixins {

    //// DAMAGE TYPE ////
    interface SlashingWeapon    extends Weapon { default Set<Damage.DamageDescriptor> getDamageTypes() { return Collections.singleton(Slashing); } }
    interface PiercingWeapon    extends Weapon { default Set<Damage.DamageDescriptor> getDamageTypes() { return Collections.singleton(Piercing); } }
    interface BludgeoningWeapon extends Weapon { default Set<Damage.DamageDescriptor> getDamageTypes() { return Collections.singleton(Bludgeoning); } }

    interface MeleeWeapon extends Weapon {}
    interface RangedWeapon extends Weapon {}

    // Important for handedness algorithms, I guess.
    interface LightWeapon extends Weapon {}
    interface OneHandedWeapon extends Weapon {}
    interface TwoHandedWeapon extends Weapon {}

    interface BaseUnarmedAttack extends MeleeWeapon, BludgeoningWeapon {
        @Override default Dice getMediumDamageDice() { return Dice._1d3; }
    }
    interface LightMeleeWeapon extends LightWeapon, MeleeWeapon {
        @Override default Collection<? extends Attack> getAttacks(Character wielder) {
            return Collections.singleton(new LightMeleeWeaponAttack(wielder, this));
        }}
    interface OneHandedMeleeWeapon extends OneHandedWeapon, MeleeWeapon {
        @Override default Collection<? extends Attack> getAttacks(Character wielder) {
            return Arrays.asList(
                new OneHandedMeleeWeaponAttack(wielder, this),
                new TwoHandedMeleeWeaponAttack(wielder, this)  // http://paizo.com/threads/rzs2mrq6?Can-a-one-handed-weapon-be-used-two-handed
            );
        }}
    interface TwoHandedMeleeWeapon extends TwoHandedWeapon, MeleeWeapon {
        @Override default Collection<? extends Attack> getAttacks(Character wielder) {
            return Collections.singleton(new TwoHandedMeleeWeaponAttack(wielder, this));
        }
    }

    interface SimpleWeapon extends Weapon {}
    interface MartialWeapon extends Weapon {}
    interface ExoticWeapon extends Weapon {}


    interface BaseBow extends RangedWeapon, TwoHandedWeapon, MartialWeapon, PiercingWeapon {
        @Override default Collection<? extends Attack> getAttacks(Character wielder) {
            return Collections.singleton(new ShootDrawstringBowAttack(wielder, this));
        }

//        @Override default int getDamageAbilityBonus(Character character) {
//            // Strength can only subtract from ranged damage, not add.
//            // (Unless you're wielding a Composite Longbow)
//            return Math.max(0, character.getAbilityBonus(Ability.STR));
//        }
    }
}
