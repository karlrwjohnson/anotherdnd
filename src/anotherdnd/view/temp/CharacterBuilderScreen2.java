package anotherdnd.view.temp;

import anotherdnd.model.Character;
import anotherdnd.model.mechanics.Ability;
import anotherdnd.view.temp.Wizard.WizardScreen;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static anotherdnd.view.util.EZGridBag.*;
import static anotherdnd.view.util.ModelSync.sync;
import static java.awt.GridBagConstraints.*;

public class CharacterBuilderScreen2 extends JPanel implements WizardScreen {
    @Override public JPanel       getPanel()    { return this; }
    @Override public String       getTitle()    { return "Abilities"; }
    @Override public boolean      hasPrevious() { return true; }
    @Override public WizardScreen getPrevious() { return previous; }
    @Override public boolean      hasNext()     { return false; }
    @Override public WizardScreen getNext()     { return null; }

    private final CharacterBuilderScreen1   previous;
    private final Character                 character;

    private final InfoPanel infoPanel = new InfoPanel(getClass().getName()) {{
        setBorder(new EmptyBorder(0, 0, 0, 2*MARGIN)); // padding
    }};

    public CharacterBuilderScreen2(CharacterBuilderScreen1 previous, Character character) {
        super(new GridBagLayout());
        this.previous = previous;
        this.character = character;

        add(infoPanel, gbc(gx(0), wx(1), wy(1), noInsets(), fill(), align(0, -1)));

        add(new JPanel(new GridBagLayout()) {{
            int y = -1;
            for (Ability ability : Ability.values()) {
                ++y;
                int x = -1;
                
                JLabel abilityLabel    = new JLabel(ability.name);
                JLabel abilityScore    = sync(new JLabel(), () -> character.getAbilityScore(ability));
                JButton decrementScore = new JButton("<");
                JButton incrementScore = new JButton(">");
                
                add(abilityLabel,   gbc(gx(++x), gy(y), fill()));
                add(decrementScore, gbc(gx(++x), gy(y), fill()));
                add(abilityScore,   gbc(gx(++x), gy(y), fill()));
                add(incrementScore, gbc(gx(++x), gy(y), fill()));

                infoPanel.showForComponent(abilityLabel,   ability.name());
                infoPanel.showForComponent(decrementScore, ability.name());
                infoPanel.showForComponent(abilityScore,   ability.name());
                infoPanel.showForComponent(incrementScore, ability.name());
            }
        }}, gbc(gx(1), wy(1), noInsets(), anchor(NORTH)));
    }

}
