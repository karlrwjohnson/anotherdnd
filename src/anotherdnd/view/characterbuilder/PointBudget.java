package anotherdnd.view.characterbuilder;

import anotherdnd.model.util.SimpleObservable;

import java.util.HashSet;
import java.util.Set;

public class PointBudget extends SimpleObservable {
    public final int budget;
    private Set<PointBuyNumberModel> numberModels = new HashSet<>();

    public PointBudget() { this(25); }
    public PointBudget(int budget) { this.budget = budget; }

    public int getTotalPoints() { return this.budget; }

    public int getPointsRemaining() {
        return budget - this.numberModels.stream()
            .mapToInt(PointBuyNumberModel::getPointsSpent)
            .sum();
    }

    void addNumberModel(PointBuyNumberModel numberModel) {
        this.numberModels.add(numberModel);
    }

    int getPointsRemainingExcept(PointBuyNumberModel thisModel) {
        return budget - this.numberModels.stream()
            .filter(model -> model != thisModel)
            .mapToInt(PointBuyNumberModel::getPointsSpent)
            .sum();
    }
}
