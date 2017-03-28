package anotherdnd.view.characterbuilder;

import javax.swing.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class PointBuyNumberModel extends AbstractSpinnerModel {

    private static final int[] POINTS_BY_SCORE = new int[]{ /*0*/ 0,0,0,0,0,0,0, /* 7 */ -4,-2,-1, /*10*/ 0,1,2,3,5,7,10,13, /*18*/ 17 };
    private static final int MINIMUM = 7;
    private static final int MAXIMUM = 18;


    private int score = 10;
    private final PointBudget budget;

    public PointBuyNumberModel(PointBudget budget) {
        this.budget = budget;
        this.budget.addNumberModel(this);
    }

    public int getScore() { return score; }

    public int getPointsSpent() {
        return POINTS_BY_SCORE[score];
    }

    @Override
    public Object getValue() {
        return score;
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Number) {
            score = rail(((Number) value).intValue());
            fireStateChanged();
            budget.fireObservers();
        } else {
            throw new IllegalArgumentException("Not a number: got " + value + (value != null ? " (" + value.getClass() + ")" : ""));
        }
    }

    @Override
    public Object getNextValue() {
        return rail(score + 1);
    }

    @Override
    public Object getPreviousValue() {
        return rail(score - 1);
    }

    private int rail(int value) {
        value = min(value, MAXIMUM);
        value = max(value, MINIMUM);
        if (value > score && budget.getPointsRemainingExcept(this) < POINTS_BY_SCORE[value]) {
            return score;
        }
        return value;
    }
}
