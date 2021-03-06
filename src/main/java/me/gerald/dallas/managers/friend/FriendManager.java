package me.gerald.dallas.managers.friend;

import me.gerald.dallas.managers.ConfigManager;
import me.gerald.dallas.utils.FileUtil;
import net.minecraft.client.gui.GuiNewChat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendManager {
    String filePath = "Dallas" + File.separator + "Client" + File.separator + "Friends.txt";
    public List<String> friends = new ArrayList<>();
    File friendsFile;

    public FriendManager() {
        friendsFile = new File(ConfigManager.clientPath, "Friends.txt");
        if (!friendsFile.exists()) {
            try {
                friendsFile.createNewFile();
            } catch (IOException ignored) {
            }
        }
        FileUtil.loadMessages(friends, filePath);
    }

    public void addFriend(String entity) {
        friends.add(entity);
        try {
            FileWriter fileWriter = new FileWriter(friendsFile, true);
            fileWriter.write(entity + "\n");
            fileWriter.close();
        } catch (IOException ignored) {
        }
    }

    public void delFriend(String entity) {
        try {
            List<String> friendsList = Files.readAllLines(Paths.get(friendsFile.toURI()));
            for (String friend : friendsList) {
                if (friend.equalsIgnoreCase(entity)) {
                    FileUtil.removeLineFromFile(friendsFile, entity);
                }
            }
            friends.removeIf(string -> string.equalsIgnoreCase(entity));
        } catch (IOException ignored) {
        }
    }

    public boolean isFriend(String name) {
        for (String string : friends) {
            if (string.equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public List<String> getFriends() {
        return friends;
    }
}
