package me.gerald.dallas;

import me.gerald.dallas.managers.CommandManager;
import me.gerald.dallas.event.EventManager;
import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import me.gerald.dallas.managers.CPSManager;
import me.gerald.dallas.managers.ModuleManager;
import me.gerald.dallas.managers.ConfigManager;
import me.gerald.dallas.managers.RotationManager;
import me.gerald.dallas.managers.FriendManager;
import me.gerald.dallas.utils.ProjectionUtil;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(
        modid = Yeehaw.MOD_ID,
        name = Yeehaw.MOD_NAME,
        version = Yeehaw.VERSION,
        guiFactory = Yeehaw.GUI_HOOK
)
public class Yeehaw {
    //cum
    public static final String MOD_ID = "dallas";
    public static final String MOD_NAME = "Dallas";
    public static final String VERSION = "1.0";
    public static final String GUI_HOOK = "me.gerald.dallas.features.gui.api.ForgeConfigHook";

    public ModuleManager moduleManager;
    public EventManager eventManager;
    public ConfigManager configManager;
    public CommandManager commandManager;
    public FriendManager friendManager;
    public RotationManager rotationManager;
    public CPSManager cpsManager;
    public ClickGUI clickGUI;

    @Mod.Instance(MOD_ID)
    public static Yeehaw INSTANCE;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        configManager = new ConfigManager();
        friendManager = new FriendManager();
        eventManager = new EventManager();
        rotationManager = new RotationManager();
        cpsManager = new CPSManager();
        clickGUI = new ClickGUI();
        ConfigManager.load();
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        ProjectionUtil.updateMatrix();
    }
}
