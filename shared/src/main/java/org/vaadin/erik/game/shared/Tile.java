package org.vaadin.erik.game.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Tile implements GameObject {

    private final Point position;
    private final TileType tileType;

    public Tile(Point position, TileType tileType) {
        this.position = position;
        this.tileType = tileType;
    }

    public TileType getTileType() {
        return tileType;
    }

    @Override
    @JsonIgnore
    public Point getPreviousPosition() {
        return getPosition();
    }

    @Override
    public void setPreviousPosition(Point position) {
        // No-op
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void setPosition(Point position) {
        // No-op
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

    @Override
    public void setVelocity(Vector2D velocity) {
        // No-op
    }
}
