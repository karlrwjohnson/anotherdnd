package anotherdnd.model.mechanics;

import anotherdnd.model.weapons.Weapons;

import static anotherdnd.model.mechanics.Die.*;

public class Dice {

    public static final Dice NO_BONUS = Dice.of(0);
    public static final Dice _1d2 = Dice.of(D2);
    public static final Dice _1d3 = Dice.of(D3);
    public static final Dice _1d4 = Dice.of(D4);
    public static final Dice _1d6 = Dice.of(D6);
    public static final Dice _1d8 = Dice.of(D8);
    public static final Dice _1d10 = Dice.of(D10);
    public static final Dice _1d12 = Dice.of(D12);
    public static final Dice _2d6 = Dice.of(D6, D6);
    public static final Dice _3d6 = Dice.of(D6, D6, D6);
    public static final Dice _4d6 = Dice.of(D6, D6, D6, D6);

    public static Dice of(int constant, Die... dice) {
        return new Dice(constant, dice);
    }

    public static Dice of(int constant) {
        return new Dice(constant, NO_DICE);
    }

    public static Dice of(Die... dice) {
        return new Dice(0, dice);
    }

    public static Dice sum (Dice lhs, Dice rhs) {
        return lhs.plus(rhs);
    }

    public Dice plus (Dice other) {
        int newCounts[] = new int[values().length];
        System.arraycopy(counts, 0, newCounts, 0, counts.length);

        for (int i = 0; i < newCounts.length; i++) {
            newCounts[i] += other.counts[i];
        }

        return new Dice(newCounts);
    }

    public Dice plus (int constant, Die... dice) {
        int newCounts[] = new int[values().length];
        System.arraycopy(counts, 0, newCounts, 0, counts.length);

        for (Die die : dice) {
            newCounts[die.ordinal()]++;
        }

        return new Dice(newCounts);
    }

    public Dice plus (int constant) {
        return plus(constant, NO_DICE);
    }

    public Dice plus (Die... dice) {
        return plus(0, dice);
    }

    public Dice minus (Dice other) {
        int newCounts[] = new int[values().length];
        System.arraycopy(counts, 0, newCounts, 0, counts.length);

        for (int i = 0; i < newCounts.length; i++) {
            newCounts[i] -= other.counts[i];
        }

        return new Dice(newCounts);
    }

    public Dice minus (int constant) {
        return plus(-constant, NO_DICE);
    }

    public int getConstant() { return counts[0]; }

    public String toString() {
        return string;
    }

    public int roll() {
        int total = 0;
        Die[] dieSizes = Die.values();
        for (int i = 1; i < counts.length; i++) {
            total += dieSizes[i].roll(counts[i]);
        }
        return total;
    }

    private static final Die[] NO_DICE = new Die[]{};
    private final int counts[];
    private final String string;

    private Dice(int counts[]) {
        this.counts = counts;
        this.string = buildString(counts);
    }

    private Dice(int constant, Die... dice) {
        counts = new int[values().length];
        counts[0] = constant;
        for (Die die : dice) {
            counts[die.ordinal()]++;
        }
        this.string = buildString(counts);
    }

    private static String buildString(int counts[]) {
        final Die[] dieValues = values();

        final StringBuilder sb = new StringBuilder();
        boolean builderHasText = false;

        // Dice-to-String
        for (int i = dieValues.length - 1; i > 0; i--) {
            if (counts[i] > 0) {
                if (builderHasText && counts[i] >= 0) {
                    sb.append('+');
                }

                sb.append(counts[i]);
                sb.append('d');
                sb.append(dieValues[i].size);

                builderHasText = true;
            }
        }

        if (counts[0] == 0) {
            if (!builderHasText) {
                sb.append('0');
            }
        }
        if (counts[0] > 0) {
            if (builderHasText) {
                sb.append('+');
            }
            sb.append(counts[0]);
        } else if (!builderHasText) {
            sb.append('0');
        }

        return sb.toString();
    }
}
