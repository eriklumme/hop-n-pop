package org.vaadin.erik.game.client.communication.json;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSObjects;
import org.vaadin.erik.game.shared.TileType;

public abstract class GameObjectJson implements JSObject {

    @JSProperty
    public abstract double getX();

    @JSProperty
    public abstract double getY();

    @JSProperty
    public abstract double getWidth();

    @JSProperty
    public abstract double getHeight();

    @JSProperty("tileType")
    public abstract String getTileTypeName();

    public TileType getTileType() {
        return TileType.valueOf(getTileTypeName());
    }

    public boolean isTile() {
        return JSObjects.hasProperty(this, "tileType");
    }
}
