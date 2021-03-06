package org.vaadin.erik.game.client.communication.json;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSNumber;

public interface PlayerCommandJson extends JSObject {

    @JSProperty
    void setUuid(String uuid);

    @JSProperty
    void setNickname(String nickname);

    @JSProperty
    void setDirections(JSArray<JSNumber> directions);

    @JSProperty
    void setOrder(int order);
}
