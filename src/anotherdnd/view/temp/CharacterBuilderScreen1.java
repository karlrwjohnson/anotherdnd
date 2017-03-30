package anotherdnd.view.temp;

import anotherdnd.model.Character;
import anotherdnd.model.bio.Sex;
import anotherdnd.model.race.Race;
import anotherdnd.view.temp.Wizard.WizardScreen;
import anotherdnd.view.util.EnumListModel;
import anotherdnd.view.util.ModelSync;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Vector;

import static anotherdnd.view.util.EZGridBag.*;
import static anotherdnd.view.util.Misc.setMinimumWidth;
import static anotherdnd.view.util.ModelSync.sync;
import static java.awt.GridBagConstraints.NORTH;

public class CharacterBuilderScreen1 extends JPanel implements WizardScreen {

    private final JTextField      nameField       = new JTextField() {{ setBackground(Color.WHITE); }};
    private final JComboBox<Sex>  sexField        = new JComboBox<>(new EnumListModel<>(Sex.class));
    private final JComboBox<Race> raceField       = new JComboBox<>(new Vector<>());
    private final AlignmentPicker alignmentPicker = new AlignmentPicker();
    
    private final Character character;

    public CharacterBuilderScreen1() {
        super(new GridBagLayout());
        
        character = new Character();
        sync(nameField, character::getName, character::setName);
        sync(sexField, character::getSex, character::setSex);
        sync(raceField, character::getRace, character::setRace);
        alignmentPicker.observe(picker -> {
            character.setCivicAlignment(picker.getCivicAlignment());
            character.setMoralAlignment(picker.getMoralAlignment());
            ModelSync.scheduleUpdate();
        });

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
    @Override public WizardScreen getNext() { return new CharacterBuilderScreen2(this, character); }
}
