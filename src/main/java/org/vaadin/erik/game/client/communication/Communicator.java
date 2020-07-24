package org.vaadin.erik.game.client.communication;

import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSObjects;
import org.vaadin.erik.game.client.Logger;

/**
 * Wrapper around the {@link WebSocket} class handling communication with the server.
 */
public class Communicator {

    private static final String WS_URL = "game/command";

    private final WebSocket webSocket;

    private int order = 0;

    public Communicator(OnMessageHandler onMessageHandler) {
        webSocket = WebSocket.connect(
                Window.current().getLocation().getFullURL() + WS_URL,
                onMessageHandler);
    }

    public void sendPlayerCommand(String uuid, JSArray<JSNumber> directions) {
        PlayerCommand playerCommand = JSObjects.create();
        playerCommand.setDirections(directions);
        playerCommand.setOrder(order++);
        playerCommand.setUuid(uuid);

        Logger.warn("client.Communicator: Sending player command");
        webSocket.sendMessage(playerCommand);
    }
}
