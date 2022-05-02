package me.gerald.dallas.utils;

import me.gerald.dallas.managers.ConfigManager;

import java.io.IOException;

public class ShutDownHook extends Thread {
    @Override
    public void run() {
        try {
            ConfigManager.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
