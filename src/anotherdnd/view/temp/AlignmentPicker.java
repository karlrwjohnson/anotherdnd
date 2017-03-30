package anotherdnd.view.temp;

import anotherdnd.model.bio.CivicAlignment;
import anotherdnd.model.bio.MoralAlignment;
import anotherdnd.view.util.IObservable;

import javax.swing.*;
import java.awt.*;

class AlignmentPicker extends JPanel implements IObservable<AlignmentPicker, Void> {
    private static class AlignmentButton extends JButton {
        private final AlignmentPicker picker;
        public final CivicAlignment civicAlignment;
        public final MoralAlignment moralAlignment;

        public AlignmentButton(AlignmentPicker picker, CivicAlignment civicAlignment, MoralAlignment moralAlignment, Color backgroundColor) {
            this.picker = picker;
            this.civicAlignment = civicAlignment;
            this.moralAlignment = moralAlignment;
//                setUI(new MotifButtonUI());
//                setBackground(backgroundColor);

            setText((civicAlignment == CivicAlignment.NEUTRAL && moralAlignment == MoralAlignment.NEUTRAL) ?
                    "<html><center>True<br />Neutral</center></html>" :
                    String.format("<html><center>%s<br />%s</center></html>", civicAlignment.toString(), moralAlignment.toString())
            );
            addActionListener(e -> picker.clicked(this));
        }

        @Override
        protected void processEvent(AWTEvent e) {
            super.processEvent(e);
            picker.processEvent(e);
        }
    }

    private final AlignmentButton alignmentButtons[] = new AlignmentButton[]{
            new AlignmentButton(this, CivicAlignment.LAWFUL, MoralAlignment.GOOD, new Color(0x88ccff)),
            new AlignmentButton(this, CivicAlignment.NEUTRAL, MoralAlignment.GOOD, new Color(0x88eeff)),
            new AlignmentButton(this, CivicAlignment.CHAOTIC, MoralAlignment.GOOD, new Color(0x88ee99)),
            new AlignmentButton(this, CivicAlignment.LAWFUL, MoralAlignment.NEUTRAL, new Color(0xaa88ff)),
            new AlignmentButton(this, CivicAlignment.NEUTRAL, MoralAlignment.NEUTRAL, new Color(0xcccccc)),
            new AlignmentButton(this, CivicAlignment.CHAOTIC, MoralAlignment.NEUTRAL, new Color(0xcccc88)),
            new AlignmentButton(this, CivicAlignment.LAWFUL, MoralAlignment.EVIL, new Color(0xcc88cc)),
            new AlignmentButton(this, CivicAlignment.NEUTRAL, MoralAlignment.EVIL, new Color(0x999999)),
            new AlignmentButton(this, CivicAlignment.CHAOTIC, MoralAlignment.EVIL, new Color(0xcc8888)),
    };

    private AlignmentButton selected = null;

    public AlignmentPicker() {
        super(new GridLayout(3, 3));
        for (JButton button : alignmentButtons) {
            add(button);
        }
        clicked(alignmentButtons[4]);
    }

    public CivicAlignment getCivicAlignment() {
        return selected.civicAlignment;
    }

    public MoralAlignment getMoralAlignment() {
        return selected.moralAlignment;
    }

    private void clicked(AlignmentButton button) {
        if (button == null) {
            throw new NullPointerException();
        }
        if (button != selected) {
            if (selected != null) {
                selected.setSelected(false);
            }
            selected = button;
            selected.setSelected(true);
            notifyObservers();
        }
    }
}
