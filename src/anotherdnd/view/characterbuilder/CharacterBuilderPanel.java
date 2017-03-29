package anotherdnd.view.characterbuilder;

import anotherdnd.model.Character;
import anotherdnd.model.bio.CivicAlignment;
import anotherdnd.model.bio.MoralAlignment;
import anotherdnd.model.bio.Sex;
import anotherdnd.model.mechanics.Ability;
import anotherdnd.model.mechanics.Save;
import anotherdnd.model.race.Dwarf;
import anotherdnd.model.race.Elf;
import anotherdnd.model.race.Human;
import anotherdnd.model.race.Race;
import anotherdnd.view.customui.AbilitySpinnerUI;
import anotherdnd.view.util.EnumListModel;
import anotherdnd.view.util.Run;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.*;
import java.util.List;

import static anotherdnd.view.util.Misc.formatBonus;
import static anotherdnd.view.util.ModelSync.sync;
import static anotherdnd.view.util.EZGridBag.*;
import static java.awt.GridBagConstraints.*;

/*

Character builder:
---------------------

Screen 1:
    Pick your race, sex, name, alignment
        (race is the first foray into "this is not like your world".)
        (Can I create a window that *doesn't* look like Java Swing?
    Pick your starting class
        (how much fanfare do I want around these things?)

Screen 2: Pick your base stats (affected by race)
    (Combine with first screen?)

 */


@SuppressWarnings("WeakerAccess")
public class CharacterBuilderPanel extends JPanel {
    public static void main(String[] args) {
        Run.runInFrame("Character Sheet", () -> new CharacterBuilderPanel());
    }

    static final List<Race> RACE_LIST = Arrays.asList(
        new Human(),
        new Elf(),
        new Dwarf()
    );

    private final Character character = new Character();

    private final JTextField                nameField           = new JTextField();
    private final JComboBox<MoralAlignment> moralAlignmentField = new JComboBox<>(new EnumListModel<>(MoralAlignment.class));
    private final JComboBox<CivicAlignment> civicAlignmentField = new JComboBox<>(new EnumListModel<>(CivicAlignment.class));
    private final JComboBox<Sex>            sexField            = new JComboBox<>(new EnumListModel<>(Sex.class));
    private final JComboBox<Race>           raceField           = new JComboBox<>(new Vector<>(RACE_LIST));
    private final JLabel                    raceDescription     = new JLabel();

    {
        sync(nameField,           character::getName,           character::setName);
        sync(moralAlignmentField, character::getMoralAlignment, character::setMoralAlignment);
        sync(civicAlignmentField, character::getCivicAlignment, character::setCivicAlignment);
        sync(sexField,            character::getSex,            character::setSex);
        sync(raceField,           character::getRace,           character::setRace);
        sync(raceDescription,     ()->character.getRace().getDescription());
    }

    private final JButton applyButton = new JButton("Next");
    {
        applyButton.setDefaultCapable(true);
    }

