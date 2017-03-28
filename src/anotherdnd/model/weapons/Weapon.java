package anotherdnd.model.weapons;

import anotherdnd.model.Character;
import anotherdnd.model.Item;
import anotherdnd.model.mechanics.Actions.Action;
import anotherdnd.model.mechanics.Attacks.Attack;
import anotherdnd.model.mechanics.Damage;
import anotherdnd.model.mechanics.Dice;

import java.util.Collection;
import java.util.Set;

public interface Weapon extends Item {
    Dice getMediumDamageDice();
    Set<Damage.DamageDescriptor> getDamageTypes();

    Collection<? extends Attack> getAttacks(Character wielder);

    @Override default Collection<? extends Action> getActions(Character user) {
        return getAttacks(user);
    }

    default int getThreatRange() { return 1; }
    default int getCritMultiplier() { return 2; }
}
