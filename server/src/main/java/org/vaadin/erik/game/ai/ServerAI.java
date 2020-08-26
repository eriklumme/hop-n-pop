package org.vaadin.erik.game.ai;

import org.vaadin.erik.game.ai.pathing.NodeData;
import org.vaadin.erik.game.ai.pathing.PathingData;
import org.vaadin.erik.game.ai.step.Step;
import org.vaadin.erik.game.server.Server;
import org.vaadin.erik.game.shared.Direction;
import org.vaadin.erik.game.shared.Player;
import org.vaadin.erik.game.shared.data.PlayerCommand;

import java.util.*;

import static org.vaadin.erik.game.ai.pathing.PathingData.EMPTY_STACK;

public class ServerAI extends Player {

    private static final double SECONDS_BETWEEN_DECISIONS = 2;
    private final Server server;

    private Stack<Step> steps = EMPTY_STACK;
    private Step currentStep = null;

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

        if (currentStep != null) {
            if (currentStep.targetReached(this)) {
                currentStep = getNextStep();
            } else {
                server.handleCommand(createPlayerCommand(currentStep.getCommand(this, delta)));
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
            currentStep = getNextStep();
        }
    }

    private Step getNextStep() {
        return steps.isEmpty() ? null : steps.pop();
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
