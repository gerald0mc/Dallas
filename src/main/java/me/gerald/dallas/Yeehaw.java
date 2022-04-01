package me.gerald.dallas;

import me.gerald.dallas.event.EventManager;
import me.gerald.dallas.gui.ClickGUI;
import me.gerald.dallas.mod.ModuleManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
    public ClickGUI clickGUI;

    @Mod.Instance(MOD_ID)
    public static Yeehaw INSTANCE;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        moduleManager = new ModuleManager();
        eventManager = new EventManager();
        clickGUI = new ClickGUI();
    }
}
