package org.vaadin.erik.game.client;

import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.json.JSON;
import org.vaadin.erik.game.client.communication.Communicator;
import org.vaadin.erik.game.client.communication.MessageEvent;
import org.vaadin.erik.game.client.communication.json.GameSnapshotJson;
import org.vaadin.erik.game.client.communication.json.PlayerJson;
import org.vaadin.erik.game.client.communication.json.RegistrationMessageJson;
import org.vaadin.erik.game.client.communication.json.TileCollisionJson;
import org.vaadin.erik.game.client.tilemap.TileMap;
import org.vaadin.erik.game.shared.Direction;
import org.vaadin.erik.game.shared.TileCollision;

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

    private GameClient() {
        communicator = new Communicator(this::onMessageReceived);
        gameCanvas = new GameCanvas();
        tileMap = new TileMap();
    }

    private void start() {
        document.getBody().listenKeyDown(event -> {
            Logger.warn("Keydown!");
            Direction direction = null;
            switch (event.getCode()) {
                case "ArrowUp":
                    direction = Direction.UP;
                    break;
                case "ArrowLeft":
                    direction = Direction.LEFT;
                    break;
                case "ArrowRight":
                    direction = Direction.RIGHT;
                    break;
            }
            if (direction != null) {
                communicator.sendPlayerCommand(playerUuid, direction);
            }
        });
    }

    private void onMessageReceived(MessageEvent event) {
        Logger.warn("Message received in the TeaVM GameClient");

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
            gameCanvas.drawPlayer(player.getX().intValue(), player.getY().intValue());

            for (int j = 0; j < player.getTileCollisions().getLength(); j++) {
                TileCollisionJson collision = player.getTileCollisions().get(j);
                gameCanvas.drawCollision(collision.getTile(), collision.getFromDirection());
            }
        }
    }
}
