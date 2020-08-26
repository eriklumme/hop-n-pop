package org.vaadin.erik.game.ai.pathing;

import org.vaadin.erik.game.ai.step.Step;
import org.vaadin.erik.game.ai.step.StepFactory;
import org.vaadin.erik.game.shared.Constants;
import org.vaadin.erik.game.shared.Point;

import java.util.ArrayList;
import java.util.List;

public class NodeData {

    private final int x;
    private final int y;
    private final List<StepFactory> steps = new ArrayList<>(2);

    public NodeData(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getIndexX() {
        return x;
    }

    public int getIndexY() {
        return y;
    }

    public int getX() {
        return x * Constants.BLOCK_SIZE;
    }

    public int getY() {
        return y * Constants.BLOCK_SIZE;
    }

    public Point getPosition() {
        return new Point(getX(), getY());
    }

    public List<StepFactory> getSteps() {
        return steps;
    }

    @Override
    public String toString() {
        return "NodeData{" +
                "x=" + x +
                ", y=" + y +
                ", steps=" + steps.size() +
                '}';
    }
}
