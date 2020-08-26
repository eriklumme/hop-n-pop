package org.vaadin.erik.game.ai.step;

import org.vaadin.erik.game.ai.pathing.NodeData;

public abstract class AbstractStepFactory implements StepFactory {

    private final NodeData target;

    protected AbstractStepFactory(NodeData target) {
        this.target = target;
    }

    @Override
    public NodeData getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "AbstractStep{target=" + target + '}';
    }
}