    Border border(String title) {
        return BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title);
    }

    private JPanel bioPanel() {
        return new JPanel(new GridBagLayout()) {{
            setBorder(border("Bio"));

            int y = 0;
            add(new JLabel("Name"),      gbc(gx(0), gy(y), fill(BOTH)));
            add(nameField,               gbc(gx(1), gy(y), fill(BOTH), gw(2), wx(1)));

            y++;
            add(new JLabel("Alignment"), gbc(gx(0), gy(y), fill(BOTH)));
            add(civicAlignmentField,     gbc(gx(2), gy(y), fill(BOTH), wx(0.5)));
            add(moralAlignmentField,     gbc(gx(1), gy(y), fill(BOTH), wx(0.5)));

            y++;
            add(new JLabel("Sex"),       gbc(gx(0), gy(y), fill(BOTH)));
            add(sexField,                gbc(gx(1), gy(y), fill(BOTH), gw(2), wx(1)));

            y++;
            add(new JLabel("Race"),      gbc(gx(0), gy(y), fill(BOTH)));
            add(raceField,               gbc(gx(1), gy(y), fill(BOTH), gw(2), wx(1)));

            y++;
            add(raceDescription,         gbc(gx(0), gy(y), fill(BOTH), gw(3)));
        }};
    }

    @SuppressWarnings("UnusedAssignment")
    private JPanel abilitiesPanel() {

        JLabel pointsRemainingLabel = new JLabel();

        PointBudget budget = new PointBudget() {{
            observe(() -> pointsRemainingLabel.setText("" + getPointsRemaining()));
            pointsRemainingLabel.setText("" + getPointsRemaining());
        }};

        return new JPanel(new GridBagLayout()) {{
            setBorder(border("Stats"));

            int y = 0;

            for (Ability ability : Ability.values()) {
                PointBuyNumberModel startScore = new PointBuyNumberModel(budget);

                JLabel pointsUsed = new JLabel("" + startScore.getPointsSpent());
                JLabel racialBonus = new JLabel();
                JLabel totalScore = new JLabel();
                JLabel totalModifier = new JLabel();

                JSpinner spinner = new JSpinner(startScore) {{
                    setUI(new AbilitySpinnerUI());
                    addChangeListener(changeEvent -> pointsUsed.setText("" + startScore.getPointsSpent()));
                }};

                sync(spinner,
                    () -> character.getAbilityBaseScore(ability),
                    score -> character.setAbilityBaseScore(ability, ((Number) score).intValue())
                );

                sync(racialBonus,   () -> "" + character.getRace().getAbilityScoreBonus(ability));
                sync(totalScore,    () -> "" + character.getAbilityScore(ability));
                sync(totalModifier, () -> formatBonus(character.getAbilityBonus(ability)));

                int x = 0;
                add(new JLabel(ability.name()), gbc(gx(x++), gy(y), fill(BOTH)));
                add(spinner,                    gbc(gx(x++), gy(y), fill(BOTH)));
                add(pointsUsed,                 gbc(gx(x++), gy(y), anchor(CENTER)));
                add(totalScore,                 gbc(gx(x++), gy(y), anchor(CENTER)));
                add(totalModifier,              gbc(gx(x++), gy(y), anchor(CENTER)));
                y++;
            }

            add(new JLabel("Points Remaining"), gbc(gy(y), gw(2), fill(HORIZONTAL), anchor(LINE_END)));
            add(pointsRemainingLabel, gbc(gx(2), gy(y), fill(HORIZONTAL), anchor(LINE_END)));
        }};
    }

