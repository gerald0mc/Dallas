package me.gerald.dallas.managers;

import me.gerald.dallas.utils.FileUtil;
import me.gerald.dallas.utils.Friend;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FriendManager {
    public List<Friend> friends;
    File friendsFile;

    public FriendManager() {
        friends = new ArrayList<>();
        friendsFile = new File(ConfigManager.clientPath, "Friends.txt");
        if (!friendsFile.exists()) {
            try {
                friendsFile.createNewFile();
            } catch (IOException e) {
            }
        }
    }

    public void addFriend(String entity) {
        friends.add(new Friend(entity));
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
                    friends.remove(new Friend(entity));
                }
            }
        } catch (IOException e) {
        }
    }

    public boolean isFriend(String name) {
        for (Friend friend : friends) {
            if (friend.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public List<Friend> getFriends() {
        return friends;
    }
}
