package me.gerald.dallas.managers;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.command.impl.Friend;
import me.gerald.dallas.utils.FileUtil;
import me.gerald.dallas.utils.FriendConstructor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FriendManager {
    public List<FriendConstructor> friends;
    File friendsFile;

    public FriendManager() {
        friends = new ArrayList<>();
        friendsFile = new File(ConfigManager.clientPath, "Friends.txt");
        if (!friendsFile.exists()) {
            try {
                friendsFile.createNewFile();
            } catch (IOException ignored) {}
        }
    }

    public void addFriend(String entity) {
        friends.add(new FriendConstructor(entity));
        try {
            FileWriter fileWriter = new FileWriter(friendsFile, true);
            fileWriter.write(entity + "\n");
            fileWriter.close();
        } catch (IOException ignored) {}
    }

    public void delFriend(String entity) {
        try {
            List<String> friendsList = Files.readAllLines(Paths.get(friendsFile.toURI()));
            for (String friend : friendsList) {
                if (friend.equalsIgnoreCase(entity)) {
                    FileUtil.removeLineFromFile(friendsFile, entity);
                }
                for(FriendConstructor friendConstructor : getFriends()) {
                    if(friendConstructor.getName().equalsIgnoreCase(entity)) {
                        friends.remove(friendConstructor);
                    }
                }
            }
        } catch (IOException ignored) {}
    }

    public boolean isFriend(String name) {
        for (FriendConstructor friendConstructor : friends) {
            if (friendConstructor.getName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public List<FriendConstructor> getFriends() {
        return friends;
    }
}
