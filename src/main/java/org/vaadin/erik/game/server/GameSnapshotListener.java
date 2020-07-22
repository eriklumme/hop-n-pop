package org.vaadin.erik.game.server;

import org.vaadin.erik.game.shared.Player;
import org.vaadin.erik.game.shared.data.Event;

import java.util.Collection;

public interface GameSnapshotListener {

    void onSnapshotPosted(Collection<Player> players, Collection<Event> events);
}
