package me.gerald.dallas.features.modules.misc;

import me.bush.translator.Language;
import me.bush.translator.Translation;
import me.bush.translator.Translator;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.StringSetting;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.mojang.realmsclient.gui.ChatFormatting.BOLD;
import static com.mojang.realmsclient.gui.ChatFormatting.GRAY;
import static me.bush.translator.LanguageKt.languageOf;

/**
 * @author bush
 * @since 6/9/2022
 */
public class AutoTranslate extends Module {
    private final BooleanSetting incoming = new BooleanSetting("Incoming", true, "Translate incoming messages.");
    private final StringSetting inLang = new StringSetting("In Lang", "en", "Language to translate incoming messages to.", incoming::getValue);
    private final BooleanSetting suffix = new BooleanSetting("Suffix Translations", true, "Mark translated messages with \"[Translated]\".", incoming::getValue);
    private final BooleanSetting outgoing = new BooleanSetting("Outgoing", true, "Translate outgoing messages.");
    private final StringSetting outLang = new StringSetting("Out Lang", "en", "Language to translate outgoing messages to.", outgoing::getValue);

    private final Translator translator = new Translator();
    private final Executor executor = Executors.newSingleThreadExecutor();

    public AutoTranslate() {
        super("AutoTranslate", Category.MISC, "Translates incoming/outgoing chat messages");
    }

    @SubscribeEvent
    public void onChatReceive(ClientChatReceivedEvent event) {
        Language language = getLanguage(inLang.getValue());
        if (!incoming.getValue() || language == null || nullCheck()) return;
        event.setCanceled(true);
        executor.execute(() -> {
            Translation translation = getTranslation(event.getMessage().getUnformattedText(), language);
            if (translation != null) mc.addScheduledTask(() -> {
                String messageSuffix = (suffix.getValue() && translation.getSourceLanguage() != translation.getTargetLanguage()) ? GRAY + " [Translated]" : "";
                mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(translation.getTranslatedText() + messageSuffix));
            });
        });
    }

    @SubscribeEvent
    public void onChatSend(ClientChatEvent event) {
        Language language = getLanguage(outLang.getValue());
        if (!outgoing.getValue() || language == null || event.getMessage().startsWith("/") || nullCheck()) return;
        event.setCanceled(true);
        executor.execute(() -> {
            Translation translation = getTranslation(event.getMessage(), language);
            if (translation != null)
                mc.addScheduledTask(() -> mc.player.sendChatMessage(translation.getTranslatedText()));
        });
    }

    private Translation getTranslation(String message, Language language) {
        Translation translation = null;
        try {
            translation = translator.translateBlocking(message, language);
        } catch (Exception exception) {
            MessageUtil.sendMessage(BOLD + "AutoTranslate", "Could not process translation request. Disabling.", MessageUtil.MessageType.CONSTANT);
            toggle();
        }
        return translation;
    }

    private Language getLanguage(String string) {
        Language language = languageOf(string);
        if (language == null) {
            MessageUtil.sendMessage(BOLD + "AutoTranslate", string + " is not a valid language. Disabling.", MessageUtil.MessageType.CONSTANT);
            toggle();
        }
        return language;
    }
}
