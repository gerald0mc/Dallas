package me.gerald.dallas.features.module.misc;

import me.gerald.dallas.features.module.Module;
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
    public final StringSetting webhookURL = register(new StringSetting("Webhook", "Haha nothing."));
    public final StringSetting message = register(new StringSetting("Message", "@everyone PWNED BY DALLAS"));
    private final NumberSetting delay = register(new NumberSetting("Delay(Secs)", 2, 1, 30));
    private final NumberSetting cooldown = register(new NumberSetting("Cooldown(Secs)", 30, 1, 60));
    private final NumberSetting messages = register(new NumberSetting("MessagesPerCycle", 50, 1, 300));
    private int messagesSent = 0;

    public WebhookSpammer() {
        super("WebhookSpammer", Category.MISC, "Allows you to spam a webhook directly in your minecraft client.");
    }

    @Override
    public void onEnable() {
        Thread thread = new Thread(() -> {
            while (isEnabled()) {
                if (messagesSent >= messages.getValue()) {
                    MessageUtil.sendMessage("On cooldown to avoid being rate limited.");
                    delay((int) cooldown.getValue() * 1000);
                    MessageUtil.sendMessage("No longer on cooldown, continuing webhook spam.");
                    messagesSent = 0;
                } else {
                    WebhookUtil webhook = new WebhookUtil(webhookURL.getValue());
                    webhook.setContent("PWNED BY DALLAS EZ");
                    try {
                        webhook.execute();
                        MessageUtil.sendMessage("Executed");
                        messagesSent++;
                    } catch (MalformedURLException e) {
                        MessageUtil.sendMessage("You have entered a invalid Webhook URL.");
                        toggle();
                    } catch (IOException exception) {
                        MessageUtil.sendMessage("There was an error while trying to send to the webhook.");
                    }
                    delay((int) delay.getValue() * 1000);
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
