package me.gerald.dallas.features.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import me.gerald.dallas.managers.command.Command;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.SpotifyUtil;

public class Spotify extends Command {
    public Spotify() {
        super("Spotify", "Spotify command.", new String[]{"spotify"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        if (SpotifyUtil.ready) {
            CurrentlyPlaying playing = SpotifyUtil.playing;
            if (playing != null) {
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Spotify", "Listening to: " + playing.getItem().getName(), true);
            } else {
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Spotify", "You either are not listening to anything or we are waiting for spotify to respond to us...", true);
            }
        } else {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Spotify", "Logging into spotify...", true);
            SpotifyUtil.connect();
        }
    }
}
