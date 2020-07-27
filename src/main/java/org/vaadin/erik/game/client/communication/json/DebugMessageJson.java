package org.vaadin.erik.game.client.communication.json;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface DebugMessageJson extends JSObject {

    @JSProperty
    void setSlowdownFactor(double slowdownFactor);
}
