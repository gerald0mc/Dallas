package me.gerald.dallas.managers.notification;

public class NotificationConstructor {
    private final String title;
    private final String message;
    private final long startTime;

    public NotificationConstructor(String title, String message, long startTime) {
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