//    @SuppressWarnings("UnusedAssignment")
//    private JPanel abilitiesPanel2() {
//        /*
//           -1) We can just get rid of JSpinner because the amount of stuff I want is so custom.
//            0) Buttons should have the cost/rebate written on them
//            1) Racial modifiers should be incorporated into the spinner -- if you have a +2, it should just say "12"
//                 -> to do this, I just don't need to state the base score.
//            2) Floating +2's (like Human) can be automatically allocated toward the highest-selected stat
//
//                                    Score          Modifier
//
//            Strength      < (+2) ] [__10__] [ -1 >   +0
//
//            Dexterity     < (+2) ] [__10__] [ -1 >   +0
//
//            Constitution  < (+2) ] [__10__] [ -1 >   +0
//
//            Intelligence  < (+2) ] [__10__] [ -1 >   +0
//
//            Wisdom        < (+2) ] [__10__] [ -1 >   +0
//
//            Charisma      < (+2) ] [__10__] [ -1 >   +0
//
//         */
//
//        JLabel pointsRemainingLabel = new JLabel();
//
//        PointBudget budget = new PointBudget() {{
//            observe(() -> pointsRemainingLabel.setText("" + getPointsRemaining()));
//            pointsRemainingLabel.setText("" + getPointsRemaining());
//        }};
//
//        return new JPanel(new GridBagLayout()) {{
//            setBorder(border("Stats"));
//
//            int y = 0;
//
//            for (Ability ability : Ability.values()) {
//                PointBuyNumberModel startScore = new PointBuyNumberModel(budget);
//
//                JButton decrementScore = new JButton();
//                JButton incrementScore = new JButton();
//
//                decrementScore.addActionListener(e -> {
//                    character.setAbilityBaseScore(ability, character.getAbilityBaseScore(ability) - 1);
//                    ModelSync.scheduleUpdate();
//                });
//                watch(decrementScore,
//                    () -> !startScore.getPreviousValue().equals(startScore.getValue()),
//                    decrementScore::setEnabled
//                );
//
////                sync(
////                    // Install updater
////                    onUpdate -> decrementScore.addActionListener(evt -> onUpdate.accept(null)),
////
////                    // On-click: Install value into model
////                    nil -> character.setAbilityBaseScore(ability, character.getAbilityBaseScore(ability) - 1),
////
////                    // Get value from model
////                    () -> !startScore.getPreviousValue().equals(startScore.getValue()),
////
////                    // Update GUI
////                    decrementScore::setEnabled
////                );
//
//                watch(decrementScore,
//                    () -> startScore.getScore() + 1 budget.getPointsRemainingExcept(startScore));
//
//                incrementScore.addActionListener(e -> {
//                    character.setAbilityBaseScore(ability, character.getAbilityBaseScore(ability) - 1);
//                    ModelSync.scheduleUpdate();
//                });
//
//                JLabel pointsUsed = new JLabel("" + startScore.getPointsSpent());
//                JLabel racialBonus = new JLabel();
//                JLabel totalScore = new JLabel();
//                JLabel totalModifier = new JLabel();
//
//                JSpinner spinner = new JSpinner(startScore) {{
//                    setUI(new AbilitySpinnerUI());
//                    addChangeListener(changeEvent -> pointsUsed.setText("" + startScore.getPointsSpent()));
//                }};
//
//                sync(spinner,
//                    () -> character.getAbilityBaseScore(ability),
//                    score -> character.setAbilityBaseScore(ability, ((Number) score).intValue())
//                );
//
//                sync(racialBonus,   () -> "" + character.getRace().getAbilityScoreBonus(ability));
//                sync(totalScore,    () -> "" + character.getAbilityScore(ability));
//                sync(totalModifier, () -> formatBonus(character.getAbilityBonus(ability)));
//
//                int x = 0;
//                add(new JLabel(ability.name()), gbc(gx(x++), gy(y), fill(BOTH)));
//                add(spinner,                    gbc(gx(x++), gy(y), fill(BOTH)));
//                add(pointsUsed,                 gbc(gx(x++), gy(y), anchor(CENTER)));
//                add(totalScore,                 gbc(gx(x++), gy(y), anchor(CENTER)));
//                add(totalModifier,              gbc(gx(x++), gy(y), anchor(CENTER)));
//                y++;
//            }
//
//            add(new JLabel("Points Remaining"), gbc(gy(y), gw(2), fill(HORIZONTAL), anchor(LINE_END)));
//            add(pointsRemainingLabel, gbc(gx(2), gy(y), fill(HORIZONTAL), anchor(LINE_END)));
//        }};
//    }


    private JPanel savesPanel() {
        return new JPanel(new GridBagLayout()) {{
            setBorder(border("Stats"));

            int y = 0;

            for (Save save : Save.values()) {
                JLabel saveBonus = new JLabel();

                sync(saveBonus, () -> formatBonus(character.getSaveBonus(save)));

                add(new JLabel(save.name()), gbc(gy(y), fill(BOTH)));
                add(saveBonus,               gbc(gy(y), fill(BOTH), gx(1)));
                y++;
            }
        }};
    }

    public CharacterBuilderPanel() {
        super(new GridBagLayout());

        add(bioPanel(), gbc(gx(0), wx(0.5), fill(BOTH)));
        add(abilitiesPanel(), gbc(gx(1), fill(BOTH)));
        add(savesPanel(), gbc(gx(0), gy(1), anchor(FIRST_LINE_START)));

        JLabel test = new JLabel("test");
        add(test, gbc(gx(1), gy(1), anchor(FIRST_LINE_START)));

        //test.getUI()
    }

}
