package anotherdnd.model.bio;

import static anotherdnd.model.util.Util.unMacroCase;

public enum CivicAlignment {
    NEUTRAL,
    LAWFUL,
    CHAOTIC;

    private final String string = unMacroCase(name());

    @Override
    public String toString() {
        return string;
    }
}
