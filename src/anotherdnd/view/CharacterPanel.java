package anotherdnd.view;

import anotherdnd.model.Character;
import anotherdnd.view.util.Run;

import static anotherdnd.view.util.Actions.onChange;
import static anotherdnd.view.util.EZGridBag.*;

import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.*;


public class CharacterPanel extends JPanel   {
    public static void main(String[] args) {
        Run.runInFrame("Character Sheet", () -> new CharacterPanel(Character.CharacterBuilder.newCharacter().build()));
    }

    private Character character;

    private JTextField name      = new JTextField(10) {{ setEditable(false); }};
    private JTextField alignment = new JTextField() {{ setEditable(false); }};
    private JTextField gender    = new JTextField() {{ setEditable(false); }};

    @SuppressWarnings("UnusedAssignment") // final y++
    private void init() {
        int y = 0;

        add(new JLabel("Name"),      gbc(gx(0), gy(y), fill(BOTH)));
        add(name,                    gbc(gx(1), gy(y), weightx(1), fill(BOTH)));
        y++;

        add(new JLabel("Alignment"), gbc(gx(0), gy(y), fill(BOTH)));
        add(alignment,               gbc(gx(1), gy(y), weightx(1), fill(BOTH)));
        y++;

        add(new JLabel("Sex"),    gbc(gx(0), gy(y), fill(BOTH)));
        add(gender,                  gbc(gx(1), gy(y), weightx(1), fill(BOTH)));
        y++;

        onChange(name, name -> character.setName(name));
    }

    public CharacterPanel(Character character) {
        super(new GridBagLayout());
        init();
        setCharacter(character);
    }

    public void setCharacter(Character character) {
        this.character = character;
        updated();
    }

    public Character getCharacter() {
        return character;
    }

    public void updated() {
        name.setText(character.getName());
    }
}
