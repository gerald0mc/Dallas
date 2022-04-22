package me.gerald.dallas.features.module.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.utils.InventoryUtil;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;

public class MCP extends Module {
    public MCP() {
        super("MCP", Category.COMBAT, "Middle click pearl.");
    }

    public BooleanSetting switchBack = register(new BooleanSetting("SwitchBack", true));
    public BooleanSetting toggle = register(new BooleanSetting("Toggle", true));
    public BooleanSetting noPearlToggle = register(new BooleanSetting("NoPearlToggle", true, () -> toggle.getValue()));
    public BooleanSetting onThrowToggle = register(new BooleanSetting("OnThrowToggle", false, () -> toggle.getValue()));

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        if (Mouse.getEventButtonState()) {
            int mouseButton = Mouse.getEventButton();
            if (mouseButton == 2) {
                int pearlSlot = InventoryUtil.getItemHotbar(Items.ENDER_PEARL);
                int originalSlot;
                if (pearlSlot == -1) {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "MCP", "You have no ender pearls in your inventory.", true);
                    if(noPearlToggle.getValue())
                        toggle();
                    return;
                }
                originalSlot = mc.player.inventory.currentItem;
                InventoryUtil.switchToSlot(pearlSlot);
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                if(switchBack.getValue())
                    InventoryUtil.switchToSlot(originalSlot);
                if(onThrowToggle.getValue())
                    toggle();
            }
        }
    }
}