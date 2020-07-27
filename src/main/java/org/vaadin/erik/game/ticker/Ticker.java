package org.vaadin.erik.game.ticker;

import java.util.Timer;
import java.util.TimerTask;

public class Ticker {

    private static final String THREAD_NAME = "GameTicker";
    private static final int TICKS_PER_SECOND = 50;
    private static final double ONE_MILLION = 1000000;

    private final TickerTask tickerTask;

    private Timer timer;
    private double slowdownFactor = 1;

    public Ticker(TickerTask tickerTask) {
        this.tickerTask = tickerTask;
    }

    private TimerTask createTimerTask() {
        return new TimerTask() {

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

                tickerTask.tick(deltaMs / slowdownFactor);
            }
        };
    }

    public void start() {
        timer = new Timer(THREAD_NAME);
        TimerTask timerTask = createTimerTask();
        timer.scheduleAtFixedRate(timerTask, 0, (long) (1000 / TICKS_PER_SECOND * slowdownFactor));
    }

    public void stop() {
        timer.cancel();
    }

    public void setSlowdownFactor(double slowdownFactor) {
        this.slowdownFactor = slowdownFactor;
        stop();
        start();
    }
}
