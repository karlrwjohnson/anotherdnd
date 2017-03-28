package anotherdnd.model.mechanics;

public interface Actions {

    enum ActionDuration {
        FREE,
        SWIFT,
        MOVE,
        STANDARD,
        FULL
    }

    interface Action {
        ActionDuration getDuration();
    }
}