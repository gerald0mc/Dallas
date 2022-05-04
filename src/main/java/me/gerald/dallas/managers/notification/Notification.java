package me.gerald.dallas.managers.notification;

public class Notification {
    private final String title;
    private final String message;
    private final long startTime;

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
