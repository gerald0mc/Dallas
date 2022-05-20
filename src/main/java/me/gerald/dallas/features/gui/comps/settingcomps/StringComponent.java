package me.gerald.dallas.features.gui.comps.settingcomps;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.SettingComponent;
import me.gerald.dallas.setting.settings.StringSetting;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class StringComponent extends SettingComponent {
    public StringSetting setting;
    public boolean listening;
    public String entryString = "";

    public StringComponent(StringSetting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);
        this.setting = setting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 125).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(trimValue("", setting.getName(), listening ? ChatFormatting.WHITE + "TYPING" : !isInside(mouseX, mouseY) ? ChatFormatting.GRAY + "HOVER" : ChatFormatting.WHITE + "HOVERING"), x + 2, y + 1, -1);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(listening ? ChatFormatting.WHITE + "TYPING" : !isInside(mouseX, mouseY) ? ChatFormatting.GRAY + "HOVER" : ChatFormatting.WHITE + "HOVERING", x + width - Minecraft.getMinecraft().fontRenderer.getStringWidth(listening ? "TYPING" : !isInside(mouseX, mouseY) ? "HOVER" : "HOVERING") - 2, y + 1, -1);
        RenderUtil.renderBorderToggle(x, y, x + width, y + height, 1, new Color(0, 0, 0, 255), false, true, true, last);
        if (isInside(mouseX, mouseY)) {
            Gui.drawRect(mouseX + 5, mouseY - 5 - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, mouseX + 8 + Minecraft.getMinecraft().fontRenderer.getStringWidth(listening ? entryString : setting.getValue()), mouseY - 5, new Color(0, 0, 0, 255).getRGB());
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(listening ? entryString : setting.getValue(), mouseX + 7, mouseY - 13, -1);
            Yeehaw.INSTANCE.clickGUI.descriptionBox.text = "A mode setting called (" + setting.getName() + ").";
            Yeehaw.INSTANCE.clickGUI.descriptionBox.width = Minecraft.getMinecraft().fontRenderer.getStringWidth("A mode setting called (" + setting.getName() + ").") + 3;
        } else if (listening) {
            Gui.drawRect(mouseX + 5, mouseY - 5 - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, mouseX + 8 + Minecraft.getMinecraft().fontRenderer.getStringWidth(entryString), mouseY - 5, new Color(0, 0, 0, 255).getRGB());
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(entryString, mouseX + 7, mouseY - 13, -1);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY)) {
            if (mouseButton == 0)
                listening = !listening;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {
        if (listening) {
            switch (key) {
                case Keyboard.KEY_BACK:
                    entryString = removeLastLetter(entryString);
                    break;
                case Keyboard.KEY_RETURN:
                    if (entryString.length() > 0) {
                        setting.setValue(entryString);
                    }
                    listening = false;
                    entryString = "";
                    break;
                case Keyboard.KEY_V:
                    if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
                        entryString += Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    break;
                case Keyboard.KEY_C:
                    if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                        if (entryString.length() == 0) {
                            MessageUtil.sendMessage(ChatFormatting.BOLD + setting.getName(), "Nothing to copy.", true);
                            return;
                        }
                        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(entryString), null);
                        MessageUtil.sendMessage(ChatFormatting.BOLD + setting.getName(), "Copied text in string box to clipboard.", true);
                    }
                    break;
            }
            if (ChatAllowedCharacters.isAllowedCharacter(keyChar))
                entryString += keyChar;
        }
    }

    public String removeLastLetter(String string) {
        String out = "";
        if (string != null && string.length() > 0) {
            out = string.substring(0, string.length() - 1);
        }
        return out;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
