package org.vaadin.erik.game.shared;

import java.util.Collection;
import java.util.UUID;

public class Player implements HasPosition {

    private final String uuid;
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;
    private boolean onGround;

    private boolean isInGame = false;

    private Collection<TileCollision> tileCollisions;

    public Player() {
        uuid = UUID.randomUUID().toString();
    }

    public String getUUID() {
        return uuid;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public Collection<TileCollision> getTileCollisions() {
        return tileCollisions;
    }

    public void setTileCollisions(Collection<TileCollision> tileCollisions) {
        this.tileCollisions = tileCollisions;
    }

    public double getWidth() {
        return Constants.BLOCK_SIZE;
    }

    public double getHeight() {
        return Constants.BLOCK_SIZE;
    }

    /**
     * Whether or not the player is currently in the game.
     *
     * There are two situations where a player is not in the game:
     * - The player has not yet spawned for the first time
     * - The player was killed, and will spawn on the next tick
     */
    public boolean isInGame() {
        return isInGame;
    }

    public void setInGame(boolean inGame) {
        isInGame = inGame;
    }
}
