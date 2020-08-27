package org.vaadin.erik.game.shared;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameMath {

    private static final Logger logger = LogManager.getLogger(GameMath.class);

    /**
     * Limits the _absolute value_ of the first variable to the limit given.
     */
    public static double signedLimited(double value, double limit) {
        double sign = Math.signum(value);
        double result = Math.min(Math.abs(value), limit);
        return sign * result;
    }

    public static double getDistanceBetween(Point a, Point b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) +
                        Math.pow(a.getY() - b.getY(), 2));
    }
}
