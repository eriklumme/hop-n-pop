package org.vaadin.erik.game.ai;

import org.vaadin.erik.game.server.Server;
import org.vaadin.erik.game.shared.Direction;
import org.vaadin.erik.game.shared.Player;
import org.vaadin.erik.game.shared.data.PlayerCommand;

import java.util.Timer;
import java.util.TimerTask;

public class ServerAI {

    private static int aiIndex;

    private final Player player;

    private ServerAI(Server server) {
        player = server.spawn();

        Timer timer = new Timer("AI-" + aiIndex++);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                server.handleCommand(getJumpCommand());
            }
        }, 0, 500);

    }

    private PlayerCommand getJumpCommand() {
        return new PlayerCommand() {
            @Override
            public String getUUID() {
                return player.getUUID();
            }

            @Override
            public Direction[] getDirections() {
                return new Direction[]{ Direction.UP };
            }
        };
    }

    public static ServerAI getStarted(Server server) {
        return new ServerAI(server);
    }
}
