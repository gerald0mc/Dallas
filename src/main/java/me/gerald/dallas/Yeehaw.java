package me.gerald.dallas;

import me.gerald.dallas.command.CommandManager;
import me.gerald.dallas.event.EventManager;
import me.gerald.dallas.gui.ClickGUI;
import me.gerald.dallas.mod.ModuleManager;
import me.gerald.dallas.utils.ConfigManager;
import me.gerald.dallas.utils.RotationManager;
import me.gerald.dallas.utils.friend.FriendManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(
        modid = Yeehaw.MOD_ID,
        name = Yeehaw.MOD_NAME,
        version = Yeehaw.VERSION
)
public class Yeehaw {

    public static final String MOD_ID = "dallas";
    public static final String MOD_NAME = "Dallas";
    public static final String VERSION = "1.0";

    public ModuleManager moduleManager;
    public EventManager eventManager;
    public ConfigManager configManager;
    public CommandManager commandManager;
    public FriendManager friendManager;
    public RotationManager rotationManager;
    public ClickGUI clickGUI;

    @Mod.Instance(MOD_ID)
    public static Yeehaw INSTANCE;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        moduleManager = new ModuleManager();
        eventManager = new EventManager();
        configManager = new ConfigManager();
        commandManager = new CommandManager();
        friendManager = new FriendManager();
        rotationManager = new RotationManager();
        clickGUI = new ClickGUI();
        ConfigManager.load();
    }
}
