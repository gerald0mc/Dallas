package me.gerald.dallas.features.module;

import me.gerald.dallas.event.events.ModuleToggleEvent;
import me.gerald.dallas.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class Module {
    public static Minecraft mc = Minecraft.getMinecraft();
    private final List<Setting> settings;
    private String name;
    private Category category;
    private int keybind;
    private String description;
    private boolean isEnabled = false;

    public Module(String name, Category category, String description) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.keybind = name.equalsIgnoreCase("gui") ? Keyboard.KEY_U : Keyboard.KEY_NONE;
        this.settings = new ArrayList<>();
    }

    public static boolean nullCheck() {
        return mc == null || mc.world == null || mc.player == null;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getKeybind() {
        return this.keybind;
    }

    public void setKeybind(int keybind) {
        this.keybind = keybind;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public <T extends Setting> T register(T setting) {
        this.settings.add(setting);
        return setting;
    }

    public List<Setting> getSettings() {
        return this.settings;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public void toggle() {
        this.isEnabled = !this.isEnabled;
        if (this.isEnabled)
            this.enable();
        else
            this.disable();
    }

    public void onEnable() {
    }

    public void enable() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.post(new ModuleToggleEvent.Enable(this));
        this.onEnable();
    }

    public void onDisable() {
    }

    public void disable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        MinecraftForge.EVENT_BUS.post(new ModuleToggleEvent.Disable(this));
        this.onDisable();
    }

    public enum Category {
        COMBAT,
        MOVEMENT,
        RENDER,
        MISC,
        CLIENT,
        HUD
    }
}
