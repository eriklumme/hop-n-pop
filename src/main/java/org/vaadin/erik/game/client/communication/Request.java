package org.vaadin.erik.game.client.communication;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public abstract class Request implements JSObject {

    @JSProperty
    abstract void setUrl(String url);

    @JSProperty
    abstract void setTransport(String transport);

    @JSProperty
    abstract void setOnMessage(OnMessageHandler onMessageHandler);
}
