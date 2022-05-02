package me.gerald.dallas.features.module.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.event.events.TotemPopEvent;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.features.module.hud.armor.Armor;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.InventoryUtil;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Offhand extends Module {
    public Offhand() {
        super("Offhand", Category.COMBAT, "You know what this does.");
    }

    public ModeSetting item = new ModeSetting("Item", "Totem", "Totem", "Crystals", "Gapples");
    public NumberSetting totemHealth = new NumberSetting("TotemHealth", 10, 1, 36, () -> !item.getMode().equalsIgnoreCase("totem"));
    public BooleanSetting instantSwitch = new BooleanSetting("InstantSwitch", true);
    public NumberSetting delay = new NumberSetting("Delay(MS)", 5, 1, 1000, () -> !instantSwitch.getValue());
//    public BooleanSetting absorptionAdd = new BooleanSetting("AbsorptionAdd", true);
    public BooleanSetting checks = new BooleanSetting("Checks", true);
    public BooleanSetting fallCheck = new BooleanSetting("FallCheck", true, () -> checks.getValue());
    public NumberSetting minDistance = new NumberSetting("MinDistance", 10, 1, 100, () -> checks.getValue() && fallCheck.getValue());
    public BooleanSetting liquidCheck = new BooleanSetting("LiquidCheck", true, () -> checks.getValue());
    public BooleanSetting water = new BooleanSetting("Water", true, () -> checks.getValue() && liquidCheck.getValue());
    public BooleanSetting lava = new BooleanSetting("Lava", true, () -> checks.getValue() && liquidCheck.getValue());
    public BooleanSetting elytraCheck = new BooleanSetting("ElytraCheck", true, () -> checks.getValue());
    public BooleanSetting flyingOnly = new BooleanSetting("FlyingOnly", true, () -> checks.getValue() && elytraCheck.getValue());
    public BooleanSetting message = new BooleanSetting("Message", false);

    public TimerUtil delayTimer = new TimerUtil();
    public boolean needsItem = false;

    @Override
    public String getMetaData() {
        return "[" + ChatFormatting.WHITE + InventoryUtil.getTotalAmountOfItem(Items.TOTEM_OF_UNDYING) + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        boolean forceTotem = false;
        if(liquidCheck.getValue() && liquidCheck.isVisible() && needsItem) {
            if(mc.player.isInLava() && lava.getValue() || mc.player.isInWater() && water.getValue()) {
                needsItem = false;
                forceTotem = true;
            }
        }
        if(elytraCheck.getValue() && elytraCheck.isVisible() && !forceTotem && needsItem) {
            for(ItemStack armor : mc.player.getArmorInventoryList()) {
                if(armor.getItem() instanceof ItemElytra) {
                    if(flyingOnly.getValue()) {
                        if(mc.player.isElytraFlying()) {
                            needsItem = false;
                            forceTotem = true;
                        }
                    } else {
                        needsItem = false;
                        forceTotem = true;
                    }
                    break;
                }
            }
        }
        if (fallCheck.getValue() && mc.player.fallDistance > minDistance.getValue() && !forceTotem && needsItem) {
            needsItem = false;
            forceTotem = true;
        }
        if (!needsItem) {
            if(!forceTotem) {
                if(mc.player.getHealth() >= totemHealth.getValue()) {
                    needsItem = true;
                    return;
                }
            }
            if(mc.player.getHeldItemOffhand().getItem().equals(Items.TOTEM_OF_UNDYING)) return;
            int totemSlot = InventoryUtil.getItemInventory(Items.TOTEM_OF_UNDYING, true);
            if(totemSlot != -1) {
                doThing(totemSlot, ChatFormatting.GRAY + "Moved a " + ChatFormatting.GREEN + "Totem of Undying" + ChatFormatting.GRAY + " to offhand slot.");
            }
        } else {
            if(mc.player.getHealth() <= totemHealth.getValue()) {
                needsItem = false;
                return;
            }
            if(mc.player.getHeldItemOffhand().getItem().equals(getItem())) return;
            int itemSlot = InventoryUtil.getItemInventory(getItem(), true);
            if(itemSlot != -1) {
                doThing(itemSlot, ChatFormatting.GRAY + "Moved " + ChatFormatting.GREEN + "<item>" + ChatFormatting.GRAY + " to offhand slot.");
            }else {
                int totemSlot = InventoryUtil.getItemInventory(Items.TOTEM_OF_UNDYING, true);
                if(totemSlot != -1)
                    doThing(totemSlot, ChatFormatting.GRAY + "Moved a " + ChatFormatting.GREEN + "Totem of Undying" + ChatFormatting.GRAY + " to offhand slot.");
            }
        }
    }

    @Override
    public void onDisable() {
        needsItem = false;
    }

    public Item getItem() {
        switch (item.getMode()) {
            case "Totem":
                return Items.TOTEM_OF_UNDYING;
            case "Crystals":
                return Items.END_CRYSTAL;
            case "Gapples":
                return Items.GOLDEN_APPLE;
        }
        return null;
    }

    @SubscribeEvent
    public void onPop(TotemPopEvent event) {
        if(event.getEntity() == mc.player) {
            if(!instantSwitch.getValue()) {
                delayTimer.reset();
            }
        }
    }

    public void doThing(int slot, String string) {
        if(instantSwitch.getValue()) {
            InventoryUtil.moveItemToSlot(45, slot);
            if(message.getValue())
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Offhand", string.replace("<item>", mc.player.getHeldItemOffhand().getDisplayName()), true);
        } else {
            if(delayTimer.passedMs((long) delay.getValue())) {
                InventoryUtil.moveItemToSlot(45, slot);
                if(message.getValue())
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Offhand", string.replace("<item>", mc.player.getHeldItemOffhand().getDisplayName()), true);
            }
        }
    }
}
