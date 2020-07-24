package org.vaadin.erik.game.shared;

import org.vaadin.erik.game.entity.Action;
import org.vaadin.erik.game.entity.PlayerCommand;
import org.vaadin.erik.game.shared.data.Event;
import org.vaadin.erik.game.tiles.TileMap;

import java.util.*;

public class GameEngine {

    private static final float GRAVITY_ACCELERATION = 3000;

    private static final float JUMP_VELOCITY = -800;

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
            player.setVelocityY(player.getVelocityY() + delta * GRAVITY_ACCELERATION);
        }

        // In our world, the ground friction and air resistance is the same
        if (!directions.contains(Direction.LEFT) && !directions.contains(Direction.RIGHT)) {
            // Everyone knows friction doesn't apply if you're actively trying to move
            double sign = Math.signum(player.getVelocityX());
            double newVelocity = Math.abs(player.getVelocityX()) - (delta * FRICTION_DECELERATION);
            player.setVelocityX(Math.max(newVelocity, 0) * sign);
        }
    }

    private static void handleDirection(Player player, Set<Direction> directions, double delta) {
        // TODO: Costly transformation?
        for(Direction direction: directions) {
            switch (direction) {
                case UP:
                    if (player.isOnGround()) {
                        player.setVelocityY(JUMP_VELOCITY);
                    }
                    break;
                case LEFT:
                case RIGHT:
                    double change = RUN_ACCELERATION * delta * direction.getSign();
                    player.setVelocityX(player.getVelocityX() + change);
                    break;
            }
        }
    }

    private static void updatePosition(Player player, double delta) {
        player.setX(player.getX() + delta * player.getVelocityX());
        player.setY(player.getY() + delta * player.getVelocityY());
    }

    private static void handleTileCollision(Player player) {
        player.setTileCollisions(TileMap.getIntersectingTiles(player));

        player.setOnGround(false);

        // TODO: only reference to getTileCollisions()?
        // TODO: improve direction detection to depend on player's previous position?
        for (TileCollision collision: player.getTileCollisions()) {
            switch (collision.getFromDirection()) {

                case UP:
                    player.setY(collision.getTile().getY() - player.getHeight());
                    player.setVelocityY(0);
                    player.setOnGround(true);
                    break;
                case DOWN:
                    player.setY(collision.getTile().getY() + collision.getTile().getHeight());
                    player.setVelocityY(0);
                    break;
                case LEFT:
                    player.setX(collision.getTile().getX() + collision.getTile().getWidth());
                    player.setVelocityX(0);
                    break;
                case RIGHT:
                    player.setX(collision.getTile().getX() - player.getWidth());
                    player.setVelocityX(0);
                    break;
            }
        }
    }
}
