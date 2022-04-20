package me.gerald.dallas.managers.notification;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.features.module.client.GUI;
import me.gerald.dallas.features.module.hud.notification.Notifications;
import me.gerald.dallas.managers.notification.NotificationConstructor;
import me.gerald.dallas.utils.AnimationUtil;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class NotificationManager {
    public Queue<NotificationConstructor> notifications;
    public static List<NotificationConstructor> notificationHistory;

    public NotificationManager() {
        MinecraftForge.EVENT_BUS.register(this);
        notifications = new ConcurrentLinkedDeque<>();
        notificationHistory = new ArrayList<>();
    }

    public Queue<NotificationConstructor> getNotifications() {
        return notifications;
    }

    public void addNotification(String title, String message, long startTime) {
        notifications.add(new NotificationConstructor(title, message, startTime));
//        notificationHistory.add(new NotificationConstructor(title, message, startTime));
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
//        if(notificationHistory.size() > Yeehaw.INSTANCE.moduleManager.getModule(Notifications.class).historyAmount.getValue()) {
//            for(int i = notificationHistory.size(); i > Yeehaw.INSTANCE.moduleManager.getModule(Notifications.class).historyAmount.getValue(); i--)
//                notificationHistory.remove(i - 1);
//        }
        if(notifications.isEmpty()) return;
        for(NotificationConstructor notificationConstructor : getNotifications()) {
            if(System.currentTimeMillis() - notificationConstructor.getStartTime() >= (Yeehaw.INSTANCE.moduleManager.getModule(Notifications.class).timeToRemove.getValue() * 1000)) {
                notifications.remove(notificationConstructor);
            }
        }
    }
}
