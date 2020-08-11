package org.vaadin.erik.game.shared;

public class TileCollision {

    private final Tile tile;
    private final Direction fromDirection;

    public TileCollision(Tile tile, Direction fromDirection) {
        this.tile = tile;
        this.fromDirection = fromDirection;
    }

    public Tile getTile() {
        return tile;
    }

    /**
     * The direction from which the player (likely) collided with the tile
     */
    public Direction getFromDirection() {
        return fromDirection;
    }
}
