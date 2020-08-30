package org.vaadin.erik.game.shared.data;

import org.vaadin.erik.game.shared.Player;

/**
 * An event that occurred due to player interaction
 */
public class Event {

    private final Action action;
    private final Player source;
    private final Player target;
    private final Object data;

    public Event(Action action, Player source) {
        this(action, source, null);
    }

    public Event(Action action, Player source, Player target) {
        this(action, source, target, null);
    }

    public Event(Action action, Player source, Player target, Object data) {
        this.action = action;
        this.source = source;
        this.target = target;
        this.data = data;
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

    public Object getData() {
        return data;
    }
}
