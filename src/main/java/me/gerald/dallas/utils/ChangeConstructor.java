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
                if(secondaryValue.equals(""))
                    return ChatFormatting.GRAY + "[" + ChatFormatting.GREEN + "+" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + "Added " + ChatFormatting.AQUA + value + ChatFormatting.RESET + "!";
                else
                    return ChatFormatting.GRAY + "[" + ChatFormatting.GREEN + "+" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + "Added " + ChatFormatting.AQUA + secondaryValue + ChatFormatting.RESET + " to " + value;
            case COMMAND:
                return ChatFormatting.GRAY + "[" + ChatFormatting.GREEN + "+" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + "Added " + ChatFormatting.AQUA + value + ChatFormatting.RESET + " command!";
            case CHANGE:
                return ChatFormatting.GRAY + "[" + ChatFormatting.RESET + "*" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + "Changed " + ChatFormatting.AQUA + value + ChatFormatting.RESET + " to " + ChatFormatting.YELLOW + secondaryValue + ChatFormatting.RESET + "!";
            case DELETE:
                return ChatFormatting.GRAY + "[" + ChatFormatting.RED + "-" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + "Deleted " + ChatFormatting.AQUA + value + ChatFormatting.RESET + "!";
        }
        return "";
    }

    public enum ChangeType {MODULE, COMMAND, CHANGE, DELETE}
}
