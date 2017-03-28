package anotherdnd.view.util;

import javax.swing.*;
import java.lang.ref.Reference;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.*;

public class ModelSync {

    public static void scheduleUpdate() {
        if (!updateScheduled) {
            updateScheduled = true;

            SwingUtilities.invokeLater(() -> {
                updateScheduled = false;

                Instant start = Instant.now();

                for (Map.Entry<Object, Consumer<Object>> entry : modelCheckers.entrySet()) {
                    try {
                        entry.getValue().accept(entry.getKey());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // I'm worried this whole model-dirty-checking thing is going to blow up eventually.
                // Log a message in case it happens so we know what was slow.
                Instant end = Instant.now();
                long interval = Duration.between(start, end).toMillis();
                if (interval > 200) {
                    System.err.println("Warning! Model check took " + interval + " ms to run " + modelCheckers.size() + " model checkers");
                }
            });
        }
    }

    public static void sync(JLabel field, Supplier<String> modelGetter) {
        field.setText(modelGetter.get());

        modelCheckers.put(field, component -> {
            JLabel _field = ((JLabel) component);
            String newValue = modelGetter.get();
            if (!_field.getText().equals(newValue)) {
                _field.setText(newValue);
            }
        });
    }

    public static void sync(JTextField field, Supplier<String> modelGetter, Consumer<String> modelSetter) {
        field.setText(modelGetter.get());

        field.addActionListener(evt -> {
            JTextField _field = ((JTextField) evt.getSource());
            modelSetter.accept(_field.getText());
            scheduleUpdate();
        });

        modelCheckers.put(field, component -> {
            JTextField _field = ((JTextField) component);
            String newValue = modelGetter.get();
            if (!_field.getText().equals(newValue)) {
                _field.setText(newValue);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> void sync(JComboBox<T> field, Supplier<T> modelGetter, Consumer<T> modelSetter) {
        field.setSelectedItem(modelGetter.get());

        field.addActionListener(evt -> {
            JComboBox<T> _field = ((JComboBox<T>) evt.getSource());
            modelSetter.accept((T) _field.getSelectedItem());
            scheduleUpdate();
        });

        modelCheckers.put(field, component -> {
            JComboBox<T> _field = ((JComboBox<T>) component);
            T newValue = modelGetter.get();
            if (!newValue.equals(_field.getSelectedItem())) {
                _field.setSelectedItem(newValue);
            }
        });
    }

    public static void sync(JSpinner field, Supplier<Object> modelGetter, Consumer<Object> modelSetter) {
        field.setValue(modelGetter.get());

        field.addChangeListener(evt -> {
            JSpinner _field = ((JSpinner) evt.getSource());
            modelSetter.accept(_field.getValue());
            scheduleUpdate();
        });

        modelCheckers.put(field, component -> {
            JSpinner _field = ((JSpinner) component);
            Object newValue = modelGetter.get();
            if (!_field.getValue().equals(newValue)) {
                _field.setValue(newValue);
            }
        });
    }
//
//    public static void sync(SpinnerNumberModel field, Supplier<Number> modelGetter, Consumer<Number> modelSetter) {
//        field.setValue(modelGetter.get());
//
//        field.addChangeListener(evt -> {
//            SpinnerNumberModel _field = ((SpinnerNumberModel) evt.getSource());
//            modelSetter.accept(_field.getNumber());
//        });
//
//        modelCheckers.put(field, component -> {
//            SpinnerNumberModel _field = ((SpinnerNumberModel) component);
//            Number newValue = modelGetter.get();
//            if (_field.getNumber().equals(newValue)) {
//                _field.setValue(newValue);
//            }
//        });
//    }


//    public static <C, T> Consumer<C> sync(
//        Function<C, T> installGuiSetter, BiConsumer<C, T> guiSetter,
//        Supplier<T> modelGetter, Consumer<T> modelSetter
//    ) {
//        guiSetter.accept(gui, modelGetter.get());
//
//        Runnable ret = () -> {
//
//        }
//
//        registrator.accept(evt -> {
//            C controller = ((C) evt.getSource)
//        });
//    }

    public static <T> void watch(Object component, Supplier<T> modelGetter, Consumer<T> guiSetter) {
        guiSetter.accept(modelGetter.get());

        // Mutable container for state information
        List<T> oldValueRef = new ArrayList<T>(1);

        modelCheckers.put(component, _component -> {
            T newValue = modelGetter.get();
            if (oldValueRef.size() > 0) {
                if (!oldValueRef.get(0).equals(newValue)) {
                    guiSetter.accept(newValue);
                }
            } else {
                oldValueRef.add(newValue);
                guiSetter.accept(newValue);
            }
        });
    }

    public static <T> void watch(Object component, Supplier<T> modelGetter, Supplier<T> guiGetter, Consumer<T> guiSetter) {
        guiSetter.accept(modelGetter.get());

        modelCheckers.put(component, _component -> {
            T newValue = modelGetter.get();
            T oldValue = guiGetter.get();
            if (!oldValue.equals(newValue)) {
                guiSetter.accept(newValue);
            }
        });
    }

    ///// Private /////

    private ModelSync(){} // No instantiations!

    private static final WeakHashMap<Object, Consumer<Object>> modelCheckers = new WeakHashMap<>();
    private static boolean updateScheduled = false;
}
