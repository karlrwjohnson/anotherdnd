package anotherdnd.model.mechanics;

import anotherdnd.model.Character;
import anotherdnd.model.mechanics.Attacks.Attack;

import java.util.OptionalInt;

public interface Hooks {

    interface Hook {}

    interface ModifiesAttackRoll extends Hook {
        void modifyAttackRoll(Character character, DiceBonusSet dice);
    }

    interface ModifiesDamageRoll extends Hook {
        void modifyDamage(Character character, DiceBonusSet damage);
    }

    interface ModifiesWillSaveRoll extends Hook {
        void modifyWillSave(Character character, DiceBonusSet save);
    }
}
