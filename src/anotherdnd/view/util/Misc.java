package anotherdnd.view.util;

import javax.swing.*;
import java.awt.*;

import static anotherdnd.view.util.EZGridBag.gbc;
import static anotherdnd.view.util.EZGridBag.gw;

public interface Misc {
    static String formatBonus(int bonus) {
        if (bonus >= 0) {
            return "+" + bonus;
        } else {
            return "" + bonus;
        }
    }

    static Component spacer(int width, int height) {
        JPanel panel = new JPanel();
        Dimension size = new Dimension(width, height);
        panel.setMinimumSize(size);    // Both "Minimum" and "Preferred" are necessary so that GBL sizes
        panel.setPreferredSize(size);  // it the same regardless of whether it thinks it has "enough room"
        return panel;
    }

    static void setMinimumWidth(JPanel gridBagPanel, int width) {
        // This is hard to do since GBL doesn't seem to respect the minimum size if it can respect the preferred
        // size (you'd think it would enforce each dimension individually???)
        // So, we hack around it by giving it an empty element.
        gridBagPanel.add(spacer(width, 0), gbc(gw(99))); // 99 = "sufficiently large"... If I go too big, will GridBagLayout freak out?
    }
}
