package org.vaadin.erik.game.shared.data;

import org.vaadin.erik.game.shared.Direction;

import java.util.Arrays;

/**
 * A class representing a command issued by a player, such as keyboard input.
 *
 * @author erik@vaadin.com
 * @since 11/07/2020
 */
public class PlayerCommand {
    private String uuid;

    private String nickname;

    private long order;

    private Direction[] directions;

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
     * The direction(s) the player wishes to move in.
     */
    public Direction[] getDirections() {
        return directions;
    }

    @Override
    public String toString() {
        return String.format("%s [order=%d, directions=%s]", super.toString(), order, Arrays.toString(directions));
    }

    public String getNickname() {
        return nickname;
    }
}
