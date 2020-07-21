package org.vaadin.erik.game.server;

import com.vaadin.flow.shared.Registration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.vaadin.erik.game.TileMap;
import org.vaadin.erik.game.entity.Player;
import org.vaadin.erik.game.entity.PlayerCommand;
import org.vaadin.erik.game.shared.GameEngine;
import org.vaadin.erik.game.ticker.Ticker;
import org.vaadin.erik.game.ticker.TickerTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The server keeps track of where players are
 */
@Service
public class Server implements TickerTask {

    private static final Logger logger = LogManager.getLogger(Server.class);

    private Map<String, Player> players = new HashMap<>();
    private Set<GameSnapshotListener> gameSnapshotListeners = new HashSet<>();

    private final Ticker ticker;

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
        player.setX(50);
        player.setY(50);
        players.put(player.getUUID(), player);
        return player;
    }

    public void despawn(Player player) {
        players.remove(player.getUUID());
    }

    public void handleCommand(PlayerCommand playerCommand) {
        Player player = players.get(playerCommand.getUUID());

        if (player == null) {
            logger.warn("No player found with UUID [" + playerCommand.getUUID() + "]");
            return;
        }

        GameEngine.handleDirection(player, playerCommand.getDirection());

        TileMap.getGroundIntersectionPoint(null, null);
    }

    @Override
    public void tick(double delta) {
        players.values().forEach(player -> GameEngine.applyPhysics(player, delta));
        gameSnapshotListeners.forEach(listener -> listener.onSnapshotPosted(players.values()));
    }

    public Registration addGameSnapshotListener(GameSnapshotListener listener) {
        gameSnapshotListeners.add(listener);
        return () -> gameSnapshotListeners.remove(listener);
    }
}
