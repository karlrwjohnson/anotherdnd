package anotherdnd.model;

import anotherdnd.model.mechanics.Actions.Action;

import java.util.Collection;

public interface Item {
    Collection<? extends Action> getActions(Character user);

    // TODO: getMonetaryValue() or something. For fun, look into the Joda-Money library
}
