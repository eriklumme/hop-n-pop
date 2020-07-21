package org.vaadin.erik.game.client.communication;

import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

/**
 * Interface invoked when a WebSocket message is received.
 */
@JSFunctor
public interface OnMessageHandler extends JSObject {

    void onMessage(MessageEvent event);
}
