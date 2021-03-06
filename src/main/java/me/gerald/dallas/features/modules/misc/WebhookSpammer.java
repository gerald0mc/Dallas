package me.gerald.dallas.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.setting.settings.StringSetting;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.WebhookUtil;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * @author gerald0mc + bush
 * @since 4/14/22
 */
public class WebhookSpammer extends Module {
    public StringSetting webhookURL = new StringSetting("Webhook", "Haha nothing.", "The URL of the webhook you are gonna be trolling.");
    public StringSetting message = new StringSetting("Message", "@everyone PWNED BY DALLAS", "The message you will be sending the webhook.");
    public ModeSetting mode = new ModeSetting("Mode", "Spammer", "The way you are gonna be trolling said webhook.", "Spammer", "Crasher");
    public NumberSetting delay = new NumberSetting("Delay(Secs)", 2, 1, 30, "The delay in seconds of how fast it will spam.", () -> mode.getMode().equals("Spammer"));
    public NumberSetting cooldown = new NumberSetting("Cooldown(Secs)", 30, 1, 60, "How fast in seconds the cooldown is to not get rate blocked.", () -> mode.getMode().equals("Spammer"));
    public NumberSetting messages = new NumberSetting("MessagesPerCycle", 50, 1, 300, "How many messages to send before going into cooldown.", () -> mode.getMode().equals("Spammer"));

    private int messagesSent = 0;
    private int totalMessages = 0;

    public WebhookSpammer() {
        super("WebhookSpammer", Category.MISC, "Allows you to spam a webhook directly in your minecraft client.");
    }

    @Override
    public String getMetaData() {
        return totalMessages != 0 ? String.valueOf(totalMessages) : "";
    }

    @Override
    public void onDisable() {
        messagesSent = 0;
        totalMessages = 0;
    }

    @Override
    public void onEnable() {
        Thread thread = new Thread(() -> {
            while (isEnabled()) {
                switch (mode.getMode()) {
                    case "Spammer":
                        if (messagesSent >= messages.getValue()) {
                            MessageUtil.sendMessage(ChatFormatting.BOLD + "Webhook Spammer", "On cooldown to avoid being rate limited.", MessageUtil.MessageType.CONSTANT);
                            delay((int) cooldown.getValue() * 1000);
                            MessageUtil.sendMessage(ChatFormatting.BOLD + "Webhook Spammer", "No longer on cooldown, continuing webhook spam.", MessageUtil.MessageType.CONSTANT);
                            messagesSent = 0;
                        } else {
                            WebhookUtil webhook = new WebhookUtil(webhookURL.getValue());
                            webhook.setContent(message.getValue());
                            try {
                                webhook.execute();
                                MessageUtil.sendMessage(ChatFormatting.BOLD + "Webhook Spammer", "Executed", MessageUtil.MessageType.CONSTANT);
                                messagesSent++;
                                totalMessages++;
                            } catch (MalformedURLException e) {
                                MessageUtil.sendMessage(ChatFormatting.BOLD + "Webhook Spammer", "You have entered a invalid Webhook URL.", MessageUtil.MessageType.CONSTANT);
                                toggle();
                            } catch (IOException exception) {
                                MessageUtil.sendMessage(ChatFormatting.BOLD + "Webhook Spammer", "There was an error while trying to send to the webhook.", MessageUtil.MessageType.CONSTANT);
                                toggle();
                            }
                            delay((int) delay.getValue() * 1000);
                        }
                        break;
                    case "Crasher":
                        WebhookUtil webhook = new WebhookUtil(webhookURL.getValue());
                        webhook.setContent(message.getValue());
                        try {
                            webhook.execute();
                            totalMessages++;
                        } catch (MalformedURLException e) {
                            MessageUtil.sendMessage(ChatFormatting.BOLD + "Webhook Spammer", "You have entered a invalid Webhook URL.", MessageUtil.MessageType.CONSTANT);
                            toggle();
                        } catch (IOException exception) {
                            MessageUtil.sendMessage(ChatFormatting.BOLD + "Webhook Spammer", "There was an error while trying to send to the webhook.", MessageUtil.MessageType.CONSTANT);
                            toggle();
                        }
                        break;
                }
            }
        });
        thread.setDaemon(true);
        thread.setName("Dallas/WebhookSpammer");
        thread.start();
    }

    private void delay(int time) {
        long now = System.currentTimeMillis();
        // Better performance than Thread.sleep() lol
        while (System.currentTimeMillis() < now + time) {
            synchronized (this) {
                try {
                    wait(time);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}
