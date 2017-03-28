package anotherdnd.model.level;

import anotherdnd.model.mechanics.BaseAttackBonusProgression;
import anotherdnd.model.mechanics.SaveProgression;

public interface ProgressionMixins {
    interface PoorBaseAttackBonusProgression extends ClassLevel {
        @Override default BaseAttackBonusProgression getBaseAttackBonusProgression() {
            return BaseAttackBonusProgression.POOR;
        }
    }
    interface AverageBaseAttackBonusProgression extends ClassLevel {
        @Override default BaseAttackBonusProgression getBaseAttackBonusProgression() {
            return BaseAttackBonusProgression.AVERAGE;
        }
    }
    interface GoodBaseAttackBonusProgression extends ClassLevel {
        @Override default BaseAttackBonusProgression getBaseAttackBonusProgression() {
            return BaseAttackBonusProgression.GOOD;
        }
    }

    interface GoodFortitudeSaveProgression extends ClassLevel {
        @Override default SaveProgression getFortitudeSaveProgression() { return SaveProgression.GOOD; }
    }
    interface PoorFortitudeSaveProgression extends ClassLevel {
        @Override default SaveProgression getFortitudeSaveProgression() { return SaveProgression.POOR; }
    }

    interface GoodReflexSaveProgression extends ClassLevel {
        @Override default SaveProgression getReflexSaveProgression() { return SaveProgression.GOOD; }
    }
    interface PoorReflexSaveProgression extends ClassLevel {
        @Override default SaveProgression getReflexSaveProgression() { return SaveProgression.POOR; }
    }

    interface GoodWillSaveProgression extends ClassLevel {
        @Override default SaveProgression getWillSaveProgression() { return SaveProgression.GOOD; }
    }
    interface PoorWillSaveProgression extends ClassLevel {
        @Override default SaveProgression getWillSaveProgression() { return SaveProgression.POOR; }
    }
}
