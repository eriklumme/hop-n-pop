package org.vaadin.erik.game.client;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.json.JSON;
import org.vaadin.erik.game.client.communication.Communicator;
import org.vaadin.erik.game.client.communication.GameSnapshot;
import org.vaadin.erik.game.client.communication.MessageEvent;
import org.vaadin.erik.game.entity.Action;
import org.vaadin.erik.game.entity.Direction;

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

    private String playerUuid;

    private GameClient() {
        communicator = new Communicator(this::onMessageReceived);
        gameCanvas = new GameCanvas();
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

        GameSnapshot snapshot = (GameSnapshot) JSON.parse(event.getResponseBody());
        if (snapshot.hasAction()) {
            Action action = Action.values()[snapshot.getAction()];
            switch (action) {
                case SPAWN:
                    Logger.warn("Player with UUID [" + snapshot.getUuid() + "] has spawned");
                    playerUuid = snapshot.getUuid();
                    break;
                case KILL:
                    break;
                case DIE:
                    break;
            }
        }
        gameCanvas.clear();
        gameCanvas.drawPlayer(snapshot.getX(), snapshot.getY());
    }
}
