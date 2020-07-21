package org.vaadin.erik.game.client.communication;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSObjects;

/**
 * A snapshot of the game state as received from the server.
 */
public abstract class GameSnapshot implements JSObject {

    public boolean hasAction() {
        return JSObjects.hasProperty(this, "action");
    }

    @JSProperty
    public abstract int getAction();

    @JSProperty
    public abstract String getUuid();

    @JSProperty
    public abstract int getX();

    @JSProperty
    public abstract int getY();
}
