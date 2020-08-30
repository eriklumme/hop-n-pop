package org.vaadin.erik.game.client;

import org.teavm.jso.JSBody;
import org.teavm.jso.canvas.CanvasGradient;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.vaadin.erik.game.client.communication.json.EventJson;
import org.vaadin.erik.game.client.communication.json.PlayerJson;
import org.vaadin.erik.game.client.tilemap.Animation;
import org.vaadin.erik.game.client.tilemap.SpriteManager;
import org.vaadin.erik.game.client.tilemap.TileMap;
import org.vaadin.erik.game.shared.Constants;
import org.vaadin.erik.game.shared.Tile;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GameCanvas {

    private final HTMLCanvasElement canvas;

    private final CanvasRenderingContext2D context;

    private final CanvasGradient scoreGradient;

    private final int width;
    private final int height;

    private List<Animation> animations = new LinkedList<>();

    GameCanvas() {
        canvas = getCanvas();
        context = (CanvasRenderingContext2D) canvas.getContext("2d");

        width = canvas.getWidth();
        height = canvas.getHeight();

        scoreGradient = context.createLinearGradient(0, 0, 0, height);
        scoreGradient.addColorStop(0, "#3e3e3e");
        scoreGradient.addColorStop(1, "#1f1f1f");

        context.setTextBaseline("middle");
        context.setTextAlign("center");
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
                SpriteManager.drawTile(context, tile);
            }
        }
    }

    public void drawPlayer(PlayerJson playerJson) {
        setPlayerNameFont();
        context.fillText(playerJson.getNickname(),
                playerJson.getPosition().getX() + 16,
                playerJson.getPosition().getY() - 16);

        int icon = playerJson.getIcon();
        int offsetX = icon % 4;
        int offsetY = icon / 4;

        context.drawImage(SpriteManager.getVaadin(),
                offsetX * Constants.BLOCK_SIZE,
                offsetY * Constants.BLOCK_SIZE,
                Constants.BLOCK_SIZE,
                Constants.BLOCK_SIZE,
                playerJson.getPosition().getX(),
                playerJson.getPosition().getY(),
                Constants.BLOCK_SIZE,
                Constants.BLOCK_SIZE);
    }

    public void drawScoreBackground() {
        context.setFillStyle(scoreGradient);
        context.fillRect(Constants.GAME_WIDTH, 0, width, height);
    }

    public void drawScore(PlayerJson playerJson, int index, boolean currentPlayer) {
        int height = Constants.GAME_HEIGHT / Constants.MAX_PLAYERS;
        int width = this.width - Constants.GAME_WIDTH;

        int offsetX = Constants.GAME_WIDTH;
        int offsetY = index * height;

        setFillStyleDark();
        context.fillRect(offsetX, offsetY, width, height);
        setFillStyleLight();
        context.fillRect(offsetX, offsetY, width, 1);

        if (currentPlayer) {
            setFillStyleLight();
            context.fillRect(offsetX, offsetY, 10, height);
        }

        setPlayerInfoFont();
        context.fillText(playerJson.getNickname(), offsetX + (width / 2.0), offsetY + 32);
        setPlayerScoreFont();
        context.fillText(String.valueOf(playerJson.getPoints()), offsetX + (width / 2.0), offsetY + 64);
    }

    public void drawEnding(PlayerJson winner, int countdown) {
        int rectWidth = 360;
        int rectHeight = 140;

        int offsetX = (Constants.GAME_WIDTH - rectWidth) / 2;
        int offsetY = (height - rectHeight) / 2;

        setFillStyleDark();
        context.fillRect(offsetX, offsetY, rectWidth, rectHeight);

        setEndGameFont();

        context.fillText("Winner is " + winner.getNickname(),
                offsetX + (rectWidth / 2.0),
                offsetY + (rectHeight / 4.0));

        context.fillText("Next round in " + countdown,
                offsetX + (rectWidth / 2.0),
                offsetY + (rectHeight / 4.0 * 3));
    }

    public void addEventAnimations(JSArray<EventJson> events) {
        for (int i = 0; i < events.getLength(); i++) {
            EventJson event = events.get(i);
            switch (event.getAction()) {
                case KILL:
                    animations.add(SpriteManager.createDeathAnimation(event.getTarget().getPosition()));
                    break;
                case SPAWN:
                    animations.add(SpriteManager.createSpawnAnimation(event.getSource().getPosition()));
                    break;
            }
        }
    }

    public void drawAnimations(double delta) {
        Iterator<Animation> iterator = animations.iterator();
        while (iterator.hasNext()) {
            Animation animation = iterator.next();
            animation.drawFrame(context, delta);
            if (animation.hasCompleted()) {
                iterator.remove();
            }
        }
    }

    private void setPlayerNameFont() {
        context.setFillStyle("white");
        context.setFont("18px Helvetica");
    }

    private void setPlayerInfoFont() {
        setFillStyleLight();
        context.setFont("18px Helvetica");
    }

    private void setPlayerScoreFont() {
        setFillStyleLight();
        context.setFont("24px Helvetica");
    }

    private void setEndGameFont() {
        setFillStyleLight();
        context.setFont("24px Helvetica");
    }

    private void setFillStyleLight() {
        context.setFillStyle("rgba(255, 255, 255, 0.8)");
    }

    private void setFillStyleDark() {
        context.setFillStyle("rgba(0, 0, 0, 0.5)");
    }
}
