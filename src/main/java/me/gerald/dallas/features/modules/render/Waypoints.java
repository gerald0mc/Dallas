package me.gerald.dallas.features.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.event.events.DeathEvent;
import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import me.gerald.dallas.managers.ConfigManager;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ColorSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.FileUtil;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.ProjectionUtil;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Waypoints extends Module {
    public BooleanSetting distance = new BooleanSetting("Distance", true, "Toggles rendering how far away you are from the waypoint.");
    public BooleanSetting deathWaypoint = new BooleanSetting("DeathWaypoint", true, "Toggles adding of a death waypoint whenever you die.");
    public BooleanSetting render = new BooleanSetting("Render", false, "Parent setting for all render settings.");
    public NumberSetting scale = new NumberSetting("Scale", 2.5f, 1, 10, "How much you are scaling your waypoint.", () -> render.getValue());
    public BooleanSetting background = new BooleanSetting("Background", true, "Toggles rendering of a background.", () -> render.getValue());
    public BooleanSetting border = new BooleanSetting("Border", false, "Toggles rendering of a border around the background.", () -> background.getValue() && background.isVisible());
    public BooleanSetting clientSync = new BooleanSetting("ClientSync", true, "Toggles the border color being client color or not.", () -> border.getValue() && border.isVisible());
    public ColorSetting borderColor = new ColorSetting("BorderColor", 0, 0, 0, 255, "Sets the border color.", () -> border.getValue() && !clientSync.getValue() && border.isVisible());
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
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Waypoints", "Please go into your " + ChatFormatting.GREEN + ".minecraft" + ChatFormatting.RESET + " folder and navigate to " + ChatFormatting.AQUA + "Dallas" + ChatFormatting.GRAY + File.separator + ChatFormatting.AQUA + "Client" + ChatFormatting.GRAY + File.separator + ChatFormatting.AQUA + "Waypoints.txt" + ChatFormatting.RESET + " and your waypoints you can also use " + Yeehaw.INSTANCE.commandManager.PREFIX + "waypoint [add].", MessageUtil.MessageType.CONSTANT);
            } catch (IOException ignored) {
            }
        }
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if (!deathWaypoint.getValue()) return;
        if (!event.getEntity().equals(mc.player)) return;
        File waypointFile = new File(ConfigManager.clientPath, "Waypoints.txt");
        try {
            String server = "Singleplayer";
            ServerData data = Minecraft.getMinecraft().getCurrentServerData();
            if (data != null) {
                server = data.serverIP;
            }
            FileWriter fileWriter = new FileWriter(waypointFile, true);
            fileWriter.write("Name Death X " + mc.player.getPosition().getX() + " Y " + mc.player.getPosition().getY() + " Z " + mc.player.getPosition().getZ() + " Dimension " + getDimension(mc.player.dimension) + " Server " + server + "\n");
            fileWriter.close();
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Waypoint", "Added new waypoint called " + ChatFormatting.AQUA + "Death" + ChatFormatting.RESET + " and it is at " + ChatFormatting.GRAY + "X: " + ChatFormatting.GREEN + mc.player.getPosition().getX() + ChatFormatting.GRAY + " Y: " + ChatFormatting.GREEN + mc.player.getPosition().getY() + ChatFormatting.GRAY + " Z: " + ChatFormatting.GREEN + mc.player.getPosition().getZ() + ChatFormatting.RESET + " and is in the " + ChatFormatting.GOLD + getDimension(mc.player.dimension), MessageUtil.MessageType.CONSTANT);
        } catch (IOException ignored) {
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (nullCheck()) return;
        FileUtil.loadMessages(waypoints, filePath);
        Color clientColor = ClickGUI.clientColor;
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
            if (background.getValue()) {
                Gui.drawRect((int) (-(mc.fontRenderer.getStringWidth(name + " [" + x + " " + y + " " + z + "] " + (distance.getValue() ? "[" + (int) mc.player.getDistance(x, y, z) + "]" : "")) / 2f) - 1), -(mc.fontRenderer.FONT_HEIGHT) - 1, (int) (Minecraft.getMinecraft().fontRenderer.getStringWidth(name + " [" + x + " " + y + " " + z + "] " + (distance.getValue() ? "[" + (int) mc.player.getDistance(x, y, z) + "]" : "")) / 2f) + 1, 0, new Color(12, 12, 12, 100).getRGB());
                if (border.getValue())
                    RenderUtil.renderBorder((int) (-(mc.fontRenderer.getStringWidth(name + " [" + x + " " + y + " " + z + "] " + (distance.getValue() ? "[" + (int) mc.player.getDistance(x, y, z) + "]" : "")) / 2f) - 2), -(mc.fontRenderer.FONT_HEIGHT) - 2, (int) (Minecraft.getMinecraft().fontRenderer.getStringWidth(name + " [" + x + " " + y + " " + z + "] " + (distance.getValue() ? "[" + (int) mc.player.getDistance(x, y, z) + "]" : "")) / 2f) + 2, 1, 1, clientSync.getValue() ? clientColor : borderColor.getColor());
            }
            mc.fontRenderer.drawStringWithShadow(name + ChatFormatting.GRAY + " [" + ChatFormatting.GREEN + x + " " + y + " " + z + ChatFormatting.GRAY + "] " + (distance.getValue() ? "[" + ChatFormatting.AQUA + (int) mc.player.getDistance(x, y, z) + ChatFormatting.GRAY + "]" : ""), -(mc.fontRenderer.getStringWidth(name + " [" + x + " " + y + " " + z + "] " + (distance.getValue() ? "[" + (int) mc.player.getDistance(x, y, z) + "]" : "")) / 2f), -(mc.fontRenderer.FONT_HEIGHT), -1);
            GlStateManager.popMatrix();
        }
    }

    public String getDimension(int dimension) {
        switch (dimension) {
            case -1:
                return "Nether";
            case 0:
                return "Overworld";
            case 1:
                return "End";
        }
        return "";
    }
}
