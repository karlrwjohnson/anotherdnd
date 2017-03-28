package anotherdnd.model.mechanics;

import java.util.List;

public enum SaveProgression {
    /*
    Save    Good        Poor
    (Lvl 0)         2           0
     Lvl 1       2  2½       0  0⅓
     Lvl 2      +3  3        0  0⅔
     Lvl 3       3  3½      +1  1
     Lvl 4      +4  4        1  1⅓
     Lvl 5       4  4½       1  1⅔
     Lvl 6      +5  5       +2  2
     Lvl 7       5  5½       2  2⅓
     Lvl 8      +6  6        2  2⅔
     Lvl 9       6  6½      +3  3
     */

    POOR (2.0f, 1.0f/2),
    GOOD (0.0f, 1.0f/3);

    public final float base;
    public final float rate;

    // With IEEE floats, floor(1/3.0f + 1/3.0f + 1/3.0f) == 0.
    // Counter the rouding error by adding a number barely small
    // enough not to have an effect on the calculation.
    private static final float FLOATING_POINT_ERROR_FIX = 1.0f / 6;

    SaveProgression(float base, float rate) {
        this.base = base;
        this.rate = rate;
    }

    public static int getTotalSaveBonus(List<SaveProgression> saves) {
        if (saves.size() > 0) {
            float total = saves.get(0).base + FLOATING_POINT_ERROR_FIX;
            for (SaveProgression save : saves) {
                total += save.rate;
            }
            return (int) total;
        } else {
            return 0;
        }
    }
}
