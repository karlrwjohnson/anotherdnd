package anotherdnd.model;

import anotherdnd.model.bio.CivicAlignment;
import anotherdnd.model.bio.MoralAlignment;
import anotherdnd.model.level.CharacterClass;
import anotherdnd.model.level.ClassLevel;
import anotherdnd.model.level.Fighter;
import anotherdnd.model.mechanics.Ability;
import anotherdnd.model.race.Human;
import anotherdnd.model.race.Race;

import java.lang.reflect.InvocationTargetException;

public interface Builder {

    class CharacterBuilder {
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
            character.setAbilityBaseScore(Ability.STR, strength);
            character.setAbilityBaseScore(Ability.DEX, dexterity);
            character.setAbilityBaseScore(Ability.CON, constitution);
            character.setAbilityBaseScore(Ability.INT, intelligence);
            character.setAbilityBaseScore(Ability.WIS, wisdom);
            character.setAbilityBaseScore(Ability.CHA, charisma);
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

    class CharacterLeveler {
        private final Character character;
        private ClassLevel classLevel;

        CharacterLeveler(Character character, CharacterClass characterClass) {
            this.character = character;

            Class<? extends ClassLevel> nextLevelClass = characterClass.nextLevel(character.getClassLevels())
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


    static void main(String[] args) {
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