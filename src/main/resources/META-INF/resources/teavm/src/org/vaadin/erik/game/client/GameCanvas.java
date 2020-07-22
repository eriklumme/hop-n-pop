package org.vaadin.erik.game.client;

import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.vaadin.erik.game.client.communication.json.PointJson;
import org.vaadin.erik.game.client.communication.json.TileJson;
import org.vaadin.erik.game.client.tilemap.TileMap;
import org.vaadin.erik.game.shared.Constants;
import org.vaadin.erik.game.shared.Direction;
import org.vaadin.erik.game.shared.Tile;
import org.vaadin.erik.game.shared.TileType;

public class GameCanvas {

    private static final String CANVAS_ID = "canvas";

    private final HTMLDocument document = Window.current().getDocument();

    private final HTMLCanvasElement canvas;
    private final CanvasRenderingContext2D context;

    private final int width;
    private final int height;

    GameCanvas() {
        canvas = (HTMLCanvasElement) document.getElementById(CANVAS_ID);
        context = (CanvasRenderingContext2D) canvas.getContext("2d");
        width = canvas.getWidth();
        height = canvas.getHeight();
    }

    public void clear() {
        context.clearRect(0, 0, width, height);
    }

    public void drawPlayer(int x, int y) {
        context.setFillStyle("black");
        context.fillRect(x, y, 32, 32);
    }

    public void drawTileMap(TileMap tileMap) {
        for (Tile[] rowTiles : tileMap.getTiles()) {
            for (Tile tile : rowTiles) {
                String color = tile.getTileType() == TileType.GROUND ? "green" : "blue";
                drawTile(tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight(), color);
            }
        }
    }

    public void drawCollision(TileJson tileJson, Direction direction) {
        PointJson point = tileJson.getTopLeftCorner();
        double x = point.getX();
        double y = point.getY();
        double w = Constants.BLOCK_SIZE;
        double h = Constants.BLOCK_SIZE;
        drawTile(x, y, w, h, "red");

        switch (direction) {
            case UP:
                drawTile(x, y, w, 5, "yellow");
                break;
            case DOWN:
                drawTile(x, y + h - 5, w, 5, "yellow");
                break;
            case LEFT:
                drawTile(x, y, 5, h, "yellow");
                break;
            case RIGHT:
                drawTile(x + w - 5, y, 5, h, "yellow");
                break;
        }
    }

    public void drawTile(double x, double y, double w, double h, String color) {
        context.setFillStyle(color);
        context.fillRect(x, y, w, h);
    }
}
