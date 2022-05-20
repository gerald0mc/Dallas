package me.gerald.dallas.features.modules.client;

import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.*;

public class Test extends Module {
    public Test() {
        super("Test12345678910", Category.CLIENT, "Test");
    }

    public BooleanSetting booleanSetting = new BooleanSetting("Boolean12345678910", true);
    public NumberSetting numberSetting = new NumberSetting("Number12345678910", 5, 1, 10);
    public ColorSetting colorSetting = new ColorSetting("Color12345678910", 255, 255, 255, 255);
    public ModeSetting modeSetting = new ModeSetting("Mode12345678910", "Test1", "Test1", "Test2");
    public StringSetting stringSetting = new StringSetting("String12345678910", "Cum");
}
