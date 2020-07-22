package org.vaadin.erik.game.shared;

public class GameMath {

    public static boolean areIntersecting(HasPosition a, HasPosition b) {
        return !(a.getX() + a.getWidth() < b.getX() || b.getX() + b.getWidth() < a.getX() ||
                a.getY() + a.getHeight() < b.getY() || b.getY() + b.getHeight() < a.getY());
    }

    /**
     * Returns the likely direction from which a collided into b.
     *
     * Assumes the shapes are squares.
     */
    public static Direction getCollisionDirection(HasPosition a, HasPosition b) {
        Point centerA = new Point(a.getX() + a.getWidth() / 2, a.getY() + a.getHeight() / 2);
        Point centerB = new Point(b.getX() + b.getWidth() / 2, b.getY() + b.getHeight() / 2);

        double distanceX =  centerA.getX() - centerB.getX();
        double distanceY = centerA.getY() - centerB.getY();

        if (Math.abs(distanceX) < Math.abs(distanceY)) {
            return distanceX > 0 ? Direction.RIGHT : Direction.LEFT;
        }
        return distanceY > 0 ? Direction.DOWN : Direction.UP;
    }
}
