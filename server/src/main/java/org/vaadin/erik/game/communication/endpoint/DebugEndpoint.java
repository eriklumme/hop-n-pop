package org.vaadin.erik.game.communication.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import org.vaadin.erik.game.ai.pathing.PathingManager;
import org.vaadin.erik.game.ai.recording.RecordingManager;
import org.vaadin.erik.game.server.DebugServer;
import org.vaadin.erik.game.server.Server;

import javax.validation.constraints.Positive;

@Endpoint
@AnonymousAllowed
public class DebugEndpoint {

    private final DebugServer server;

    public DebugEndpoint(Server server) {
        if (server instanceof DebugServer) {
            this.server = (DebugServer) server;
        } else {
            this.server = null;
        }
    }

    public void setServerSlowDown(@Positive int slowDownFactor) {
        if (server != null) {
            server.setSlowDownFactor(slowDownFactor);
        }
    }

    public void setFixedDelta(boolean fixedDelta) {
        if (server != null) {
            server.setFixedDelta(fixedDelta);
        }
    }

    public void calculateAIPathing() {
        if (server != null) {
            PathingManager.initialize();
        }
    }

    public void spawnAI() {
        if (server != null) {
            server.spawnAI();
        }
    }

    public void despawnAIS() {
        if (server != null) {
            server.despawnAIS();
        }
    }

    /**
     * Records the movement between two nodes, such that the AI can use it.
     *
     * Starts recording when close enough to an existing node,
     * and stops recording when first coming enough to any other node.
     */
    public void recordMovementForAI() {
        if (server != null) {
            server.startRecording();
        }
    }

    public void saveRecordedData() {
        if (server != null) {
            RecordingManager.writeToFile();
        }
    }
}
