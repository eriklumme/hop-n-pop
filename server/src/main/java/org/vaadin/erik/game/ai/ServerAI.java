package org.vaadin.erik.game.ai;

import org.vaadin.erik.game.ai.pathing.NodeData;
import org.vaadin.erik.game.ai.pathing.PathingData;
import org.vaadin.erik.game.ai.pathing.PathingManager;
import org.vaadin.erik.game.ai.step.RecordedStepFactory;
import org.vaadin.erik.game.ai.step.Step;
import org.vaadin.erik.game.server.Server;
import org.vaadin.erik.game.shared.Constants;
import org.vaadin.erik.game.shared.Direction;
import org.vaadin.erik.game.shared.GameMath;
import org.vaadin.erik.game.shared.Player;
import org.vaadin.erik.game.shared.data.PlayerCommand;

import java.util.*;

import static org.vaadin.erik.game.ai.pathing.PathingManager.EMPTY_STACK;

public class ServerAI extends Player {

    private static final double SECONDS_BETWEEN_DECISIONS = 0.5;
    private static final int CLOSE_PLAYER_THRESHOLD = 96;

    private final Server server;

    private Stack<Step> steps = EMPTY_STACK;
    private Step currentStep = null;

    private double secondsSinceLastDecision = 0;

    public ServerAI(int icon, String nickname, Server server) {
        super(icon, nickname);
        this.server = server;
    }

    public void takeAction(Collection<Player> players, double delta) {
        secondsSinceLastDecision += delta / 1000;

        // If a player is close, we go by instinct
        Player closePlayer = getClosePlayer(players);
        if (closePlayer != null) {
            steps = EMPTY_STACK;
            currentStep = null;
            server.handleCommand(createPlayerCommand(actOnClosePlayer(closePlayer)));
            return;
        }

        if (secondsSinceLastDecision > SECONDS_BETWEEN_DECISIONS) {
            secondsSinceLastDecision = 0;
            makeNewDecision(players);
        }

        if (currentStep != null) {
            if (currentStep.targetReached()) {
                currentStep = getNextStep();
            } else {
                server.handleCommand(createPlayerCommand(currentStep.getCommand(delta)));
            }
        }
    }

    private Direction[] actOnClosePlayer(Player closePlayer) {
        if (getPosition().getY() + Constants.BLOCK_SIZE < closePlayer.getPosition().getY()) {
            // We are above the player, try to hit the player
            if (getPosition().getX() < closePlayer.getPosition().getX()) {
                return new Direction[]{ Direction.RIGHT };
            } else {
                return new Direction[]{ Direction.LEFT };
            }
        }

        if (closePlayer.getPosition().getY() + Constants.BLOCK_SIZE < getPosition().getY()) {
            // We are below the player, try to avoid the player. Trying to jump is always a smart plan
            if (getPosition().getX() < closePlayer.getPosition().getX()) {
                return new Direction[]{ Direction.LEFT, Direction.UP };
            } else {
                return new Direction[]{ Direction.RIGHT, Direction.UP };
            }
        }

        // When in doubt, try to jump
        return new Direction[]{ Direction.UP };
    }

    private void makeNewDecision(Collection<Player> players) {
        // if we are executing a recorded step, execute it to the end
        if (currentStep != null && currentStep instanceof RecordedStepFactory.RecordStep) {
            RecordedStepFactory.RecordStep recordStep = (RecordedStepFactory.RecordStep) currentStep;
            if (!recordStep.targetReached()) {
                return;
            }
        }

        PathingData pathingData = PathingManager.getPathingData();
        if (pathingData == null) {
            return;
        }

        Player closestPlayer = getClosestPlayer(players);
        if (closestPlayer != null) {
            NodeData currentClosest = pathingData.getClosestNode(this, PathingData.SearchMode.BESIDE);
            NodeData target = pathingData.getClosestNode(closestPlayer, PathingData.SearchMode.BESIDE);

            if (currentClosest == null || target == null) {
                return;
            }
            steps = pathingData.getSteps(currentClosest, target, this);
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
            double distance = GameMath.getDistanceBetween(this.getPosition(), player.getPosition());
            if (distance < smallestDistance) {
                smallestDistance = distance;
                closestPlayer = player;
            }
        }
        return closestPlayer;
    }

    /**
     * Finds a very close player somewhat efficiently
     */
    private Player getClosePlayer(Collection<Player> players) {
        for (Player player: players) {
            if (player == this) {
                continue;
            }
            if (Math.abs(player.getPosition().getX() - getPosition().getX()) < CLOSE_PLAYER_THRESHOLD &&
                Math.abs(player.getPosition().getY() - getPosition().getY()) < CLOSE_PLAYER_THRESHOLD) {
                return player;
            }
        }
        return null;
    }

    private PlayerCommand createPlayerCommand(Direction[] directions) {
        return new PlayerCommand() {
            @Override
            public String getUUID() {
                return ServerAI.this.getUUID();
            }

            @Override
            public Direction[] getDirections() {
                return directions;
            }
        };
    }
}
