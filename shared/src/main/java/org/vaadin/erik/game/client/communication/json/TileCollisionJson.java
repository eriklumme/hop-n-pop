package org.vaadin.erik.game.client.communication.json;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.vaadin.erik.game.shared.Direction;

public abstract class TileCollisionJson implements JSObject {

    @JSProperty
    public abstract TileJson getTile();

    @JSProperty("fromDirection")
    public abstract String getTileName();

    public Direction getFromDirection() {
        return Direction.valueOf(getTileName());
    }
}
