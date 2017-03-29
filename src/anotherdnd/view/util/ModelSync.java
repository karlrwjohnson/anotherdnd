package anotherdnd.view.util;

import javax.swing.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.*;

/**
 * Lightweight Model/View binding framework (still in progress)
 * 
 * When properly used, this will synchronously update a model based on changes in the view
 * and asynchronously update a view based on changes in the model.
 * 
 * The goal is to make it easy to update the UI based on changes in the model without littering
 * the model with an observer framework.
 *
 * TL;DR: 
 * - Updates from the View -> Model use Swing's own actionListener callbacks
 * - Updates from the Model -> View use dirty-checking (The design was inspired by AngularJS)
 * - Asynchronous dirty-checking passes are automatically scheduled whenever the View changes the model.
 * - Call ModelSync::scheduleUpdate() explicitly when the model may have been changed by something other than ModelSync.
 * 
 * How to use:
 * 
 *      // Create model and Swing component, add to JPanel
 *      MyModelClass myModel = new MyModelClass();
 *      JTextField myField = new JTextField();      // No need to set initial value; ModelSync::sync will handle it later
 *      jpanel.add(myField);
 *
 *      // Bind the model and the view 
 *      ModelSync.sync(
 *          myField,                // Reference to the component to be updated.
 *                                  //     (Not all Swing components are supported yet.)
 *          myModel::getMyValue,    // Getter to retrieve the model's value on each update pass
 *                                  //     (Also invoked immediately to initialize the component's value
 *          myModel::setMyValue     // Setter to update the model's value
 *      )
 * 
 */
public class ModelSync {

    public static void scheduleUpdate() {
        if (!updateScheduled) {
            updateScheduled = true;

            SwingUtilities.invokeLater(ModelSync::update);
        }
    }
    
    /**
     * Low-level method behind sync(), useful when a component has multiple ways it might need to be updated
     * Example:
     *      JButton myJButton = new JButton("Submit");
     *      watch(myJButton, JButton::setEnabled, () -> model.isValid());
     * @param component - Component to be updated. Passed to guiSetter, and used as a key in a WeakHashMap so the
     *                    binding is deleted when the component is no longer used.
     * @param guiSetter - Updater function for the component. Parameters are (component, value).
     *                    MUST NOT have a reference to the component, or else the binding will never be deleted!
     *                    Use the parameter to refer to the component.
     * @param modelGetter - Retrieves the new value from the model.
     */
    public static <C, T> void watch(C component, BiConsumer<C, T> guiSetter, Supplier<T> modelGetter) {
        guiSetter.accept(component, modelGetter.get());

        // Mutable container for state information
        List<T> oldValueRef = new ArrayList<T>(1);

        addModelChecker(component, _component -> {
            T newValue = modelGetter.get();
            if (oldValueRef.size() > 0) {
                if (!oldValueRef.get(0).equals(newValue)) {
                    guiSetter.accept(_component, newValue);
                }
            } else {
                oldValueRef.add(newValue);
                guiSetter.accept(_component, newValue);
            }
        });
    }

    public static void sync(JLabel component, Supplier<String> modelGetter) {
        component.setText(modelGetter.get());

        watch(component, JLabel::setText, modelGetter);
    }

    public static void sync(JTextField component, Supplier<String> modelGetter, Consumer<String> modelSetter) {
        component.setText(modelGetter.get());

        component.addActionListener(evt -> {
            JTextField _component = ((JTextField) evt.getSource());
            modelSetter.accept(_component.getText());
            scheduleUpdate();
        });

        watch(component, JTextField::setText, modelGetter);
    }

    @SuppressWarnings("unchecked")
    public static <T> void sync(JComboBox<T> component, Supplier<T> modelGetter, Consumer<T> modelSetter) {
        component.setSelectedItem(modelGetter.get());

        component.addActionListener(evt -> {
            JComboBox<T> _component = ((JComboBox<T>) evt.getSource());
            modelSetter.accept((T) _component.getSelectedItem());
            scheduleUpdate();
        });

        watch(component, JComboBox::setSelectedItem, modelGetter);
    }

    public static void sync(JSpinner component, Supplier<Object> modelGetter, Consumer<Object> modelSetter) {
        component.setValue(modelGetter.get());

        component.addChangeListener(evt -> {
            JSpinner _component = ((JSpinner) evt.getSource());
            modelSetter.accept(_component.getValue());
            scheduleUpdate();
        });

        watch(component, JSpinner::setValue, modelGetter);
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
//        addModelChecker(field, component -> {
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


//    public static <T> void watch(Object component, Supplier<T> modelGetter, Supplier<T> guiGetter, Consumer<T> guiSetter) {
//        guiSetter.accept(modelGetter.get());
//
//        addModelChecker(component, _component -> {
//            T newValue = modelGetter.get();
//            T oldValue = guiGetter.get();
//            if (!oldValue.equals(newValue)) {
//                guiSetter.accept(newValue);
//            }
//        });
//    }

    ///// Private /////

    private ModelSync(){} // No instantiations!

    private static final WeakHashMap<Object, List<Consumer<Object>>> modelCheckers = new WeakHashMap<>();
    private static boolean updateScheduled = false;

    private static <C> void addModelChecker(C component, Consumer<C> operation) {
        // Java's type system is incapable of expressing generic types across the keys and values of modelCheckers.
        // This method's signature guarantees that the `operation` callback receives the right type of parameter.
        @SuppressWarnings("unchecked")
        Consumer<Object> genericOperation = (Consumer<Object>) operation;

        modelCheckers.computeIfAbsent(component, x -> new ArrayList<>())
                .add(genericOperation);
    }

    private static void update() {
        updateScheduled = false;

        Instant start = Instant.now();

        for (Map.Entry<Object, List<Consumer<Object>>> entry : modelCheckers.entrySet()) {
            Object component = entry.getKey();
            for (Consumer<Object> operation : entry.getValue()) {
                try {
                    operation.accept(component);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // I'm worried this whole model-dirty-checking thing is going to blow up eventually.
        // Log a message in case it happens so we know what was slow.
        Instant end = Instant.now();
        long interval = Duration.between(start, end).toMillis();
        if (interval > 200) {
            System.err.println("Warning! Model check took " + interval + " ms to run " + modelCheckers.size() + " model checkers");
        }
    }
}
