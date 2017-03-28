package anotherdnd.model.feats;

public abstract class ImmutableFeat implements Feat {

    @Override public boolean equals(Object other) {
        return getClass().equals(other.getClass());
    }

    @Override public int hashCode() {
        return getClass().hashCode();
    }

    @Override public boolean isStateless() { return false; }
}
