package me.gerald.dallas.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class FileUtils {
    public static String getRandomMessage(List<String> messages) {
        Random rand = new Random();
        return messages.get(MathUtils.clamp(rand.nextInt(messages.size()), 0, messages.size() - 1));
    }

    public static String getRandomMessageWithDefault(List<String> messages, String defaultMessage, String filePath) {
        FileUtils.loadMessages(messages, filePath);
        Random rand = new Random();
        if(messages.size() == 0)
            return defaultMessage;
        if(messages.size() == 1)
            return messages.get(0);
        return messages.get(MathUtils.clamp(rand.nextInt(messages.size()), 0, messages.size() - 1));
    }

    public static void loadMessages(List<String> messageList, String filePath) {
        try {
            messageList.clear();
            Scanner s = new Scanner(new File(filePath));
            while (s.hasNextLine()){
                messageList.add(s.nextLine());
            }
            s.close();
        }catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }
}
