package me.gerald.dallas.features.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import me.gerald.dallas.features.modules.misc.NameChanger;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ColorSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.BlockUtil;
import me.gerald.dallas.utils.ProjectionUtil;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.List;
import java.util.*;

import static com.mojang.realmsclient.gui.ChatFormatting.*;

public class Nametags extends Module {
    public NumberSetting scale = new NumberSetting("Scale", 2, 0, 5, "The scaling of the nametags.");
    public BooleanSetting backGround = new BooleanSetting("BackGround", true, "Toggles the rendering of the background.");
    public BooleanSetting border = new BooleanSetting("Border", true, "Toggles the rendering of the border.", () -> backGround.getValue());
    public BooleanSetting clientSync = new BooleanSetting("ClientSync", true, "Toggles the color of your border being the cliet color.", () -> border.getValue());
    public ColorSetting borderColor = new ColorSetting("BorderColor", 0, 0, 0, 255, "The color of your border.", () -> !clientSync.getValue());
    public BooleanSetting health = new BooleanSetting("Health", true, "Toggles the rendering of players health.");
    public BooleanSetting ping = new BooleanSetting("Ping", true, "Toggles the rendering of players ping.");
    public BooleanSetting totemPops = new BooleanSetting("TotemPops", true, "Toggles the rendering of a players pops.");
    public BooleanSetting allEntities = new BooleanSetting("AllEntities", true, "Toggles the rendering of nametags on all entities.");
    public BooleanSetting entityHealth = new BooleanSetting("EntityHealth", true, "Toggles the rendering of health on entities.", () -> allEntities.getValue());
    public BooleanSetting animals = new BooleanSetting("Animals", true, "Toggles the rendering of nametags on animals.", () -> allEntities.getValue());
    public BooleanSetting mobs = new BooleanSetting("Mobs", true, "Toggles the rendering of nametags on mobs.", () -> allEntities.getValue());
    public BooleanSetting villagers = new BooleanSetting("Villagers", true, "Toggles the rendering of nametags on villagers.", () -> allEntities.getValue());
    public BooleanSetting villagerTrades = new BooleanSetting("VillagerTrades", true, "Toggles the rendering of villager trades on villager nametags.", () -> villagers.getValue() && villagers.isVisible());

