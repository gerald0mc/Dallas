package me.gerald.dallas.features.modules.client;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ColorSetting;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.setting.settings.NumberSetting;

public class GUI extends Module {
    public ModeSetting categoryAlignment = new ModeSetting("CategoryAlignment", "Middle", "Alignment of the category name.", "Left", "Middle", "Right");
    public BooleanSetting categoryOverhang = new BooleanSetting("CategoryOverhang", true, "Toggles category overhang.");
    public BooleanSetting moduleCount = new BooleanSetting("ModuleCount", true, "Toggles rendering of module count.");
    public BooleanSetting renderParent = new BooleanSetting("Render", false, "Render parent.");
    public ColorSetting color = new ColorSetting("Color", 127, 0, 255, 255, "Color of the GUI.", () -> renderParent.getValue());
    public BooleanSetting border = new BooleanSetting("Border", true, "Toggles the rendering of the border in GUI components.", () -> renderParent.getValue());
    public ColorSetting borderColor = new ColorSetting("BorderColor", 0, 0, 0, 255, "The color of the border.", () -> renderParent.getValue() && border.getValue());
    public BooleanSetting rainbow = new BooleanSetting("Rainbow", false, "Toggles the color of the GUI being rainbow.", () -> renderParent.getValue());
    public NumberSetting rainbowSpeed = new NumberSetting("RainbowSpeed", 3, 1, 10, "Speed of GUI rainbow.", () -> renderParent.getValue() && rainbow.getValue());

    public GUI() {
        super("GUI", Category.CLIENT, "Displays the client GUI.");
    }

    @Override
    public void onEnable() {
        Yeehaw.INSTANCE.consoleGUI.selectionBox.inConsoleGUI = false;
        Yeehaw.INSTANCE.consoleGUI.selectionBox.inClickGUI = true;
        Yeehaw.INSTANCE.clickGUI.selectionBox.inConsoleGUI = false;
        Yeehaw.INSTANCE.clickGUI.selectionBox.inClickGUI = true;
        mc.displayGuiScreen(Yeehaw.INSTANCE.clickGUI);
        toggle();
    }
}
