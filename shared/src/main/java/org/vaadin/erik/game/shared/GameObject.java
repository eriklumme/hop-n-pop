package org.vaadin.erik.game.shared;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public interface GameObject {

    Point getPreviousPosition();

    void setPreviousPosition(Point position);

    Point getPosition();

    void setPosition(Point position);

    double getWidth();

    double getHeight();

    Vector2D getVelocity();

    void setVelocity(Vector2D velocity);
}
