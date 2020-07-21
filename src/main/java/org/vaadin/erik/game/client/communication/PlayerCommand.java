package org.vaadin.erik.game.client.communication;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface PlayerCommand extends JSObject {

    @JSProperty
    void setUuid(String uuid);

    @JSProperty
    void setDirection(int direction);

    @JSProperty
    void setOrder(int order);
}
