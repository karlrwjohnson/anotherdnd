package anotherdnd.model.mechanics;

import anotherdnd.model.Character;
import anotherdnd.model.mechanics.Actions.ActionDuration;
import anotherdnd.model.mechanics.BonusSet.*;
import anotherdnd.model.weapons.Weapon;

import java.util.Optional;

import static anotherdnd.model.util.Log.log;

public interface Attacks {

    /// Hooks ///

    interface AddsAttackBonus extends Hooks.Hook, BonusSource {
        Optional<Bonus> getAttackBonus(Character attacker, Character defender, Weapon weapon);
    }

    interface ImposesAttackPenalty extends Hooks.Hook, BonusSource {
        Optional<Penalty> getAttackPenalty(Character attacker, Weapon weapon);
    }

    interface AddsDamageBonus extends Hooks.Hook, BonusSource {
        BonusSet.Bonus getBonus(Character attacker);
    }

    /** Represents one character taking a shot or swing at another. Super abstract. **/
    abstract class Attack implements Actions.Action {

        private Character attacker;
        private Character defender;
        private Weapon weapon;

        public Character getAttacker() { return attacker; }
        public Character getDefender() { return defender; }
        public Weapon getWeapon() { return weapon; }

        public Attack (Character attacker, Weapon weapon) {
            this.attacker = attacker;
            this.weapon = weapon;
        }

        @Override public ActionDuration getDuration() {
            return ActionDuration.STANDARD; // normal
        }

        // For Melee* and Ranged* to override
        abstract int getAbilityAttackBonus ();
//        abstract int getDamageAttackBonus ();

        public BonusSet getAttackBonus() {
            Character attacker = getAttacker();
            Character defender = getDefender();
            Weapon weapon = getWeapon();

            BonusSet attackBonus = new BonusSet();

            attackBonus.addBonus(BonusType.Ability, getAttacker(), getAbilityAttackBonus());

            // Hooks for attack bonus substitution
            getAttacker().getHooks(AddsAttackBonus.class)
                .forEach(hook -> hook.getAttackBonus(attacker, defender, weapon)
                    .ifPresent(attackBonus::add));

//            getAttacker().getHooks(ImposesAttackPenalty.class)
//                .forEach(hook -> hook.getAttackPenalty(attacker, weapon)
//                    .ifPresent(attackBonus::add));

            // Enforce proficiency

            return attackBonus;
        }

        public void execute(Character defender) {
            Character attacker = getAttacker();

            // Provoke attack of opportunity...?

            int attack = Die.D20.roll() + getAttackBonus().total();
            log("{} attacking with {} rolled a {}", attacker.getName(), getWeapon(), attack);
            int defense = 10;// defender.getArmorClass();

            if (attack > defense) {
                // calculate damage -- ???
                // deal damage -- ???
            }
        }
    }

    abstract class MeleeAttack extends Attack {
        public MeleeAttack(Character attacker, Weapon weapon) { super(attacker, weapon); }

        @Override int getAbilityAttackBonus () {
            return getAttacker().getAbilityBonus(Ability.STR);
        }
    }

    class MeleeWeaponAttack extends MeleeAttack {
        public MeleeWeaponAttack(Character attacker, Weapon weapon) { super(attacker, weapon); }
    }

    class LightMeleeWeaponAttack extends MeleeWeaponAttack {
        public LightMeleeWeaponAttack (Character attacker, Weapon weapon) { super(attacker, weapon); }
    }

    class OneHandedMeleeWeaponAttack extends MeleeWeaponAttack {
        public OneHandedMeleeWeaponAttack (Character attacker, Weapon weapon) { super(attacker, weapon); }
    }

    class TwoHandedMeleeWeaponAttack extends MeleeWeaponAttack {
        public TwoHandedMeleeWeaponAttack (Character attacker, Weapon weapon) { super(attacker, weapon); }
    }

    class UnarmedAttack extends MeleeAttack {
        public UnarmedAttack (Character attacker) { super(attacker, null); }
    }


    class ShootDrawstringBowAttack extends Attack {
        public ShootDrawstringBowAttack (Character attacker, Weapon weapon) { super(attacker, weapon); }

        @Override int getAbilityAttackBonus () {
            return getAttacker().getAbilityBonus(Ability.DEX);
        }
    }

}