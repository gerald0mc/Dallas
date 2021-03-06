package me.gerald.dallas.managers.module;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.event.events.ModuleToggleEvent;
import me.gerald.dallas.setting.Setting;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Module {
    public static Minecraft mc = Minecraft.getMinecraft();
    private final List<Setting> settings;
    private final String metaData = "";
    private final String description;
    private String name;
    private Category category;
    private int keybind;
    private boolean isBetaModule = false;
    private boolean isEnabled = false;

    public Module(String name, Category category, String description) {
        this.name = name;
        this.category = category;
        this.description = description;
        keybind = name.equalsIgnoreCase("gui") ? Keyboard.KEY_U : Keyboard.KEY_NONE;
        settings = new ArrayList<>();
    }

    public static boolean nullCheck() {
        return mc == null || mc.world == null || mc.player == null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getKeybind() {
        return keybind;
    }

    public void setKeybind(int keybind) {
        this.keybind = keybind;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isBetaModule() {
        return isBetaModule;
    }

    public void setBetaModule(boolean value) {
        isBetaModule = value;
    }

    public void toggle() {
        isEnabled = !isEnabled;
        if (isEnabled)
            enable();
        else
            disable();
    }

    public void onEnable() {
    }

    public void enable() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.post(new ModuleToggleEvent.Enable(this));
        if (isBetaModule) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Warning", "As this modules is a Alpha module it might be prone to crashing and is not recommended for use.", MessageUtil.MessageType.CONSTANT);
        }
        onEnable();
    }

    public void onDisable() {
    }

    public void disable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        MinecraftForge.EVENT_BUS.post(new ModuleToggleEvent.Disable(this));
        onDisable();
    }

    public String getMetaData() {
        return metaData;
    }

    public void registerValues() {
        Arrays.stream(getClass().getDeclaredFields())
                .filter((f) -> Setting.class.isAssignableFrom(f.getType()))
                .forEach((f) -> {
                    boolean access = f.isAccessible();
                    if (!access) {
                        f.setAccessible(true);
                    }
                    try {
                        settings.add((Setting) f.get(this));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    f.setAccessible(access);
                });
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public Color getCategoryColor() {
        switch (category) {
            case COMBAT:
                return Color.RED;
            case MOVEMENT:
                return Color.BLUE;
            case RENDER:
                return Color.MAGENTA;
            case MISC:
                return Color.GREEN;
            case CLIENT:
                return Color.WHITE;
            case HUD:
                return Color.CYAN;
        }
        return Color.WHITE;
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
