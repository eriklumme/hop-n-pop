package org.vaadin.erik.game.ai;

import java.util.ArrayList;
import java.util.List;

public class NodeData {

    private final int x;
    private final int y;
    private final List<StepData> stepData = new ArrayList<>(2);

    public NodeData(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<StepData> getStepData() {
        return stepData;
    }
}
