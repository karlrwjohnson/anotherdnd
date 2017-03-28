package anotherdnd.model.level;

import anotherdnd.model.Character;
import anotherdnd.model.feats.Feat;
import anotherdnd.model.mechanics.BaseAttackBonusProgression;
import anotherdnd.model.mechanics.Save;
import anotherdnd.model.mechanics.SaveProgression;
import anotherdnd.model.skills.Skill;
import com.google.common.collect.Iterables;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collections;
import java.util.Map;

public interface ClassLevel {
    default void apply(Character character) {
        for (Feat feat : getFeats()) {
            character.addFeat(feat);
        }
    }

    default Iterable<ClassFeature> getFeatures() { return Collections.emptyList(); }

    //// FEATS ////

    default Iterable<Feat> getFeats() {
        return Iterables.concat(getAutomaticFeats(), getConfiguredFeats());
    }

    default Iterable<Feat> getAutomaticFeats() { return Collections.emptyList(); }

    default Iterable<Feat> getConfiguredFeats() { return Collections.emptyList(); }

    default Iterable<Class<? extends Feat>> getBonusFeatSlots() { return Collections.emptyList(); }

    //// SAVE & BAB PROGRESSIONS ////

    BaseAttackBonusProgression getBaseAttackBonusProgression();
    SaveProgression getFortitudeSaveProgression();
    SaveProgression getReflexSaveProgression();
    SaveProgression getWillSaveProgression();

    default SaveProgression getSaveProgression(Save save) {
        switch (save) {
            case FORT: return getFortitudeSaveProgression();
            case REFL: return getReflexSaveProgression();
            case WILL: return getWillSaveProgression();
        }
        // Should not happen
        throw new RuntimeException("Missing switch case ??!?");
    }

    //// SKILLS ////

    int getSkillRankSlots();
//    Iterable<Skill> getClassSkills();
//    int getUnspentSkillRanks();
//    Map<Skill, Integer> getSpentSkillRanks();
//    int getSpentSkillRanks(Skill skill);
//    void setSpentSkillRanks(Skill skill, int ranks);
}
