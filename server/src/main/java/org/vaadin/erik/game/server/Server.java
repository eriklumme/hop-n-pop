package org.vaadin.erik.game.server;

import com.vaadin.flow.shared.Registration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.vaadin.erik.game.ai.ServerAI;
import org.vaadin.erik.game.ai.pathing.PathingManager;
import org.vaadin.erik.game.shared.*;
import org.vaadin.erik.game.shared.data.Action;
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
@Profile("!debug")
public class Server implements TickerTask {

    private static final Logger logger = LogManager.getLogger(Server.class);

    protected final Map<String, Player> players = new HashMap<>();
    protected final Map<Player, PlayerCommand> queuedCommands = new HashMap<>();
    protected final List<ServerAI> serverAIS = new LinkedList<>();
    protected final Ticker ticker;

    private final Set<GameSnapshotListener> gameSnapshotListeners = new HashSet<>();
    private final Set<Integer> usedIcons = new HashSet<>();

    private boolean ended;
    private Player winner;
    private long gameEndMs;

    public Server() {
        ticker = new Ticker(this);
        PathingManager.initialize();
        start();
    }

    public void start() {
        logger.info("Game server starting");
        ticker.start();
        spawnAI();
    }

    public Player spawn(String nickname) {
        synchronized (players) {
            if (isFull()) {
                return null;
            }
            Player player = new Player(getIcon(), nickname);
            players.put(player.getUUID(), player);
            return player;
        }
    }

    public void spawnAI() {
        synchronized (players) {
            ServerAI serverAI = new ServerAI(getIcon(), "Abot", this);
            serverAI.setPosition(new Point(128, 0));
            players.put(serverAI.getUUID(), serverAI);
            serverAIS.add(serverAI);
        }
    }

    private int getIcon() {
        int selectedIcon = 0;
        for (int icon = 0; icon < 8; icon++) {
            if (!usedIcons.contains(icon)) {
                selectedIcon = icon;
                usedIcons.add(selectedIcon);
                break;
            }
        }
        return selectedIcon;
    }

    public void despawn(Player player) {
        players.remove(player.getUUID());
    }

    public void kill(Player killer, Player victim) {
        killer.addPoint();
        victim.setInGame(false);

        if (killer.getPoints() >= Constants.WIN_POINTS) {
            endGame(killer);
        }
    }

    private void endGame(Player winner) {
        this.winner = winner;
        gameEndMs = GameMath.nanosToMs(System.nanoTime());
        ended = true;

        new ArrayList<>(players.values()).forEach(Player::reset);
    }

    public void handleCommand(PlayerCommand playerCommand) {
        Player player = players.get(playerCommand.getUUID());

        if (player == null || !player.isInGame()) {
            return;
        }

        queuedCommands.put(player, playerCommand);
    }

    @Override
    public void tick(double delta) {
        List<Player> players = new ArrayList<>(this.players.values());
        List<ServerAI> serverAIS = new ArrayList<>(this.serverAIS);
        List<Event> events = new ArrayList<>();

        if (!ended) {
            serverAIS.forEach(serverAI -> serverAI.takeAction(players, delta));

            players.forEach(player -> {
                if (!player.isInGame()) {
                    Point spawnPoint = getSpawnPoint(player, players);
                    if (spawnPoint != null) {
                        player.setInGame(true);
                        player.setPosition(spawnPoint);
                        events.add(new Event(Action.SPAWN, player));
                    }
                }

                List<Event> playerEvents = GameEngine.applyPhysics(player, queuedCommands.get(player), delta);
                events.addAll(playerEvents);
            });

            Collection<Event> collisionEvents = CollisionHandler.handleCollisions(players, TileMap::getOverlappingTiles);
            for (Event event: collisionEvents) {
                if (event.getAction() == Action.KILL) {
                    kill(event.getSource(), event.getTarget());
                }
            }
            events.addAll(collisionEvents);
        } else {
            double secondsElapsed = (GameMath.nanosToMs(System.nanoTime()) - gameEndMs) / 1000.0;
            double countdownSeconds = Constants.SECONDS_BETWEEN_ROUNDS - secondsElapsed;
            events.add(new Event(Action.END, winner, null, countdownSeconds));

            if (countdownSeconds <= 0) {
                ended = false;
            }
        }

        gameSnapshotListeners.forEach(listener -> listener.onSnapshotPosted(players, events));
        queuedCommands.clear();
    }

    public Registration addGameSnapshotListener(GameSnapshotListener listener) {
        gameSnapshotListeners.add(listener);
        return () -> gameSnapshotListeners.remove(listener);
    }

    private static final Point[] SPAWN_POINTS = new Point[]{
            new Point(3, 0),
            new Point(8, 0),
            new Point(20, 0),
            new Point(3, 8),
            new Point(0, 10),
            new Point(20, 10)
    };

    /**
     * Hardcoded points and inefficient code because time.
     *
     * Tries to return a spawn point that is not currently intersecting with a player.
     */
    private static Point getSpawnPoint(Player forPlayer, Collection<Player> players) {
        List<Point> spawnPoints = Arrays.asList(SPAWN_POINTS);
        Collections.shuffle(spawnPoints);

        for(Point point: spawnPoints) {
            boolean found = true;
            for(Player player: players) {
                if (player == forPlayer) {
                    continue;
                }
                Tile tile = TileMap.getTiles()[(int) point.getY()][(int) point.getX()];
                if (CollisionHandler.areIntersecting(player, tile)) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return new Point(Constants.BLOCK_SIZE * point.getX(), Constants.BLOCK_SIZE * point.getY());
            }
        }
        return null;
    }

    public boolean isFull() {
        return players.size() >= Constants.MAX_PLAYERS;
    }
}
