package org.vaadin.erik.game.shared.communication;

import org.vaadin.erik.game.shared.Player;
import org.vaadin.erik.game.shared.data.Event;

import java.util.Collection;

/**
 * The standard type of message that is sent to all users.
 */
public class GameSnapshot {

    private final Collection<Player> players;
    private final Collection<Event> events;

    public GameSnapshot(Collection<Player> players, Collection<Event> events) {
        this.players = players;
        this.events = events;
    }

    public Collection<Player> getPlayers() {
        return players;
    }

    public Collection<Event> getEvents() {
        return events;
    }
}
