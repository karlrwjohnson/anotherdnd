package anotherdnd.model.mechanics;

import anotherdnd.model.util.Immutable;

import java.lang.ref.WeakReference;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static anotherdnd.model.util.MoreStreams.intStream;
import static anotherdnd.model.util.Util.unCamelCase;

public class BonusSet {

    public interface BonusSource {}

    public enum BonusType {
        Untyped             (true),  // The holy grail of bonuses, usually an editing error
        Ability             (false),
        Alchemical          (false), // e.g. antitoxin. Affects ability scores & saves
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

    @Immutable
    @SuppressWarnings("WeakerAccess")
    public static class Bonus {
        public final BonusType type;
        public final int amount;
        public final BonusSource source;

        public Bonus (BonusType type, int amount, BonusSource source) {
            this.type = type;
            this.amount = amount;
            this.source = source;
        }

        public BonusType getType() { return type; }
        public int getAmount() { return amount; }
        public Object getSource() { return source; }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Bonus && other.getClass() == Bonus.class) {
                Bonus otherBonus = (Bonus) other;
                return otherBonus.type == type
                    && otherBonus.amount == amount
                    && otherBonus.source == source;
            } else {
                return false;
            }
        }
    }

    @Immutable
    @SuppressWarnings("WeakerAccess")
    public static class Penalty {
        public final int amount;
        public final BonusSource source;

        public Penalty (int amount, BonusSource source) {
            this.amount = amount;
            this.source = source;
        }

        public int getAmount() { return amount; }
        public Object getSource() { return source; }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Penalty && other.getClass() == Penalty.class) {
                Penalty otherBonus = (Penalty) other;
                return otherBonus.amount == amount
                    && otherBonus.source == source;
            } else {
                return false;
            }
        }
    }

    public static class Remover {
        private WeakReference<BonusSet> bonusSetRef;
        private BonusSource bonusSource;

        Remover(BonusSet bonusSet, BonusSource bonusSource) {
            this.bonusSetRef = new WeakReference<BonusSet>(bonusSet);
            this.bonusSource = bonusSource;
        }

        void removeSource() {
            BonusSet bonusSet = bonusSetRef.get();
            if (bonusSet != null) {
                bonusSet.removeSource(bonusSource);
                bonusSetRef.clear();
            }
        }
    }

    public BonusSet() {}

    public Remover add(Bonus bonus) {
        bonuses.computeIfAbsent(bonus.type, t -> new HashMap<>()).put(bonus.source, bonus.amount);
        return new Remover(this, bonus.source);
    }

    public Remover add(Penalty penalty) {
        penalties.put(penalty.source, penalty.amount);
        return new Remover(this, penalty.source);
    }

    public Remover addBonus(BonusType type, BonusSource source, int amount) {
        bonuses.computeIfAbsent(type, t -> new HashMap<>()).put(source, amount);
        return new Remover(this, source);
    }

    public Remover addPenalty(BonusSource source, int amount) {
        penalties.put(source, amount);
        return new Remover(this, source);
    }

    public void removeSource(BonusSource source) {
        // I'd prefer immutable objects...
        bonuses.entrySet().forEach(entry -> entry.getValue().remove(source));
        penalties.remove(source);
    }

    public int total() {
        int allBonuses = bonuses.entrySet().stream()
            .mapToInt(entry -> {
                IntStream bonusesOfType = intStream(entry.getValue().values());
                return entry.getKey().stacks ?
                    bonusesOfType.sum() :
                    bonusesOfType.max().orElse(0);
            }).sum();

        int allPenalties = intStream(penalties.values()).sum();

        return allBonuses - allPenalties;
    }

    private Map<BonusType, Map<Object, Integer>> bonuses = new EnumMap<>(BonusType.class);
    private Map<Object, Integer> penalties = new HashMap<>();
}
