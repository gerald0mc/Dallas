package me.gerald.dallas.features.modules.combat;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.BlockUtil;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.spongepowered.asm.mixin.Shadow;

public class SandAura extends Module {
    public SandAura() {
        super("SandAura", Category.COMBAT, "Fuck you Woodz.");
    }

    public NumberSetting delay = new NumberSetting("Delay(MS)", 100, 5, 500);
    public NumberSetting range = new NumberSetting("Range", 4, 1, 6);

    public TimerUtil delayTimer = new TimerUtil();

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        EntityPlayer target = getTarget((int) range.getValue());
    }

    public EntityPlayer getTarget(int range) {
        for (EntityPlayer player : mc.world.playerEntities) {
            if(player.equals(mc.player)) continue;
            if(player.getDistance(mc.player) <= range) continue;
            if(!hasPlaceableSpot(player.getPosition())) continue;
            if(Yeehaw.INSTANCE.friendManager.isFriend(player.getDisplayNameString())) continue;
            return player;
        }
        return null;
    }

    public boolean hasPlaceableSpot(BlockPos playerPos) {
        BlockPos firstCheck = playerPos.up().up();
        BlockPos[] checkList = BlockUtil.getSurroundBlocks(firstCheck);
        for(BlockPos pos : checkList) {
            if(BlockUtil.isClickable(pos)) {
                return true;
            }
        }
        return false;
    }
}
