package org.vaadin.erik.game.client.communication;

import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSObjects;
import org.vaadin.erik.game.client.communication.json.PlayerCommandJson;

/**
 * Wrapper around the {@link WebSocket} class handling communication with the server.
 */
public class Communicator {

    private static final String WS_URL = "game/command";

    private final OnMessageHandler onMessageHandler;

    private WebSocket webSocket;
    private int order = 0;

    public Communicator(OnMessageHandler onMessageHandler) {
        this.onMessageHandler = onMessageHandler;
        webSocket = WebSocket.connect(
                Window.current().getLocation().getFullURL() + WS_URL,
                onMessageHandler);
    }

    public void joinGame() {
        if (webSocket == null) {
            return;
        }

        PlayerCommandJson playerCommand = JSObjects.create();
        webSocket.sendMessage(playerCommand);
    }

    public void sendPlayerCommand(String uuid, JSArray<JSNumber> directions) {
        if (webSocket == null) {
            return;
        }

        PlayerCommandJson playerCommand = JSObjects.create();
        playerCommand.setDirections(directions);
        playerCommand.setOrder(order++);
        playerCommand.setUuid(uuid);
        webSocket.sendMessage(playerCommand);
    }
}
