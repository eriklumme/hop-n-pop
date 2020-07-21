package org.vaadin.erik.game;

import elemental.json.Json;
import elemental.json.JsonArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.erik.game.entity.Point;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class TileMap {

    private static final Logger logger = LogManager.getLogger(TileMap.class);

    private static final int[][] tiles;

    static {
        try {

            long time = System.nanoTime();

            InputStream inputStream = TileMap.class.getResourceAsStream("/tilemap.json");
            String data = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining());
            JsonArray rows = Json.parse(data).getArray("tiles");

            tiles = new int[rows.length()][rows.getArray(0).length()];

            for (int rowIndex = 0; rowIndex < rows.length(); rowIndex++) {
                JsonArray columns = rows.getArray(rowIndex);
                for(int columnIndex = 0; columnIndex < columns.length(); columnIndex++) {
                    tiles[rowIndex][columnIndex] = (int) columns.getNumber(columnIndex);
                }
            }

            logger.info("Loaded tile map in {} ms", (System.nanoTime() - time)/1000000);
        } catch (Exception e) {
            logger.error("Error reading tile map", e);
            throw e;
        }
    }

    /**
     * Calculates the point at which the line of points from-to intersects with the ground. Returns null if there
     * is no intersection.
     *
     * @param from  The point from which the movement is made.
     * @param to    The point to where the movement is made.
     * @return      The point where the movement intersects the ground, or null if no intersection is made
     */
    public static Point getGroundIntersectionPoint(Point from, Point to) {
        return null;
    }
}
