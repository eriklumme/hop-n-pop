package org.vaadin.erik.game.client;

import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.json.JSON;
import org.vaadin.erik.game.client.communication.Communicator;
import org.vaadin.erik.game.client.communication.MessageEvent;
import org.vaadin.erik.game.client.communication.json.*;
import org.vaadin.erik.game.client.tilemap.TileMap;

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

    private Set<String> pressedButtons = new HashSet<>();

    private GameClient() {
        communicator = new Communicator(this::onMessageReceived);
        gameCanvas = new GameCanvas();
        tileMap = new TileMap();
    }

    private void start() {
        document.getBody().listenKeyDown(event -> {
            Logger.warn("Keydown!");
            pressedButtons.add(event.getCode());
        });
        document.getBody().listenKeyUp(event -> {
            Logger.warn("Keyup!");
            pressedButtons.remove(event.getCode());
        });

        new GameLoop(this, communicator).start();
    }

    Set<String> getPressedButtons() {
        return pressedButtons;
    }

    String getPlayerUuid() {
        return playerUuid;
    }

    private void onMessageReceived(MessageEvent event) {
        JSObject object = JSON.parse(event.getResponseBody());

        if (JSObjects.hasProperty(object, "uuid")) {
            playerUuid = ((RegistrationMessageJson) object).getUuid();
            return;
        }

        GameSnapshotJson snapshot = (GameSnapshotJson) JSON.parse(event.getResponseBody());

        gameCanvas.clear();
        gameCanvas.drawTileMap(tileMap);

        for (int i = 0; i < snapshot.getPlayers().getLength(); i++) {
            PlayerJson player = snapshot.getPlayers().get(i);
            gameCanvas.drawPlayer((int) player.getPosition().getX(), (int) player.getPosition().getY());

            for (int j = 0; j < player.getCollisions().getLength(); j++) {
                CollisionJson collision = player.getCollisions().get(j);
                if (collision.getTarget().isTile()) {
                    gameCanvas.drawCollision(collision.getTarget(), collision.getTargetCollisionSide());
                }
            }
        }
    }
}
