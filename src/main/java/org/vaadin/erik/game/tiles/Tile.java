package org.vaadin.erik.game.tiles;

import org.vaadin.erik.game.entity.Point;
import org.vaadin.erik.game.shared.Constants;
import org.vaadin.erik.game.shared.TileType;

public class Tile {

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

    public int getWidth() {
        return Constants.BLOCK_SIZE;
    }

    public int getHeight() {
        return Constants.BLOCK_SIZE;
    }
}
