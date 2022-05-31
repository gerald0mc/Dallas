package me.gerald.dallas.features.modules.render;

import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.ProjectionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RuneScapeChat extends Module {
    public NumberSetting timeToRemove = new NumberSetting("TimeToRemove", 2, 1, 5);
    public NumberSetting scale = new NumberSetting("Scale", 1.5f, 0, 5);

    public List<Player> playerList = new ArrayList<>();

    public RuneScapeChat() {
        super("RuneScapeChat", Category.RENDER, "Renders chat like how RuneScape does.");
        setBetaModule(true);
    }

    @SubscribeEvent
    public void onChatR(ClientChatReceivedEvent event) {
        String sender = StringUtils.substringBetween(event.getMessage().getFormattedText(), "<", ">");
        EntityPlayer entityPlayer = mc.world.getPlayerEntityByName(sender);
        if(entityPlayer == null) return;
        if(entityPlayer.equals(mc.player)) return;
        for(Player p : playerList) {
            if(p.sender.equals(entityPlayer)) {
                p.messageMap.put(event.getMessage().getFormattedText().replace("<" + sender + ">", ""), System.currentTimeMillis());
                return;
            }
        }
        Player player = new Player(entityPlayer);
        playerList.add(player);
        for(Player player1 : playerList) {
            if(player1.sender.equals(player.sender)) {
                player1.messageMap.put(event.getMessage().getFormattedText().replace("<" + sender + ">", ""), System.currentTimeMillis());
                return;
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if(nullCheck()) return;
        if(playerList.isEmpty()) return;
        for(Player player : playerList) {
            if(player.messageMap.isEmpty()) {
                playerList.remove(player);
                return;
            }
            int yOffset = 0;
            for(Map.Entry<String, Long> message : player.messageMap.entrySet()) {
                if(System.currentTimeMillis() - message.getValue() >= timeToRemove.getValue() * 1000) {
                    player.messageMap.remove(message.getKey());
                    continue;
                }
                double yAdd = player.sender.isSneaking() ? 1.75 : 2.25;
                double deltaX = MathHelper.clampedLerp(player.sender.lastTickPosX, player.sender.posX, event.getPartialTicks());
                double deltaY = MathHelper.clampedLerp(player.sender.lastTickPosY, player.sender.posY, event.getPartialTicks());
                double deltaZ = MathHelper.clampedLerp(player.sender.lastTickPosZ, player.sender.posZ, event.getPartialTicks());
                Vec3d projection = ProjectionUtil.toScaledScreenPos(new Vec3d(deltaX, deltaY, deltaZ).add(0, yAdd, 0));
                //Render stuff
                GlStateManager.pushMatrix();
                GlStateManager.translate(projection.x, projection.y, 0);
                GlStateManager.scale(scale.getValue(), scale.getValue(), 0);
                mc.fontRenderer.drawStringWithShadow(message.getKey(), -(mc.fontRenderer.getStringWidth(message.getKey()) / 2f), -(mc.fontRenderer.FONT_HEIGHT) - yOffset, new Color(255, 255, 0).getRGB());
                GlStateManager.popMatrix();
                yOffset += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1;
            }
        }
    }

    public static class Player {
        private final EntityPlayer sender;
        public ConcurrentHashMap<String, Long> messageMap;

        public Player(EntityPlayer sender) {
            this.sender = sender;
            messageMap = new ConcurrentHashMap<>();
        }
    }
}
