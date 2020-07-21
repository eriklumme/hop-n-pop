package org.vaadin.erik.game.client.communication;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface MessageEvent extends JSObject {

    @JSProperty
    String getResponseBody();
}
