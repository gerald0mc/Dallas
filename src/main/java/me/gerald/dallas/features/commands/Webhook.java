package me.gerald.dallas.features.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.modules.misc.WebhookSpammer;
import me.gerald.dallas.managers.command.Command;
import me.gerald.dallas.utils.MessageUtil;

public class Webhook extends Command {
    public Webhook() {
        super("Webhook", "Webhook stuff.", new String[]{"webhook", "[set [webhook/url]/start]"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        if (args.length == 1) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Webhook", "Please specify which sub command you would like to use.", true);
            return;
        }
        switch (args[1]) {
            case "set":
                if (args.length == 2) {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Webhook", "Please specify either set webhook or message.", true);
                    return;
                }
                switch (args[2]) {
                    case "url":
                        String webhookURL = args[3];
                        Yeehaw.INSTANCE.moduleManager.getModule(WebhookSpammer.class).webhookURL.setValue(webhookURL);
                        MessageUtil.sendMessage(ChatFormatting.BOLD + "Webhook", "Set Webhook URL to " + ChatFormatting.AQUA + webhookURL, true);
                        break;
                    case "message":
                        StringBuilder string = new StringBuilder(args[3]);
                        for (int i = 1; i < (args.length - 3); i++) {
                            string.append(" ").append(args[(3 + i)]);
                        }
                        Yeehaw.INSTANCE.moduleManager.getModule(WebhookSpammer.class).message.setValue(string.toString());
                        MessageUtil.sendMessage(ChatFormatting.BOLD + "Webhook", "Set message to " + ChatFormatting.AQUA + string, true);
                        break;
                }
                break;
            case "start":
                Yeehaw.INSTANCE.moduleManager.getModule(WebhookSpammer.class).toggle();
                break;
        }
    }
}
