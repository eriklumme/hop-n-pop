package org.vaadin.erik.game.ai.pathing;

import org.vaadin.erik.game.ai.recording.RecordData;
import org.vaadin.erik.game.ai.recording.RecordingManager;
import org.vaadin.erik.game.ai.step.HorizontalStepFactory;
import org.vaadin.erik.game.ai.step.RecordedStepFactory;
import org.vaadin.erik.game.ai.step.Step;
import org.vaadin.erik.game.shared.*;
import org.vaadin.erik.game.tiles.TileMap;

import java.util.*;

public class PathingData {

    public enum SearchMode { EXACT, BELOW, BESIDE, ALL };

    private final Tile[][] tiles = TileMap.getTiles();
    private final Map<Tile, NodeData> tileToNodeData = new HashMap<>();

    PathingData() {
    }

    void initializeNodeData() {
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

    void addHorizontalSteps() {
        for (NodeData nodeData: tileToNodeData.values()) {
            NodeData neighbor;
            for (int i = -1; i <= 1; i += 2) {
                neighbor = getClosestNode(nodeData.getIndexX() + i, nodeData.getIndexY(), SearchMode.BELOW);
                if (neighbor != null) {
                    nodeData.getSteps().add(new HorizontalStepFactory(neighbor));
                }
            }
        }
    }

    void addJumpSteps() {
        for (NodeData nodeData: tileToNodeData.values()) {
            RecordData recordData = RecordingManager.getRecordedPathFrom(nodeData.getIndexX(), nodeData.getIndexY());
            if (recordData != null) {
                nodeData.getSteps().add(new RecordedStepFactory(recordData));
            }
        }
    }

    /**
     * Gets the {@link NodeData} closest to the given position, or below the given position if searchBelow is true.
     *
     * Returns null if none was found.
     */
    public NodeData getClosestNode(GameObject gameObject, SearchMode searchMode) {
        int x = (int) gameObject.getPosition().getX() / Constants.BLOCK_SIZE;
        int y = (int) gameObject.getPosition().getY() / Constants.BLOCK_SIZE;
        return getClosestNode(x, y, searchMode);
    }

    public NodeData getClosestNode(int x, int y, SearchMode searchMode) {
        boolean searchBelow = searchMode == SearchMode.ALL || searchMode == SearchMode.BELOW;
        NodeData nodeData = doGetClosestNode(x, y, searchBelow);

        if (nodeData == null && (searchMode == SearchMode.ALL || searchMode == SearchMode.BESIDE)) {
            nodeData = doGetClosestNode(x - 1, y, searchBelow);
            if (nodeData == null) {
                nodeData = doGetClosestNode(x + 1, y, searchBelow);
            }
        }
        return nodeData;
    }

    private NodeData doGetClosestNode(int x, int y, boolean searchBelow) {
        if (x < 0 || x >= tiles[0].length || y >= tiles.length) {
            return null;
        }
        NodeData nodeData;
        do {
            Tile tile = tiles[y++][x];
            if (tile.getTileType() == TileType.GROUND) {
                // NodeData will only exists for air tiles right above ground tiles.
                // If we hit ground, we return, to avoid finding NodeData below the ground that we can't access.
                return null;
            }
            nodeData = tileToNodeData.get(tile);
        }
        while (y < tiles.length && nodeData == null && searchBelow);
        return nodeData;
    }

    public Stack<Step> getSteps(NodeData from, NodeData to, Player player) {
        return new PathCalculator(from, to, player).calculate();
    }
}
