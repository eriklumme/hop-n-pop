package org.vaadin.erik.game.ai.step;

import org.vaadin.erik.game.ai.pathing.NodeData;
import org.vaadin.erik.game.shared.Direction;
import org.vaadin.erik.game.shared.Player;

import javax.validation.constraints.NotNull;

public interface Step {

    /**
     * Returns the target node.
     */
    NodeData getTarget();

    /**
     * Given the player in its current state, and the seconds since this step started,
     * returns the directions to take, can be empty.
     */
    @NotNull
    Direction[] getCommand(Player player, double delta);

    /**
     * A measure more or less relative to how long this step would take
     */
    double getWeight();

    /**
     * True if the player is sufficiently close to the target
     */
    boolean targetReached(Player player);
}
