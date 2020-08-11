package org.vaadin.erik.game.client.tilemap;

import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSNumber;
import org.vaadin.erik.game.client.Logger;
import org.vaadin.erik.game.shared.Constants;
import org.vaadin.erik.game.shared.Point;
import org.vaadin.erik.game.shared.Tile;
import org.vaadin.erik.game.shared.TileType;

public class TileMap {

    private Tile[][] tiles;

    public TileMap() {
        JSArray<JSArray<JSNumber>> tileData = TileMapService.getTileMap();

        tiles = new Tile[tileData.getLength()][tileData.get(0).getLength()];

        for (int rowIndex = 0; rowIndex < tileData.getLength(); rowIndex++) {
            JSArray<JSNumber> columns = tileData.get(rowIndex);
            for(int columnIndex = 0; columnIndex < columns.getLength(); columnIndex++) {
                TileType tileType = TileType.values()[columns.get(columnIndex).intValue()];
                int x = columnIndex * Constants.BLOCK_SIZE;
                int y = rowIndex * Constants.BLOCK_SIZE;
                tiles[rowIndex][columnIndex] = new Tile(new Point(x, y), tileType);
            }
        }

        Logger.warn("Successfully created an array!");
    }

    public Tile[][] getTiles() {
        return tiles;
    }
}
