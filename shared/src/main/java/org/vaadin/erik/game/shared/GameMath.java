package org.vaadin.erik.game.shared;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Function;

public class GameMath {

    private static final Logger logger = LogManager.getLogger(GameMath.class);

    public static boolean areIntersecting(GameObject a, GameObject b) {
        return !(a.getPosition().getX() + a.getWidth() < b.getPosition().getX() ||
                b.getPosition().getX() + b.getWidth() < a.getPosition().getX() ||
                a.getPosition().getY() + a.getHeight() < b.getPosition().getY() ||
                b.getPosition().getY() + b.getHeight() < a.getPosition().getY());
    }

    public static void handleCollisions(Collection<Player> players, Function<Player, Collection<Tile>> tileCollisionProvider) {
        for (int i = 0; i < 2; i++) {
            SortedSet<Collision> orderedCollisions = new TreeSet<>(Comparator.comparing(Collision::getCollisionDelta).reversed());
            for (Player player : players) {
                for (Tile tile : tileCollisionProvider.apply(player)) {
                    orderedCollisions.add(getCollision(player, tile));
                }
            }
            Set<Player> handledPlayers = new HashSet<>();
            for (Collision collision : orderedCollisions) {
                handleCollision(collision, handledPlayers, i);
            }
        }
    }

    public static Collision getCollision(GameObject source, GameObject target) {
        double overlapX, overlapY;
        if (source.getPreviousPosition().getX() + source.getWidth() <= target.getPreviousPosition().getX()) {
            overlapX = source.getPosition().getX() + source.getWidth() - target.getPosition().getX();
        } else if (source.getPreviousPosition().getX() >= target.getPreviousPosition().getX() + target.getWidth()) {
            overlapX = target.getPosition().getX() + target.getWidth() - source.getPosition().getX();
        } else {
            overlapX = Double.MAX_VALUE;
        }
        if (source.getPreviousPosition().getY() + source.getHeight() <= target.getPreviousPosition().getY()) {
            overlapY = source.getPosition().getY() + source.getHeight() - target.getPosition().getY();
        } else if (source.getPreviousPosition().getY() >= target.getPreviousPosition().getY() + target.getHeight()){
            overlapY = target.getPosition().getY() + target.getHeight() - source.getPosition().getY();
        } else {
            overlapY = Double.MAX_VALUE;
        }

        /*
        TODO: The overlap calculation is incorrect.
         */
        // The amount of separation in each axis per second, if moved backwards in time
        double separationVX = Math.abs(source.getVelocity().getX() - target.getVelocity().getX());
        double separationVY = Math.abs(source.getVelocity().getY() - target.getVelocity().getY());

        double collisionXTimeAgo = overlapX == 0 ? 0 : (separationVX > 0 ? overlapX / separationVX : Double.MAX_VALUE);
        double collisionYTimeAgo = overlapY == 0 ? 0 : (separationVY > 0 ? overlapY / separationVY : Double.MAX_VALUE);
        double collisionTimeAgo = Math.min(collisionYTimeAgo, collisionXTimeAgo);

        Point sourceCollisionPoint = getPointAgo(source, collisionTimeAgo),
                targetCollisionPoint = getPointAgo(target, collisionTimeAgo);

        // Calculate which side collided
        Direction sourceCollisionSide, targetCollisionSide;
        if (collisionYTimeAgo <= collisionXTimeAgo) {
            // Vertical collision
            if (sourceCollisionPoint.getY() < targetCollisionPoint.getY()) {
                sourceCollisionSide = Direction.DOWN;
                targetCollisionSide = Direction.UP;
            } else {
                sourceCollisionSide = Direction.UP;
                targetCollisionSide = Direction.DOWN;
            }
        } else {
            // Horizontal collision
            if (sourceCollisionPoint.getX() < targetCollisionPoint.getX()) {
                sourceCollisionSide = Direction.RIGHT;
                targetCollisionSide = Direction.LEFT;
            } else {
                sourceCollisionSide = Direction.LEFT;
                targetCollisionSide = Direction.RIGHT;
            }
        }

        return new Collision(
                source, target,
                sourceCollisionPoint, targetCollisionPoint,
                sourceCollisionSide, targetCollisionSide,
                collisionTimeAgo
        );
    }

    private static void handleCollision(Collision collision, Set<Player> handledPlayers, int i) {
        if (isHandled(collision.getSource(), handledPlayers) ||
                isHandled(collision.getTarget(), handledPlayers)) {
            return;
        }

        boolean staticCollision = collision.getSource().getPosition().equals(collision.getSourceCollisionPoint());

        if (collision.getSourceCollisionSide().isHorizontal()) {
            if (!staticCollision) {
                collision.getSource().setVelocity(new Vector2D(0, collision.getSource().getVelocity().getY()));
                collision.getSource().setPosition(new Point(
                        collision.getSourceCollisionPoint().getX(),
                        collision.getSource().getPosition().getY()));
                // TODO: Target velocity, previous position
            }
        } else {
            if (!staticCollision) {
                collision.getSource().setVelocity(new Vector2D(collision.getSource().getVelocity().getX(), 0));
                collision.getSource().setPosition(new Point(
                        collision.getSource().getPosition().getX(),
                        collision.getSourceCollisionPoint().getY()));
            }
            // TODO: Target velocity, previous position
        }

        if (collision.getSource() instanceof Player) {
            ((Player) collision.getSource()).setOnGround(collision.getSourceCollisionSide() == Direction.DOWN);
        }

        if (!staticCollision) {
            collision.getSource().setPreviousPosition(collision.getSourceCollisionPoint());

            addIfPlayer(collision.getSource(), handledPlayers);
            addIfPlayer(collision.getTarget(), handledPlayers);
        }
    }

