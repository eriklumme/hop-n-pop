package org.vaadin.erik.game.ai;

public class StepData {

    private final int targetIndex;

    public StepData(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    public int getTargetIndex() {
        return targetIndex;
    }
}
