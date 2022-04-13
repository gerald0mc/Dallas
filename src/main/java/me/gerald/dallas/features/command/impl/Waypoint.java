package me.gerald.dallas.features.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.command.Command;
import me.gerald.dallas.managers.ConfigManager;
import me.gerald.dallas.utils.FileUtil;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.text.TextComponentString;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Waypoint extends Command {
    public Waypoint() {
        super("Waypoint", "Allows you to modify waypoints.", new String[] {"waypoint", "[add/remove/list]"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        File waypointFile = new File(ConfigManager.clientPath, "Waypoints.txt");
        if(!waypointFile.exists()) {
            try {
                waypointFile.createNewFile();
                MessageUtil.sendMessage("Please go into your " + ChatFormatting.GREEN + ".minecraft" + ChatFormatting.RESET + " folder and navigate to " + ChatFormatting.AQUA + "Dallas" + ChatFormatting.GRAY + File.separator + ChatFormatting.AQUA + "Client" + ChatFormatting.GRAY + File.separator + ChatFormatting.AQUA + "Waypoints.txt" + ChatFormatting.RESET + " and your waypoints you can also use " + Yeehaw.INSTANCE.commandManager.PREFIX + "waypoint [add].");
            }catch (IOException ignored) {}
        }
        switch (args[1]) {
            case "add":
                String waypointName = "Unknown";
                int x = 1;
                int y = 69;
                int z = 1;
                String dimension = "Unknown";
                String server = "Singleplayer";
                switch (args.length) {
                    case 2:
                        MessageUtil.sendMessage("Please specify the name of your Waypoint.");
                        return;
                    case 3:
                        waypointName = args[2];
                        x = (int) Minecraft.getMinecraft().player.posX;
                        y = (int) Minecraft.getMinecraft().player.posY;
                        z = (int) Minecraft.getMinecraft().player.posZ;
                        switch (Minecraft.getMinecraft().player.dimension) {
                            case -1:
                                dimension = "Nether";
                                break;
                            case 0:
                                dimension = "Overworld";
                                break;
                            case 1:
                                dimension = "End";
                                break;
                        }
                        ServerData data = Minecraft.getMinecraft().getCurrentServerData();
                        if(data != null) {
                            server = data.serverIP;
                        }
                        break;
                    case 4:
                        MessageUtil.sendMessage("Please specify Y, Z, and Dimension of your custom waypoint.");
                        return;
                    case 5:
                        MessageUtil.sendMessage("Please specify Z and Dimension of your custom waypoint.");
                        return;
                    case 6:
                        MessageUtil.sendMessage("Please specify Dimension of your custom waypoint.");
                        return;
                    case 7:
                        waypointName = args[2];
                        x = Integer.parseInt(args[3]);
                        y = Integer.parseInt(args[4]);
                        z = Integer.parseInt(args[5]);
                        if(args[6].equalsIgnoreCase("nether")) {
                            dimension = "Nether";
                        }else if(args[6].equalsIgnoreCase("overworld")) {
                            dimension = "Overworld";
                        }else if(args[6].equalsIgnoreCase("end")) {
                            dimension = "End";
                        }else {
                            MessageUtil.sendMessage("Please make your dimension you are trying to set is one of three (Overworld, Nether, or End).");
                            return;
                        }
                        ServerData data2 = Minecraft.getMinecraft().getCurrentServerData();
                        if(data2 != null) {
                            server = data2.serverIP;
                        }
                        break;
                }
                try {
                    FileWriter fileWriter = new FileWriter(waypointFile, true);
                    fileWriter.write("Name " + waypointName + " X " + x + " Y " + y + " Z " + z + " Dimension " + dimension + " Server " + server + "\n");
                    fileWriter.close();
                    MessageUtil.sendMessage("Added new waypoint called " + waypointName + " and is it X: " + x + " Y: " + y + " Z: " + z + " and is in " + dimension);
                }catch (IOException ignored) {}
                break;
            case "remove":
                if(args.length == 2) {
                    MessageUtil.sendMessage("Please specify the name of the Waypoint you are trying to remove.");
                    return;
                }
                String target = args[2];
                try {
                    List<String> waypointList = Files.readAllLines(Paths.get(waypointFile.toURI()));
                    for(String waypoint : waypointList) {
                        String[] values = waypoint.split(" ");
                        if(values[1].equalsIgnoreCase(target)) {
                            FileUtil.removeLineFromFile(waypointFile, waypoint);
                            MessageUtil.sendMessage("Removed " + target + " from Waypoints list.");
                        }
                    }
                }catch (IOException ignored) {}
                break;
            case "list":
                try {
                    List<String> waypoints = Files.readAllLines(Paths.get(waypointFile.toURI()));
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString(ChatFormatting.BLUE + "Da" + ChatFormatting.WHITE + "ll" + ChatFormatting.RED + "as " + ChatFormatting.WHITE + "Waypoint List"));
                    for(String waypoint : waypoints) {
                        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(waypoint));
                    }
                }catch (IOException ignored) {}
        }
    }
}
