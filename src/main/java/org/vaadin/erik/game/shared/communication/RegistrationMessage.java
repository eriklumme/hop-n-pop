package org.vaadin.erik.game.shared.communication;

/**
 * The message that is sent when a player first joins the game,
 * informing the client of the player's UUID.
 */
public class RegistrationMessage {

    private final String uuid;

    public RegistrationMessage(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
