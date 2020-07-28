package org.vaadin.erik.game.shared;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.erik.game.entity.Action;
import org.vaadin.erik.game.entity.PlayerCommand;
import org.vaadin.erik.game.server.Server;
import org.vaadin.erik.game.shared.data.Event;
import org.vaadin.erik.game.tiles.TileMap;

import java.util.*;

public class GameEngine {

    private static final Logger logger = LogManager.getLogger(GameEngine.class);

    private static final float GRAVITY_ACCELERATION = 3000;

    private static final float JUMP_VELOCITY = -1000;

    private static final float RUN_ACCELERATION = 3000;

    private static final float FRICTION_DECELERATION = RUN_ACCELERATION;

    /**
     * Applies physics, which is more or less gravity, to the player.
     *
     * TODO: Split into two methods
     *
     * @param player    The player to update
     * @param delta     The time in milliseconds since the last update
     */
    public static List<Event> applyPhysics(Player player, PlayerCommand command, double delta) {
        List<Event> events = new ArrayList<>();

        // Our units are in pixels per second
        delta /= 1000;

        Set<Direction> nonDuplicateDirections;
        if (command != null) {
            nonDuplicateDirections = new HashSet<>(Arrays.asList(command.getDirections()));
        } else {
            nonDuplicateDirections = Collections.emptySet();
        }

        if (!player.isInGame()) {
            player.setInGame(true);
            events.add(new Event(Action.SPAWN, player));
        }

        // If the player is in the air, apply gravity
        applyForces(player, nonDuplicateDirections, delta);

        // Apply movement commands
        if (command != null) {
            handleDirection(player, nonDuplicateDirections, delta);
        }

        // Update the player's position
        updatePosition(player, delta);

        // Check and correct for tile collision
        handleTileCollision(player);

        return events;
    }

    private static void applyForces(Player player, Set<Direction> directions, double delta) {
        if (!player.isOnGround()) {
            player.addVelocity(new Vector2D(0, delta * GRAVITY_ACCELERATION));
        }

        // In our world, the ground friction and air resistance is the same
        if (!directions.contains(Direction.LEFT) && !directions.contains(Direction.RIGHT)) {
            // Everyone knows friction doesn't apply if you're actively trying to move
            double sign = Math.signum(player.getVelocity().getX());
            double newVelocity = Math.abs(player.getVelocity().getX()) - (delta * FRICTION_DECELERATION);
            player.setVelocity(new Vector2D(Math.max(newVelocity, 0) * sign, player.getVelocity().getY()));
        }
    }

    private static void handleDirection(Player player, Set<Direction> directions, double delta) {
        // TODO: Costly transformation?
        for(Direction direction: directions) {
            switch (direction) {
                case UP:
                    if (player.isOnGround()) {
                        player.setVelocity(new Vector2D(player.getVelocity().getX(), JUMP_VELOCITY));
                    }
                    break;
                case LEFT:
                case RIGHT:
                    double change = RUN_ACCELERATION * delta * direction.getSign();
                    player.addVelocity(new Vector2D(change, 0));
                    break;
            }
        }
    }

    private static void updatePosition(Player player, double delta) {
        player.setPreviousPosition(new Point(player.getX(), player.getY()));
        player.setX(player.getX() + delta * player.getVelocity().getX());
        player.setY(player.getY() + delta * player.getVelocity().getY());
    }

    private static void handleTileCollision(Player player) {
        TileMap.updateCollisions(player);

        player.setOnGround(false);

        Optional<Collision> firstCollision = player.getCollisions().stream()
                .max(Comparator.comparing(Collision::getCollisionDelta));

        firstCollision.ifPresent(collision-> {
            if (collision.getSourceCollisionSide() == Direction.DOWN) {
                player.setOnGround(true);
            }
            /*
            if (Math.abs(player.getX() - collision.getSourceCollisionPoint().getX()) < Constants.EPSILON &&
                    Math.abs(player.getY() - collision.getSourceCollisionPoint().getY()) < Constants.EPSILON) {
                // Small collision, no need to correct
                return;
            }*/

            switch (collision.getSourceCollisionSide()) {
                case DOWN:
                case UP:
                    player.setVelocity(new Vector2D(player.getVelocity().getX(), 0));
                    if (Server.DEBUG_GAME_STATE) {
                        logger.info("Resetting y velocity for player {} due to collision", player.getUUID());
                    }
                    break;
                case LEFT:
                case RIGHT:
                    player.setVelocity(new Vector2D(0, player.getVelocity().getY()));
                    if (Server.DEBUG_GAME_STATE) {
                        logger.info("Resetting x velocity for player {} due to collision", player.getUUID());
                    }
                    break;
            }
            player.setX(collision.getSourceCollisionPoint().getX());
            player.setY(collision.getSourceCollisionPoint().getY());
            if (Server.DEBUG_GAME_STATE) {
                logger.info("Player collided, resetting to position {}x {}y for player {}",
                        collision.getSourceCollisionPoint().getX(),
                        collision.getSourceCollisionPoint().getY(),
                        player.getUUID());
            }
        });
    }
}
