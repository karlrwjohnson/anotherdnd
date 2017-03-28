package anotherdnd.model.bio;

import static anotherdnd.model.util.Util.unMacroCase;

public enum Sex {
    NONE,
    MALE,
    FEMALE;

    private final String string = unMacroCase(name());

    @Override
    public String toString() {
        return string;
    }
}
