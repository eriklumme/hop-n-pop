package org.vaadin.erik.game.client.communication.json;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSArray;

/**
 * A snapshot of the game state as received from the server.
 */
public abstract class GameSnapshotJson implements JSObject {

    @JSProperty
    public abstract JSArray<PlayerJson> getPlayers();

    @JSProperty
    public abstract JSArray<EventJson> getEvents();
}
