package org.vaadin.erik.game.shared;

public class Tile implements HasPosition {

    private final Point topLeftCorner;
    private final TileType tileType;

    public Tile(Point topLeftCorner, TileType tileType) {
        this.topLeftCorner = topLeftCorner;
        this.tileType = tileType;
    }

    public Point getTopLeftCorner() {
        return topLeftCorner;
    }

    public TileType getTileType() {
        return tileType;
    }

    public double getX() {
        return topLeftCorner.getX();
    }

    public double getY() {
        return topLeftCorner.getY();
    }

    public double getWidth() {
        return Constants.BLOCK_SIZE;
    }

    public double getHeight() {
        return Constants.BLOCK_SIZE;
    }
}
