package org.vaadin.erik.game.client.communication.json;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.vaadin.erik.game.shared.TileType;

public abstract class TileJson implements JSObject {

    @JSProperty
    public abstract PointJson getTopLeftCorner();

    @JSProperty("tileType")
    public abstract String getTileTypeName();

    public TileType getTileType() {
        return TileType.valueOf(getTileTypeName());
    }
}
