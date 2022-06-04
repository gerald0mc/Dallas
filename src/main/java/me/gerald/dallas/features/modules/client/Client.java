package me.gerald.dallas.features.modules.client;

import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.setting.settings.NumberSetting;

public class Client extends Module {
    public BooleanSetting toggleMessage = new BooleanSetting("ToggleMessage", true, "Module Toggle notifications.");
    public ModeSetting messageMode = new ModeSetting("Mode", "Default", "How the module toggle messages will render.", () -> toggleMessage.getValue(), "Default", "Simple");
    public BooleanSetting customSplashText = new BooleanSetting("CustomSplashText", true, "Toggles the custom splash text.");

    public Client() {
        super("Client", Category.CLIENT, "Client stuff.");
    }
}
