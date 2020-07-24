package org.vaadin.erik.game.client.communication.json;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.vaadin.erik.game.shared.TileType;

public abstract class TileJson implements JSObject {

    @JSProperty("tileType")
    public abstract String getTileTypeName();

    @JSProperty
    public abstract double getX();

    @JSProperty
    public abstract double getY();

    @JSProperty
    public abstract double getWidth();

    @JSProperty
    public abstract double getHeight();

    public TileType getTileType() {
        return TileType.valueOf(getTileTypeName());
    }
}
