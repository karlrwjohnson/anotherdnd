package anotherdnd.model.weapons;

import anotherdnd.model.mechanics.Damage;
import anotherdnd.model.mechanics.Dice;
import com.google.common.collect.Sets;

import java.util.Set;

import static anotherdnd.model.weapons.WeaponMixins.*;

public interface Weapons {

    //class Punch implements BaseUnarmedAttack, SimpleWeapon {}

    class Dagger implements LightMeleeWeapon, SimpleWeapon, SlashingWeapon, PiercingWeapon {
        @Override public Dice getMediumDamageDice() { return Dice._1d4; }
        @Override public Set<Damage.DamageDescriptor> getDamageTypes() {
            return Sets.union(
                SlashingWeapon.super.getDamageTypes(),
                PiercingWeapon.super.getDamageTypes()
            );
        }
    }

    class Club implements OneHandedMeleeWeapon, SimpleWeapon, BludgeoningWeapon {
        @Override public Dice getMediumDamageDice() { return Dice._1d6; }
    }

    class Morningstar implements OneHandedMeleeWeapon, SimpleWeapon, BludgeoningWeapon, PiercingWeapon {
        @Override public Dice getMediumDamageDice() { return Dice._1d8; }
        @Override public Set<Damage.DamageDescriptor> getDamageTypes() {
            return Sets.union(
                BludgeoningWeapon.super.getDamageTypes(),
                PiercingWeapon.super.getDamageTypes()
            );
        }
    }

    class Shortsword implements LightMeleeWeapon, MartialWeapon, PiercingWeapon {
        @Override public Dice getMediumDamageDice() { return Dice._1d6; }
        @Override public int getThreatRange() { return 2; }
    }

    class Longsword implements OneHandedMeleeWeapon, MartialWeapon, PiercingWeapon {
        @Override public Dice getMediumDamageDice() { return Dice._1d8; }
        @Override public int getThreatRange() { return 2; }
    }

    class Greatsword implements TwoHandedMeleeWeapon, MartialWeapon, PiercingWeapon {
        @Override public Dice getMediumDamageDice() { return Dice._2d6; }
        @Override public int getThreatRange() { return 2; }
    }

    class Longbow implements BaseBow {

        @Override public Dice getMediumDamageDice() { return Dice._1d8; }
    }
}
