package me.gerald.dallas.features.module.client;

import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.setting.settings.NumberSetting;

public class Client extends Module {
    public BooleanSetting toggleMessage = new BooleanSetting("ToggleMessage", true);
    public BooleanSetting toggleSave = new BooleanSetting("ToggleSave", true);
    public ModeSetting messageMode = new ModeSetting("Mode", "Default", () -> toggleMessage.getValue(), "Default", "Simple");
    public BooleanSetting messageHistory = new BooleanSetting("MessageHistory", true);
    public NumberSetting historyAmount = new NumberSetting("HistoryAmount", 25, 5, 50, () -> messageHistory.getValue());
    public BooleanSetting background = new BooleanSetting("Background", true, () -> messageHistory.getValue());
    public Client() {
        super("Client", Category.CLIENT, "Client stuff.");
    }
}
