package org.vaadin.erik.game.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.h2.schema.Constant;

import java.util.UUID;

public class Player {

    private String uuid;
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;

    @JsonIgnore
    private boolean onGround;

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

    public double getWidth() {
        return Constants.BLOCK_SIZE;
    }

    public double getHeight() {
        return Constants.BLOCK_SIZE;
    }
}
