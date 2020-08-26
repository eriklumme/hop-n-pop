package org.vaadin.erik.game.communication.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import org.vaadin.erik.game.ai.pathing.PathingData;
import org.vaadin.erik.game.server.Server;

import javax.validation.constraints.Positive;

@Endpoint
@AnonymousAllowed
public class DebugEndpoint {

    private final Server server;

    public DebugEndpoint(Server server) {
        this.server = server;
    }

    public void setServerSlowDown(@Positive int slowDownFactor) {
        server.setSlowDownFactor(slowDownFactor);
    }

    public void setFixedDelta(boolean fixedDelta) {
        server.setFixedDelta(fixedDelta);
    }

    public void calculateAIPathing() {
        PathingData.calculate();
    }

    public void spawnAI() {
        server.spawnAI();
    }

    public void despawnAIS() {
        server.despawnAIS();
    }
}
