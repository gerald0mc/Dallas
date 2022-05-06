package me.gerald.dallas.features.modules.client;

import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.setting.settings.NumberSetting;

public class Client extends Module {
    public BooleanSetting toggleMessage = new BooleanSetting("ToggleMessage", true);
    public BooleanSetting toggleSave = new BooleanSetting("ToggleSave", true);
    public ModeSetting messageMode = new ModeSetting("Mode", "Default", () -> toggleMessage.getValue(), "Default", "Simple");

    public Client() {
        super("Client", Category.CLIENT, "Client stuff.");
    }
}
