package org.vaadin.erik.game.ai.pathing;

import org.vaadin.erik.game.ai.step.HorizontalStep;
import org.vaadin.erik.game.ai.step.Step;
import org.vaadin.erik.game.shared.Constants;
import org.vaadin.erik.game.shared.GameObject;
import org.vaadin.erik.game.shared.Tile;
import org.vaadin.erik.game.shared.TileType;
import org.vaadin.erik.game.tiles.TileMap;

import java.util.*;

public class PathingData {

    public static final Stack<Step> EMPTY_STACK = new Stack<>();

    private static PathingData pathingData;

    private final Tile[][] tiles = TileMap.getTiles();
    private final Map<Tile, NodeData> tileToNodeData = new HashMap<>();

    private PathingData() {
    }

    private Tile[][] getTiles() {
        return tiles;
    }

    private Map<Tile, NodeData> getTileToNodeData() {
        return tileToNodeData;
    }

    private void createNodeData() {
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                Tile tile = tiles[y][x];
                if (tile.getTileType() == TileType.AIR) {
                    if (y < tiles.length - 1 && tiles[y+1][x].getTileType() == TileType.GROUND) {
                        tileToNodeData.put(tile, new NodeData(x, y));
                    }
                }
            }
        }
    }

    private void addHorizontalSteps() {
        for (NodeData nodeData: tileToNodeData.values()) {
            NodeData neighbor;
            for (int i = -1; i <= 1; i += 2) {
                neighbor = getClosestNode(nodeData.getIndexX() + i, nodeData.getIndexY());
                if (neighbor != null) {
                    nodeData.getSteps().add(new HorizontalStep(neighbor));
                }
            }
        }
    }

    /**
     * Gets the {@link NodeData} closest to the given position, or below the given position.
     *
     * Returns null if none was found.
     */
    public NodeData getClosestNode(GameObject gameObject) {
        int x = (int) gameObject.getPosition().getX() / Constants.BLOCK_SIZE;
        int y = (int) gameObject.getPosition().getY() / Constants.BLOCK_SIZE;
        return getClosestNode(x, y);
    }

    private NodeData getClosestNode(int x, int y) {
        if (x < 0 || x >= tiles[0].length) {
            return null;
        }
        NodeData nodeData = null;
        while (y < tiles.length && nodeData == null) {
            Tile tile = tiles[y++][x];
            if (tile.getTileType() == TileType.GROUND) {
                // NodeData will only exists for air tiles right above ground tiles.
                // If we hit ground, we return, to avoid finding NodeData below the ground that we can't access.
                return null;
            }
            nodeData = tileToNodeData.get(tile);
        }
        return nodeData;
    }

    public Stack<Step> getSteps(NodeData from, NodeData to) {
        return new PathCalculator(from, to).calculate();
    }

    public static void calculate() {
        PathingData pathingData = new PathingData();
        pathingData.createNodeData();
        pathingData.addHorizontalSteps();
        PathingData.pathingData = pathingData;
    }

    public static PathingData getPathingData() {
        return pathingData;
    }

}
