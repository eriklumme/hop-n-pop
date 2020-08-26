package org.vaadin.erik.game.shared;

/**
 * Describes possible player movement
 *
 * @author erik@vaadin.com
 * @since 11/07/2020
 */
public enum Direction {
    UP(-1), DOWN(1), LEFT(-1), RIGHT(1), NONE(0);

    private final double sign;

    Direction(double sign) {
        this.sign = sign;
    }

    public double getSign() {
        return sign;
    }

    public boolean isHorizontal() {
        return this == LEFT || this == RIGHT;
    }

    public static Direction getOpposite(Direction direction) {
        return direction.getSign() == -1 ?
                (direction == Direction.UP ? DOWN : RIGHT) :
                (direction == Direction.RIGHT ? LEFT : UP);
    }

    public static Direction getHorizontal(double from, double to) {
        return from == to ? NONE : from < to ? RIGHT : LEFT;
    }

    public static Direction getVertical(double from, double to) {
        return from == to ? NONE : from < to ? DOWN : UP;
    }

    public static int[] toIntArray(Direction[] directions) {
        int[] intArray = new int[directions.length];
        int i = 0;
        for (Direction direction: directions) {
            intArray[i++] = direction.ordinal();
        }
        return intArray;
    }
}
