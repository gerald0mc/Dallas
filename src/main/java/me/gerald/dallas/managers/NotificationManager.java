package me.gerald.dallas.managers;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.module.hud.notification.Notifications;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.Notification;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class NotificationManager {
    public Queue<Notification> notifications;

    public NotificationManager() {
        MinecraftForge.EVENT_BUS.register(this);
        notifications = new ConcurrentLinkedDeque<>();
    }

    public Queue<Notification> getNotifications() {
        return notifications;
    }

    public void addNotification(String title, String message, long startTime) {
        notifications.add(new Notification(title, message, startTime));
        MessageUtil.sendMessage("Added Notification to list.");
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(notifications.isEmpty()) return;
        for(Notification notification : getNotifications()) {
            if(System.currentTimeMillis() - notification.getStartTime() >= (Yeehaw.INSTANCE.moduleManager.getModule(Notifications.class).timeToRemove.getValue() * 1000)) {
                notifications.remove(notification);
            }
        }
    }
}
