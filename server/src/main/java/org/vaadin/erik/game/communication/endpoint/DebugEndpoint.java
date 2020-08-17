package org.vaadin.erik.game.communication.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import org.vaadin.erik.game.ai.NodeData;
import org.vaadin.erik.game.ai.PathingCalculator;
import org.vaadin.erik.game.ai.StepData;
import org.vaadin.erik.game.server.Server;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Map;

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

    public void calculateAIPathing() {
        PathingCalculator.calculate();
    }

    public Map<Integer, NodeData> getPathingData() {
        return PathingCalculator.getPathingData();
    }
}
