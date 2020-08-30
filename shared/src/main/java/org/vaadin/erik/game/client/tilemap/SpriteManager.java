package org.vaadin.erik.game.client.tilemap;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLImageElement;

public class SpriteManager {
    private static final String BASE_PATH = "img/";

    private static final HTMLImageElement VAADIN = createImage("vaadin.png");

    private static HTMLImageElement createImage(String src) {
        HTMLImageElement image = (HTMLImageElement) Window.current().getDocument().createElement("img");
        image.setSrc(BASE_PATH + src);
        return image;
    }

    public static HTMLImageElement getVaadin() {
        return VAADIN;
    }
}
