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
            Queue<Collision> orderedCollisions = new PriorityQueue<>(Comparator.comparing(Collision::getCollisionDelta).reversed());
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
        double overlapX = getOverlap(source, target, true),
                overlapY = getOverlap(source, target, false);

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

    private static double getOverlap(GameObject source, GameObject target, boolean horizontal) {
        double sourceSize = horizontal ? source.getWidth() : source.getHeight();
        double targetSize = horizontal ? target.getWidth() : target.getHeight();
        double overlap;
        if (source.getPreviousPosition().get(horizontal) + sourceSize <= target.getPreviousPosition().get(horizontal)) {
            overlap = source.getPosition().get(horizontal) + sourceSize - target.getPosition().get(horizontal);
        } else if (source.getPreviousPosition().get(horizontal) >= target.getPreviousPosition().get(horizontal) + targetSize) {
            overlap = target.getPosition().get(horizontal) + targetSize - source.getPosition().get(horizontal);
        } else {
            overlap = Double.MAX_VALUE;
        }
        return overlap;
    }

    private static void handleCollision(Collision collision, Set<Player> handledPlayers, int i) {
        if (isHandled(collision.getSource(), handledPlayers) ||
                isHandled(collision.getTarget(), handledPlayers)) {
            return;
        }

        if (collision.getSourceCollisionSide().isHorizontal()) {
            collision.getSource().setVelocity(new Vector2D(0, collision.getSource().getVelocity().getY()));
            collision.getSource().setPosition(new Point(
                    collision.getSourceCollisionPoint().getX(),
                    collision.getSource().getPosition().getY()));

            if (collision.getTarget() instanceof Player) {
                collision.getTarget().setVelocity(new Vector2D(0, collision.getTarget().getVelocity().getY()));
                collision.getTarget().setPosition(new Point(
                        collision.getTargetCollisionPoint().getX(),
                        collision.getTarget().getPosition().getY()));
            }
        } else {
            collision.getSource().setVelocity(new Vector2D(collision.getSource().getVelocity().getX(), 0));
            collision.getSource().setPosition(new Point(
                    collision.getSource().getPosition().getX(),
                    collision.getSourceCollisionPoint().getY()));

            if (collision.getTarget() instanceof Player) {
                collision.getTarget().setVelocity(new Vector2D(collision.getTarget().getVelocity().getX(), 0));
                collision.getTarget().setPosition(new Point(
                        collision.getTarget().getPosition().getX(),
                        collision.getTargetCollisionPoint().getY()));
            }

            if (collision.getSource() instanceof Player) {
                ((Player) collision.getSource()).setOnGround(collision.getSourceCollisionSide() == Direction.DOWN);
            }
        }

        addIfPlayer(collision.getSource(), handledPlayers);
        addIfPlayer(collision.getTarget(), handledPlayers);
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
