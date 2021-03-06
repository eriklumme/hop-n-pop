package org.vaadin.erik.game.tiles;

import elemental.json.Json;
import elemental.json.JsonArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.erik.game.shared.Point;
import org.vaadin.erik.game.shared.*;
import org.vaadin.erik.game.shared.GameMath;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TileMap {

    private static final Logger logger = LogManager.getLogger(TileMap.class);

    private static final Tile[][] tiles;

    static {
        try {

            long time = System.nanoTime();

            InputStream inputStream = TileMap.class.getResourceAsStream("/META-INF/resources/tilemap.json");
            String data = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining());
            JsonArray rows = Json.parse(data).getArray("tiles");

            tiles = new Tile[rows.length()][rows.getArray(0).length()];

            for (int rowIndex = 0; rowIndex < rows.length(); rowIndex++) {
                JsonArray columns = rows.getArray(rowIndex);
                for(int columnIndex = 0; columnIndex < columns.length(); columnIndex++) {
                    JsonArray tileData = columns.getArray(columnIndex);
                    TileType tileType = TileType.values()[(int) tileData.getNumber(0)];
                    int spriteCode = (int) tileData.getNumber(1);
                    int x = columnIndex * Constants.BLOCK_SIZE;
                    int y = rowIndex * Constants.BLOCK_SIZE;
                    tiles[rowIndex][columnIndex] = new Tile(new Point(x, y), tileType, spriteCode);
                }
            }

            logger.info("Loaded tile map in {} ms", (System.nanoTime() - time)/1000000);
        } catch (Exception e) {
            logger.error("Error reading tile map", e);
            throw e;
        }
    }

    public static List<Tile> getOverlappingTiles(Player player) {
        List<Tile> overlappingTiles = new ArrayList<>(2);

        int minXIndex = (int) player.getPosition().getX() / Constants.BLOCK_SIZE;
        int minYIndex = (int) player.getPosition().getY() / Constants.BLOCK_SIZE;

        for (int y = Math.max(minYIndex, 0); y < Math.min(minYIndex + 2, tiles.length); y++) {
            for (int x = Math.max(minXIndex, 0); x < Math.min(tiles[0].length, minXIndex + 2); x++) {
                Tile tile = tiles[y][x];
                if (tile.getTileType() == TileType.GROUND && CollisionHandler.areIntersecting(player, tile)) {
                    overlappingTiles.add(tile);
                }
            }
        }

        return overlappingTiles;
    }

    public static Tile[][] getTiles() {
        return tiles;
    }
}
