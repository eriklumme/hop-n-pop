package org.vaadin.erik.game.ai.step;

import org.vaadin.erik.game.ai.pathing.NodeData;
import org.vaadin.erik.game.shared.Player;

public interface StepFactory {

    /**
     * Gets an instance of this step specific for the given player.
     */
    Step getInstance(Player forPlayer);

    /**
     * A measure more or less relative to how long this step would take
     */
    double getWeight();

    /**
     * Returns the target node.
     */
    NodeData getTarget();

}
