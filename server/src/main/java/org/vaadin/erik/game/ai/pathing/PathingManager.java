package org.vaadin.erik.game.ai.pathing;

import org.vaadin.erik.game.ai.step.Step;

import java.util.Stack;

public class PathingManager {

    public static final Stack<Step> EMPTY_STACK = new Stack<>();

    private static PathingData pathingData;

    public static void initialize() {
        PathingData pathingData = new PathingData();
        pathingData.initializeNodeData();
        PathingManager.pathingData = pathingData;
        pathingData.addHorizontalSteps();
        pathingData.addJumpSteps();
    }

    public static PathingData getPathingData() {
        return pathingData;
    }

}
