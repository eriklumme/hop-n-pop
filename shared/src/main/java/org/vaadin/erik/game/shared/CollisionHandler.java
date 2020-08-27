package org.vaadin.erik.game.shared;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.vaadin.erik.game.shared.data.Action;
import org.vaadin.erik.game.shared.data.Event;

import java.util.*;
import java.util.function.Function;

public class CollisionHandler {

    public static boolean areIntersecting(GameObject a, GameObject b) {
        return !(a.getPosition().getX() + a.getWidth() < b.getPosition().getX() ||
                b.getPosition().getX() + b.getWidth() < a.getPosition().getX() ||
                a.getPosition().getY() + a.getHeight() < b.getPosition().getY() ||
                b.getPosition().getY() + b.getHeight() < a.getPosition().getY());
    }

    private static void addPlayerCollisions(Collection<Player> players, Queue<Collision> orderedCollisions) {
        Set<Player> handledPlayers = new HashSet<>();
        for (Player player: players) {
            handledPlayers.add(player);

            for (Player otherPlayer: players) {
                if (handledPlayers.contains(otherPlayer)) {
                    continue;
                }

                if (areIntersecting(player, otherPlayer)) {
                    orderedCollisions.add(getCollision(player, otherPlayer));
                }
            }
        }
    }

    public static Collection<Event> handleCollisions(Collection<Player> players,
                                                     Function<Player, Collection<Tile>> tileCollisionProvider) {
        Set<Player> activePlayers = new HashSet<>(players);
        List<Event> events = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            Queue<Collision> orderedCollisions = new PriorityQueue<>(Comparator.comparing(Collision::getCollisionDelta).reversed());
            for (Player player : activePlayers) {
                for (Tile tile : tileCollisionProvider.apply(player)) {
                    orderedCollisions.add(getCollision(player, tile));
                }
            }
            addPlayerCollisions(activePlayers, orderedCollisions);

            Set<Player> handledPlayers = new HashSet<>();
            for (Collision collision : orderedCollisions) {
                Event event = handleCollision(collision, handledPlayers);

                if (event != null) {
                    events.add(event);
                    // If a player was killed, there is no need to consider that player for a potential second
                    // round of collision detection.
                    if (event.getAction() == Action.KILL) {
                        activePlayers.remove(event.getTarget());
                    }
                }
            }
        }

        return events;
    }

    private static Event handleCollision(Collision collision, Set<Player> handledPlayers) {
        if (isHandled(collision.getSource(), handledPlayers) ||
                isHandled(collision.getTarget(), handledPlayers)) {
            return null;
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
            // Vertical collision between players means one dies, the other's position is not affected
            if (collision.getTarget() instanceof Player) {
                Player source, target;
                if (collision.getSourceCollisionSide() == Direction.DOWN) {
                    source = (Player) collision.getSource();
                    target = (Player) collision.getTarget();
                } else {
                    source = (Player) collision.getTarget();
                    target = (Player) collision.getSource();
                }
                handledPlayers.add(target);
                return new Event(Action.KILL, source, target);
            }

            collision.getSource().setVelocity(new Vector2D(collision.getSource().getVelocity().getX(), 0));
            collision.getSource().setPosition(new Point(
                    collision.getSource().getPosition().getX(),
                    collision.getSourceCollisionPoint().getY()));

            if (collision.getSource() instanceof Player) {
                ((Player) collision.getSource()).setOnGround(collision.getSourceCollisionSide() == Direction.DOWN);
            }
        }

        addIfPlayer(collision.getSource(), handledPlayers);
        addIfPlayer(collision.getTarget(), handledPlayers);

        return null;
    }

    private static Collision getCollision(GameObject source, GameObject target) {
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
}
