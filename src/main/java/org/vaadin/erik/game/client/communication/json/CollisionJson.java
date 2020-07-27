package org.vaadin.erik.game.client.communication.json;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.vaadin.erik.game.shared.Direction;

public abstract class CollisionJson implements JSObject {

    @JSProperty
    public abstract GameObjectJson getTarget();

    @JSProperty
    public abstract PointJson getSourceCollisionPoint();

    @JSProperty
    public abstract PointJson getTargetCollisionPoint();

    @JSProperty("sourceCollisionSide")
    abstract String getSourceCollisionSideName();

    @JSProperty("targetCollisionSide")
    abstract String getTargetCollisionSideName();

    public Direction getSourceCollisionSide() {
        return Direction.valueOf(getSourceCollisionSideName());
    }

    public Direction getTargetCollisionSide() {
        return Direction.valueOf(getTargetCollisionSideName());
    }
}
