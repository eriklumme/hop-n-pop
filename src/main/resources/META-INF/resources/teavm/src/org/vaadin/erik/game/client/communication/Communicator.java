package org.vaadin.erik.game.client.communication;

import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSObjects;
import org.vaadin.erik.game.entity.Direction;

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

    public void sendPlayerCommand(String uuid, Direction direction) {
        PlayerCommand playerCommand = JSObjects.create();
        playerCommand.setDirection(direction.ordinal());
        playerCommand.setOrder(order++);
        playerCommand.setUuid(uuid);
        webSocket.sendMessage(playerCommand);
    }
}
