package org.vaadin.erik.game.server;

import com.vaadin.flow.shared.Registration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.vaadin.erik.game.entity.DebugMessage;
import org.vaadin.erik.game.shared.Player;
import org.vaadin.erik.game.entity.PlayerCommand;
import org.vaadin.erik.game.shared.GameEngine;
import org.vaadin.erik.game.shared.data.Event;
import org.vaadin.erik.game.ticker.Ticker;
import org.vaadin.erik.game.ticker.TickerTask;

import java.util.*;

/**
 * The server keeps track of where players are
 */
@Service
public class Server implements TickerTask {

    public static final boolean DEBUG_GAME_STATE = true;

    private static final Logger logger = LogManager.getLogger(Server.class);

    private Map<String, Player> players = new HashMap<>();
    private Map<Player, PlayerCommand> queuedCommands = new HashMap<>();
    private Set<GameSnapshotListener> gameSnapshotListeners = new HashSet<>();

    private final Ticker ticker;

    private long currentTime = 0;
    private int i = 0;

    public Server() {
        ticker = new Ticker(this);
        start();
    }

    public void start() {
        logger.info("Game server starting");
        ticker.start();
    }

    public Player spawn() {
        Player player = new Player();
        players.put(player.getUUID(), player);
        return player;
    }

    public void despawn(Player player) {
        players.remove(player.getUUID());
    }

    public void handleCommand(PlayerCommand playerCommand) {
        if (playerCommand.getDebugMessage() != null) {
            handleDebugMessage(playerCommand.getDebugMessage());
            return;
        }

        Player player = players.get(playerCommand.getUUID());

        if (player == null) {
            logger.warn("No player found with UUID [" + playerCommand.getUUID() + "]");
            return;
        }

        queuedCommands.put(player, playerCommand);
    }

    private void handleDebugMessage(DebugMessage debugMessage) {
        logger.warn("Handling debug message");
        ticker.setSlowdownFactor(debugMessage.getSlowdownFactor());
    }

    @Override
    public void tick(double delta) {
        if (i++ < 50) {
            long time = System.currentTimeMillis() - currentTime;
            currentTime = System.currentTimeMillis();
            System.out.println("Executing tick at " + System.currentTimeMillis() + " after " + time + " ms with delta " + delta);
        }
        List<Event> events = new ArrayList<>();
        players.values().forEach(player -> {
            List<Event> playerEvents = GameEngine.applyPhysics(player, queuedCommands.get(player), delta);
            events.addAll(playerEvents);
        });
        gameSnapshotListeners.forEach(listener -> listener.onSnapshotPosted(players.values(), events));
        queuedCommands.clear();
    }

    public Registration addGameSnapshotListener(GameSnapshotListener listener) {
        gameSnapshotListeners.add(listener);
        return () -> gameSnapshotListeners.remove(listener);
    }
}
