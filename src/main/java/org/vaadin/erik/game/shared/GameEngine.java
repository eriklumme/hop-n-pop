package org.vaadin.erik.game.shared;

import org.vaadin.erik.game.entity.PlayerCommand;

public class GameEngine {

    private static final float GRAVITY = -5;

    private static final float JUMP_VELOCITY = 30;

    private static final float RUN_VELOCITY = 5;


    /**
     * Applies physics, which is more or less gravity, to the player.
     *
     * TODO: Split into two methods
     *
     * @param player    The player to update
     * @param delta     The time in milliseconds since the last update
     */
    public static void applyPhysics(Player player, PlayerCommand command, double delta) {
        // Our units are in pixels per second
        delta /= 1000;

        // 1. If the player is in the air, apply gravity
        applyGravity(player, delta);

        // 2. Apply movement commands
        if (command != null) {
            handleDirection(player, command.getDirection(), delta);
        }

        // 3. Update the player's position
        updatePosition(player, delta);

        // 4. Check for ground collision and correct position
        handleGroundCollision(player);

        double newY = player.getY() + player.getVelocityY() * delta;
        // TODO: Relative numbers? Like interpolated -1 to 1
        newY = Math.min(newY, 512 - 20);
        player.setY(newY);
    }

    private static void applyGravity(Player player, double delta) {
        if (!player.isOnGround()) {
            player.setVelocityY(player.getVelocityY() - delta* GRAVITY);
        }
    }

    private static void handleDirection(Player player, Direction direction, double delta) {
        switch (direction) {
            case UP:
                if (player.isOnGround()) {
                    player.setVelocityY(JUMP_VELOCITY);
                }
                break;
            case LEFT:
                player.setVelocityX(player.getVelocityX() -RUN_VELOCITY * delta);
                break;
            case RIGHT:
                player.setVelocityX(player.getVelocityX() + RUN_VELOCITY * delta);
                break;
        }
    }

    private static void updatePosition(Player player, double delta) {
        player.setX(player.getX() + delta * player.getVelocityX());
        player.setY(player.getY() + delta * player.getVelocityY());
    }

    private static void handleGroundCollision(Player player) {

    }

    /**
     * Updates whether or not the player is standing on solid ground
     */
    public static void updateGroundState(Player player) {

    }
}
