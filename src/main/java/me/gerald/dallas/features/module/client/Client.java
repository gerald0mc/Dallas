package me.gerald.dallas.features.module.client;

import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ModeSetting;

public class Client extends Module {
    public Client() {
        super("Client", Category.CLIENT, "Client stuff.");
    }

    public BooleanSetting toggleMessage = new BooleanSetting("ToggleMessage", true);
    public ModeSetting messageMode = new ModeSetting("Mode", "Default", () -> toggleMessage.getValue(), "Default", "Simple");
}