    private static Point getPointAgo(GameObject gameObject, double timeAgo) {
        return new Point(
                gameObject.getPosition().getX() - timeAgo * gameObject.getVelocity().getX(),
                gameObject.getPosition().getY() - timeAgo * gameObject.getVelocity().getY()
        );
    }

    private static boolean isHandled(GameObject gameObject, Set<Player> handledPlayers) {
        return gameObject instanceof Player && handledPlayers.contains(gameObject);
    }

    private static void addIfPlayer(GameObject gameObject, Set<Player> handledPlayers) {
        if (gameObject instanceof Player) handledPlayers.add((Player) gameObject);
    }

    /**
     * Returns details about the collision between two objects.
     * <p>
     * If Collision is null, the GameObject passed as source must have moved since the last frame.
     * <p>
     * Two objects can not collide if neither of them are moving, and this code assumes
     * that if only one of them was moving, it is the source one.
     */
    public static Collision getCollisionDetails(GameObject source, GameObject target, Collision previousCollision) {
        // The side of the object that first collided
        Direction sourceCollisionSide, targetCollisionSide;

        // If the objects were colliding in the previous frame as well, we'll just update it with
        // the side the objects are currently colliding on
        if (previousCollision != null && source.getVelocity().getX() == 0 && source.getVelocity().getY() == 0) {
            sourceCollisionSide = getCurrentCollisionSide(source, target);
            targetCollisionSide = Direction.getOpposite(sourceCollisionSide);

            return new Collision(
                    source, target,
                    new Point(source.getPosition().getX(), source.getPosition().getY()),
                    new Point(target.getPosition().getX(), target.getPosition().getY()),
                    sourceCollisionSide, targetCollisionSide,
                    0);
        }

        // Get the direction in which the source is moving on each axis
        Direction sourceXDir = Direction.getHorizontal(source.getPreviousPosition().getX(), source.getPosition().getX());
        Direction sourceYDir = Direction.getVertical(source.getPreviousPosition().getY(), source.getPosition().getY());

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

        double sourceOverlapRight = source.getPosition().getX() + source.getWidth() - target.getPosition().getX();
        double sourceOverlapLeft = target.getPosition().getX() + target.getWidth() - source.getPosition().getX();
        if (sourceXDir != Direction.NONE) {
            double overlap = sourceXDir == Direction.RIGHT ? sourceOverlapRight : sourceOverlapLeft;
            preCollisionDeltaX = overlap / separationVX;
        } else if (sourceOverlapLeft < Constants.EPSILON || sourceOverlapRight < Constants.EPSILON) {
            preCollisionDeltaX = 0;
        }

        double sourceOverlapDown = source.getPosition().getY() + source.getHeight() - target.getPosition().getY();
        double sourceOverlapUp = target.getPosition().getY() + target.getHeight() - source.getPosition().getY();
        if (sourceYDir != Direction.NONE) {
            double overlap = sourceYDir == Direction.DOWN ? sourceOverlapDown : sourceOverlapUp;
            preCollisionDeltaY = overlap / separationVY;
        } else if (sourceOverlapUp < Constants.EPSILON || sourceOverlapDown < Constants.EPSILON) {
            preCollisionDeltaY = 0;
        }
        preCollisionDelta = Math.min(preCollisionDeltaX, preCollisionDeltaY);

        // Calculate points right before the collision
        sourceCollisionPoint = new Point(
                source.getPosition().getX() - preCollisionDelta * source.getVelocity().getX(),
                source.getPosition().getY() - preCollisionDelta * source.getVelocity().getY()
        );
        targetCollisionPoint = new Point(
                target.getPosition().getX() - preCollisionDelta * target.getVelocity().getX(),
                target.getPosition().getY() - preCollisionDelta * target.getVelocity().getY()
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

        if (GameEngine.DEBUG_GAME_STATE) {
            logger.info(String.format("Object %s side %s collided with %s side %s",
                    source, target, sourceCollisionSide, targetCollisionSide));
            logger.info(String.format("Was at position %.1fx %.1fy", source.getPosition().getX(), source.getPosition().getY()));
            logger.info(String.format("Corrected position is %.1fx %.1fy", sourceCollisionPoint.getX(), sourceCollisionPoint.getY()));
        }

        return new Collision(
                source, target,
                sourceCollisionPoint, targetCollisionPoint,
                sourceCollisionSide, targetCollisionSide,
                preCollisionDelta);
    }

    private static Direction getCurrentCollisionSide(GameObject a, GameObject b) {
        double leftOverlap = b.getPosition().getX() + b.getWidth() - a.getPosition().getX();
        double rightOverlap = a.getPosition().getX() + a.getWidth() - b.getPosition().getX();
        double upOverlap = b.getPosition().getY() + b.getHeight() - a.getPosition().getY();
        double downOverlap = a.getPosition().getY() + a.getHeight() - b.getPosition().getY();
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
