package anotherdnd.model.skills;

import anotherdnd.model.mechanics.Ability;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

import static anotherdnd.model.util.Util.unCamelCase;

public interface Skill {

    Ability getAbility();
    boolean isUsableUntrained();
    boolean hasArmorPenalty();

    static Stream<Skill> getAllSkills() {
        return Stream.of(
            Arrays.stream(UntypedSkill.values()),
            Arrays.stream(TypedKnowledgeSkill.values()),
            Arrays.stream(TypedPerformSkill.values())
        ).flatMap(Function.identity());
    }

    enum UntypedSkill implements Skill {
        Acrobatics     (Ability.DEX, true,  true),
        Appraise       (Ability.INT, true,  false),
        Bluff          (Ability.CHA, true,  false),
        Climb          (Ability.STR, true,  true),
        Craft          (Ability.INT, true,  false),
        Diplomacy      (Ability.CHA, true,  false),
        DisableDevice  (Ability.DEX, false, true),
        Disguise       (Ability.CHA, true,  false),
        EscapeArtist   (Ability.DEX, true,  true),
        Fly            (Ability.DEX, true,  true),
        HandleAnimal   (Ability.CHA, false, false),
        Heal           (Ability.WIS, true,  false),
        Intimidate     (Ability.CHA, true,  false),
        Linguistics    (Ability.INT, false, false),
        Perception     (Ability.WIS, true,  false),
        Perform        (Ability.CHA, true,  false),
        Profession     (Ability.WIS, false, false),
        Ride           (Ability.DEX, true,  true),
        SenseMotive    (Ability.WIS, true,  false),
        SleightOfHand  (Ability.DEX, false, true),
        Spellcraft     (Ability.INT, false, false),
        Stealth        (Ability.DEX, true,  true),
        Survival       (Ability.WIS, true,  false),
        Swim           (Ability.STR, true,  true),
        UseMagicDevice (Ability.CHA, false, false);

        public final Ability ability;
        public final boolean untrained;
        public final boolean armorPenalty;

        private final String label = unCamelCase(this.name());

        private UntypedSkill (Ability ability, boolean untrained, boolean armorPenalty) {
            this.ability = ability;
            this.untrained = untrained;
            this.armorPenalty = armorPenalty;
        }

        public Ability getAbility() { return ability; }
        public boolean isUsableUntrained() { return untrained; }
        public boolean hasArmorPenalty() { return armorPenalty; }
    }

    interface KnowledgeSkill extends Skill {
        default Ability getAbility() { return Ability.INT; }
        default boolean isUsableUntrained() { return false; }
        default boolean hasArmorPenalty() { return false; }
    }

    enum TypedKnowledgeSkill implements KnowledgeSkill {
        Arcana        (),
        Dungeoneering (),
        Engineering   (),
        Geography     (),
        History       (),
        Local         (),
        Nature        (),
        Nobility      (),
        Planes        (),
        Religion      ();
    }

    Skill Knowledge = new Skill() {
        @Override
        public Ability getAbility() {
            return null;
        }

        @Override
        public boolean isUsableUntrained() {
            return false;
        }

        @Override
        public boolean hasArmorPenalty() {
            return false;
        }
    };

    interface PerformSkill extends Skill {
        default Ability getAbility() { return Ability.CHA; }
        default boolean isUsableUntrained() { return true; }
        default boolean hasArmorPenalty() { return false; }
    }

    enum TypedPerformSkill implements PerformSkill {
        Act,
        Comedy,
        Dance,
        Keyboard,
        Oratory,
        Percussion,
        String,
        Wind,
        Sing;
    }

}
