package org.vaadin.erik.game.client.tilemap;

import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.html.HTMLImageElement;
import org.vaadin.erik.game.client.communication.json.TileJson;
import org.vaadin.erik.game.shared.Constants;
import org.vaadin.erik.game.shared.Tile;

public class SpriteManager {
    private static final String BASE_PATH = "img/";

    private static final HTMLImageElement VAADIN = createImage("vaadin.png");
    private static final HTMLImageElement TILES = createImage("tiles.png");

    private static HTMLImageElement createImage(String src) {
        HTMLImageElement image = (HTMLImageElement) Window.current().getDocument().createElement("img");
        image.setSrc(BASE_PATH + src);
        return image;
    }

    public static HTMLImageElement getVaadin() {
        return VAADIN;
    }

    public static void drawTile(CanvasRenderingContext2D context, Tile tile) {
        int offsetX = tile.getSpriteCode() & 0xf;
        int offsetY = (tile.getSpriteCode() >> 4) & 0xf;
        context.drawImage(TILES,
                offsetX * Constants.BLOCK_SIZE,
                offsetY * Constants.BLOCK_SIZE,
                Constants.BLOCK_SIZE,
                Constants.BLOCK_SIZE,
                tile.getPosition().getX(),
                tile.getPosition().getY(),
                Constants.BLOCK_SIZE,
                Constants.BLOCK_SIZE);
    }
}
