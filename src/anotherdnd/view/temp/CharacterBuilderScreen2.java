package anotherdnd.view.temp;

import anotherdnd.model.Character;
import anotherdnd.model.mechanics.Ability;
import anotherdnd.view.temp.Wizard.WizardScreen;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static anotherdnd.view.util.EZGridBag.*;
import static anotherdnd.view.util.Misc.formatBonus;
import static anotherdnd.view.util.ModelSync.sync;
import static anotherdnd.view.util.ModelSync.watch;
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
    private final AbilityPointBuyBudget     budget;

    private final InfoPanel infoPanel = new InfoPanel(getClass().getName()) {{
        setBorder(new EmptyBorder(0, 0, 0, 2*MARGIN)); // padding
    }};

    public CharacterBuilderScreen2(CharacterBuilderScreen1 previous, Character character) {
        super(new GridBagLayout());
        this.previous = previous;
        this.character = character;
        this.budget = AbilityPointBuyBudget.of(character, 25);

        add(infoPanel, gbc(gx(0), wx(1), wy(1), noInsets(), fill(), align(0, -1)));

        add(new JPanel(new GridBagLayout()) {{
            int y = 0;

            add(new JPanel(new GridBagLayout()) {{
                int y = -1;

                for (Ability ability : Ability.values()) {
                    ++y;
                    int x = -1;

                    JLabel abilityLabel = new JLabel(ability.name);
                    JLabel abilityScore = sync(new JLabel(), () -> formatBonus(character.getAbilityScore(ability)));
                    JButton decrementScore = new JButton("-");
                    JButton incrementScore = new JButton("+");
                    JLabel abilityBonus = sync(new JLabel(), () -> formatBonus(character.getAbilityBonus(ability)));

                    decrementScore.addActionListener(e -> budget.decrementScore(ability));
                    incrementScore.addActionListener(e -> budget.incrementScore(ability));

                    watch(decrementScore, JButton::setEnabled, () -> budget.canDecrementScore(ability));
                    watch(incrementScore, JButton::setEnabled, () -> budget.canIncrementScore(ability));

                    add(abilityLabel,   gbc(gx(++x), gy(y), align(-1, 0)));
                    add(decrementScore, gbc(gx(++x), gy(y), align(+0, 0), fill()));
                    add(abilityScore,   gbc(gx(++x), gy(y), align(+1, 0)));
                    add(incrementScore, gbc(gx(++x), gy(y), align(+0, 0), fill()));
                    add(abilityBonus,   gbc(gx(++x), gy(y), align(+0, 0)));

                    infoPanel.showForComponent(abilityLabel,   ability.name());
                    infoPanel.showForComponent(decrementScore, ability.name());
                    infoPanel.showForComponent(abilityScore,   ability.name());
                    infoPanel.showForComponent(incrementScore, ability.name());
                    infoPanel.showForComponent(abilityBonus,   ability.name());
                }
            }}, gbc(gy(++y), wy(1), fill(), noInsets()));

            add(sync(new JLabel(), () -> "Total Budget: " + budget.getBudget()), gbc(gy(++y), fill()));
            add(sync(new JLabel(), () -> "Points spent: " + budget.getTotalPointsSpent()), gbc(gy(++y), fill()));
            add(sync(new JLabel(), () -> "Points remaining: " + budget.getPointsRemaining()), gbc(gy(++y), fill()));
            add(new JLabel("Budget type: " + budget.getClass().getSimpleName()), gbc(gy(++y), fill()));
            add(new JLabel("-------"), gbc(gy(++y), fill()));
            for (Ability ability : Ability.values()) {
                add(sync(new JLabel(), () -> String.format(
                    "%s: %d spent, %d base, %d total",
                        ability.name(), budget.getPointsSpentOn(ability), character.getAbilityBaseScore(ability), character.getAbilityScore(ability)
                )), gbc(gy(++y), fill()));
            }
        }}, gbc(gx(1), wy(1), noInsets(), anchor(NORTH)));
    }

}
