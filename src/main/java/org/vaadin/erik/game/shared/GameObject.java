package org.vaadin.erik.game.shared;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public interface GameObject {

    Point getPreviousPosition();

    double getX();

    double getY();

    double getWidth();

    double getHeight();

    Vector2D getVelocity();
}
