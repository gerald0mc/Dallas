package me.gerald.dallas.features.modules.client;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;

public class Messages extends Module {
    public Messages() {
        super("Messages", Category.CLIENT, "Stuff for client messages.");
    }

    public BooleanSetting generalMessages = new BooleanSetting("GeneralMessages", true, "Toggles general client messages.");
    public BooleanSetting infoMessages = new BooleanSetting("InfoMessages", true, "Toggles info client messages.");
    public BooleanSetting errorMessages = new BooleanSetting("ErrorMessages", true, "Toggles error client messages.");
    public BooleanSetting debugMessages = new BooleanSetting("DebugMessages", Yeehaw.INSTANCE.isDevJar, "Toggles debug client messages.");
}
