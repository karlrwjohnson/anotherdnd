package anotherdnd.view.temp;

import anotherdnd.model.bio.CivicAlignment;
import anotherdnd.model.bio.MoralAlignment;
import anotherdnd.model.bio.Sex;
import anotherdnd.model.race.Race;
import anotherdnd.view.temp.Wizard.WizardScreen;
import anotherdnd.view.util.EnumListModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Vector;

import static anotherdnd.view.util.EZGridBag.*;
import static anotherdnd.view.util.Misc.setMinimumWidth;
import static java.awt.GridBagConstraints.NORTH;

public class CharacterBuilderScreen1 extends JPanel implements WizardScreen {

    private final JTextField                nameField           = new JTextField() {{ setBackground(Color.WHITE); }};
    private final AlignmentPicker           alignmentPicker     = new AlignmentPicker();
    private final JComboBox<Sex>            sexField            = new JComboBox<>(new EnumListModel<>(Sex.class));
    private final JComboBox<Race>           raceField           = new JComboBox<>(new Vector<>());

    public CharacterBuilderScreen1() {
        super(new GridBagLayout());

        add(new InfoPanel(getClass().getName()) {{
                setBorder(new EmptyBorder(0, 0, 0, 2*MARGIN)); // padding

                showForComponent(nameField, "name");
                showForComponent(raceField, "race");
                showForComponent(alignmentPicker, "alignment");
                showForComponent(sexField, "sex");
        }}, gbc(gx(0), wx(1), wy(1), noInsets(), fill(), align(0, -1)));

        add(new JPanel(new GridBagLayout()) {{

            setMinimumWidth(this, 200);

            int y = 0;

            add(new JLabel("Name"),      gbc(gy(++y), fill(), wx(1)));
            add(nameField,               gbc(gy(++y), fill()));

            add(new JLabel("Race"),      gbc(gy(++y), fill()));
            add(raceField,               gbc(gy(++y), fill()));

            add(new JLabel("Alignment"), gbc(gy(++y), fill()));
            add(alignmentPicker,         gbc(gy(++y), fill()));

            add(new JLabel("Sex"),       gbc(gy(++y), fill()));
            add(sexField,                gbc(gy(++y), fill()));

        }}, gbc(gx(1), wy(1), noInsets(), anchor(NORTH)));
    }

    @Override public JPanel getPanel() { return this; }
    @Override public String getTitle() { return "Create Character"; }
    @Override public boolean hasPrevious() { return false; }
    @Override public boolean hasNext() { return true; }
    @Override public WizardScreen getPrevious() { return null; }
    @Override public WizardScreen getNext() { return new CharacterBuilderScreen2(this); }

    private static class AlignmentPicker extends JPanel {
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
            new AlignmentButton(this, CivicAlignment.LAWFUL,  MoralAlignment.GOOD   , new Color(0x88ccff)),
            new AlignmentButton(this, CivicAlignment.NEUTRAL, MoralAlignment.GOOD   , new Color(0x88eeff)),
            new AlignmentButton(this, CivicAlignment.CHAOTIC, MoralAlignment.GOOD   , new Color(0x88ee99)),
            new AlignmentButton(this, CivicAlignment.LAWFUL,  MoralAlignment.NEUTRAL, new Color(0xaa88ff)),
            new AlignmentButton(this, CivicAlignment.NEUTRAL, MoralAlignment.NEUTRAL, new Color(0xcccccc)),
            new AlignmentButton(this, CivicAlignment.CHAOTIC, MoralAlignment.NEUTRAL, new Color(0xcccc88)),
            new AlignmentButton(this, CivicAlignment.LAWFUL,  MoralAlignment.EVIL   , new Color(0xcc88cc)),
            new AlignmentButton(this, CivicAlignment.NEUTRAL, MoralAlignment.EVIL   , new Color(0x999999)),
            new AlignmentButton(this, CivicAlignment.CHAOTIC, MoralAlignment.EVIL   , new Color(0xcc8888)),
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
            }
        }
    }
}
