package org.vaadin.erik.game.shared;

public class Constants {

    public static final int BLOCK_SIZE = 32;
    public static final int BLOCKS_HORIZONTAL = 32;
    public static final int BLOCKS_VERTICAL = 24;

    public static final int MAX_PLAYERS = 8;

    public static final int GAME_WIDTH = BLOCK_SIZE * BLOCKS_HORIZONTAL;
    public static final int GAME_HEIGHT = BLOCK_SIZE * BLOCKS_VERTICAL;

    public static final float MAX_VELOCITY = 500;
    public static final float VERTICAL_MAX_VELOCITY = 1000;

    public static final int WIN_POINTS = 2;
    public static final int SECONDS_BETWEEN_ROUNDS = 10;
}
