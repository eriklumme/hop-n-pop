package org.vaadin.erik.game.ai.step;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.vaadin.erik.game.ai.NodeData;
import org.vaadin.erik.game.shared.Direction;

public abstract class AbstractStep implements Step {

    protected static final int EPSILON = 5;
    protected static final Direction[] EMPTY = new Direction[0];

    private final NodeData target;

    protected AbstractStep(NodeData target) {
        this.target = target;
    }

    protected Direction[] directions(Direction... directions) {
        return directions;
    }

    @Override
    @JsonIgnore
    public NodeData getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "AbstractStep{target=" + target + '}';
    }
}
