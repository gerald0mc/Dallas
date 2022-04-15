package me.gerald.dallas.utils;

public class Notification {
    private String title;
    private String message;
    private long startTime;

    public Notification(String title, String message, long startTime) {
        this.title = title;
        this.message = message;
        this.startTime = startTime;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }

    public long getStartTime() {
        return startTime;
    }
}
