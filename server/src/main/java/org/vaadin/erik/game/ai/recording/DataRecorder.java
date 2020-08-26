package org.vaadin.erik.game.ai.recording;

import org.vaadin.erik.game.ai.pathing.NodeData;
import org.vaadin.erik.game.ai.pathing.PathingData;
import org.vaadin.erik.game.ai.pathing.PathingManager;
import org.vaadin.erik.game.shared.Direction;
import org.vaadin.erik.game.shared.GameMath;
import org.vaadin.erik.game.shared.Player;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataRecorder {

    private static final double MAX_DISTANCE_TO_NODE = 5;
    private static final double SAMPLE_TIME = 1000.0 / 10;

    private final Player player;
    private final PathingData pathingData;
    private final Map<Double, Direction[]> actionMap = new LinkedHashMap<>();

    private double lastSampleDelta;
    private double sumDelta;
    private boolean completed;

    private NodeData startNode;
    private NodeData endNode;

    public DataRecorder(Player player) {
        this.player = player;
        this.pathingData = PathingManager.getPathingData();
    }

    public void record(double delta, Direction[] directions) {
        if (pathingData == null) {
            throw new IllegalStateException("Can not start a recording without pathing data");
        }
        if (getStartNode() == null) {
            return;
        }

        if (directions != null && directions.length > 0 &&
                (lastSampleDelta == 0 || (sumDelta - lastSampleDelta) < SAMPLE_TIME)) {
            actionMap.put(sumDelta, directions);
            lastSampleDelta = sumDelta;
        }
        // Only record delta after the first action
        if (!actionMap.isEmpty()) {
            sumDelta += delta;
        }

        if (getEndNode() != null) {
            completed = true;
            printRecording();
            RecordingManager.addRecordData(new RecordData(startNode, endNode, actionMap));
        }
    }

    private NodeData getStartNode() {
        if (startNode == null) {
            startNode = getClosestNode();
            if (startNode != null) {
                System.out.println(String.format("Starting recording from node [%d,%d]",
                        startNode.getIndexX(), startNode.getIndexY()));
            }
        }
        return startNode;
    }

    private NodeData getEndNode() {
        if (endNode != null) {
            throw new IllegalStateException("getEndNode() should not be called after an end node has already been found");
        }
        NodeData candidate = getClosestNode();
        if (candidate != null && candidate != startNode) {
            endNode = candidate;
            System.out.println(String.format("Ending recording at node [%d,%d]",
                    endNode.getIndexX(), endNode.getIndexY()));
        }
        return endNode;
    }

    private NodeData getClosestNode() {
        NodeData candidate = pathingData.getClosestNode(player, PathingData.SearchMode.EXACT);
        if (candidate != null &&
                GameMath.getDistanceBetween(player.getPosition(), candidate.getPosition()) < MAX_DISTANCE_TO_NODE) {
            return candidate;
        }
        return null;
    }

    private void printRecording() {
        System.out.println("=============================");
        System.out.println(String.format("Started at [%d,%d]", startNode.getIndexX(), startNode.getIndexY()));
        for (Map.Entry<Double, Direction[]> action: actionMap.entrySet()) {
            System.out.println(String.format("At %.3f took actions %s",
                    action.getKey(),
                    Arrays.toString(action.getValue())));
        }
        System.out.println(String.format("Ended at [%d,%d]", endNode.getIndexX(), endNode.getIndexY()));
        System.out.println("=============================");
    }

    public boolean isCompleted() {
        return completed;
    }

    public Player getPlayer() {
        return player;
    }
}
