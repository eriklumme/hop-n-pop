package org.vaadin.erik.game.client.communication.json;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSArray;

public interface PlayerJson extends JSObject {

    @JSProperty
    String getUuid();

    @JSProperty
    PointJson getPosition();

    @JSProperty
    Vector2DJson getVelocity();

    @JSProperty
    boolean isOnGround();

    @JSProperty
    boolean isIsInGame();

    @JSProperty
    JSArray<CollisionJson> getCollisions();
}
