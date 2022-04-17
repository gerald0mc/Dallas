package me.gerald.dallas.features.module.hud.notification;

import me.gerald.dallas.features.module.hud.HUDModule;
import me.gerald.dallas.setting.settings.NumberSetting;

public class Notifications extends HUDModule {
    public Notifications() {
        super(new NotificationsComponent(1, 151, 1, 1), "Notifications", Category.HUD, "Shows the players ping.");
    }

    public NumberSetting timeToRemove = register(new NumberSetting("TimeToRemove", 2, 1, 5));
}
