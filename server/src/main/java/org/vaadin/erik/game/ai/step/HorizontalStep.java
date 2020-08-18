package org.vaadin.erik.game.ai.step;

import org.vaadin.erik.game.ai.NodeData;
import org.vaadin.erik.game.shared.Constants;
import org.vaadin.erik.game.shared.Direction;
import org.vaadin.erik.game.shared.Player;

public class HorizontalStep extends AbstractStep {

    public HorizontalStep(NodeData target) {
        super(target);
    }

    private double getDistanceToTarget(Player player) {
        return Math.abs(player.getPosition().getX() - getTarget().getX());
    }

    @Override
    public Direction[] getCommand(Player player, double delta) {
        if (targetReached(player)) {
            return null;
        } else if (player.getPosition().getX() < getTarget().getX()) {
            return directions(Direction.RIGHT);
        }
        return directions(Direction.LEFT);
    }

    @Override
    public double getWeight() {
        return 1;
    }

    @Override
    public boolean targetReached(Player player) {
        return getDistanceToTarget(player) < EPSILON;
    }
}
