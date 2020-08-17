package org.vaadin.erik.game.ai;

import org.vaadin.erik.game.shared.Tile;
import org.vaadin.erik.game.shared.TileType;
import org.vaadin.erik.game.tiles.TileMap;

import java.util.HashMap;
import java.util.Map;

public class PathingCalculator {

    private static Map<Integer, NodeData> pathingData;

    public static void calculate() {
        Tile[][] tiles = TileMap.getTiles();
        int counter = 0;
        pathingData = new HashMap<>();

        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                Tile tile = tiles[y][x];
                if (tile.getTileType() == TileType.AIR) {
                    if (y < tiles.length - 1 && tiles[y+1][x].getTileType() == TileType.GROUND) {
                        pathingData.put(counter++, new NodeData(x, y));
                    }
                }
            }
        }
    }

    public static Map<Integer, NodeData> getPathingData() {
        return pathingData;
    }

}
