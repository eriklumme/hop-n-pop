package org.vaadin.erik.game.ticker;

import java.util.Timer;
import java.util.TimerTask;

public class Ticker {

    private static final String THREAD_NAME = "GameTicker";
    private static final int TICKS_PER_SECOND = 50;
    private static final double ONE_MILLION = 1000000;

    private final Timer timer;
    private final TimerTask timerTask;

    public Ticker(TickerTask tickerTask) {
        this.timer = new Timer(THREAD_NAME);
        this.timerTask = new TimerTask() {

            private long lastRunTimeNanos = -1;

            @Override
            public void run() {
                if (lastRunTimeNanos < 0) {
                    lastRunTimeNanos = System.nanoTime();
                    return;
                }
                long timeNanos = System.nanoTime();
                double deltaMs = (timeNanos - lastRunTimeNanos) / ONE_MILLION;
                lastRunTimeNanos = timeNanos;

                tickerTask.tick(deltaMs);
            }
        };
    }

    public void start() {
        timer.scheduleAtFixedRate(timerTask, 0, 1000/TICKS_PER_SECOND);
    }

    public void stop() {
        timer.cancel();
    }
}
