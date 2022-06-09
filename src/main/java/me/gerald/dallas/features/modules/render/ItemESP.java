package me.gerald.dallas.features.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ColorSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.ProjectionUtil;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;
import java.util.*;

// #TODO RARITY RENDER

public class ItemESP extends Module {
    public NumberSetting scale = new NumberSetting("Scale", 1, 0, 5, "The scaling of the item nametags.");
    public BooleanSetting count = new BooleanSetting("Count", true, "Toggles the rendering of the stack count.");
    public BooleanSetting backGround = new BooleanSetting("BackGround", true, "Toggles the rendering of a background.");
    public BooleanSetting border = new BooleanSetting("Border", true, "Toggles the rendering of a border with your background.", () -> backGround.getValue());
    public BooleanSetting clientSync = new BooleanSetting("ClientSync", true, "", () -> border.getValue() && border.isVisible());
    public ColorSetting borderColor = new ColorSetting("BorderColor", 0, 0, 0, 255, "The color of your background.", () -> border.getValue() && border.isVisible() && !clientSync.getValue());
//    public BooleanSetting rarityRencer = new BooleanSetting("RarityRender", true);
//    public ModeSetting renderType = new ModeSetting("RenderType", "Glow", "Glow", "Text(WIP)");

    public ItemESP() {
        super("ItemESP", Category.RENDER, "Renders a items name and quantity.");
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (nullCheck()) return;
        Color clientColor = ClickGUI.clientColor;
//        Scoreboard scoreboard = mc.world.getScoreboard();
//        if(!scoreboard.getTeamNames().contains("Common")) {
//            scoreboard.createTeam("Common");
//            scoreboard.getTeam("Common").setPrefix("§7");
//        }
//        if(!scoreboard.getTeamNames().contains("Uncommon")) {
//            scoreboard.createTeam("Uncommon");
//            scoreboard.getTeam("Uncommon").setPrefix("§a");
//        }
//        if(!scoreboard.getTeamNames().contains("Rare")) {
//            scoreboard.createTeam("Rare");
//            scoreboard.getTeam("Rare").setPrefix("§9");
//        }
//        if(!scoreboard.getTeamNames().contains("Exotic")) {
//            scoreboard.createTeam("Exotic");
//            scoreboard.getTeam("Exotic").setPrefix("§5");
//        }
//        if(!scoreboard.getTeamNames().contains("Legendary")) {
//            scoreboard.createTeam("Legendary");
//            scoreboard.getTeam("Legendary").setPrefix("§6");
//        }
        for (Entity e : mc.world.getLoadedEntityList()) {
            if (e instanceof EntityItem) {
                EntityItem entityItem = (EntityItem) e;
                if (entityItem.getItem().getDisplayName().length() > 50) continue;
                double deltaX = MathHelper.clampedLerp(entityItem.lastTickPosX, entityItem.posX, event.getPartialTicks());
                double deltaY = MathHelper.clampedLerp(entityItem.lastTickPosY, entityItem.posY, event.getPartialTicks());
                double deltaZ = MathHelper.clampedLerp(entityItem.lastTickPosZ, entityItem.posZ, event.getPartialTicks());
                Vec3d projection = ProjectionUtil.toScaledScreenPos(new Vec3d(deltaX, deltaY, deltaZ).add(0, 0.25, 0));
                //Render stuff
                GlStateManager.pushMatrix();
                GlStateManager.translate(projection.x, projection.y, 0);
                GlStateManager.scale(scale.getValue(), scale.getValue(), 0);
                List<String> lines = new ArrayList<>();
                lines.add(entityItem.getItem().getItem().getItemStackDisplayName(entityItem.getItem()) + (count.getValue() ? (entityItem.getItem().getCount() == 1 ? "" : ChatFormatting.GRAY + " [" + ChatFormatting.AQUA + "x" + entityItem.getItem().getCount() + ChatFormatting.GRAY + "]") : ""));
                if (entityItem.getItem().getItem().equals(Items.ENCHANTED_BOOK)) {
                    Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(entityItem.getItem());
                    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                        lines.add(entry.getKey().getTranslatedName(entry.getValue()));
                    }
                }
                if (backGround.getValue()) {
                    Gui.drawRect((int) -((mc.fontRenderer.getStringWidth(getLongestWordString(lines)) + 2) / 2f) - 2, -(mc.fontRenderer.FONT_HEIGHT + 2) - 1, ((mc.fontRenderer.getStringWidth(getLongestWordString(lines)) + 2) / (int) 2f) + 1, 2 + (lines.size() != 1 ? lines.size() * (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1) - (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1) : 0), new Color(12, 12, 12, 100).getRGB());
                    if (border.getValue())
                        RenderUtil.renderBorder((int) -((mc.fontRenderer.getStringWidth(getLongestWordString(lines)) + 2) / 2f) - 2, -(mc.fontRenderer.FONT_HEIGHT + 2) - 1, ((mc.fontRenderer.getStringWidth(getLongestWordString(lines)) + 2) / (int) 2f) + 1, 2 + (lines.size() != 1 ? lines.size() * (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1) - (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1) : 0), 1, clientSync.getValue() ? clientColor : borderColor.getColor());
                }
                int yOffset = 0;
                for (String line : lines) {
                    mc.fontRenderer.drawStringWithShadow(line, -(mc.fontRenderer.getStringWidth(getLongestWordString(lines)) / 2f), -(mc.fontRenderer.FONT_HEIGHT) + yOffset, -1);
                    yOffset += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1;
                }
                GlStateManager.popMatrix();
            }
        }
    }

    public String getLongestWordString(List<String> strings) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (String s : strings)
            hashMap.put(s, Minecraft.getMinecraft().fontRenderer.getStringWidth(s));
        return Collections.max(hashMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }

//    public void handleGlow(Entity realEntity, EntityItem entityItem, Scoreboard currentScoreboard) {
//        Item item = entityItem.getItem().getItem();
//        if(item instanceof ItemBlock) {
//            ItemBlock itemBlock = (ItemBlock) item;
//            Block block = itemBlock.getBlock();
//            if(block instanceof BlockOre) {
//                currentScoreboard.addPlayerToTeam(realEntity.getCachedUniqueIdString(), "Rare");
//                realEntity.setGlowing(true);
//            } else {
//                currentScoreboard.addPlayerToTeam(realEntity.getCachedUniqueIdString(), "Common");
//                realEntity.setGlowing(true);
//            }
//        }
//    }
}
