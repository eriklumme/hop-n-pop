package org.vaadin.erik.game.shared;

import org.vaadin.erik.game.entity.Direction;
import org.vaadin.erik.game.entity.Player;

public class GameEngine {

    private static final float GRAVITY = -5;

    private static final float JUMP_VELOCITY = 30;

    private static final float RUN_VELOCITY = 5;

    public static void handleDirection(Player player, Direction direction) {
        // TODO: Check on ground
        player.setVelocityY(player.getVelocityY() - 1);
        switch (direction) {
            case UP:
                // TODO: Check on ground
                player.setVelocityY(JUMP_VELOCITY);
                break;
            case LEFT:
                player.setVelocityX(-RUN_VELOCITY);
                break;
            case RIGHT:
                player.setVelocityX(RUN_VELOCITY);
                break;
        }
    }

    /**
     * Applies physics, which is more or less gravity, to the player.
     *
     * @param player    The player to update
     * @param delta     The time in milliseconds since the last update
     */
    public static void applyPhysics(Player player, double delta) {
        player.setVelocityY(player.getVelocityY() - ((delta / 1000) * GRAVITY));

        double newY = player.getY() + player.getVelocityY() * delta;
        // TODO: Relative numbers? Like interpolated -1 to 1
        newY = Math.min(newY, 512 - 20);
        player.setY(newY);
    }
}
