package me.gerald.dallas.event;

import com.mojang.realmsclient.gui.ChatFormatting;
import io.netty.util.internal.MathUtil;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.event.events.PacketEvent;
import me.gerald.dallas.mod.Module;
import me.gerald.dallas.utils.MessageUtils;
import me.gerald.dallas.utils.TimerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.apache.commons.lang3.RandomStringUtils;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EventManager {
    public TimerUtils texasFactTimer;
    public List<String> texasFacts;

    public EventManager() {
        MinecraftForge.EVENT_BUS.register(this);
        texasFactTimer = new TimerUtils();
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(Keyboard.getEventKeyState()) {
            int key = Keyboard.getEventKey();
            for(Module module : Yeehaw.INSTANCE.moduleManager.getModules()) {
                if(module.getKeybind() == key) {
                    module.toggle();
                    MessageUtils.sendMessage(ChatFormatting.BOLD + module.getName() + ChatFormatting.BOLD + (module.isEnabled() ? ChatFormatting.GREEN + " ENABLED" : ChatFormatting.RED + " DISABLED"));
                }
            }
        }
    }

    @SubscribeEvent
    public void onMessageReceive(PacketEvent.Receive event) {
        if(event.getPacket() instanceof SPacketChat) {
            SPacketChat chat = (SPacketChat) event.getPacket();
            if(chat.getChatComponent().getUnformattedText().startsWith("!texasfact") && texasFactTimer.passedMs(5000)) {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("TRYING TO SEND MESSAGE"));
                Minecraft.getMinecraft().player.sendChatMessage("[Texas Fact] " + getRandomMessage() + RandomStringUtils.random(7, true, true));
                texasFactTimer.reset();
            }
        }
    }

    public void loadMessages(String file) {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString("TRYING TO LOAD MESSAGES"));
        try {
            Path path = Paths.get(file);
            texasFacts = Files.readAllLines(path, StandardCharsets.UTF_8);
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("LOADED MESSAGES"));
        } catch (IOException e) {
            try {
                Path path = Paths.get(file);
                Files.write(path, Collections.singletonList(""), StandardCharsets.UTF_8, Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("CREATED NEW FILE"));
            } catch (IOException ignored) { }
        }
    }

    public String getRandomMessage() {
        loadMessages(Minecraft.getMinecraft().gameDir + File.separator + "Dallas" + File.separator + "Client" + File.separator + "TexasFacts.txt");
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString("TRYING TO GET RANDOM MESSAGE"));
        Random rand = new Random();
        if(texasFacts.size() == 0) {
            return "Texas is the best state!";
        }
        if(texasFacts.size() == 1) {
            return texasFacts.get(0);
        }
        return texasFacts.get(MathHelper.clamp(rand.nextInt(texasFacts.size()), 0, texasFacts.size() - 1));
    }
}
