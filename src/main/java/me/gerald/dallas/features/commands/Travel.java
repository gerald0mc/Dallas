package me.gerald.dallas.features.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.modules.movement.SmartTravel;
import me.gerald.dallas.managers.command.Command;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.util.math.BlockPos;

public class Travel extends Command {
    public Travel() {
        super("Travel", "Command for travel module.", new String[]{"travel", "<x> <y>"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        switch (args.length) {
            case 1:
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Travel", "Please specify the " + ChatFormatting.AQUA + "X" + ChatFormatting.RESET + " and " + ChatFormatting.AQUA + "Y" + ChatFormatting.RESET + " of your target location.", MessageUtil.MessageType.CONSTANT);
                return;
            case 2:
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Travel", "Please specify the " + ChatFormatting.AQUA + "Y" + ChatFormatting.RESET + " of your target location.", MessageUtil.MessageType.CONSTANT);
                return;
            case 3:
                int targetX = Integer.parseInt(args[1]);
                int targetZ = Integer.parseInt(args[2]);
                Yeehaw.INSTANCE.moduleManager.getModule(SmartTravel.class).targetPos = new BlockPos(targetX, 1, targetZ);
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Travel", "Set SmartTravel location to " + ChatFormatting.AQUA + targetX + " " + targetZ + ChatFormatting.RESET + ".", MessageUtil.MessageType.CONSTANT);
        }
    }
}