    public Nametags() {
        super("Nametags", Category.RENDER, "Renders info about players.");
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (nullCheck()) return;
        Color clientColor = ClickGUI.clientColor;
        for (Entity e : mc.world.loadedEntityList) {
            if (e == mc.player) continue;
            if (e instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) e;
                if (e instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) e;
                    double playerHealth = player.getHealth() + player.getAbsorptionAmount();
                    String str = "";
                    double yAdd = e.getEyeHeight() + .5f;
                    double deltaX = MathHelper.clampedLerp(player.lastTickPosX, player.posX, event.getPartialTicks());
                    double deltaY = MathHelper.clampedLerp(player.lastTickPosY, player.posY, event.getPartialTicks());
                    double deltaZ = MathHelper.clampedLerp(player.lastTickPosZ, player.posZ, event.getPartialTicks());
                    Vec3d projection = ProjectionUtil.toScaledScreenPos(new Vec3d(deltaX, deltaY, deltaZ).add(0, yAdd, 0));
                    //Render stuff
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(projection.x, projection.y, 0);
                    GlStateManager.scale(scale.getValue(), scale.getValue(), 0);
                    if (Yeehaw.INSTANCE.moduleManager.getModule(NameChanger.class).fakeClips.getValue() && Yeehaw.INSTANCE.moduleManager.getModule(NameChanger.class).isEnabled() && !Yeehaw.INSTANCE.friendManager.isFriend(player.getDisplayNameString()) && BlockUtil.findClosestPlayer() == player)
                        str += Yeehaw.INSTANCE.moduleManager.getModule(NameChanger.class).fakeName.getValue();
                    else
                        str += Yeehaw.INSTANCE.friendManager.isFriend(player.getDisplayNameString()) ? ChatFormatting.AQUA + player.getDisplayNameString() + ChatFormatting.RESET : player.getDisplayNameString();
                    if (ping.getValue())
                        str += " " + GRAY + MathHelper.ceil(Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime()) + "ms";
                    if (totemPops.getValue())
                        str += " Pops: " + Yeehaw.INSTANCE.eventManager.totemPopListener.getTotalPops(player);
                    if (health.getValue())
                        str += " " + getHealthColor(player) + MathHelper.ceil(playerHealth) + RESET;
                    if (backGround.getValue()) {
                        Gui.drawRect((int) -((mc.fontRenderer.getStringWidth(str) + 2) / 2f) - 1, -(mc.fontRenderer.FONT_HEIGHT + 2) - 1, ((mc.fontRenderer.getStringWidth(str) + 2) / (int) 2f) + 1, 2, new Color(12, 12, 12, 100).getRGB());
                        if (border.getValue())
                            RenderUtil.renderBorder((int) -((mc.fontRenderer.getStringWidth(str) + 2) / 2f) - 1, -(mc.fontRenderer.FONT_HEIGHT + 2) - 1, ((mc.fontRenderer.getStringWidth(str) + 2) / (int) 2f) + 1, 2, 1, clientSync.getValue() ? clientColor : borderColor.getColor());
                    }
                    mc.fontRenderer.drawStringWithShadow(str, -(mc.fontRenderer.getStringWidth(str) / 2f), -(mc.fontRenderer.FONT_HEIGHT), -1);
                    GlStateManager.popMatrix();
                }
                if (allEntities.getValue()) {
                    if (entity instanceof EntityAnimal && !animals.getValue()) continue;
                    else if ((entity instanceof EntityMob || entity instanceof EntitySlime) && !mobs.getValue())
                        continue;
                    else if (entity instanceof EntityVillager && !villagers.getValue()) continue;
                    List<String> lines = new ArrayList<>();
                    double health = entity.getHealth() + entity.getAbsorptionAmount();
                    lines.add(entity.getDisplayName().getFormattedText() + (entityHealth.getValue() ? " " + MathHelper.ceil(health) : ""));
                    if (entity instanceof EntityVillager) {
                        EntityVillager villager = (EntityVillager) entity;
                        MerchantRecipeList recipeList = villager.getRecipes(mc.player);
                        if (recipeList == null) return;
                        for (MerchantRecipe recipe : recipeList) {
                            String bookEnchant = null;
                            if (!villagerTrades.getValue()) break;
                            if (recipe.getItemToSell().getItem().equals(Items.ENCHANTED_BOOK)) {
                                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(recipe.getItemToSell());
                                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                                    bookEnchant = entry.getKey().getTranslatedName(entry.getValue());
                                    break;
                                }
                            }
                            lines.add("Buying" + GRAY + ": x" + recipe.getItemToBuy().getCount() + GREEN + " " + recipe.getItemToBuy().getItem().getItemStackDisplayName(recipe.getItemToBuy()) + (recipe.getSecondItemToBuy().getItem().equals(Items.AIR) ? "" : GRAY + " + x" + recipe.getSecondItemToBuy().getCount() + ChatFormatting.GREEN + " " + recipe.getSecondItemToBuy().getItem().getItemStackDisplayName(recipe.getSecondItemToBuy())) + ChatFormatting.RESET + " Selling" + GRAY + ": x" + recipe.getItemToSell().getCount() + ChatFormatting.AQUA + " " + (bookEnchant != null ? bookEnchant : recipe.getItemToSell().getItem().getItemStackDisplayName(recipe.getItemToSell())));
                        }
                    }
                    double yAdd = e.getEyeHeight() + .5f;
                    double deltaX = MathHelper.clampedLerp(entity.lastTickPosX, entity.posX, event.getPartialTicks());
                    double deltaY = MathHelper.clampedLerp(entity.lastTickPosY, entity.posY, event.getPartialTicks());
                    double deltaZ = MathHelper.clampedLerp(entity.lastTickPosZ, entity.posZ, event.getPartialTicks());
                    Vec3d projection = ProjectionUtil.toScaledScreenPos(new Vec3d(deltaX, deltaY, deltaZ).add(0, yAdd, 0));
                    //Render stuff
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(projection.x, projection.y, 0);
                    GlStateManager.scale(scale.getValue(), scale.getValue(), 0);
                    if (backGround.getValue()) {
                        Gui.drawRect((int) -((mc.fontRenderer.getStringWidth(getLongestWordString(lines)) + 2) / 2f) - 1, -(mc.fontRenderer.FONT_HEIGHT + 2) - 1, ((mc.fontRenderer.getStringWidth(getLongestWordString(lines)) + 2) / (int) 2f) + 1, 1 + (lines.size() != 1 ? lines.size() * (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1) - (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1) : 0), new Color(12, 12, 12, 100).getRGB());
                        if (border.getValue())
                            RenderUtil.renderBorder((int) -((mc.fontRenderer.getStringWidth(getLongestWordString(lines)) + 2) / 2f) - 1, -(mc.fontRenderer.FONT_HEIGHT + 2) - 1, ((mc.fontRenderer.getStringWidth(getLongestWordString(lines)) + 2) / (int) 2f) + 1, 1 + (lines.size() > 1 ? lines.size() * (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1) - (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1) : 0), 1, clientSync.getValue() ? clientColor : borderColor.getColor());
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
    }

    public ChatFormatting getHealthColor(EntityPlayer entity) {
        if (entity.getHealth() + entity.getAbsorptionAmount() > 14) {
            return ChatFormatting.GREEN;
        } else if (entity.getHealth() + entity.getAbsorptionAmount() > 6) {
            return ChatFormatting.YELLOW;
        } else {
            return ChatFormatting.RED;
        }
    }

    public String getLongestWordString(List<String> strings) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (String s : strings)
            hashMap.put(s, Minecraft.getMinecraft().fontRenderer.getStringWidth(s));
        return Collections.max(hashMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }
}
