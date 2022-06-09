package me.gerald.dallas.features.modules.hud.spotify;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.wrapper.spotify.enums.CurrentlyPlayingType;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Image;
import com.wrapper.spotify.model_objects.specification.Track;
import me.gerald.dallas.features.gui.api.HUDContainer;
import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import me.gerald.dallas.utils.RenderUtil;
import me.gerald.dallas.utils.SpotifyUtil;
import me.gerald.dallas.utils.TextureUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SpotifyComponent extends HUDContainer {
    private final Map<String, Integer> CACHE = new ConcurrentHashMap<>();

    public SpotifyComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateDragPosition(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        CurrentlyPlaying playing = null;
        try {
            playing = SpotifyUtil.playing;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (playing == null) {
            return;
        }

        if (!playing.getCurrentlyPlayingType().equals(CurrentlyPlayingType.TRACK)) {
            return;
        }

        // background
        Gui.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 175).getRGB());

        Track track = (Track) playing.getItem();
        String name = track.getName();

        double x1 = x;

        AlbumSimplified album = track.getAlbum();

        Image[] images = album.getImages();
        if (images != null && images.length > 0) {
            int texId = getTextureId(images[0].getUrl(), name);
            if (texId != -1) {
                double dimensions = getHeight();

                TextureUtil.bindTexture(texId);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glColor4d(1, 1, 1, 1);
                TextureUtil.drawTexture(null, x1, y, 0, 0, dimensions, dimensions);
                GL11.glDisable(GL11.GL_BLEND);

                x1 += dimensions + 4.0;
            }
        }

        float y1 = (float) (y + 10.0);

        // title
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(customTrimValue("", name, "", (int) ((x + width) - x1), 4), (float) x1, y1, -1);
        y1 += (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 2.5f);

        // artists
        String artists = Arrays.stream(track.getArtists()).map(ArtistSimplified::getName).collect(Collectors.joining(", "));
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(customTrimValue(ChatFormatting.GRAY + "by ", artists, "", (int) ((x + width) - x1), 4), (float) x1, y1, -1);
        y1 += (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 2.5f);

        // progress bar

        y1 += 5.0;

        int duration = track.getDurationMs();
        int position = playing.getProgress_ms();

        float max = (float) duration;
        float percent = (float) position / max;

        double width = 140;

        Gui.drawRect((int) x1, (int) y1, (int) (x1 + width), (int) (y1 + 15), 0x34000000);
        Gui.drawRect((int) x1, (int) y1, (int) (x1 + width * percent), (int) (y1 + 15), ClickGUI.clientColor.getRGB());
        RenderUtil.renderBorder((int) x1, (int) y1, (int) (x1 + width), (int) (y1 + 15), 1, new Color(0, 0, 0, 255));

        // timestamps
        y1 += 17.0;
        String text = formatTime(position) + "/" + formatTime(duration);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(ChatFormatting.GRAY + text, (float) (x1 + (width / 2.0) - Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2.0), y1, -1);
        RenderUtil.renderBorder(x, y, x + this.width, y + height, 1, new Color(0, 0, 0, 255));
        this.width = 225;
        height = 75;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY)) {
            if (mouseButton == 0)
                beginDragging(mouseX, mouseY);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        stopDragging();
    }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {
    }

    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Gets the texture id for this url
     *
     * @param url  the spotify image url
     * @param name the name of the song for caching
     * @return the texture id or -1 if invalid
     */
    private int getTextureId(String url, String name) {
        if (CACHE.containsKey(name)) return CACHE.get(name);
        try {
            URL uri = new URL(url);
            BufferedImage image = ImageIO.read(uri);

            if (image != null) {
                DynamicTexture texture = new DynamicTexture(image);
                CACHE.put(name, texture.getGlTextureId());
            }
        } catch (IOException ignored) {
        }
        return -1;
    }

    private String formatTime(long time) {
        StringBuilder builder = new StringBuilder();
        double hours = Math.floor((time / (1e3 * 60 * 60)) % 60);
        double minutes = Math.floor(time / 6e4);
        double seconds = ((time % 6e4) / 1e3);
        if (hours > 0.0) builder.append((int) Math.round(hours)).append(":");
        builder.append((int) Math.round(minutes)).append(":").append((int) Math.round(seconds));
        return builder.toString();
    }
}
