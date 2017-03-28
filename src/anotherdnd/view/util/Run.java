package anotherdnd.view.util;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

public interface Run {
    static JFrame runInFrame(String title, Supplier<JPanel> panel) {
        JFrame frame = new JFrame(title);

        try {
            Font macondo = Font.createFont(Font.TRUETYPE_FONT, new File("resources/macondo.ttf")).deriveFont(18.0f);
            Font balthazar = Font.createFont(Font.TRUETYPE_FONT, new File("resources/balthazar.ttf")).deriveFont(18.0f);
            javax.swing.plaf.FontUIResource fontResource = new javax.swing.plaf.FontUIResource(balthazar);

            UIDefaults defaults = UIManager.getLookAndFeel().getDefaults();
            java.util.Enumeration<Object> keys = defaults.keys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = UIManager.get(key);
                System.out.printf("%-60s %-60s %s\n", key, (value == null ? "(null)" : value.getClass().getName()), value);
                if (value != null && value instanceof javax.swing.plaf.FontUIResource) {
//                    System.out.println(key);
                    UIManager.put(key, balthazar);
                    defaults.put(key, balthazar);

                }
            }
        } catch (FontFormatException
            | IOException e) {
            throw new RuntimeException(e);
        }
        try {
            System.out.println(UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName()
//                "com.sun.java.swing.plaf.gtk.GTKLookAndFeel"
//                "com.sun.java.swing.plaf.motif.MotifLookAndFeel"
//                "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
//                "javax.swing.plaf.metal.MetalLookAndFeel"
//                "javax.swing.plaf.basic.BasicLookAndFeel"
            );
        } catch (ClassNotFoundException
            | InstantiationException
            | IllegalAccessException
            | UnsupportedLookAndFeelException e) {
//            e.printStackTrace();
            throw new RuntimeException(e);
        }


        try {
            Font macondo = Font.createFont(Font.TRUETYPE_FONT, new File("resources/macondo.ttf")).deriveFont(18.0f);
            Font balthazar = Font.createFont(Font.TRUETYPE_FONT, new File("resources/balthazar.ttf")).deriveFont(18.0f);
            javax.swing.plaf.FontUIResource fontResource = new javax.swing.plaf.FontUIResource(balthazar);

            UIDefaults defaults = UIManager.getLookAndFeel().getDefaults();
            java.util.Enumeration<Object> keys = defaults.keys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = UIManager.get(key);
                System.out.printf("%-60s %-60s %s\n", key, (value == null ? "(null)" : value.getClass().getName()), value);
                if (value != null && value instanceof javax.swing.plaf.FontUIResource) {
//                    System.out.println(key);
                    UIManager.put(key, balthazar);
                    defaults.put(key, balthazar);

                }
            }
        } catch (FontFormatException
                | IOException e) {
            throw new RuntimeException(e);
        }


        frame.setContentPane(panel.get());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(frame.getPreferredSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        return frame;
    }
}
