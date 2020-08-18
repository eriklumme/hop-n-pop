package org.vaadin.erik.game.ai;

import org.vaadin.erik.game.ai.step.HorizontalStep;
import org.vaadin.erik.game.ai.step.Step;
import org.vaadin.erik.game.shared.Constants;
import org.vaadin.erik.game.shared.GameObject;
import org.vaadin.erik.game.shared.Tile;
import org.vaadin.erik.game.shared.TileType;
import org.vaadin.erik.game.tiles.TileMap;

import java.util.*;

public class PathingData {

    private static PathingData pathingData;

    private final Tile[][] tiles = TileMap.getTiles();
    private final Map<Tile, NodeData> tileToNodeData = new HashMap<>();

    private PathingData() {
    }

    private Tile[][] getTiles() {
        return tiles;
    }

    private Map<Tile, NodeData> getTileToNodeData() {
        return tileToNodeData;
    }

    private void createNodeData() {
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                Tile tile = tiles[y][x];
                if (tile.getTileType() == TileType.AIR) {
                    if (y < tiles.length - 1 && tiles[y+1][x].getTileType() == TileType.GROUND) {
                        tileToNodeData.put(tile, new NodeData(x, y));
                    }
                }
            }
        }
    }

    private void addHorizontalSteps() {
        for (NodeData nodeData: tileToNodeData.values()) {
            NodeData neighbor;
            for (int i = -1; i <= 1; i += 2) {
                neighbor = dataAtIndex(nodeData.getIndexX() + i, nodeData.getIndexY());
                if (neighbor != null) {
                    nodeData.getSteps().add(new HorizontalStep(neighbor));
                }
            }
        }
    }

    private NodeData dataAtIndex(int x, int y) {
        if (y >= 0 && y < tiles.length) {
            Tile[] tileRow = tiles[y];
            if (x >= 0 && x < tileRow.length) {
                return tileToNodeData.get(tileRow[x]);
            }
        }
        return null;
    }

    /**
     * Gets the {@link NodeData} closest to the given position, or below the given position.
     *
     * Returns null if none was found.
     */
    public NodeData getClosestNode(GameObject gameObject) {
        int x = (int) gameObject.getPosition().getX() / Constants.BLOCK_SIZE;
        int y = (int) gameObject.getPosition().getY() / Constants.BLOCK_SIZE;

        NodeData nodeData = null;
        while (y < tiles.length && nodeData == null) {
            nodeData = dataAtIndex(x, y++);
        }
        return nodeData;
    }

    public List<Step> getSteps(NodeData from, NodeData to) {
        return new PathCalculator(from, to).calculate();
    }

    public static void calculate() {
        PathingData pathingData = new PathingData();
        pathingData.createNodeData();
        pathingData.addHorizontalSteps();
        PathingData.pathingData = pathingData;
    }

    public static PathingData getPathingData() {
        return pathingData;
    }

    /**
     * Calculates the shortest path between two nodes using A*
     */
    private static class PathCalculator {

        private final NodeData source;
        private final NodeData target;

        private final Set<NodeData> closedNodes = new HashSet<>();
        private final Set<NodeData> openNodes = new HashSet<>();
        private final Map<NodeData, StepData> nodeStepData = new HashMap<>();

        private PathCalculator(NodeData source, NodeData target) {
            this.source = source;
            this.target = target;
        }

        private List<Step> calculate() {
            openNodes.add(source);
            nodeStepData.put(source, new StepData(null, 0, 0));

            NodeData currentNode = null;
            while (!openNodes.isEmpty()) {
                currentNode = getAndRemoveSmallest();
                if (currentNode == target) {
                    break;
                }
                double currentG = nodeStepData.get(currentNode).g;

                for (Step step: currentNode.getSteps()) {
                    NodeData node = step.getTarget();
                    double nodeG = currentG + step.getWeight();

                    if (closedNodes.contains(node)) {
                        continue;
                    }

                    if (!openNodes.contains(node)) {
                        openNodes.add(node);
                        nodeStepData.put(node, new StepData(currentNode, getHeuristicCost(node), nodeG));
                    } else {
                        StepData stepData = nodeStepData.get(node);
                        if (nodeG < stepData.g) {
                            stepData.g = nodeG;
                            stepData.parent = currentNode;
                        }
                    }

                }
            }

            if (currentNode != target) {
                return Collections.emptyList();
            }

            List<Step> steps = new ArrayList<>();
            StepData stepData = nodeStepData.get(currentNode);
            while (stepData.parent != null) {
                steps.add(getStepToFrom(stepData.parent, currentNode));
                currentNode = stepData.parent;
                stepData = nodeStepData.get(currentNode);
            }
            return steps;
        }

        private Step getStepToFrom(NodeData from, NodeData to) {
            for (Step step: from.getSteps()) {
                if (step.getTarget() == to) {
                    return step;
                }
            }
            throw new IllegalStateException("Could not find a step back from nodes for which a step forward was found.");
        }

        private NodeData getAndRemoveSmallest() {
            NodeData minCostNode = null;
            double minCost = Double.MAX_VALUE;

            for (NodeData nodeData: openNodes) {
                double cost = nodeStepData.get(nodeData).getF();
                if (cost < minCost) {
                    minCost = cost;
                    minCostNode = nodeData;
                }
            }
            openNodes.remove(minCostNode);
            closedNodes.add(minCostNode);
            return minCostNode;
        }

        private double getHeuristicCost(NodeData from) {
            return Math.pow(from.getX() - target.getX(), 2) +
                    Math.pow(from.getY() - target.getY(), 2);
        }
    }

    private static class StepData {

        private final double h;

        private NodeData parent;
        private double g;

        private StepData(NodeData parent, double h, double g) {
            this.parent = parent;
            this.h = h;
            this.g = g;
        }

        private double getF() {
            return g + h;
        }
    }
}
