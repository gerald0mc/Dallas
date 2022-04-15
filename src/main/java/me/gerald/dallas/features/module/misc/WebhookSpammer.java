package me.gerald.dallas.features.module.misc;

import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.setting.settings.StringSetting;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.WebhookUtil;

import java.io.IOException;
import java.net.MalformedURLException;

public class WebhookSpammer extends Module {
    public NumberSetting delay = register(new NumberSetting("Delay(Secs)", 2, 1, 30));
    public NumberSetting cooldown = register(new NumberSetting("Cooldown(Secs)", 30, 1, 60));
    public NumberSetting messages = register(new NumberSetting("Messages per cycle", 50, 1, 300));
    public StringSetting webhookURL = register(new StringSetting("Webhook", "Haha nothing."));
    public int messagesSent = 0;

    public WebhookSpammer() {
        super("WebhookSpammer", Category.MISC, "Allows you to spam a webhook directly in your minecraft client.");

        Thread thread = new Thread(() -> {
            if (!isEnabled()) return;
            if (messagesSent >= this.messages.getValue()) {
                MessageUtil.sendMessage("On cooldown to avoid being rate limited.");
                try {
                    Thread.sleep((int) cooldown.getValue() * 1000L);
                    MessageUtil.sendMessage("No longer on cooldown continuing webhook spam.");
                    messagesSent = 0;
                } catch (InterruptedException exception) {
                    MessageUtil.sendMessage("Cooldown Interrupted. Trying again.");
                }
            } else {
                WebhookUtil webhook = new WebhookUtil(webhookURL.getValue());
                webhook.setContent("PWNED BY DALLAS EZ");
                try {
                    webhook.execute();
                    messagesSent++;
                    Thread.sleep((int) delay.getValue() * 1000L);
                } catch (MalformedURLException e) {
                    MessageUtil.sendMessage("You have entered a invalid Webhook URL.");
                    toggle();
                } catch (IOException | InterruptedException ignored) {
                }
            }
        });

        thread.setName("Dallas/WebhookSpammer");
        thread.setDaemon(true);
        thread.start();
    }
}
