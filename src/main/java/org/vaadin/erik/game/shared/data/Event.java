package org.vaadin.erik.game.shared.data;

import org.vaadin.erik.game.entity.Action;
import org.vaadin.erik.game.shared.Player;

/**
 * An event that occurred due to player interaction
 */
public class Event {

    private final Action action;
    private final Player source;
    private final Player target;

    public Event(Action action, Player source) {
        this(action, source, null);
    }

    public Event(Action action, Player source, Player target) {
        this.action = action;
        this.source = source;
        this.target = target;
    }

    public Action getAction() {
        return action;
    }

    public Player getSource() {
        return source;
    }

    public Player getTarget() {
        return target;
    }
}
