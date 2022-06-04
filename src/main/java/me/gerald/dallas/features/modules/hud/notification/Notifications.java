package me.gerald.dallas.features.modules.hud.notification;

import me.gerald.dallas.features.modules.hud.HUDModule;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;

public class Notifications extends HUDModule {
    public NumberSetting timeToRemove = new NumberSetting("TimeToRemove", 3, 1, 5, "How fast the notifications get removed.");
    public BooleanSetting title = new BooleanSetting("Title", false, "Toggles rendering of the notifications title.");
    public BooleanSetting clientMessages = new BooleanSetting("ClientMessages", true, "Toggle rendering of client messages.");

    public Notifications() {
        super(new NotificationsComponent(1, 151, 1, 1), "Notifications", Category.HUD, "Shows the players ping.");
    }
}
