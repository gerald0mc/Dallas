package me.gerald.dallas.features.module.misc;

import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.setting.settings.StringSetting;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.TimerUtil;
import me.gerald.dallas.utils.WebhookUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;
import java.net.MalformedURLException;

/*
    @author gerald0mc
    @created 4/14/22
    #TODO Add thread support.
 */

public class WebhookSpammer extends Module {
    public WebhookSpammer() {
        super("WebhookSpammer", Category.MISC, "Allows you to spam a webhook directly in your minecraft client.");
//        Thread thread = new Thread(() -> {
//            if (!isEnabled()) return;
//            if (messagesSent >= messages.getValue()) {
//                MessageUtil.sendMessage("On cooldown to avoid being rate limited.");
//                try {
//                    Thread.sleep((int) cooldown.getValue() * 1000L);
//                    MessageUtil.sendMessage("No longer on cooldown continuing webhook spam.");
//                    messagesSent = 0;
//                } catch (InterruptedException exception) {
//                    MessageUtil.sendMessage("Cooldown Interrupted. Trying again.");
//                }
//            } else {
//                WebhookUtil webhook = new WebhookUtil(webhookURL.getValue());
//                webhook.setContent("PWNED BY DALLAS EZ");
//                try {
//                    webhook.execute();
//                    messagesSent++;
//                    Thread.sleep((int) delay.getValue() * 1000L);
//                } catch (MalformedURLException e) {
//                    MessageUtil.sendMessage("You have entered a invalid Webhook URL.");
//                    toggle();
//                } catch (IOException | InterruptedException ignored) {
//                }
//            }
//        });
//
//        thread.setName("Dallas/WebhookSpammer");
//        thread.setDaemon(true);
//        thread.start();
    }

    public NumberSetting delay = register(new NumberSetting("Delay(Secs)", 2, 1, 30));
    public NumberSetting cooldown = register(new NumberSetting("Cooldown(Secs)", 30, 1, 60));
    public NumberSetting messages = register(new NumberSetting("MessagesPerCycle", 50, 1, 300));
    public StringSetting webhookURL = register(new StringSetting("Webhook", "Haha nothing."));
    public StringSetting message = register(new StringSetting("Message", "@everyone PWNED BY DALLAS"));

    public TimerUtil timer = new TimerUtil();
    TimerUtil cooldownTimer = new TimerUtil();
    public int messagesSent = 0;
    boolean hasSaidThing = false;

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        WebhookUtil webhook = new WebhookUtil(webhookURL.getValue());
        webhook.setContent(message.getValue());
        try {
            if(!timer.passedMs((long) delay.getValue() * 1000)) return;
            if(messagesSent >= messages.getValue()) {
                if(!hasSaidThing) {
                    MessageUtil.sendMessage("On cooldown to avoid being rate limited.");
                    cooldownTimer.reset();
                    hasSaidThing = true;
                }
                if(!cooldownTimer.passedMs((long) (cooldown.getValue() * 1000))) return;
                MessageUtil.sendMessage("No longer on cooldown continuing webhook spam.");
                hasSaidThing = false;
                messagesSent = 0;
            }
            webhook.execute();
            messagesSent++;
            timer.reset();
        }catch (MalformedURLException e) {
            MessageUtil.sendMessage("You have entered a invalid Webhook URL.");
            toggle();
        } catch (IOException ignored) {}
    }
}
