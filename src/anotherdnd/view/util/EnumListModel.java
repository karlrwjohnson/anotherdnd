package anotherdnd.view.util;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.HashSet;
import java.util.Set;

public class EnumListModel<T extends Enum<T>> implements ComboBoxModel<T> {

    private final Class<T> enumClass;
    private final T[] values;
    private final Set<ListDataListener> listDataListeners = new HashSet<>();

    private T selectedItem;

    public EnumListModel(Class<T> enumClass) {
        this.enumClass = enumClass;
        this.values = enumClass.getEnumConstants();
    }

    @Override
    public void setSelectedItem(Object o) {
        if (enumClass.isInstance(o)) {
            //noinspection unchecked
            selectedItem = (T) o;

            // screw it. There's like three different kinds of events, and I'm only firing off the most general-case
            ListDataEvent event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, this.values.length);

            listDataListeners.forEach(l -> l.contentsChanged(event));
        } else {
            throw new ClassCastException("Item " + o + " is not an instance of class " + enumClass.getName());
        }
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }

    @Override
    public int getSize() {
        return values.length;
    }

    @Override
    public T getElementAt(int i) {
        return this.values[i];
    }

    @Override
    public void addListDataListener(ListDataListener listDataListener) {
        listDataListeners.add(listDataListener);
    }

    @Override
    public void removeListDataListener(ListDataListener listDataListener) {
        listDataListeners.remove(listDataListener);
    }
}
