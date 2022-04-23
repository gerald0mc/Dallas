package me.gerald.dallas.managers;

import java.util.LinkedList;

public class FPSManager {
    private final LinkedList<Long> frames;
    private int fps;

    public FPSManager() {
        frames = new LinkedList<>();
    }

    public void update() {
        long time = System.nanoTime();
        frames.add(time);
        while (true) {
            long f = frames.getFirst();
            if (time - f <= 1000000000L)
                break;
            frames.remove();
        }
        fps = frames.size();
    }

    public int getFPS() {
        return fps;
    }

    public float getFrametime() {
        return 1.0f / fps;
    }
}
