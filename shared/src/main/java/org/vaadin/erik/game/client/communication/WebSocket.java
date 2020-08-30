package org.vaadin.erik.game.client.communication;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.json.JSON;

/**
 * Utility class for operating on a WebSocket connection.
 *
 * The underlying connection will be created using Vaadin's version of the atmosphere.js library, which
 * is available in the browser under window.vaadinPush.atmosphere.
 */
public abstract class WebSocket implements JSObject {

    private static final int SIMULATED_PING = 0;

    static WebSocket connect(String url, OnMessageHandler onMessageHandler) {
        Request request = JSObjects.create();
        request.setTransport("websocket");
        request.setUrl(url);
        request.setOnMessage(onMessageHandler);
        return subscribe(request);
    }

    @JSBody(params = "request", script = "return window.vaadinPush.atmosphere.subscribe(request)")
    private static native WebSocket subscribe(Request request);

    /**
     * Sends a message over the WebSocket
     */
    void sendMessage(JSObject message) {
        String messageString = JSON.stringify(message);
        Window.setTimeout(() -> {
            push(messageString);
        }, SIMULATED_PING / 2);
    }

    abstract void push(String message);
}
