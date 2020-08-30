package org.vaadin.erik.game.shared;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.vaadin.erik.game.shared.communication.Vector2DSerializer;

import java.util.UUID;

public class Player implements GameObject {

    private final String uuid;
    private final int icon;
    private final String nickname;

    private Point previousPosition;
    private double x;
    private double y;
    private boolean onGround;

    private int points;

    @JsonSerialize(using = Vector2DSerializer.class)
    private Vector2D velocity = Vector2D.ZERO;

    private boolean inGame = false;

    public Player(int icon, String nickname) {
        this.icon = icon;
        this.nickname = nickname;
        uuid = UUID.randomUUID().toString();
    }

    public String getUUID() {
        return uuid;
    }

    @Override
    public Point getPreviousPosition() {
        return previousPosition != null ? previousPosition : new Point(x, y);
    }

    @Override
    public Point getPosition() {
        return new Point(x, y);
    }

    @Override
    public void setPosition(Point position) {
        double newX = position.getX();
        double newY = position.getY();

        double maxX = Constants.GAME_WIDTH - getWidth();
        double maxY = Constants.GAME_HEIGHT - getHeight();

        if (newX < 0 || newX > maxX) {
            newX = newX < 0 ? 0 : maxX;
            setVelocity(new Vector2D(0, getVelocity().getY()));
        }
        if (newY < 0 || newY > maxY) {
            if (newY < 0) {
                setVelocity(new Vector2D(getVelocity().getX(), 0));
            }
            newY = newY < 0 ? 0 : maxY;
        }

        x = newX;
        y = newY;
    }

    public void setPreviousPosition(Point previousPosition) {
        this.previousPosition = previousPosition;
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
            vy = GameMath.signedLimited(velocity.getY(), Constants.VERTICAL_MAX_VELOCITY);
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

    /**
     * Whether or not the player is currently in the game.
     *
     * There are two situations where a player is not in the game:
     * - The player has not yet spawned for the first time
     * - The player was killed, and will spawn on the next tick
     */
    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public int getIcon() {
        return icon;
    }

    public void addPoint() {
        points++;
    }

    public int getPoints() {
        return points;
    }

    public void reset() {
        inGame = false;
        points = 0;
        velocity = Vector2D.ZERO;
        previousPosition = null;
    }

    public String getNickname() {
        return nickname;
    }
}
