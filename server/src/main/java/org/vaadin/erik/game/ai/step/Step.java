package org.vaadin.erik.game.ai.step;

import org.vaadin.erik.game.shared.Direction;

import javax.validation.constraints.NotNull;

public interface Step {

    /**
     * Given the player in its current state, and the seconds since this step started,
     * returns the directions to take, can be empty.
     */
    @NotNull
    Direction[] getCommand(double delta);


    /**
     * True if the player is sufficiently close to the target
     */
    boolean targetReached();
}
