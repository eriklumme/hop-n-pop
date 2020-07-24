package org.vaadin.erik.game.shared;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.vaadin.erik.game.communication.Vector2DSerializer;

import java.util.Collection;
import java.util.UUID;

public class Player implements GameObject {

    private final String uuid;
    private double x;
    private double y;
    private boolean onGround;

    @JsonSerialize(using = Vector2DSerializer.class)
    private Vector2D velocity = Vector2D.ZERO;

    private boolean isInGame = false;

    private Collection<TileCollision> tileCollisions;

    public Player() {
        uuid = UUID.randomUUID().toString();
    }

    public String getUUID() {
        return uuid;
    }

    @Override
    public double getX() {
        return x;
    }

    public void setX(double x) {
        double maxX = Constants.GAME_WIDTH - getWidth();
        if (x < 0 || x > maxX) {
            x = x < 0 ? 0 : maxX;
            velocity = new Vector2D(0, velocity.getY());
        }
        this.x = x;
    }

    @Override
    public double getY() {
        return y;
    }

    public void setY(double y) {
        double maxY = Constants.GAME_HEIGHT - getHeight();
        if (y < 0 || y > maxY) {
            y = y < 0 ? 0 : maxY;
            velocity = new Vector2D(velocity.getX(), 0);
        }
        this.y = y;
    }

    @Override
    public Vector2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2D velocity) {
        double vx = GameMath.signedLimited(velocity.getX(), Constants.MAX_VELOCITY);
        double vy = velocity.getY();
        if (velocity.getY() > 0) {
            // Vertical max velocity only applies when falling down
            vy = GameMath.signedLimited(velocity.getY(), Constants.MAX_VELOCITY);
        }
        this.velocity = new Vector2D(vx, vy);
    }

    public void addVelocity(Vector2D velocity) {
        setVelocity(this.velocity.add(velocity));
    }

    @Override
    public double getWidth() {
        return Constants.BLOCK_SIZE;
    }

    @Override
    public double getHeight() {
        return Constants.BLOCK_SIZE;
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
