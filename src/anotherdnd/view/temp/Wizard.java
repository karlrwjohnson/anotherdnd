package anotherdnd.view.temp;

import anotherdnd.view.util.ModelSync;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static anotherdnd.view.util.EZGridBag.*;
import static anotherdnd.view.util.Misc.spacer;
import static anotherdnd.view.util.ModelSync.sync;
import static anotherdnd.view.util.ModelSync.watch;

public class Wizard extends JPanel {

    private final JLabel  titleLabel     = new JLabel() {{ setFont(getFont().deriveFont(24.0f)); }};
    private final JButton cancelButton   = new JButton("Cancel");
    private final JButton nextButton     = new JButton();
    private final JButton previousButton = new JButton();

    private WizardScreen     screen;
    private JPanel           screenPanel = null;
    
    public interface WizardScreen {
        JPanel getPanel();
        String getTitle();
        boolean hasPrevious();
        boolean hasNext();
        default String getPreviousLabel() { return "< Previous"; }
        default String getNextLabel() { return "Next >"; }
        WizardScreen getPrevious();
        WizardScreen getNext();
    }

    public Wizard(WizardScreen initialScreen) {
        super(new GridBagLayout());
        setScreen(initialScreen);

        setBorder(new EmptyBorder(MARGIN, 2*MARGIN, MARGIN, 2*MARGIN)); // padding

        add(titleLabel, gbc(align(-1, 0), fill()));

        add(new JPanel(new GridBagLayout()) {{
            int x = 0;
            add(cancelButton,        gbc(gx(++x), wx(1), align(1, 0)));
            add(spacer(2*MARGIN, 0), gbc(gx(++x)));
            add(previousButton,      gbc(gx(++x)));
            add(nextButton,          gbc(gx(++x)));
        }}, gbc(gy(2), fill()));
        
        sync(titleLabel, () -> screen.getTitle());
//        watch(previousButton, (button, enabled) -> { System.out.println("enabled := " + enabled); button.setEnabled(enabled); }, () -> { System.out.println("hasPrevious = " + screen.hasPrevious()); return screen.hasPrevious(); });
        watch(previousButton, JButton::setEnabled, () -> screen.hasPrevious());
        watch(previousButton, JButton::setText,    () -> screen.getPreviousLabel());
        watch(nextButton,     JButton::setEnabled, () -> screen.hasNext());
        watch(nextButton,     JButton::setText,    () -> screen.getNextLabel());

        nextButton.addActionListener(e -> setScreen(screen.getNext()));
        previousButton.addActionListener(e -> setScreen(screen.getPrevious()));
    }

    private void setScreen(WizardScreen screen) {
        if (screenPanel != null) {
            remove(screenPanel);
        }
        this.screen = screen;
        screenPanel = screen.getPanel();
        if (screenPanel != null) {
            add(screenPanel, gbc(gy(1), wy(1), wx(1), noInsets(), fill()));
        }
        revalidate();
        repaint();
        ModelSync.scheduleUpdate();
    }
}
