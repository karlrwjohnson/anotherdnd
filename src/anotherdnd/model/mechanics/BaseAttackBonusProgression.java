package anotherdnd.model.mechanics;

import java.util.List;

public enum BaseAttackBonusProgression {

    POOR(0.5f),
    AVERAGE(0.75f),
    GOOD(1.0f);

    public final float rate;

    BaseAttackBonusProgression (float rate) {
        this.rate = rate;
    }

    public static int getTotalBaseAttackBonus(List<BaseAttackBonusProgression> babs) {
        float total = 0.0f;
        for (BaseAttackBonusProgression bab : babs) {
            total += bab.rate;
        }
        return (int) total;
    }
}
