package anotherdnd.view.util;

import javax.swing.*;
import java.awt.*;

public interface Misc {
    static String formatBonus(int bonus) {
        if (bonus >= 0) {
            return "+" + bonus;
        } else {
            return "" + bonus;
        }
    }

    static void setMinimumWidth(JPanel gridBagPanel, int width) {
        // This is hard to do since GBL doesn't seem to respect the minimum size if it can respect the preferred
        // size (you'd think it would enforce each dimension individually???)
        // So, we hack around it by giving it an empty element.
        JPanel panel = new JPanel();
        panel.setMinimumSize(new Dimension(width, 0));    // Both "Minimum" and "Preferred" are necessary so that GBL sizes
        panel.setPreferredSize(new Dimension(width, 0));  // it the same regardless of whether it thinks it has "enough room"
        gridBagPanel.add(panel, new GridBagConstraints());
    }
}
