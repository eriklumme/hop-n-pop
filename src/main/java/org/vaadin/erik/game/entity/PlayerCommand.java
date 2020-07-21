package org.vaadin.erik.game.entity;

import org.vaadin.erik.game.shared.Direction;

/**
 * A class representing a command issued by a player, such as keyboard input.
 *
 * @author erik@vaadin.com
 * @since 11/07/2020
 */
public class PlayerCommand {
    private String uuid;

    private long order;

    private Direction direction;

    /**
     * The unique identifier of the player
     */
    public String getUUID() {
        return uuid;
    }

    /**
     * The order of this command according to the client. The server will notify the client of the last order to be
     * handled, and will ignore commands with a lower order than what has already been handled.
     */
    public long getOrder() {
        return order;
    }

    /**
     * The direction the player wishes to move in.
     */
    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return String.format("%s [order=%d, direction=%s]", super.toString(), order, direction);
    }
}
