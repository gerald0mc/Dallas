package me.gerald.dallas.mod.mods.misc;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.mod.Module;
import me.gerald.dallas.utils.MessageUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;

public class MCF extends Module {
    public MCF() {
        super("MCF", Category.MISC, "Middle click friend.");
    }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        if(Mouse.isButtonDown(2)) {
            if(mc.objectMouseOver.typeOfHit.equals(RayTraceResult.Type.ENTITY)) {
                if(!(mc.objectMouseOver.entityHit instanceof EntityPlayer)) return;
                EntityPlayer player = (EntityPlayer) mc.objectMouseOver.entityHit;
                if(Yeehaw.INSTANCE.friendManager.isFriend(player.getDisplayNameString())) {
                    Yeehaw.INSTANCE.friendManager.delFriend(player.getDisplayNameString());
                    MessageUtils.sendMessage("Removed " + player.getDisplayNameString() + " from friends list.");
                }else {
                    Yeehaw.INSTANCE.friendManager.addFriend(player.getDisplayNameString());
                    MessageUtils.sendMessage("Added " + player.getDisplayNameString() + " to the friends list.");
                }
            }
        }
    }
}
