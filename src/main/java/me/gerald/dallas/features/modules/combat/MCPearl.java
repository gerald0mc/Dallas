package me.gerald.dallas.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.utils.InventoryUtil;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;

public class MCPearl extends Module {
    public BooleanSetting switchBack = new BooleanSetting("SwitchBack", true, "Toggles switching back to original slot.");
    public BooleanSetting toggle = new BooleanSetting("Toggle", true, "Toggle parent.");
    public BooleanSetting noPearlToggle = new BooleanSetting("NoPearlToggle", true, "Will toggle the module if no pearls on use.", () -> toggle.getValue());
    public BooleanSetting onThrowToggle = new BooleanSetting("OnThrowToggle", false, "Will toggle the module when a pearl is thrown.", () -> toggle.getValue());

    public MCPearl() {
        super("MCPearl", Category.COMBAT, "Middle click pearl.");
    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        if (Mouse.getEventButtonState()) {
            int mouseButton = Mouse.getEventButton();
            if (mouseButton == 2) {
                int pearlSlot = InventoryUtil.getItemHotbar(Items.ENDER_PEARL);
                int originalSlot;
                if (pearlSlot != -1) {
                    originalSlot = mc.player.inventory.currentItem;
                    InventoryUtil.switchToSlot(pearlSlot);
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    if (switchBack.getValue())
                        InventoryUtil.switchToSlot(originalSlot);
                    if (onThrowToggle.getValue())
                        toggle();
                } else {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "MCP", "You have no ender pearls in your hotbar.", MessageUtil.MessageType.CONSTANT);
                    if (noPearlToggle.getValue())
                        toggle();
                }
            }
        }
    }
}
