package anotherdnd.model.mechanics;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static anotherdnd.model.util.Util.unCamelCase;

public class DiceBonusSet {

    public enum BonusType {
        Untyped             (true),  // The holy grail of bonuses, usually an editing error
        Alchemical          (false), // e.g. antitoxin. Affects ability scores & saves
        Ability             (false), // To-hit ability bonus (ususally STR or DEX)
        Armor               (false), // Flat-footed AC
        BaseAttack          (false), //
        Circumstance        (false), // Stacks if from same source
        Competence          (false), //
        Deflection          (false), // Touch AC
        Dodge               (true),  // Touch AC, sometimes Reflex saves
        Enhancement         (false), // Ability scores, armor bonus, attacks, damage, speed
        Inherent            (false), // Manuals, Wish spell
        Insight             (false), //
        Luck                (false), //
        Morale              (false), //
        NaturalArmor        (false), //
        BonusToNaturalArmor (false), //
        Profane             (false), //
        Racial              (false), //
        Resistance          (false), //
        Sacred              (false), //
        Shield              (false), //
        Size                (false), //
        Trait               (false); //

        public final boolean stacks;
        private final String label;

        BonusType (boolean stacks) {
            this.stacks = stacks;
            this.label = unCamelCase(this.name());
        }

        @Override public String toString() {
            return label;
        }
    }

    public DiceBonusSet() {}

    public DiceBonusSet add(BonusType type, Object source, Dice amount) {
        bonuses.computeIfAbsent(type, t -> new HashMap<>()).put(source, amount);
        return this;
    }

    public DiceBonusSet addPenalty(Object source, Dice amount) {
        penalties.put(source, amount);
        return this;
    }

    public Dice total() {
        return _bonusSum(
            bonuses.entrySet().stream()
                .map(entry -> entry.getKey().stacks ?
                    _bonusSum(entry.getValue().values().stream()):
                    _bonusMax(entry.getValue().values().stream())
                )
        ).minus(
            _bonusSum(penalties.values().stream())
        );
    }

    private Map<BonusType, Map<Object, Dice>> bonuses = new EnumMap<>(BonusType.class);
    private Map<Object, Dice> penalties = new HashMap<>();

    private Dice _bonusSum (Stream<? extends Dice> bonusStream) {
        return bonusStream.collect(Collectors.reducing(Dice.NO_BONUS, Dice::sum));
    }

    private Dice _bonusMax (Stream<? extends Dice> bonusStream) {
        return Dice.of(bonusStream
            .mapToInt(Dice::getConstant)
            .max()
            .orElse(0)
        );
    }

}
