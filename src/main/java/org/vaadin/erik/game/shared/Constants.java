package org.vaadin.erik.game.shared;

public class Constants {

    public static final int BLOCK_SIZE = 32;
    public static final int BLOCKS_HORIZONTAL = 24;
    public static final int BLOCKS_VERTICAL = 16;

    public static final int GAME_WIDTH = BLOCK_SIZE * BLOCKS_HORIZONTAL;
    public static final int GAME_HEIGHT = BLOCK_SIZE * BLOCKS_VERTICAL;

    public static final float EPSILON = 0.001f;

    public static final float MAX_VELOCITY = 500;
}
