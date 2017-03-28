package anotherdnd.model.feats;

/**
 * A feat which has no configuration information -- implementations can be treated as a value type.
 */
public abstract class StatelessFeat implements Feat {
    @Override public boolean equals(Object other) {
        return getClass().equals(other.getClass());
    }

    @Override public int hashCode() {
        return getClass().hashCode();
    }

    @Override public boolean isStateless() { return true; }
}
