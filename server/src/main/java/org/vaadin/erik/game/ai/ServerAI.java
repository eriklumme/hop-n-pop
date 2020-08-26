package org.vaadin.erik.game.ai;

import org.vaadin.erik.game.ai.pathing.NodeData;
import org.vaadin.erik.game.ai.pathing.PathingData;
import org.vaadin.erik.game.ai.step.Step;
import org.vaadin.erik.game.server.Server;
import org.vaadin.erik.game.shared.Direction;
import org.vaadin.erik.game.shared.Player;
import org.vaadin.erik.game.shared.data.PlayerCommand;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ServerAI extends Player {

    private static final double SECONDS_BETWEEN_DECISIONS = 2;
    private final Server server;

    private List<Step> steps = Collections.emptyList();
    private double secondsSinceLastDecision = 0;

    public ServerAI(String color, Server server) {
        super(color);
        this.server = server;
    }

    public void takeAction(Collection<Player> players, double delta) {
        secondsSinceLastDecision += delta;
        if (secondsSinceLastDecision > SECONDS_BETWEEN_DECISIONS) {
            secondsSinceLastDecision = 0;
            makeNewDecision(players);
        }

        if (!steps.isEmpty()) {
            Step step = steps.get(0);
            if (step.targetReached(this)) {
                steps.remove(step);
            } else {
                server.handleCommand(createPlayerCommand(step.getCommand(this, delta)));
            }
        }
    }

    private void makeNewDecision(Collection<Player> players) {
        PathingData pathingData = PathingData.getPathingData();
        if (pathingData == null) {
            return;
        }

        Player closestPlayer = getClosestPlayer(players);
        if (closestPlayer != null) {
            NodeData currentClosest = pathingData.getClosestNode(this);
            NodeData target = pathingData.getClosestNode(closestPlayer);

            if (currentClosest == null || target == null) {
                System.out.println("Failed to find node close to source and/or target " +
                        "{source=" + currentClosest + ", target=" + target + "}");
                return;
            }
            steps = pathingData.getSteps(currentClosest, target);
        }
    }

    private Player getClosestPlayer(Collection<Player> players) {
        double smallestDistance = Double.MAX_VALUE;
        Player closestPlayer = null;

        for (Player player: players) {
            if (player == this) {
                continue;
            }
            double distance = Math.sqrt(
                    Math.pow(getPosition().getX() - player.getPosition().getX(), 2) +
                    Math.pow(getPosition().getY() - player.getPosition().getY(), 2)
            );
            if (distance < smallestDistance) {
                smallestDistance = distance;
                closestPlayer = player;
            }
        }
        return closestPlayer;
    }

    private PlayerCommand createPlayerCommand(Direction[] directions) {
        return new PlayerCommand() {
            @Override
            public String getUUID() {
                return ServerAI.this.getUUID();
            }

            @Override
            public Direction[] getDirections() {
                System.out.println("Walking directions " + Arrays.toString(directions));
                return directions;
            }
        };
    }
}
