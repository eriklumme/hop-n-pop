package org.vaadin.erik.game.shared;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.erik.game.shared.data.Action;
import org.vaadin.erik.game.shared.data.Event;
import org.vaadin.erik.game.shared.data.PlayerCommand;
import org.vaadin.erik.game.tiles.TileMap;

import java.util.*;

public class GameEngine {
    public static final boolean DEBUG_GAME_STATE = true;

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

        player.setOnGround(false);

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
        player.setPreviousPosition(new Point(player.getPosition().getX(), player.getPosition().getY()));
        player.setPosition(new Point(
                player.getPosition().getX() + delta * player.getVelocity().getX(),
                player.getPosition().getY() + delta * player.getVelocity().getY()
        ));
    }
}
