package org.vaadin.erik.game.ai.pathing;

import org.vaadin.erik.game.ai.step.Step;
import org.vaadin.erik.game.ai.step.StepFactory;
import org.vaadin.erik.game.shared.Player;

import java.util.*;

import static org.vaadin.erik.game.ai.pathing.PathingManager.EMPTY_STACK;

/**
 * Calculates the shortest path between two nodes using A*
 */
class PathCalculator {

    private final NodeData source;
    private final NodeData target;
    private final Player player;

    private final Set<NodeData> closedNodes = new HashSet<>();
    private final Set<NodeData> openNodes = new HashSet<>();
    private final Map<NodeData, StepData> nodeStepData = new HashMap<>();

    PathCalculator(NodeData source, NodeData target, Player player) {
        this.source = source;
        this.target = target;
        this.player = player;
    }

    public Stack<Step> calculate() {
        openNodes.add(source);
        nodeStepData.put(source, new StepData(null, 0, 0));

        NodeData currentNode = null;
        while (!openNodes.isEmpty()) {
            currentNode = getAndRemoveSmallest();
            if (isTargetNode(currentNode)) {
                break;
            }
            double currentG = nodeStepData.get(currentNode).getG();

            for (StepFactory stepFactory: currentNode.getSteps()) {
                NodeData node = stepFactory.getTarget();
                double nodeG = currentG + stepFactory.getWeight();

                if (closedNodes.contains(node)) {
                    continue;
                }

                if (!openNodes.contains(node)) {
                    openNodes.add(node);
                    nodeStepData.put(node, new StepData(currentNode, getHeuristicCost(node), nodeG));
                } else {
                    StepData stepData = nodeStepData.get(node);
                    if (nodeG < stepData.getG()) {
                        stepData.setG(nodeG);
                        stepData.setParent(currentNode);
                    }
                }

            }
        }

        if (!isTargetNode(currentNode)) {
            return EMPTY_STACK;
        }

        Stack<Step> steps = new Stack<>();
        StepData stepData = nodeStepData.get(currentNode);
        while (stepData.getParent() != null) {
            steps.push(getStepToFrom(stepData.getParent(), currentNode));
            currentNode = stepData.getParent();
            stepData = nodeStepData.get(currentNode);
        }
        return steps;
    }

    private Step getStepToFrom(NodeData from, NodeData to) {
        for (StepFactory stepFactory: from.getSteps()) {
            if (stepFactory.getTarget() == to) {
                return stepFactory.getInstance(player);
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

    private boolean isTargetNode(NodeData node) {
        return node != null &&
                node.getIndexX() == target.getIndexX() &&
                node.getIndexY() == target.getIndexY();
    }
}
