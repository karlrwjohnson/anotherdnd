package anotherdnd;


import javax.swing.*;
import java.awt.*;

import static anotherdnd.view.util.EZGridBag.*;
import static java.awt.GridBagConstraints.LINE_START;

public class Test {

    private static JPanel myTestPanel = new JPanel(new GridBagLayout()) {{
        int y = 0;

        Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        add(new JLabel(String.format("There are %d fonts:\n", fonts.length)), gbc(gy(y++), anchor(LINE_START), gw(2)));
        for (Font font : fonts) {
            if (!font.canDisplay('a')) {
                continue;
            }
            font = font.deriveFont(12.0f);
            JLabel label = new JLabel(font.getName());
            label.setFont(font);

            add(label, gbc(gy(y), anchor(LINE_START)));
            add(new JLabel(font.getName()), gbc(gx(1), gy(y), anchor(LINE_START)));

            y++;
        }
    }};

    public static void main(String[] args) {
//        Run.runInFrame("Test", myTestPanel);

//        System.setProperty("swing.aatext", "true");


        JFrame frame = new JFrame("Test");
        JScrollPane scrollPane = new JScrollPane(myTestPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        frame.setContentPane(scrollPane);

//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (ClassNotFoundException
//            | InstantiationException
//            | IllegalAccessException
//            | UnsupportedLookAndFeelException e) {
//            e.printStackTrace();
//        }

//        UIManager.setLookAndFeel();

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
