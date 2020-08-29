package org.vaadin.erik.game.client;

import org.teavm.jso.JSBody;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.vaadin.erik.game.client.communication.json.PlayerJson;
import org.vaadin.erik.game.client.tilemap.TileMap;
import org.vaadin.erik.game.shared.Constants;
import org.vaadin.erik.game.shared.Tile;

public class GameCanvas {

    private final HTMLCanvasElement canvas;
    private final HTMLCanvasElement overlay;

    private final CanvasRenderingContext2D context;
    private final CanvasRenderingContext2D overlayContext;

    private final int width;
    private final int height;

    GameCanvas() {
        canvas = getCanvas();
        context = (CanvasRenderingContext2D) canvas.getContext("2d");

        overlay = getOverlay();
        overlayContext = (CanvasRenderingContext2D) overlay.getContext("2d");

        width = canvas.getWidth();
        height = canvas.getHeight();

        context.setFont("30px Arial");
        context.setTextBaseline("top");
    }

    @JSBody(script = "return window.canvas")
    private static native HTMLCanvasElement getCanvas();

    @JSBody(script = "return window.background")
    private static native HTMLCanvasElement getBackground();

    @JSBody(script = "return window.overlay")
    private static native HTMLCanvasElement getOverlay();

    @JSBody(params = "spriteCode", script = "return window.spriteCodeToColor(spriteCode)")
    private static native String spriteCodeToColor(int spriteCode);

    public void clear() {
        context.clearRect(0, 0, width, height);
    }

    public void drawTileMap(TileMap tileMap) {
        for (Tile[] rowTiles : tileMap.getTiles()) {
            for (Tile tile : rowTiles) {
                String color = spriteCodeToColor(tile.getSpriteCode());
                drawTile(tile.getPosition().getX(), tile.getPosition().getY(), tile.getWidth(), tile.getHeight(), color);
            }
        }
    }

    public void drawTile(double x, double y, double w, double h, String color) {
        context.setFillStyle(color);
        context.fillRect(x, y, w, h);
    }

    public void drawPlayer(PlayerJson playerJson) {
        context.setFillStyle(playerJson.getColor());
        context.fillRect(
                playerJson.getPosition().getX(),
                playerJson.getPosition().getY(),
                32, 32);
    }

    public void drawScore(PlayerJson playerJson, int index, boolean currentPlayer) {
        int height = Constants.GAME_HEIGHT / Constants.MAX_PLAYERS;
        int width = this.width - Constants.GAME_WIDTH;

        int offsetX = Constants.GAME_WIDTH;
        int offsetY = index * height;

        context.setFillStyle(playerJson.getColor());
        context.fillRect(offsetX, offsetY, width, height);

        if (currentPlayer) {
            context.setFillStyle("yellow");
            context.fillRect(offsetX, offsetY, 10, height);
        }

        context.setFillStyle("black");
        context.fillText(String.valueOf(playerJson.getPoints()), offsetX + 16, offsetY + 16);
    }
}
