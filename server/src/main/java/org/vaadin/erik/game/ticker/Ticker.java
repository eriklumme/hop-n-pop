package org.vaadin.erik.game.ticker;

import java.util.Timer;
import java.util.TimerTask;

public class Ticker {

    private static final String THREAD_NAME = "GameTicker";
    public static final int TICKS_PER_SECOND = 50;
    private static final double ONE_MILLION = 1000000;

    private final TickerTask tickerTask;

    private Timer timer;

    private double slowdownFactor = 1;
    private boolean fixedDelta;

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
                double deltaMs = getDeltaMS(timeNanos);
                lastRunTimeNanos = timeNanos;

                tickerTask.tick(deltaMs / slowdownFactor);
            }

            private double getDeltaMS(long currentTimeNanos) {
                if (fixedDelta) {
                    return getTimePerTick();
                }
                return (currentTimeNanos - lastRunTimeNanos) / ONE_MILLION;
            }
        };
    }

    public void start() {
        timer = new Timer(THREAD_NAME);
        TimerTask timerTask = createTimerTask();
        timer.scheduleAtFixedRate(timerTask, 0, getTimePerTick());
    }


    public void stop() {
        timer.cancel();
    }

    private long getTimePerTick() {
        return (long) (1000 / TICKS_PER_SECOND * slowdownFactor);
    }

    public void setSlowdownFactor(double slowdownFactor) {
        this.slowdownFactor = slowdownFactor;
        stop();
        start();
    }

    /**
     * When true, calculates updated using a fixed delta (time between ticks)
     * even if the actual time passed might differ.
     *
     * This is useful when debugging, as pausing the server for many seconds would otherwise lead to a very
     * large delta for the next tick, allowing objects to pass through walls etc.
     */
    public void setFixedDelta(boolean fixedDelta) {
        this.fixedDelta = fixedDelta;
    }
}
