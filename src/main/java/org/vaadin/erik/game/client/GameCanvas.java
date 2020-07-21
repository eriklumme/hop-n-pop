package org.vaadin.erik.game.client;

import org.teavm.jso.browser.Window;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;

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
        context.fillRect(x, y, 20, 20);
    }
}
