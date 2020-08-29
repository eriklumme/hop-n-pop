package org.vaadin.erik.game.communication.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import org.vaadin.erik.game.ai.pathing.PathingManager;
import org.vaadin.erik.game.ai.recording.RecordingManager;
import org.vaadin.erik.game.server.DebugServer;
import org.vaadin.erik.game.server.Server;

import javax.validation.constraints.Positive;
import java.util.Optional;

@Endpoint
@AnonymousAllowed
public class ServerEndpoint {

    private final Server server;

    public ServerEndpoint(Server server) {
        this.server = server;
    }

    private Optional<DebugServer> getDebugServer() {
        return Optional.ofNullable(server instanceof DebugServer ? (DebugServer) server : null);
    }

    public ServerInfo getServerInfo() {
        return new ServerInfo(server.isFull(), getDebugServer().isPresent());
    }

    public void setServerSlowDown(@Positive int slowDownFactor) {
        getDebugServer().ifPresent(debugServer -> debugServer.setSlowDownFactor(slowDownFactor));
    }

    public void setFixedDelta(boolean fixedDelta) {
        getDebugServer().ifPresent(debugServer -> debugServer.setFixedDelta(fixedDelta));
    }

    public void calculateAIPathing() {
        getDebugServer().ifPresent(d -> PathingManager.initialize());
    }

    public void spawnAI() {
        getDebugServer().ifPresent(DebugServer::spawnAI);
    }

    public void despawnAIS() {
        getDebugServer().ifPresent(DebugServer::despawnAIS);
    }

    /**
     * Records the movement between two nodes, such that the AI can use it.
     *
     * Starts recording when close enough to an existing node,
     * and stops recording when first coming enough to any other node.
     */
    public void recordMovementForAI() {
        getDebugServer().ifPresent(DebugServer::startRecording);
    }

    public void saveRecordedData() {
        getDebugServer().ifPresent(d -> RecordingManager.writeToFile());
    }
}
