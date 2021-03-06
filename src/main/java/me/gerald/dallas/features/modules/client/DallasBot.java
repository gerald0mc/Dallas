package me.gerald.dallas.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.managers.ConfigManager;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.FileUtil;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DallasBot extends Module {
    public static String[] defaultFacts = {"People from Texas are called Texans, Texians, or Tejanos.", "The state's capital is Austin.", "The time zone in Texas is Central Time Zone.", "Texas is the second most populous state in the United States after California. It is home to 28.7 million people.", "It is thought that there more than 1.5 million undocumented immigrants in the state.", "Three of the most populous cities in the United States are in Texas: Dallas, Houston and San Antonio.", "It is the second most extensive state after Alaska. Its surface is 695,662 km².", "At its most distant points, Texas is 790 miles long and 660 miles wide.", "Texas was the 28th state to join the Union. It became a state on December 29, 1845.", "There are more farms and more land in Texas than in any other state. This is perhaps what Texas is most famous for.", "King Ranch is a ranch located in Kingsville, Texas which is larger than the whole state of Rhode Island. It is the home of 35,000 cattle and 200 horses.", "Texas is often referred to as The Lone Star State because it was once an independent country, The Republic of Texas, and its flag contained a single star.", "Texas is the biggest producer of oil, natural gas and sulfur in the United States.", "Texas has the second highest number of Fortune 500 companies in the United States.", " Texas shares a border with New Mexico, Oklahoma, Arkansas and Louisiana.", "Texas has 254 counties.", "The highest point in Texas is Guadalupe Peek, which is 8,751 feet tall.", "Texas is about twice the size of Germany, and it is larger than any country in Western Europe.", "The largest river in Texas is the Rio Grande.", "Big Bend National Park is called that because it is located in the largest bend of the Rio Grande river. About 400,000 people visit it every year, and it home to 450 species of birds.", "Slightly less than 10% of Texas' land is desert.", "Texas experiences about 120 tornadoes a year. It is the state which has experienced the most tornadoes since 1950.", "The Great Galveston Storm of 1900, which killed about 8,000 people in Texas, was the deadliest natural disaster to have ever happened in the United States.", "Texas has its own power grid, part of which is fuelled by one of the world's largest wind power farms.", "As of February 2018, there were approximately 297,000 oil wells in Texas.", "Texas has the largest number of species of birds of all the United States.", "The Congress Avenue Bridge in Austin, the capital of Texas, is the home of the largest urban bat colony in North America. It hosts about 1.5 million Mexican free-tail bats.", "Texas also has more cattle than any other state in the United States.", "Some rocks older than 1,600 million years have been found in Texas.", "Before Europeans settled in Texas, it was the land of Native American tribes. Spanish explorer Álvar Núñez Cabeza de Vaca was the first European to discover Texas, although of course it existed long before this \"discovery\"!", "Texas has been ruled by Spain, France and Mexico.", "The Republic of Texas' first president was Sam Houston. His statue, erected in 1994, is the biggest statue in America which represents a real person.", "Before Texas was a US state, it fought for freedom from Mexico. One of the most famous battles was the Battle of Alamo, during which the Texans were outnumbered and defeated by the Mexicans.", "Austin, Texas was named after Stephen Austin who established the state's first American colony.", "Texas is the only state to enter the United States by treaty instead of having its territory annexed. It was the 28th state to join the Union.", "Before the Second World War, Texas' economy mostly relied on cattle, cotton, timber and oil.", "The state motto of Texas is \"Friendship\".", "The square dance is the state folk dance.", "The state song is called 'Texas, our Texas'.", "The state animals of Texas are the longhorn, the armadillo and the Mexican free-tailed bat.", "The state tree is the pecan tree.", "The state fish is the Guadalupe bass.", "The state flower is the Bluebonnet.", "The state bird is the Mockingbird.", "The state insect is the Monarch butterfly.", "The Texas red grapefruit is the state fruit.", "The state vegetable is the sweet onion.", "The state dish is chilli.", "The state hat and footwear are cowboy boots and a hat, of course!", "The seal of Texas is a blue circle with a star in its centre, framed by a wreath of oak and olive tree.", "The flag of Texas is known as the Lone Star Flag as it only contains one star. It is the reason behind Texas's nickname.", "The Texan flag has one vertical blue stripe containing a white star, and two horizontal stripes, one white on top of a red one.", "According to Texan statute, when it is displayed the flag should never touch anything beneath it, including the ground.", "The blue in the flag stands for loyalty, the red represents bravery and the white purity. They are the same colours than the flag of the United States."};

    public NumberSetting delay = new NumberSetting("Delay(Secs)", 5, 1, 30, "Delay in seconds.");
    public NumberSetting remindDelay = new NumberSetting("RemindDelay(Mins)", 2.5f, 1, 5, "Remind delay in minutes.");

    public TimerUtil coolDownTimer = new TimerUtil();
    public TimerUtil remindTimer = new TimerUtil();
    public TimerUtil timer = new TimerUtil();

    String filePath = "Dallas" + File.separator + "Client" + File.separator + "Facts.txt";
    List<String> facts = new ArrayList<>();
    String afterMessage = " | Dallas Bot";

    public DallasBot() {
        super("DallasBot", Category.CLIENT, "A bot with multiple useful commands.");
        facts.addAll(Arrays.asList(defaultFacts));
    }

    @Override
    public void onEnable() {
        if (nullCheck()) return;
        MessageUtil.sendMessage(ChatFormatting.BOLD + "Dallas Bot", "For a full list of commands you can do with Dallas Bot please do dhelp.", MessageUtil.MessageType.CONSTANT);
        File factsFile = new File(ConfigManager.clientPath, "Facts.txt");
        if (!factsFile.exists()) {
            try {
                factsFile.createNewFile();
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Dallas Bot", "Please go into your " + ChatFormatting.GREEN + ".minecraft" + ChatFormatting.RESET + " folder and navigate to " + ChatFormatting.AQUA + "Dallas" + ChatFormatting.GRAY + File.separator + ChatFormatting.AQUA + "Client" + ChatFormatting.GRAY + File.separator + ChatFormatting.AQUA + "Facts.txt" + ChatFormatting.RESET + " and add what facts you would like to use.", MessageUtil.MessageType.CONSTANT);
                try {
                    FileWriter writer = new FileWriter(factsFile, true);
                    for (String lines : facts) {
                        writer.write(lines + "\n");
                    }
                    writer.close();
                } catch (IOException ignored) {
                }
            } catch (IOException ignored) {
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        if (remindTimer.passedMs((long) (remindDelay.getValue() * 60000))) {
            mc.player.sendChatMessage("Remember you can do dhelp for a list of commands for Dallas Bot." + afterMessage);
            remindTimer.reset();
        }
    }

    @SubscribeEvent
    public void onChatReceive(ClientChatReceivedEvent event) {
        String[] strings = event.getMessage().getUnformattedText().split(" ");
        if (strings[1].equalsIgnoreCase("dhelp")) {
            if (StringUtils.substringBetween(event.getMessage().getUnformattedText(), "<", ">").equalsIgnoreCase(mc.player.getDisplayNameString())) {
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Dallas Bot", ChatFormatting.BLUE + "Da" + ChatFormatting.WHITE + "ll" + ChatFormatting.RED + "as" + ChatFormatting.GRAY + " Bot " + ChatFormatting.RESET + "Commands", MessageUtil.MessageType.CONSTANT);
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Dallas Bot", ChatFormatting.GRAY + "[" + ChatFormatting.AQUA + "dhelp" + ChatFormatting.GRAY + "]: " + ChatFormatting.GREEN + "Shows bot commands that you and everyone else can do.", MessageUtil.MessageType.CONSTANT);
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Dallas Bot", ChatFormatting.GRAY + "[" + ChatFormatting.AQUA + "dtexasfact(s)" + ChatFormatting.GRAY + "]: " + ChatFormatting.GREEN + "Sends a texas fact in chat.", MessageUtil.MessageType.CONSTANT);
            } else {
                if (!coolDownTimer.passedMs((long) (delay.getValue() * 1000))) return;
                mc.player.sendChatMessage("Dallas Bot Commands: [dhelp] Says all commands." + "|" + "[dtexasfact(s)] Sends texas facts in chat." + afterMessage);
                coolDownTimer.reset();
            }
        } else if (strings[1].equalsIgnoreCase("dtexasfact") || strings[1].equalsIgnoreCase("dtexasfacts")) {
            if (!coolDownTimer.passedMs((long) (delay.getValue() * 1000))) return;
            FileUtil.loadMessages(facts, filePath);
            mc.player.sendChatMessage(FileUtil.getRandomMessage(facts) + afterMessage);
            timer.reset();
        }
    }
}
