package anotherdnd.view.util;

import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

public interface Events {

    ///// Lambda-wrappers because some event callbacks have more than one method /////

    static FocusListener onFocus(Consumer<Component> onFocus) {
        return new FocusListener() {
            @Override public void focusGained(FocusEvent e) { onFocus.accept(e.getComponent()); }
            @Override public void focusLost(FocusEvent e) {}
        };
    }

    static FocusListener onFocus(Runnable onFocus) {
        return new FocusListener() {
            @Override public void focusGained(FocusEvent e) { onFocus.run(); }
            @Override public void focusLost(FocusEvent e) {}
        };
    }

    static MouseListener onMouseOver(Consumer<Component> onMouseOver) {
        return new MouseListener() {
            @Override public void mouseEntered(MouseEvent e) { onMouseOver.accept(e.getComponent()); }
            @Override public void mouseClicked(MouseEvent e) {}
            @Override public void mousePressed(MouseEvent e) {}
            @Override public void mouseReleased(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        };
    }

    static MouseListener onMouseOver(Runnable onMouseOver) {
        return new MouseListener() {
            @Override public void mouseEntered(MouseEvent e) { onMouseOver.run(); }
            @Override public void mouseClicked(MouseEvent e) {}
            @Override public void mousePressed(MouseEvent e) {}
            @Override public void mouseReleased(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        };
    }
}
