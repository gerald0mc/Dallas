package me.gerald.dallas.gui.comps.settingcomps;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.gui.api.AbstractContainer;
import me.gerald.dallas.setting.settings.StringSetting;
import me.gerald.dallas.utils.MessageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class StringComponent extends AbstractContainer {
    public StringSetting setting;
    public boolean listening;
    public String entryString;

    public StringComponent(StringSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 125).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(listening ? entryString : setting.getName() + " " + ChatFormatting.GRAY + setting.getValue(), x + 2, y + 2f, -1);
        if(isInside(mouseX, mouseY)) {
            Yeehaw.INSTANCE.clickGUI.descriptionBox.text = "A mode setting called (" + setting.getName() + ").";
            Yeehaw.INSTANCE.clickGUI.descriptionBox.width = Minecraft.getMinecraft().fontRenderer.getStringWidth("A mode setting called (" + setting.getName() + ").") + 3;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isInside(mouseX, mouseY)) {
            if(mouseButton == 0)
                listening = !listening;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {
        if(listening) {
            switch (key) {
                case Keyboard.KEY_BACK:
                    entryString = removeLastLetter(entryString);
                    break;
                case Keyboard.KEY_RETURN:
                    if(entryString.length() > 0) {
                        setting.setValue(entryString);
                    }
                    listening = false;
                    break;
                case Keyboard.KEY_V:
                    if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
                        entryString += Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    break;
                case Keyboard.KEY_C:
                    if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                        if(entryString.length() == 0) {
                            MessageUtils.sendMessage("Nothing to copy.");
                            return;
                        }
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(entryString), null);
                        MessageUtils.sendMessage(ChatFormatting.GRAY + "Copied text in string box to clipboard.");
                    }
                    break;
            }
            if(ChatAllowedCharacters.isAllowedCharacter(keyChar)) {
                entryString = entryString + keyChar;
            }
        }
    }

    public String removeLastLetter(String string) {
        String out = "";
        if(string != null && string.length() > 0) {
            out = string.substring(0, string.length() - 1);
        }
        return out;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
