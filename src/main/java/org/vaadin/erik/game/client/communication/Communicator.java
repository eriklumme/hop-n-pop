package org.vaadin.erik.game.client.communication;

import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.dom.html.HTMLInputElement;
import org.vaadin.erik.game.client.Logger;
import org.vaadin.erik.game.client.communication.json.DebugMessageJson;
import org.vaadin.erik.game.client.communication.json.PlayerCommandJson;

/**
 * Wrapper around the {@link WebSocket} class handling communication with the server.
 */
public class Communicator {

    private static final String WS_URL = "game/command";

    private final WebSocket webSocket;
    private final HTMLInputElement slowdownInput;

    private int order = 0;

    public Communicator(OnMessageHandler onMessageHandler) {
        webSocket = WebSocket.connect(
                Window.current().getLocation().getFullURL() + WS_URL,
                onMessageHandler);
        slowdownInput = (HTMLInputElement) Window.current().getDocument().getElementById("slowdown");
        slowdownInput.addEventListener("change", e -> sendDebugCommand(Double.parseDouble(slowdownInput.getValue())));
    }

    public void sendPlayerCommand(String uuid, JSArray<JSNumber> directions) {
        PlayerCommandJson playerCommand = JSObjects.create();
        playerCommand.setDirections(directions);
        playerCommand.setOrder(order++);
        playerCommand.setUuid(uuid);

        Logger.warn("client.Communicator: Sending player command");
        webSocket.sendMessage(playerCommand);
    }

    public void sendDebugCommand(double slowdownFactor) {
        DebugMessageJson debugMessage = JSObjects.create();
        debugMessage.setSlowdownFactor(slowdownFactor);

        PlayerCommandJson playerCommand = JSObjects.create();
        playerCommand.setDebugMessage(debugMessage);

        Logger.warn("Sending message setting slowdownFactor to: " + slowdownFactor);
        webSocket.sendMessage(playerCommand);
    }
}
