package anotherdnd.model.level;

import anotherdnd.model.Character;
import anotherdnd.model.feats.Feat;
import anotherdnd.model.mechanics.Hooks;
import anotherdnd.model.feats.category.CombatFeat;
import anotherdnd.model.mechanics.Dice;
import anotherdnd.model.mechanics.Die;
import anotherdnd.model.skills.Skill;
import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.List;

import static anotherdnd.model.skills.Skill.TypedKnowledgeSkill.*;
import static anotherdnd.model.skills.Skill.UntypedSkill.*;

public interface Rogue {

    abstract class SneakAttack implements ClassFeature, Hooks.ModifiesDamageRoll {

//        @Override public Dice calculateDamage(Character character, Dice damage) {
//            //TODO: This should ONLY be applied when opponent is flat-footed!
//            return damage.plus(Die.D6);
//        }
    }

    interface RogueLevel extends ClassLevel,
        ProgressionMixins.AverageBaseAttackBonusProgression,
        ProgressionMixins.PoorFortitudeSaveProgression,
        ProgressionMixins.GoodReflexSaveProgression,
        ProgressionMixins.PoorWillSaveProgression
    {
        List<Skill> classSkills = ImmutableList.of(
            Acrobatics,     Appraise,       Bluff,          Climb,          Craft,          Diplomacy,
            DisableDevice,  Disguise,       EscapeArtist,   Intimidate,     Dungeoneering,  Local,
            Linguistics,    Perception,     Perform,        Profession,     SenseMotive,    SleightOfHand,
            Stealth,        Swim,           UseMagicDevice
        );

        default int getSkillRankSlots() { return 2; }
        default Iterable<Skill> getClassSkills() {  return classSkills; }
    }

    class RogueLevel1 implements RogueLevel {
        @Override public Iterable<Class<? extends Feat>> getBonusFeatSlots() {
            return Collections.singleton(CombatFeat.class);
        }
    }
}
