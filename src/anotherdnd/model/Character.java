package anotherdnd.model;

import anotherdnd.model.bio.CivicAlignment;
import anotherdnd.model.bio.Sex;
import anotherdnd.model.bio.MoralAlignment;
import anotherdnd.model.level.CharacterClass;
import anotherdnd.model.level.ClassLevel;
import anotherdnd.model.feats.Feat;
import anotherdnd.model.level.Fighter;
import anotherdnd.model.mechanics.*;
import anotherdnd.model.mechanics.Attacks.Attack;
import anotherdnd.model.mechanics.BonusSet.BonusSource;
import anotherdnd.model.mechanics.Hooks.Hook;
import anotherdnd.model.race.Human;
import anotherdnd.model.race.Race;
import anotherdnd.model.weapons.Weapon;
import com.google.common.collect.Streams;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static anotherdnd.model.util.MoreStreams.filterInstancesOf;
import static anotherdnd.model.util.Util.newEnumMap;

public class Character implements BonusSource {

    private String           name           = "";
    private Race             race           = new Human();
    private Sex              sex            = Sex.NONE;
    private MoralAlignment   moralAlignment = MoralAlignment.NEUTRAL;
    private CivicAlignment   civicAlignment = CivicAlignment.NEUTRAL;
    private EnumMap<Ability,
               AbilityScore> abilityScores  = newEnumMap(Ability.class, ability -> new AbilityScore(ability, this, 10));
    private EnumMap<Save,
                SavingThrow> saves          = newEnumMap(Save.class, save -> new SavingThrow(save, this));
    private Set<Feat>        feats          = new HashSet<>();
    private List<ClassLevel> classLevels    = new ArrayList<>();
    private Set<Item>        inventory      = new HashSet<>();


    //private final Weapon punch = new Weapons.Punch(); // Somehow put this into the list of attacks.

    //// Public API ////

    public Character() {}

    public String         getName()           { return name; }
    public Race           getRace()           { return race; }
    public Sex            getSex()            { return sex; }
    public MoralAlignment getMoralAlignment() { return moralAlignment; }
    public CivicAlignment getCivicAlignment() { return civicAlignment; }

    public Character setName          (String         x) { name           = x; return this; }
    public Character setRace          (Race           x) { race           = x; return this; }
    public Character setSex           (Sex            x) { sex            = x; return this; }
    public Character setMoralAlignment(MoralAlignment x) { moralAlignment = x; return this; }
    public Character setCivicAlignment(CivicAlignment x) { civicAlignment = x; return this; }

    public Stream<ClassLevel> getClassLevels() {
        return classLevels.stream();
    }

    public int countClassLevels(Class<? extends ClassLevel> theClass) {
        return (int) classLevels.stream()
            .flatMap(filterInstancesOf(theClass))
            .count();
    }

    public void addFeat(Feat feat) {
        this.feats.add(feat);
    }
    public boolean hasFeat(Feat feat) { return this.feats.contains(feat); }
    public boolean hasFeatOfType(Class<? extends Feat> featType) {
        for (Feat feat : feats) {
            if (feat.getClass() == featType) {
                return true;
            }
        }
        return false;
    }

    public <R extends Hook> Stream<R> getHooks(Class<R> hookType) {
        return Streams.concat(
            Stream.of(race),
            classLevels.stream(),
            feats.stream(),
            inventory.stream()
        ).flatMap(filterInstancesOf(hookType));
    }

    public Stream<Attack> getAttacks() {
        return inventory.stream()
                .flatMap(filterInstancesOf(Weapon.class))
                .map(weapon -> weapon.getAttacks(this))
                .flatMap(Collection::stream);
    }

    public int getAbilityBaseScore(Ability ability) { return abilityScores.get(ability).getBaseScore(); }
    public void setAbilityBaseScore(Ability ability, int score) { abilityScores.get(ability).setBaseScore(score); }
    public int getAbilityScore(Ability ability) {
        return abilityScores.get(ability).getScore();
    }
    public int getAbilityBonus(Ability ability) {
        return abilityScores.get(ability).getBonus();
    }

    public int getSaveBonus(Save save) {
        return saves.get(save).getBonus();
    }

    public int getBaseAttackBonus() {
        return BaseAttackBonusProgression.getTotalBaseAttackBonus(
            getClassLevels()
                .map(ClassLevel::getBaseAttackBonusProgression)
                .collect(Collectors.toList())
        );
    }




    public static class CharacterBuilder {
        private final Character character = new Character();
        private CharacterBuilder() {}

        public static CharacterBuilder newCharacter() {
            return new CharacterBuilder();
        }

        public CharacterBuilder setName(String name) {
            character.setName(name);
            return this;
        }

        public CharacterBuilder stats(int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma) {
            character.abilityScores.get(Ability.STR).setBaseScore(strength);
            character.abilityScores.get(Ability.DEX).setBaseScore(dexterity);
            character.abilityScores.get(Ability.CON).setBaseScore(constitution);
            character.abilityScores.get(Ability.INT).setBaseScore(intelligence);
            character.abilityScores.get(Ability.WIS).setBaseScore(wisdom);
            character.abilityScores.get(Ability.CHA).setBaseScore(charisma);
            return this;
        }

        public CharacterBuilder race(Class<? extends Race> race) {
            return this;
        }

        public CharacterBuilder alignment(MoralAlignment moralAlignment, CivicAlignment civicAlignment) {
            return this;
        }

        public CharacterLeveler firstLevel(CharacterClass characterClass) {
            return new CharacterLeveler(character, characterClass);
        }

        public Character build() {
            return character;
        }
    }

    public static class CharacterLeveler {
        private final Character character;
        private ClassLevel classLevel;

        CharacterLeveler(Character character, CharacterClass characterClass) {
            this.character = character;

            Class<? extends ClassLevel> nextLevelClass = characterClass.nextLevel(character.classLevels)
                .orElseThrow(() -> new RuntimeException(String.format("No levels of %s left to take", characterClass.getClass().getSimpleName())));

            try {
                classLevel = nextLevelClass.getConstructor().newInstance();
            } catch (InstantiationException
                    | IllegalAccessException
                    | InvocationTargetException
                    | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        public Character apply() {
            return character;
        }

        public int getSkillPointsRemaining() {
            return classLevel.getSkillRankSlots() + character.getAbilityBonus(Ability.INT);
        }

//        public Map<Skill, Integer> getSkillPointsSpent() {
//
//        }

//        public CharacterLeveler spendSkillPoints(Skill skill, int points) {
//
//        }
    }


    public static void main(String[] args) {
        Character me = CharacterBuilder.newCharacter()
            .setName("Wingblade")
            .stats(14, 13, 12, 11, 10, 9)
            .race(Human.class)
            .alignment(MoralAlignment.GOOD, CivicAlignment.NEUTRAL)
            .firstLevel(new Fighter()) // I don't like this...
            .apply();
        // character should be level 0.


    }
}
