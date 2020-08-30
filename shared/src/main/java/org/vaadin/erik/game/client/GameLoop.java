package org.vaadin.erik.game.client;

import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSNumber;
import org.vaadin.erik.game.client.communication.Communicator;
import org.vaadin.erik.game.shared.Direction;

import java.util.HashSet;
import java.util.Set;

/**
 * The client-side game loop.
 *
 * It is responsible for sending input to the server, and (hopefully) interpolating movement, predicting movement etc.
 */
public class GameLoop {

    private static final int MAX_FPS = 100;
    private static final double MIN_MS_PER_FRAME = 1000.0/MAX_FPS;

    private final GameClient gameClient;
    private final GameCanvas gameCanvas;
    private final Communicator communicator;

    private double lastTimeStamp;
    private long requestId;

    public GameLoop(GameClient gameClient, GameCanvas gameCanvas, Communicator communicator) {
        this.gameClient = gameClient;
        this.gameCanvas = gameCanvas;
        this.communicator = communicator;
    }

    public void start() {
        requestId = Window.requestAnimationFrame(this::loop);
    }

    private void loop(double timestamp) {
        double elapsedTime = timestamp - lastTimeStamp;
        // We update at a maximum of MAX_FPS times per second.
        // If enough time hasn't passed, we return early, without updating lastTimeStamp
        if (lastTimeStamp > 0 && elapsedTime < MIN_MS_PER_FRAME) {
            requestId = Window.requestAnimationFrame(this::loop);
            return;
        }
        lastTimeStamp = timestamp;

        gameCanvas.drawAnimations(elapsedTime / 1000);

        handleButtonPresses();

        requestId = Window.requestAnimationFrame(this::loop);
    }

    private void handleButtonPresses() {
        Set<Direction> directions = new HashSet<>();

        for (String keyCode: gameClient.getPressedButtons()) {
            Direction direction = null;
            switch (keyCode) {
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
                directions.add(direction);
            }
        }

        if (!directions.isEmpty()) {
            JSArray<JSNumber> jsDirections = JSArray.of(directions.stream()
                    .map(direction -> JSNumber.valueOf(direction.ordinal()))
                    .toArray(JSNumber[]::new));
            communicator.sendPlayerCommand(gameClient.getPlayerUuid(), jsDirections);
        }
    }
}
