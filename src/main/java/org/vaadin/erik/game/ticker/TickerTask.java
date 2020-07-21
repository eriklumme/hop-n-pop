package org.vaadin.erik.game.ticker;

public interface TickerTask {

    /**
     * Invoked when running the task
     *
     * @param delta Milliseconds elapsed since last execution
     */
    void tick(double delta);
}
