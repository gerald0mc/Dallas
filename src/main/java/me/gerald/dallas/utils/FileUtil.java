package me.gerald.dallas.utils;

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class FileUtil {
    public static String getRandomMessage(List<String> messages) {
        Random rand = new Random();
        return messages.get(MathUtil.clamp(rand.nextInt(messages.size()), 0, messages.size() - 1));
    }

    public static String getRandomMessageWithDefault(List<String> messages, String defaultMessage, String filePath) {
        FileUtil.loadMessages(messages, filePath);
        Random rand = new Random();
        if (messages.size() == 0)
            return defaultMessage;
        if (messages.size() == 1)
            return messages.get(0);
        return messages.get(MathUtil.clamp(rand.nextInt(messages.size()), 0, messages.size() - 1));
    }

    public static void loadMessages(List<String> messageList, String filePath) {
        try {
            messageList.clear();
            Scanner s = new Scanner(new File(filePath));
            while (s.hasNextLine()) {
                messageList.add(s.nextLine());
            }
            s.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    public static void removeLineFromFile(File file, String lineToRemove) {
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
