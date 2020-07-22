package org.vaadin.erik.game.client.communication.json;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface PointJson extends JSObject {

    @JSProperty
    double getX();

    @JSProperty
    double getY();
}
