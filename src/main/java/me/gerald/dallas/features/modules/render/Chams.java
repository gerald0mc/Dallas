package me.gerald.dallas.features.modules.render;

import com.mojang.authlib.GameProfile;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.event.events.DeathEvent;
import me.gerald.dallas.event.events.TotemPopEvent;
import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Chams extends Module {
    public BooleanSetting glow = new BooleanSetting("Glow", true, "Toggles players having a glow around them.");
    public BooleanSetting pops = new BooleanSetting("Pops", true, "Toggles pop chams.");
    public BooleanSetting deaths = new BooleanSetting("Deaths", true, "Toggles death chams.");
    public NumberSetting timeToRemove = new NumberSetting("TimeToRemove", 2, 1, 5, "How fast in seconds it takes to remove either cham.", () -> pops.getValue() || deaths.getValue());
    public ModeSetting renderMode = new ModeSetting("RenderMode", "Both", "What way your chams will be rendered.", () -> pops.getValue() || deaths.getValue(), "Both", "Fill", "Outline");
    public NumberSetting alpha = new NumberSetting("Alpha", 150, 0, 255, "The alpha of your chams.", () -> pops.getValue() || deaths.getValue());
    public NumberSetting lineWidth = new NumberSetting("Linewidth", 1, 0.1f, 5, "How thick the lines are.", () -> pops.getValue() || deaths.getValue());

    public ConcurrentHashMap<EntityOtherPlayerMP, Long> renderMap = new ConcurrentHashMap<>();

    public Chams() {
        super("Chams", Category.RENDER, "Chams for different things.");
        setBetaModule(true);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        if (nullCheck()) return;
        if (!glow.getValue()) return;
        Scoreboard scoreboard = mc.world.getScoreboard();
        if(!scoreboard.getTeamNames().contains("Friends")) {
            scoreboard.createTeam("Friends");
            scoreboard.getTeam("Friends").setPrefix("§b");
        }
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity.equals(mc.player)) continue;
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (Yeehaw.INSTANCE.friendManager.isFriend(player.getDisplayNameString())) {
                    scoreboard.addPlayerToTeam(player.getCachedUniqueIdString(), "Friends");
                }
                player.setGlowing(true);
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (nullCheck()) return;
        if (renderMap.isEmpty()) return;
        for (Map.Entry<EntityOtherPlayerMP, Long> entry : renderMap.entrySet()) {
            if (System.currentTimeMillis() - entry.getValue() >= timeToRemove.getValue() * 1000) {
                renderMap.remove(entry.getKey());
                continue;
            }
            GL11.glPushMatrix();
            GL11.glDepthRange(0.01, 1.0f);
            if (renderMode.getMode().equals("Both") || renderMode.getMode().equals("Outline")) {
                float r = ClickGUI.clientColor.getRed() / 255f;
                float g = ClickGUI.clientColor.getGreen() / 255f;
                float b = ClickGUI.clientColor.getBlue() / 255f;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glLineWidth(lineWidth.getValue());
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
                GL11.glColor4f(r, g, b, 1f);
                renderEntityStatic(entry.getKey(), event.getPartialTicks());
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(true);
                GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }
            if (renderMode.getMode().equals("Both") || renderMode.getMode().equals("Fill")) {
                float r = ClickGUI.clientColor.getRed() / 255f;
                float g = ClickGUI.clientColor.getGreen() / 255f;
                float b = ClickGUI.clientColor.getBlue() / 255f;
                GL11.glPushAttrib(GL11.GL_ALL_CLIENT_ATTRIB_BITS);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GL11.glLineWidth(1.5f);
                GL11.glColor4f(r, g, b, alpha.getValue() / 255f);
                renderEntityStatic(entry.getKey(), event.getPartialTicks());
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(true);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopAttrib();
            }
            GL11.glDepthRange(0.0, 1.0f);
            GL11.glPopMatrix();
        }
    }

    @SubscribeEvent
    public void onPop(TotemPopEvent event) {
        if (nullCheck()) return;
        if (event.getEntity() == mc.player) return;
        EntityOtherPlayerMP fakeEntity = new EntityOtherPlayerMP(mc.world, new GameProfile(event.getEntity().getUniqueID(), "Ballz"));
        fakeEntity.copyLocationAndAnglesFrom(event.getEntity());
        fakeEntity.rotationYaw = event.getEntity().rotationYaw;
        fakeEntity.prevRotationYaw = event.getEntity().rotationYaw;
        fakeEntity.rotationPitch = event.getEntity().rotationPitch;
        fakeEntity.prevRotationPitch = event.getEntity().rotationPitch;
        fakeEntity.cameraYaw = fakeEntity.rotationYaw;
        fakeEntity.cameraPitch = fakeEntity.rotationPitch;
        fakeEntity.setSneaking(event.getEntity().isSneaking());
        renderMap.put(fakeEntity, System.currentTimeMillis());
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if (nullCheck()) return;
        if (event.getEntity() == mc.player) return;
        EntityOtherPlayerMP fakeEntity = new EntityOtherPlayerMP(mc.world, new GameProfile(event.getEntity().getUniqueID(), "Ballz"));
        fakeEntity.copyLocationAndAnglesFrom(event.getEntity());
        fakeEntity.rotationYaw = event.getEntity().rotationYaw;
        fakeEntity.prevRotationYaw = event.getEntity().rotationYaw;
        fakeEntity.rotationPitch = event.getEntity().rotationPitch;
        fakeEntity.prevRotationPitch = event.getEntity().rotationPitch;
        fakeEntity.cameraYaw = fakeEntity.rotationYaw;
        fakeEntity.cameraPitch = fakeEntity.rotationPitch;
        fakeEntity.setSneaking(event.getEntity().isSneaking());
        renderMap.put(fakeEntity, System.currentTimeMillis());
    }

    public void renderEntityStatic(Entity entityIn, float partialTicks) {
        if (entityIn.ticksExisted == 0) {
            entityIn.lastTickPosX = entityIn.posX;
            entityIn.lastTickPosY = entityIn.posY;
            entityIn.lastTickPosZ = entityIn.posZ;
        }
        double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double) partialTicks;
        double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double) partialTicks;
        double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double) partialTicks;
        float f = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
        int i = entityIn.getBrightnessForRender();
        if (entityIn.isBurning())
            i = 15728880;
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
        mc.getRenderManager().renderEntity(entityIn, d0 - mc.getRenderManager().viewerPosX, d1 - mc.getRenderManager().viewerPosY, d2 - mc.getRenderManager().viewerPosZ, f, partialTicks, true);
    }
}
