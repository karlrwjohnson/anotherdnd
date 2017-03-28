package anotherdnd.view.customui;

import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;

import static anotherdnd.view.util.EZGridBag.*;
import static java.awt.GridBagConstraints.BOTH;

public class AbilitySpinnerUI extends BasicSpinnerUI {

    private static class AbilitySpinnerLayout extends GridBagLayout {
        @Override
        public void addLayoutComponent(Component component, Object name) {
            if("Next".equals(name)) {
                super.addLayoutComponent(component, gbc(gx(2), wy(1), fill(BOTH), noInsets()));
            } else if("Previous".equals(name)) {
                super.addLayoutComponent(component, gbc(gx(0), wy(1), fill(BOTH), noInsets()));
            } else if("Editor".equals(name)) {
                super.addLayoutComponent(component, gbc(gx(1), wy(1), wx(1), fill(BOTH), noInsets()));
            }
        }
    }

    @Override
    public LayoutManager createLayout() {
        return new AbilitySpinnerLayout();
    }
}
