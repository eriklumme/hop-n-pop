package org.vaadin.erik.game.client.communication.json;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSNumber;

public interface PlayerJson extends JSObject {

    @JSProperty
    String getUuid();

    @JSProperty
    JSNumber getX();

    @JSProperty
    JSNumber getY();

    @JSProperty
    double getVelocityX();

    @JSProperty
    double getVelocityY();

    @JSProperty
    boolean isOnGround();

    @JSProperty
    boolean isIsInGame();

    @JSProperty
    JSArray<CollisionJson> getCollisions();
}
