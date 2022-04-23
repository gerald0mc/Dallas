package me.gerald.dallas.features.module.hud.notification;

import me.gerald.dallas.features.module.hud.HUDModule;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;

public class Notifications extends HUDModule {
    public NumberSetting timeToRemove = register(new NumberSetting("TimeToRemove", 3, 1, 5));
    public BooleanSetting title = register(new BooleanSetting("Title", false));
    public BooleanSetting clientMessages = register(new BooleanSetting("ClientMessages", true));
    public BooleanSetting chatHistory = register(new BooleanSetting("NotificationHistory", true));
    public NumberSetting historyAmount = register(new NumberSetting("HistoryAmount", 15, 0, 40, () -> chatHistory.getValue()));
    public Notifications() {
        super(new NotificationsComponent(1, 151, 1, 1), "Notifications", Category.HUD, "Shows the players ping.");
    }
}
