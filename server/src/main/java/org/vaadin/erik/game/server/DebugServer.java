package org.vaadin.erik.game.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.vaadin.erik.game.ai.ServerAI;
import org.vaadin.erik.game.ai.recording.DataRecorder;
import org.vaadin.erik.game.shared.Direction;
import org.vaadin.erik.game.shared.Player;
import org.vaadin.erik.game.shared.data.PlayerCommand;

import java.util.Optional;

@Service
@Profile("debug")
public class DebugServer extends Server {

    private static final Logger logger = LogManager.getLogger(DebugServer.class);

    private DataRecorder dataRecorder;

    public void setSlowDownFactor(int slowDownFactor) {
        ticker.setSlowdownFactor(slowDownFactor);
    }

    public void setFixedDelta(boolean fixedDelta) {
        ticker.setFixedDelta(fixedDelta);
    }

    public void despawnAIS() {
        for (ServerAI serverAI: serverAIS) {
            despawn(serverAI);
        }
        serverAIS.clear();
    }

    public void startRecording() {
        Optional<Player> player = players.values().stream().filter(p -> !(p instanceof ServerAI)).findFirst();
        if (player.isPresent()) {
            dataRecorder = new DataRecorder(player.get());
        } else {
            logger.error("Could not find any players to record!");
        }
    }

    @Override
    public void tick(double delta) {
        if (dataRecorder != null) {
            if (dataRecorder.isCompleted()) {
                dataRecorder = null;
            } else {
                PlayerCommand playerCommand = queuedCommands.get(dataRecorder.getPlayer());
                Direction[] directions = playerCommand != null ?
                        playerCommand.getDirections() : null;
                dataRecorder.record(delta, directions);
            }
        }
        super.tick(delta);
    }
}
