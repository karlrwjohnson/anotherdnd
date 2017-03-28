package anotherdnd.model.level;

import anotherdnd.model.Character;
import anotherdnd.model.level.CharacterClass.ClassLevels;
import anotherdnd.model.level.ClassLevelMixins.*;
import anotherdnd.model.mechanics.DiceBonusSet;
import anotherdnd.model.mechanics.DiceBonusSet.BonusType;
import anotherdnd.model.mechanics.Dice;
import anotherdnd.model.mechanics.Hooks.*;
import anotherdnd.model.skills.Skill;
import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.List;

import static anotherdnd.model.skills.Skill.UntypedSkill.*;
import static anotherdnd.model.skills.Skill.TypedKnowledgeSkill.*;

@ClassLevels({
    Fighter.FighterLevel1.class,
    Fighter.FighterLevel2.class
})
public class Fighter implements CharacterClass {


    public interface FighterLevel extends ClassLevel,
        ProgressionMixins.GoodBaseAttackBonusProgression,
        ProgressionMixins.GoodFortitudeSaveProgression,
        ProgressionMixins.PoorReflexSaveProgression,
        ProgressionMixins.PoorWillSaveProgression
    {
        List<Skill> classSkills = ImmutableList.of(
            Climb,      Craft,       HandleAnimal,     Intimidate,          Profession,
            Ride,       Survival,    Swim,             Dungeoneering,       Engineering
        );

        default int getSkillRankSlots() { return 2; }
        default Iterable<Skill> getClassSkills() { return classSkills; }
    }

    public static class FighterLevel1 implements FighterLevel, GainCombatBonusFeat {
    }

    public static class FighterLevel2 implements FighterLevel, GainCombatBonusFeat {
        private final Bravery bravery = new Bravery();

        @Override public Iterable<ClassFeature> getFeatures() {
            return Collections.singleton(bravery);
        }
    }

    //// Class Features ////

    // Level 2
    public static class Bravery implements ClassFeature, ModifiesWillSaveRoll {
        @Override public void modifyWillSave(Character character, DiceBonusSet save) {
            final int fighterLevels = character.countClassLevels(FighterLevel.class);
            final int bonus = 1 + (fighterLevels - 2) / 4;
            save.add(BonusType.Untyped, this, Dice.of(bonus));
        }
    }
}
