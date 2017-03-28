package anotherdnd.view.temp;

import anotherdnd.view.util.Run;

import javax.swing.*;
import java.awt.*;

import static anotherdnd.view.util.EZGridBag.*;

public class GameMenu extends JPanel {
    public static void main(String[] args) {
        Run.runInFrame("Yet Another D&D", () -> new GameMenu());
    }

    public GameMenu() {
        super(new GridBagLayout());

        add(new JLabel("Yet Another D&D") {{ setFont(getFont().deriveFont(24.0f)); }}, gbc(padding(10)));

        add(new JButton("Create New Character") {{ addActionListener(e -> createNewCharacter());}}, gbc(padding(10), gy(1)));

        add(new JButton("Exit") {{ addActionListener(e -> exit());}}, gbc(padding(10), gy(2)));
    }

    void createNewCharacter() {
        Run.runInFrame("Create Character", () -> new CharacterBuilderScreen1());

    }

    void exit() {
    }
}
