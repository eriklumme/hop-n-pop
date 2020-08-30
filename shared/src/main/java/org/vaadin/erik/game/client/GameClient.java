package org.vaadin.erik.game.client;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.json.JSON;
import org.vaadin.erik.game.client.communication.Communicator;
import org.vaadin.erik.game.client.communication.MessageEvent;
import org.vaadin.erik.game.client.communication.json.*;
import org.vaadin.erik.game.client.service.GameServiceImpl;
import org.vaadin.erik.game.client.tilemap.TileMap;
import org.vaadin.erik.game.shared.data.Action;

import java.util.HashSet;
import java.util.Set;

/**
 * The entry point for the game client running in the browser. This will be compiled to JavaScript or WebAssembly
 * using TeaVM.
 */
public class GameClient {

    public static void main(String[] args) {
        new GameClient().start();
    }

    private final HTMLDocument document = Window.current().getDocument();
    private final Communicator communicator;
    private final GameCanvas gameCanvas;
    private final TileMap tileMap;

    private String playerUuid;

    private final Set<String> pressedButtons = new HashSet<>();

    private GameClient() {
        communicator = new Communicator(this::onMessageReceived);
        gameCanvas = new GameCanvas();
        tileMap = new TileMap();

        export("GameService", new GameServiceImpl(this));
    }

    public void joinGame() {
        communicator.joinGame();
    }

    private void start() {
        document.getBody().listenKeyDown(event -> {
            if (playerUuid != null) {
                pressedButtons.add(event.getCode());
            }
        });
        document.getBody().listenKeyUp(event -> {
            if (playerUuid != null) {
                pressedButtons.remove(event.getCode());
            }
        });

        new GameLoop(this, communicator).start();
    }

    Set<String> getPressedButtons() {
        return pressedButtons;
    }

    String getPlayerUuid() {
        return playerUuid;
    }

    private void onMessageReceived(MessageEvent messageEvent) {
        JSObject object = JSON.parse(messageEvent.getResponseBody());

        if (JSObjects.hasProperty(object, "uuid")) {
            String uuid = ((RegistrationMessageJson) object).getUuid();
            if (uuid == null) {
                alert("Could not join the game at this time");
            } else {
                playerUuid = uuid;
            }
            return;
        }

        GameSnapshotJson snapshot = (GameSnapshotJson) JSON.parse(messageEvent.getResponseBody());

        gameCanvas.clear();
        gameCanvas.drawTileMap(tileMap);

        for (int i = 0; i < snapshot.getPlayers().getLength(); i++) {
            PlayerJson player = snapshot.getPlayers().get(i);
            boolean currentPlayer = playerUuid != null && playerUuid.equals(player.getUuid());
            gameCanvas.drawPlayer(player);
            gameCanvas.drawScore(player, i, currentPlayer);
        }

        for (int i = 0; i < snapshot.getEvents().getLength(); i++) {
            EventJson event = snapshot.getEvents().get(i);
            if (event.getAction() == Action.END) {
                handleEndEvent(event);
            }
        }
    }

    private void handleEndEvent(EventJson event) {
        gameCanvas.drawEnding(event.getSource(), ((JSNumber) event.getData()).intValue());
    }

    @JSBody(params = { "name", "service" }, script = "window[name] = service;")
    private static native void export(String name, JSObject service);

    @JSBody(params = "message", script = "window.alert(message);")
    private static native void alert(String message);
}
