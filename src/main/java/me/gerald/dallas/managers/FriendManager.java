package me.gerald.dallas.managers;

import me.gerald.dallas.utils.Friend;

import java.io.*;
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
        if(!friendsFile.exists()) {
            try {
                friendsFile.createNewFile();
            }catch (IOException e) { }
        }
    }

    public void addFriend(String entity) {
        friends.add(new Friend(entity));
        try {
            FileWriter fileWriter = new FileWriter(friendsFile, true);
            fileWriter.write(entity + "\n");
            fileWriter.close();
        }catch (IOException ignored) {}
    }

    public void delFriend(String entity) {
        try {
            List<String> friendsList = Files.readAllLines(Paths.get(friendsFile.toURI()));
            for(String friend : friendsList) {
                if(friend.equalsIgnoreCase(entity)) {
                    removeLineFromFile(friendsFile, entity);
                    friends.remove(new Friend(entity));
                }
            }
        }catch (IOException e) { }
    }

    public boolean isFriend(String name) {
        for(Friend friend : friends) {
            if(friend.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void removeLineFromFile(File file, String lineToRemove) {
        try {
            if (!file.isFile()) {
                System.out.println("Parameter is not an existing file");
                return;
            }
            //Construct the new file that will later be renamed to the original filename.
            File tempFile = new File(file.getAbsolutePath() + ".tmp");
            BufferedReader br = new BufferedReader(new FileReader(file));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
            String line;
            //Read from the original file and write to the new
            //unless content matches data to be removed.
            while ((line = br.readLine()) != null) {
                if (!line.trim().equals(lineToRemove)) {
                    pw.println(line);
                    pw.flush();
                }
            }
            pw.close();
            br.close();
            //Delete the original file
            if (!file.delete()) {
                System.out.println("Could not delete file");
                return;
            }
            //Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(file))
                System.out.println("Could not rename file");
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
