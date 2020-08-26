package org.vaadin.erik.game.ai.pathing;

import org.vaadin.erik.game.ai.step.Step;

import java.util.*;

/**
 * Calculates the shortest path between two nodes using A*
 */
class PathCalculator {

    private final NodeData source;
    private final NodeData target;

    private final Set<NodeData> closedNodes = new HashSet<>();
    private final Set<NodeData> openNodes = new HashSet<>();
    private final Map<NodeData, StepData> nodeStepData = new HashMap<>();

    PathCalculator(NodeData source, NodeData target) {
        this.source = source;
        this.target = target;
    }

    public List<Step> calculate() {
        openNodes.add(source);
        nodeStepData.put(source, new StepData(null, 0, 0));

        NodeData currentNode = null;
        while (!openNodes.isEmpty()) {
            currentNode = getAndRemoveSmallest();
            if (currentNode == target) {
                break;
            }
            double currentG = nodeStepData.get(currentNode).getG();

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
                    if (nodeG < stepData.getG()) {
                        stepData.setG(nodeG);
                        stepData.setParent(currentNode);
                    }
                }

            }
        }

        if (currentNode != target) {
            return Collections.emptyList();
        }

        List<Step> steps = new ArrayList<>();
        StepData stepData = nodeStepData.get(currentNode);
        while (stepData.getParent() != null) {
            steps.add(getStepToFrom(stepData.getParent(), currentNode));
            currentNode = stepData.getParent();
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
