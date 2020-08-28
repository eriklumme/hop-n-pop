package org.vaadin.erik.game.client.tilemap;

import org.teavm.jso.JSBody;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSNumber;

public abstract class TileMapService {

    @JSBody(script = "return window.tileMapData.tiles;")
    public static native JSArray<JSArray<JSArray<JSNumber>>> getTileMap();
}
