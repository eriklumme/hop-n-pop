package org.vaadin.erik.game.client.communication.json;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

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
    int getIcon();

    @JSProperty
    String getNickname();

    @JSProperty
    int getPoints();
}
