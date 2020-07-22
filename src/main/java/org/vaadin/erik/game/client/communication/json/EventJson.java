package org.vaadin.erik.game.client.communication.json;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.vaadin.erik.game.entity.Action;

public abstract class EventJson implements JSObject {

    @JSProperty("action")
    public abstract String getActionName();

    @JSProperty
    public abstract PlayerJson getSource();

    @JSProperty
    public abstract PlayerJson getTarget();

    public Action getAction() {
        return Action.valueOf(getActionName());
    }
}
