package org.vaadin.erik.game.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Collision {

    @JsonIgnore // To avoid circular references
    private final GameObject source;
    private final GameObject target;

    private final Point sourceCollisionPoint;
    private final Point targetCollisionPoint;

    private final Direction sourceCollisionSide;
    private final Direction targetCollisionSide;

    public Collision(GameObject source, GameObject target,
                     Point sourceCollisionPoint, Point targetCollisionPoint,
                     Direction sourceCollisionSide, Direction targetCollisionSide) {
        this.source = source;
        this.target = target;
        this.sourceCollisionPoint = sourceCollisionPoint;
        this.targetCollisionPoint = targetCollisionPoint;
        this.sourceCollisionSide = sourceCollisionSide;
        this.targetCollisionSide = targetCollisionSide;
    }

    public GameObject getSource() {
        return source;
    }

    public GameObject getTarget() {
        return target;
    }

    public Point getSourceCollisionPoint() {
        return sourceCollisionPoint;
    }

    public Point getTargetCollisionPoint() {
        return targetCollisionPoint;
    }

    public Direction getSourceCollisionSide() {
        return sourceCollisionSide;
    }

    public Direction getTargetCollisionSide() {
        return targetCollisionSide;
    }
}
