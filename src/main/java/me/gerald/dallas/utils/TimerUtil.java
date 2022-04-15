package me.gerald.dallas.utils;

public class TimerUtil {
    long startTime;
    long delay;
    boolean paused;
    private long time = -1L;

    public TimerUtil() {
        startTime = System.currentTimeMillis();
        delay = 0L;
        paused = false;
    }

    public boolean passedMs(long ms) {
        return getMs(System.nanoTime() - time) >= ms;
    }

    public long getPassedTimeMs() {
        return getMs(System.nanoTime() - time);
    }

    public void reset() {
        time = System.nanoTime();
    }

    public long getMs(long time) {
        return time / 1000000L;
    }

    public long getTime() {
        return time;
    }
}
