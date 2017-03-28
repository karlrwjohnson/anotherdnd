package anotherdnd.model.level;

import anotherdnd.model.feats.Feat;
import anotherdnd.model.feats.category.CombatFeat;

import java.util.Collections;

public interface ClassLevelMixins {
    interface GainCombatBonusFeat extends ClassLevel {
        @Override default Iterable<Class<? extends Feat>> getBonusFeatSlots() {
            return Collections.singleton(CombatFeat.class);
        }
    }
}