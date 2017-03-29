package anotherdnd.view.temp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

import static anotherdnd.view.util.EZGridBag.*;

public class App extends JFrame {
    public static void main(String[] args) {
        try {
            System.out.println(UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName()
            );
        } catch (ClassNotFoundException
            | InstantiationException
            | IllegalAccessException
            | UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        new App();
    }

    final App that = this;

    JMenuItem exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X) {{
        setAccelerator(KeyStroke.getKeyStroke('Q', KeyEvent.CTRL_MASK));
        addActionListener(e -> exit());
    }};
    JMenu fileMenu = new JMenu("File") {{
        setMnemonic('f');
        add(exitMenuItem);
    }};
    JMenuBar menuBar = new JMenuBar() {{
        add(fileMenu);
    }};

    JPanel mainMenu = new JPanel(new GridBagLayout()) {{
        setBackground(new Color(0x666666));

        int y = 0;

        add(new JLabel("Yet Another D&D") {{
            setFont(getFont().deriveFont(24.0f));
            setForeground(Color.WHITE);
        }}, gbc(padding(10), gy(++y)));

        add(new JButton("New Game") {{
            addActionListener(e -> createNewCharacter());
        }}, gbc(padding(10), gy(++y), fill()));

        add(new JButton("Load Game") {{
            addActionListener(e -> loadGame());
        }}, gbc(padding(10), gy(++y), fill()));

        add(new JButton("Exit") {{
            addActionListener(e -> exit());
        }}, gbc(padding(10), gy(++y), fill()));
    }};

    private App() {
        super("Another D&D");
        setJMenuBar(menuBar);
        setContentPane(mainMenu);
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    void createNewCharacter() {
        setContentPane(new Wizard(new CharacterBuilderScreen1()));
    }

    void loadGame() {
        JOptionPane.showMessageDialog(that, "Unimplemented", "Sorry", JOptionPane.ERROR_MESSAGE);
    }

    void exit() {
        dispose();
    }
}
