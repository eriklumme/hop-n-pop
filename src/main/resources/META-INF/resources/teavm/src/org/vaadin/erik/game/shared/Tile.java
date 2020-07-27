package org.vaadin.erik.game.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Tile implements GameObject {

    private final Point topLeftCorner;
    private final TileType tileType;

    public Tile(Point topLeftCorner, TileType tileType) {
        this.topLeftCorner = topLeftCorner;
        this.tileType = tileType;
    }

    public TileType getTileType() {
        return tileType;
    }

    @Override
    @JsonIgnore
    public Point getPreviousPosition() {
        return new Point(getX(), getY());
    }

    @Override
    public double getX() {
        return topLeftCorner.getX();
    }

    @Override
    public double getY() {
        return topLeftCorner.getY();
    }

    @Override
    public double getWidth() {
        return Constants.BLOCK_SIZE;
    }

    @Override
    public double getHeight() {
        return Constants.BLOCK_SIZE;
    }

    @Override
    @JsonIgnore
    public Vector2D getVelocity() {
        return Vector2D.ZERO;
    }
}
