package me.gerald.dallas.features.module.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.managers.ConfigManager;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.FileUtil;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.ProjectionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Waypoints extends Module {
    public NumberSetting scale = register(new NumberSetting("Scale", 2.5f, 1, 10));
    public List<String> waypoints = new ArrayList<>();
    String filePath = "Dallas" + File.separator + "Client" + File.separator + "Waypoints.txt";

    public Waypoints() {
        super("Waypoints", Category.RENDER, "Allows you to add and render waypoints in game.");
    }

    @Override
    public void onEnable() {
        File waypointFile = new File(ConfigManager.clientPath, "Waypoints.txt");
        if (!waypointFile.exists()) {
            try {
                waypointFile.createNewFile();
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Waypoints", "Please go into your " + ChatFormatting.GREEN + ".minecraft" + ChatFormatting.RESET + " folder and navigate to " + ChatFormatting.AQUA + "Dallas" + ChatFormatting.GRAY + File.separator + ChatFormatting.AQUA + "Client" + ChatFormatting.GRAY + File.separator + ChatFormatting.AQUA + "Waypoints.txt" + ChatFormatting.RESET + " and your waypoints you can also use " + Yeehaw.INSTANCE.commandManager.PREFIX + "waypoint [add].", true);
            } catch (IOException ignored) {
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (nullCheck()) return;
        FileUtil.loadMessages(waypoints, filePath);
        for (String waypoint : waypoints) {
            String[] values = waypoint.split(" ");
            String name = values[1];
            int x = Integer.parseInt(values[3]);
            int y = Integer.parseInt(values[5]);
            int z = Integer.parseInt(values[7]);
            String dimension = values[9];
            String ip = values[11];
            if (Minecraft.getMinecraft().getCurrentServerData() != null) {
                ServerData data = Minecraft.getMinecraft().getCurrentServerData();
                if (!data.serverIP.equalsIgnoreCase(ip)) continue;
            }
            if (dimension.equalsIgnoreCase("nether") && mc.player.dimension != -1) continue;
            else if (dimension.equalsIgnoreCase("overworld") && mc.player.dimension != 0) continue;
            else if (dimension.equalsIgnoreCase("end") && mc.player.dimension != 1) continue;
            Vec3d projection = ProjectionUtil.toScaledScreenPos(new Vec3d(x, y, z));
            GlStateManager.pushMatrix();
            GlStateManager.translate(projection.x, projection.y, 0);
            GlStateManager.scale(scale.getValue(), scale.getValue(), 0);
            mc.fontRenderer.drawStringWithShadow(name + " [" + x + " " + y + " " + z + "]", -(mc.fontRenderer.getStringWidth(name + " [" + x + " " + y + " " + z + "]") / 2f), -(mc.fontRenderer.FONT_HEIGHT), -1);
            GlStateManager.popMatrix();
        }
    }
}
