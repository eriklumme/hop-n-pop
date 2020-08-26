package org.vaadin.erik.game.ai.pathing;

class StepData {

    private final double h;

    private NodeData parent;
    private double g;

    public StepData(NodeData parent, double h, double g) {
        this.parent = parent;
        this.h = h;
        this.g = g;
    }

    public double getF() {
        return g + h;
    }

    public double getH() {
        return h;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public NodeData getParent() {
        return parent;
    }

    public void setParent(NodeData parent) {
        this.parent = parent;
    }
}
