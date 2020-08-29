package org.vaadin.erik.game.communication.endpoint;

public class ServerInfo {

    private final boolean full;
    private final boolean debugEnabled;

    public ServerInfo(boolean full, boolean debugEnabled) {
        this.full = full;
        this.debugEnabled = debugEnabled;
    }

    public boolean isFull() {
        return full;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }
}
