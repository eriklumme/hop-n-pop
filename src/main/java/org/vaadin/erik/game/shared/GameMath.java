package org.vaadin.erik.game.shared;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.erik.game.server.Server;

public class GameMath {

    private static final Logger logger = LogManager.getLogger(GameMath.class);

    public static boolean areIntersecting(GameObject a, GameObject b) {
        return !(a.getX() + a.getWidth() < b.getX() || b.getX() + b.getWidth() < a.getX() ||
                a.getY() + a.getHeight() < b.getY() || b.getY() + b.getHeight() < a.getY());
    }

    /**
     * Returns details about the collision between two objects.
     *
     * If Collision is null, the GameObject passed as source must have moved since the last frame.
     *
     * Two objects can not collide if neither of them are moving, and this code assumes
     * that if only one of them was moving, it is the source one.
     */
    public static Collision getCollisionDetails(GameObject source, GameObject target, Collision previousCollision) {
        // The side of the object that first collided
        Direction sourceCollisionSide, targetCollisionSide;

        // If the objects were colliding in the previous frame as well, we'll just update it with
        // the side the objects are currently colliding on
        if (previousCollision != null) {
            sourceCollisionSide = getCurrentCollisionSide(source, target);
            targetCollisionSide = Direction.getOpposite(sourceCollisionSide);

            return new Collision(
                    source, target,
                    new Point(source.getX(), source.getY()),
                    new Point(target.getX(), target.getY()),
                    sourceCollisionSide, targetCollisionSide
            );
        }

        // Get the direction in which the source is moving on each axis
        Direction sourceXDir = Direction.getHorizontal(source.getPreviousPosition().getX(), source.getX());
        Direction sourceYDir = Direction.getVertical(source.getPreviousPosition().getY(), source.getY());

        if (sourceXDir == Direction.NONE && sourceYDir == Direction.NONE) {
            throw new IllegalArgumentException("If no previous collision is passed, the source object must have moved since the last frame");
        }

        // The amount of separation in each axis per second, if moved backwards
        double separationVX = Math.abs(source.getVelocity().getX() - target.getVelocity().getX());
        double separationVY = Math.abs(source.getVelocity().getY() - target.getVelocity().getY());

        // How many seconds we'd have to move back in time to get to a point where the objects have not yet collided
        double preCollisionDelta, preCollisionDeltaX = Double.MAX_VALUE, preCollisionDeltaY = Double.MAX_VALUE;
        // The point (top left corner) at which the collision happened
        Point sourceCollisionPoint, targetCollisionPoint;

        if (sourceXDir != Direction.NONE) {
            double overlap = sourceXDir == Direction.RIGHT ?
                    source.getX() + source.getWidth() - target.getX() :
                    target.getX() + target.getWidth() - source.getX();
            preCollisionDeltaX = overlap / separationVX;
        }
        if (sourceYDir != Direction.NONE) {
            double overlap = sourceYDir == Direction.DOWN ?
                    source.getY() + source.getHeight() - target.getY() :
                    target.getY() + target.getHeight() - source.getY();
            preCollisionDeltaY = overlap / separationVY;
        }
        preCollisionDelta = Math.min(preCollisionDeltaX, preCollisionDeltaY);

        // Calculate points right before the collision
        sourceCollisionPoint = new Point(
                source.getX() - preCollisionDelta * source.getVelocity().getX(),
                source.getY() - preCollisionDelta * source.getVelocity().getY()
        );
        targetCollisionPoint = new Point(
                target.getX() - preCollisionDelta * target.getVelocity().getX(),
                target.getY() - preCollisionDelta * target.getVelocity().getY()
        );

        // Calculate which side collided
        if (Math.abs(sourceCollisionPoint.getX() - targetCollisionPoint.getX()) >
                Math.abs(sourceCollisionPoint.getY() - targetCollisionPoint.getY())) {
            // Horizontal collision
            if (sourceCollisionPoint.getX() < targetCollisionPoint.getX()) {
                sourceCollisionSide = Direction.RIGHT;
                targetCollisionSide = Direction.LEFT;
            } else {
                sourceCollisionSide = Direction.LEFT;
                targetCollisionSide = Direction.RIGHT;
            }
        } else {
            // Vertical collision
            if (sourceCollisionPoint.getY() < targetCollisionPoint.getY()) {
                sourceCollisionSide = Direction.DOWN;
                targetCollisionSide = Direction.UP;
            } else {
                sourceCollisionSide = Direction.UP;
                targetCollisionSide = Direction.DOWN;
            }
        }

        if (Server.DEBUG_GAME_STATE) {
            logger.info(String.format("Object %s side %s collided with %s side %s",
                    source, target, sourceCollisionSide, targetCollisionSide));
            logger.info(String.format("Was at position %.1fx %.1fy", source.getX(), source.getY()));
            logger.info(String.format("Corrected position is %.1fx %.1fy", sourceCollisionPoint.getX(), sourceCollisionPoint.getY()));
        }

        return new Collision(
                source, target,
                sourceCollisionPoint, targetCollisionPoint,
                sourceCollisionSide, targetCollisionSide
        );
    }

    private static Direction getCurrentCollisionSide(GameObject a, GameObject b) {
        double leftOverlap = b.getX() + b.getWidth() - a.getX();
        double rightOverlap = a.getX() + a.getWidth() - b.getX();
        double upOverlap = b.getY() + b.getHeight() - a.getY();
        double downOverlap = a.getY() + a.getHeight() - b.getY();
        if (Math.min(leftOverlap, rightOverlap) < Math.min(upOverlap, downOverlap)) {
            return leftOverlap < rightOverlap ? Direction.LEFT : Direction.RIGHT;
        }
        return upOverlap < downOverlap ? Direction.UP : Direction.DOWN;
    }

    /**
     * Limits the _absolute value_ of the first variable to the limit given.
     */
    public static double signedLimited(double value, double limit) {
        double sign = Math.signum(value);
        double result = Math.min(Math.abs(value), limit);
        return sign * result;
    }
}
