package anotherdnd.model.race;

import static anotherdnd.model.util.Util.unCamelCase;

public abstract class AbstractRace implements Race {

    // I wish I could put this in the interface... :(
    @Override
    public String toString() {
        return unCamelCase(this.getClass().getSimpleName());
    }
}
