package org.vaadin.erik.game.server;

import com.vaadin.flow.shared.Registration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.vaadin.erik.game.ai.ServerAI;
import org.vaadin.erik.game.shared.GameEngine;
import org.vaadin.erik.game.shared.GameMath;
import org.vaadin.erik.game.shared.Player;
import org.vaadin.erik.game.shared.Point;
import org.vaadin.erik.game.shared.data.Event;
import org.vaadin.erik.game.shared.data.PlayerCommand;
import org.vaadin.erik.game.ticker.Ticker;
import org.vaadin.erik.game.ticker.TickerTask;
import org.vaadin.erik.game.tiles.TileMap;

import java.util.*;

/**
 * The server keeps track of where players are
 */
@Service
public class Server implements TickerTask {

    private static final Logger logger = LogManager.getLogger(Server.class);

    private Map<String, Player> players = new HashMap<>();
    private Map<Player, PlayerCommand> queuedCommands = new HashMap<>();
    private Set<GameSnapshotListener> gameSnapshotListeners = new HashSet<>();
    private List<ServerAI> serverAIS = new LinkedList<>();

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
        player.setPosition(new Point(128, 0));
        players.put(player.getUUID(), player);
        return player;
    }

    public void spawnAI() {
        ServerAI serverAI = new ServerAI(this);
        serverAI.setPosition(new Point(128, 0));
        players.put(serverAI.getUUID(), serverAI);
        serverAIS.add(serverAI);
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

        queuedCommands.put(player, playerCommand);
    }

    @Override
    public void tick(double delta) {
        serverAIS.forEach(serverAI -> serverAI.takeAction(players.values(), delta));

        List<Event> events = new ArrayList<>();
        players.values().forEach(player -> {
            List<Event> playerEvents = GameEngine.applyPhysics(player, queuedCommands.get(player), delta);
            events.addAll(playerEvents);
        });

        // TODO WIP
        GameMath.handleCollisions(players.values(), TileMap::getOverlappingTiles);

        gameSnapshotListeners.forEach(listener -> listener.onSnapshotPosted(players.values(), events));
        queuedCommands.clear();
    }

    public Registration addGameSnapshotListener(GameSnapshotListener listener) {
        gameSnapshotListeners.add(listener);
        return () -> gameSnapshotListeners.remove(listener);
    }

    public void setSlowDownFactor(int slowDownFactor) {
        ticker.setSlowdownFactor(slowDownFactor);
    }
}
