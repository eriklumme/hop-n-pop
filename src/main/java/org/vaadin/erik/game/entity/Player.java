package org.vaadin.erik.game.entity;

import java.util.UUID;

public class Player {

    private String uuid;
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;

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
}
