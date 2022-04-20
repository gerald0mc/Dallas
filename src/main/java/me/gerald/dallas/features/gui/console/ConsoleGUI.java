package me.gerald.dallas.features.gui.console;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.event.events.ConsoleMessageEvent;
import me.gerald.dallas.features.command.Command;
import me.gerald.dallas.features.gui.comps.TextComponent;
import me.gerald.dallas.utils.ChangeConstructor;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ConsoleGUI extends GuiScreen {

    int width = 300;
    int height = 250;

    public List<String> messageHistory = new ArrayList<>();
    public String entryString = "";

    public ConsoleGUI() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(messageHistory.size() >= 25)
            messageHistory.remove(0);
        this.width = getLongestWord(messageHistory) > 300 ? getLongestWord(messageHistory) + 3 : 300;
        Gui.drawRect(25, 25, width + 25, height, new Color(0, 0, 0, 175).getRGB());
        //full box
        //top lines
        Gui.drawRect(24, 25, 25 + width, 26, new Color(0, 0, 0, 255).getRGB());
        //left line
        Gui.drawRect(24, 25, 25, height, new Color(0, 0, 0, 255).getRGB());
        //right line
        Gui.drawRect(24 + width, 25, 25 + width, height, new Color(0, 0, 0, 255).getRGB());
        //bottom line
        Gui.drawRect(24, height, 25 + width, height + 1, new Color(0, 0, 0, 255).getRGB());
        int yOffset = 0;
        for(String s : messageHistory) {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(s, 27, 27 + yOffset, -1);
            yOffset += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        }
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(entryString + "_", 27, height - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {}

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {}

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        switch (keyCode) {
            case Keyboard.KEY_BACK:
                entryString = removeLastLetter(entryString);
                break;
            case Keyboard.KEY_RETURN:
                if(entryString.equalsIgnoreCase("clear")) {
                    messageHistory.clear();
                    messageHistory.add("Cleared console.");
                    return;
                }
                if (entryString.length() > 0)
                    messageHistory.add(entryString);
                String[] args = entryString.split(" ");
                for (Command command : Yeehaw.INSTANCE.commandManager.getCommands()) {
                    if (args[0].equalsIgnoreCase(command.getName())) {
                        command.onCommand(args);
                    }
                }
                entryString = "";
                break;
            case Keyboard.KEY_V:
                if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                    try {
                        entryString += Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    } catch (UnsupportedFlavorException ignored) {}
                }
                break;
            case Keyboard.KEY_C:
                if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                    if (entryString.length() == 0) {
                        MessageUtil.sendMessage(ChatFormatting.BOLD + "Console","Nothing to copy.", true);
                        return;
                    }
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(entryString), null);
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Console","Copied text in string box to clipboard.", true);
                }
                break;
        }
        if (ChatAllowedCharacters.isAllowedCharacter(typedChar))
            entryString += typedChar;
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @SubscribeEvent
    public void onMessage(ConsoleMessageEvent event) {
        messageHistory.add(event.getMessage());
    }

    public String removeLastLetter(String string) {
        String out = "";
        if (string != null && string.length() > 0) {
            out = string.substring(0, string.length() - 1);
        }
        return out;
    }

    public int getLongestWord(List<String> strings) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put(entryString + "_", Minecraft.getMinecraft().fontRenderer.getStringWidth(entryString + "_"));
        for(String s : strings)
            hashMap.put(s, Minecraft.getMinecraft().fontRenderer.getStringWidth(s));
        return Collections.max(hashMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getValue();
    }
}
