package org.vaadin.erik.game.shared;

/**
 * Describes possible player movement
 *
 * @author erik@vaadin.com
 * @since 11/07/2020
 */
public enum Direction {
    UP(-1), DOWN(1), LEFT(-1), RIGHT(1);

    private final double sign;

    Direction(double sign) {
        this.sign = sign;
    }

    public double getSign() {
        return sign;
    }
}
