package org.vaadin.erik.game.ai.step;

import org.vaadin.erik.game.ai.pathing.NodeData;
import org.vaadin.erik.game.shared.Direction;
import org.vaadin.erik.game.shared.Player;

public class HorizontalStepFactory extends AbstractStepFactory {

    public HorizontalStepFactory(NodeData target) {
        super(target);
    }

    @Override
    public Step getInstance(Player forPlayer) {
        return new HorizontalStep(forPlayer);
    }

    @Override
    public double getWeight() {
        return 1;
    }

    private class HorizontalStep extends AbstractStep {

        private final Player player;

        private HorizontalStep(Player player) {
            this.player = player;
        }

        private double getDistanceToTarget(Player player) {
            return Math.abs(player.getPosition().getX() - getTarget().getX());
        }

        @Override
        public Direction[] getCommand(double delta) {
            if (targetReached()) {
                return null;
            } else if (player.getPosition().getX() < getTarget().getX()) {
                return directions(Direction.RIGHT);
            }
            return directions(Direction.LEFT);
        }


        @Override
        public boolean targetReached() {
            return getDistanceToTarget(player) < EPSILON;
        }
    }
}
