package me.gerald.dallas.utils;

import com.mojang.realmsclient.gui.ChatFormatting;

public class ChangeConstructor {
    private final ChangeType type;
    private final String value;
    private final String secondaryValue;

    public ChangeConstructor(ChangeType type, String value) {
        this.type = type;
        this.value = value;
        secondaryValue = "";
    }

    public ChangeConstructor(ChangeType type, String value, String secondaryValue) {
        this.type = type;
        this.value = value;
        this.secondaryValue = secondaryValue;
    }

    public String getValue() {
        return value;
    }

    public ChangeType getType() {
        return type;
    }

    public String getSecondaryValue() {
        return secondaryValue;
    }

    public String getFullLog() {
        switch (type) {
            case MODULE:
                if (secondaryValue.equals(""))
                    return ChatFormatting.GRAY + "[" + ChatFormatting.GREEN + "+" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + "Added " + ChatFormatting.AQUA + value + ChatFormatting.RESET + "!";
                else
                    return ChatFormatting.GRAY + "[" + ChatFormatting.GREEN + "+" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + "Added " + ChatFormatting.AQUA + value + ChatFormatting.RESET + " in Category " + ChatFormatting.GOLD + secondaryValue + ChatFormatting.RESET + "!";
            case MODULE_ADD:
                return ChatFormatting.GRAY + "[" + ChatFormatting.GREEN + "+" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + "Added " + ChatFormatting.GOLD + secondaryValue + ChatFormatting.RESET + " to " + ChatFormatting.AQUA + value + ChatFormatting.RESET + "!";
            case COMMAND:
                return ChatFormatting.GRAY + "[" + ChatFormatting.GREEN + "+" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + "Added " + ChatFormatting.AQUA + value + ChatFormatting.RESET + " command!";
            case CHANGE:
                return ChatFormatting.GRAY + "[" + ChatFormatting.GOLD + "*" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + "Changed " + ChatFormatting.AQUA + value + ChatFormatting.RESET + " to " + ChatFormatting.GOLD + secondaryValue + ChatFormatting.RESET + "!";
            case DELETE:
                return ChatFormatting.GRAY + "[" + ChatFormatting.RED + "-" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + "Deleted " + ChatFormatting.RED + value + ChatFormatting.RESET + "!";
            case FIX:
                return ChatFormatting.GRAY + "[" + ChatFormatting.GOLD + "*" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + "Fixed " + ChatFormatting.AQUA + value + ChatFormatting.RESET + "!";
        }
        return "";
    }

    public enum ChangeType {MODULE, MODULE_ADD, COMMAND, CHANGE, DELETE, FIX}
}
