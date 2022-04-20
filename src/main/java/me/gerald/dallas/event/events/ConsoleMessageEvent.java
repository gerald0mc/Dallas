package me.gerald.dallas.event.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class ConsoleMessageEvent extends Event {
    private final String message;

    public ConsoleMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
