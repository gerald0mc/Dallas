package me.gerald.dallas;

import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import me.gerald.dallas.features.gui.console.ConsoleGUI;
import me.gerald.dallas.managers.CPSManager;
import me.gerald.dallas.managers.ConfigManager;
import me.gerald.dallas.managers.EventManager;
import me.gerald.dallas.managers.FPSManager;
import me.gerald.dallas.managers.command.CommandManager;
import me.gerald.dallas.managers.friend.FriendManager;
import me.gerald.dallas.managers.module.ModuleManager;
import me.gerald.dallas.managers.notification.NotificationManager;
import me.gerald.dallas.utils.ProjectionUtil;
import me.gerald.dallas.utils.ShutDownHook;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

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

    @Mod.Instance(MOD_ID)
    public static Yeehaw INSTANCE;

    //major client managers
    public ModuleManager moduleManager;
    public EventManager eventManager;
    public ConfigManager configManager;
    public CommandManager commandManager;
    //secondary managers
    public FriendManager friendManager;
    public NotificationManager notificationManager;
    public CPSManager cpsManager;
    public FPSManager fpsManager;
    public ClickGUI clickGUI;
    public ConsoleGUI consoleGUI;

    public List<String> splashText;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        splashText = new ArrayList<>();
        splashText.add("Dallas on top!");
        splashText.add("gerald0mc owns everyone.");
        splashText.add("Fuck bitches get money.");
        splashText.add("Dallas owns you!");
        splashText.add("All these nn's on me.");
        splashText.add("Fuck niggers.");
        //major managers
        moduleManager = new ModuleManager();
        eventManager = new EventManager();
        configManager = new ConfigManager();
        commandManager = new CommandManager();
        //secondary managers
        friendManager = new FriendManager();
        notificationManager = new NotificationManager();
        cpsManager = new CPSManager();
        fpsManager = new FPSManager();
        clickGUI = new ClickGUI();
        consoleGUI = new ConsoleGUI();

        ConfigManager.load();
        Runtime.getRuntime().addShutdownHook(new ShutDownHook());
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        ProjectionUtil.updateMatrix();
        fpsManager.update();
    }
}
