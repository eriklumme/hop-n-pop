package org.vaadin.erik.game.ai.step;

import org.vaadin.erik.game.shared.Direction;

public abstract class AbstractStep implements Step {

    protected static final int EPSILON = 5;
    protected static final Direction[] EMPTY = new Direction[0];

    protected Direction[] directions(Direction... directions) {
        return directions;
    }
}
