package org.vaadin.erik.game.client.tilemap;

import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.html.HTMLImageElement;
import org.vaadin.erik.game.client.communication.json.PointJson;
import org.vaadin.erik.game.shared.Constants;
import org.vaadin.erik.game.shared.Point;

public class Animation {

    private static final double SECONDS_PER_FRAME = 1.0 / 24;

    private final HTMLImageElement image;
    private final PointJson position;
    private final int numFrames;

    private final int cols;

    private double timeElapsed;

    public Animation(HTMLImageElement image, PointJson position, int numFrames) {
        this.image = image;
        this.position = position;
        this.numFrames = numFrames;
        cols = image.getWidth() / Constants.BLOCK_SIZE;
    }

    public void drawFrame(CanvasRenderingContext2D context, double delta) {
        timeElapsed += delta;
        int frame = (int) (timeElapsed / SECONDS_PER_FRAME);

        if (hasCompleted()) {
            return;
        }

        int offsetX = frame % cols;
        int offsetY = frame / cols;

        context.drawImage(image,
                offsetX * Constants.BLOCK_SIZE,
                offsetY * Constants.BLOCK_SIZE,
                Constants.BLOCK_SIZE,
                Constants.BLOCK_SIZE,
                position.getX(),
                position.getY(),
                Constants.BLOCK_SIZE,
                Constants.BLOCK_SIZE);
    }

    public boolean hasCompleted() {
        return (int) (timeElapsed / SECONDS_PER_FRAME) >= numFrames;
    }
}
