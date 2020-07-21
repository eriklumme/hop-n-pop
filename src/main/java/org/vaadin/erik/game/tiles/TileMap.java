package org.vaadin.erik.game.tiles;

import elemental.json.Json;
import elemental.json.JsonArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.erik.game.entity.Point;
import org.vaadin.erik.game.shared.Constants;
import org.vaadin.erik.game.shared.Player;
import org.vaadin.erik.game.shared.TileType;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class TileMap {

    private static final Logger logger = LogManager.getLogger(TileMap.class);

    private static final Tile[][] tiles;

    static {
        try {

            long time = System.nanoTime();

            InputStream inputStream = TileMap.class.getResourceAsStream("/tilemap.json");
            String data = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining());
            JsonArray rows = Json.parse(data).getArray("tiles");

            tiles = new Tile[rows.length()][rows.getArray(0).length()];

            for (int rowIndex = 0; rowIndex < rows.length(); rowIndex++) {
                JsonArray columns = rows.getArray(rowIndex);
                for(int columnIndex = 0; columnIndex < columns.length(); columnIndex++) {
                    TileType tileType = TileType.values()[(int) columns.getNumber(columnIndex)];
                    int x = columnIndex * Constants.BLOCK_SIZE;
                    int y = rowIndex * Constants.BLOCK_SIZE;
                    tiles[rowIndex][columnIndex] = new Tile(new Point(x, y), tileType);
                }
            }

            logger.info("Loaded tile map in {} ms", (System.nanoTime() - time)/1000000);
        } catch (Exception e) {
            logger.error("Error reading tile map", e);
            throw e;
        }
    }

    public static Tile[] getIntersectingTiles(Player player) {
        // We can't intersect more than four tiles at a time
        Tile[] intersectingTiles = new Tile[4];

        // TODO
        return intersectingTiles;
    }
}
