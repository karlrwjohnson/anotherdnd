package anotherdnd.view.temp;

import anotherdnd.model.bio.CivicAlignment;
import anotherdnd.model.bio.MoralAlignment;
import anotherdnd.model.bio.Sex;
import anotherdnd.model.race.Race;
import anotherdnd.view.util.EnumListModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.*;

import static anotherdnd.view.util.EZGridBag.*;
import static anotherdnd.view.util.Events.onFocus;
import static anotherdnd.view.util.Events.onMouseOver;
import static anotherdnd.view.util.Misc.setMinimumWidth;
import static java.awt.GridBagConstraints.HORIZONTAL;

public class CharacterBuilderScreen1 extends JPanel {
    private final CharacterBuilderScreen1 that = this;

    private final JTextField                nameField           = new JTextField() {{ setBackground(Color.WHITE); }};
    private final AlignmentPicker           alignmentPicker     = new AlignmentPicker();
    private final JComboBox<Sex>            sexField            = new JComboBox<>(new EnumListModel<>(Sex.class));
    private final JComboBox<Race>           raceField           = new JComboBox<>(new Vector<>());
    private final JLabel                    raceDescription     = new JLabel();

    private final JLabel                    infoTitle           = new JLabel() {{ setFont(getFont().deriveFont(18.0f)); setBorder(new MatteBorder(0, 0, 1, 0, new Color(0x666666))); setVisible(true); }};
    private final JLabel                    infoContent         = new JLabel();

    private final HashMap<Component, String[]> infoPanels = new HashMap<Component, String[]>() {
        {
            put(nameField, new String[]{
                "Name",
                "<html>" +
                    "<p>Your character's name identifies him or her in the world. It can be unique and meaningful, or common.</p>" +
                    "</html>"
            });
            put(raceField, new String[]{
                "Race",
                "<html>" +
                    "<p>The world is full of folks of diverse origins and backgrounds, not all of them human.</p>" +
                    "<p>Your character's race doesn't just affect their backstory; different races are granted different benefits</p>" +
                    "</html>"
            });
            put(alignmentPicker, new String[]{
                "Alignment",
                "<html>" +
                    "<p>How does your character react in ethically-challenging situations? Your character's alignment is a" +
                    "generalization of their moral compass and civic sense of duty.</p>" +
                    "<br />" +
                    "<p><b>Moral Axis</b> (vertical)</p>" +
                    "<ul>" +
                    "<li><b>Good</b> characters generally desire to help people and want to make the world become a better place for everyone.</li>" +
                    "<li><b>Evil</b> characters are primarily driven by self-interest and do not care who they hurt while achieving their goals.</li>" +
                    "<li><b>Neutral</b> characters fall somewhere in between, helping people when it's convenient but are content to let others save the world.</li>" +
                    "</ul>" +
                    "<p><b>Civic Axis</b> (horizontal)</p>" +
                    "<ul>" +
                    "<li><b>Lawful</b> characters believe that the law is vital to keeping the world in balance and wish to uphold it.</li>" +
                    "<li><b>Chaotic</b> characters believe that laws are overly-restrictive. If they can get away with it, they tend to make their own rules.</li>" +
                    "<li><b>Neutral</b> characters take a pragmatic approach, believing that laws are \"guidelines\" that uphold the peace most of the time, " +
                    "but some situations call for them to be broken.</li>" +
                    "</ul>" +
                    "</html>"
            });
            put(sexField, new String[]{
                "Sex/Gender",
                "<html>" +
                    "<p>Your character's sex does not affect game mechanics in any meaningful way.</p>" +
                    "</html>"
            });

            for (Component component : keySet()) {
                component.addFocusListener(onFocus(this::showInfoPanelFor));
                component.addMouseListener(onMouseOver(this::showInfoPanelFor));
            }
        }

        private void showInfoPanelFor(Component component) {
            String strings[];
            for (strings = null; strings == null; component = component.getParent()) {
                strings = get(component);
            }

            infoTitle.setVisible(true);
            infoTitle.setText(strings[0]);
            infoContent.setText(strings[1]);
        }
    };

    public CharacterBuilderScreen1() {
        super(new GridBagLayout());
        setBorder(new EmptyBorder(MARGIN, 2*MARGIN, MARGIN, 2*MARGIN)); // padding

        add(new JLabel("Create Character") {{ setFont(getFont().deriveFont(24.0f)); }}, gbc(gw(2), align(-1, 0), fill()));

        add(new JPanel(new GridBagLayout()) {{
            int y = 0;

            setMinimumWidth(this, 200);

            add(new JLabel("Name"),      gbc(gy(++y), fill()));
            add(nameField,               gbc(gy(++y), fill()));

            add(new JLabel("Race"),      gbc(gy(++y), fill()));
            add(raceField,               gbc(gy(++y), fill()));

            add(new JLabel("Alignment"), gbc(gy(++y), fill()));
            add(alignmentPicker,         gbc(gy(++y), fill()));

            add(new JLabel("Sex"),       gbc(gy(++y), fill()));
            add(sexField,                gbc(gy(++y), fill()));

            add(raceDescription,         gbc(gy(++y), fill()));
        }}, gbc(gy(1), wy(1), noInsets(), align(0, -1)));

        add(new JPanel(new GridBagLayout()) {{
            setBorder(new EmptyBorder(0, 2*MARGIN, 0, 0)); // padding

            add(infoTitle, gbc(wx(1), align(-1, 0), fill()));
            add(infoContent, gbc(gy(1), wx(1), wy(1), align(-1, -1), fill(HORIZONTAL)));
        }}, gbc(gy(1), gx(1), wx(1), wy(1), noInsets(), fill(), align(0, -1)));

        add(new JPanel(new FlowLayout(FlowLayout.RIGHT)) {{
            add(new JButton("Next ▶"));
        }}, gbc(gy(2), gw(2), fill()));
    }

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
