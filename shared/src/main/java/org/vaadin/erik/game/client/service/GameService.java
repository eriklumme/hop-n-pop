package org.vaadin.erik.game.client.service;

import org.teavm.jso.JSObject;

/**
 * A service that exports methods to be called from JavaScript
 */
public interface GameService extends JSObject {

    void joinGame(String nickname);

}
