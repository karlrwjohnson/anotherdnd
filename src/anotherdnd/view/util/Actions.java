package anotherdnd.view.util;

import javax.swing.*;
import java.util.function.Consumer;

public interface Actions {
    static void onChange(JTextField component, Consumer<String> then) {
        component.addActionListener(evt -> then.accept(component.getText()));
    }
}
