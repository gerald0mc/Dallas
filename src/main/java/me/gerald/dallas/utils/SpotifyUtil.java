package me.gerald.dallas.utils;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.sun.net.httpserver.HttpServer;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.requests.authorization.authorization_code.pkce.AuthorizationCodePKCERequest;
import org.apache.hc.core5.http.ParseException;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class SpotifyUtil implements Globals {
    // my client id - this can be public as it only allows us to use spotify
    private static final String CLIENT_ID = "0cc6060392e0497bbeca323f61d66f10";

    // our spotify scopes
    private static final String[] SCOPES = { "user-modify-playback-state", "user-read-playback-state",
            "user-read-currently-playing", "user-read-recently-played", "user-read-playback-position" };

    // https://developer.spotify.com/documentation/general/guides/authorization/code-flow/
    // We'll create a random verifier string that we'll use so we don't have to store any sensitive information in here
    private static final CodeGenerator CODE_GEN = new CodeGenerator();

    // You have to put this URI in your spotify developer panel, or this will not work.
    // This is the URI that spotify will use once the user says "ok you can have my firstborn child"
    private static final URI CALLBACK_URI = SpotifyHttpManager.makeUri("http://localhost:7832/callback");

    // The spotify API instance, can be null if we have not connected.
    public static SpotifyApi api;
    public static User user;
    public static CurrentlyPlaying playing;

    // cache our HTTP server in case for some reason if we need to reconnect and it did not unbind & shutdown
    private static HttpServer httpServer;

    // Cache our threads, so we interrupt them if we want to stop
    private static Thread codeRefreshThread, playbackUpdateThread;
    private static int refreshInterval = 0;

    // if we are ready to use the spotify API
    public static boolean ready = false;

    /**
     * Attempts to connect to spotify
     */
    public static void connect() {
        CODE_GEN.generate();

        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                if (httpServer != null)
                    httpServer.stop(0);

                // create our web callback server so we can get the code once the user allows us to use their spotify shit
                httpServer = HttpServer.create(new InetSocketAddress(7832), 0);
                httpServer.setExecutor(null);

                httpServer.createContext("/callback", (ext) -> {
                    String query = ext.getRequestURI().getQuery();
                    String code = null;

                    boolean success = false;

                    try {
                        String[] params = query.split("=");

                        if (params[0].equalsIgnoreCase("code")) {
                            code = params[1];
                            success = !code.equals("access_denied");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String response;
                    if (success && !code.isEmpty()) {
                        // authenticate with spotify so we can do shit :)
                        authenticate(code);

                        response = "Successfully got the code, you may close this window and use Spotify! :)";
                    } else {
                        response = "Something bad happened. You either denied permissions, or spotify shit itself. Please retry the spotify command.";
                    }

                    // set our response
                    ext.sendResponseHeaders(success ? 200 : 400, response.length());

                    // write the response
                    OutputStream stream = ext.getResponseBody();
                    stream.write(response.getBytes(StandardCharsets.UTF_8), 0, response.length());
                    stream.close();

                    httpServer.stop(0);
                });

                httpServer.start();

                // create a new instance of the spotify api
                api = new SpotifyApi.Builder().setClientId(CLIENT_ID).setRedirectUri(CALLBACK_URI).build();

                // we're using PKCE because we cannot safely store our secret (as this is a damnhack client lel)
                // spotify's documentation recommends we use PKCE for this exact reason, for extra security
                Desktop.getDesktop().browse(api.authorizationCodePKCEUri(CODE_GEN.challenge)
                        .scope(String.join(" ", SCOPES)).build().execute());
            } else {
                System.out.println("Your desktop environment does not support browsing!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: print in mc chat if needed
        }
    }

    /**
     * Stops our spotify
     */
    public static void stop() {
        if (codeRefreshThread != null && !codeRefreshThread.isInterrupted())
            codeRefreshThread.interrupt();
        if (playbackUpdateThread != null && !playbackUpdateThread.isInterrupted())
            playbackUpdateThread.interrupt();
        codeRefreshThread = null;
        playbackUpdateThread = null;
        refreshInterval = 0;
        api = null;
        user = null;
        ready = false;
    }

    private static void authenticate(String code) {
        AuthorizationCodePKCERequest authorizationCodePKCERequest = api.authorizationCodePKCE(code, CODE_GEN.verifier).build();
        try {
            AuthorizationCodeCredentials credentials = authorizationCodePKCERequest.execute();

            // set our credential data so we can manage shit and do some fun shit
            api.setAccessToken(credentials.getAccessToken());
            api.setRefreshToken(credentials.getRefreshToken());

            // we don't wanna instantly create another request
            refreshInterval = credentials.getExpiresIn();

            // fetch the users profile
            user = api.getCurrentUsersProfile().build().execute();
            if (user != null) {
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Spotify", "Hey there, " + user.getDisplayName() + "!", true);
                ready = true; // mark as ready :0
            }

            // create the thread we'll use
            codeRefreshThread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        // we do this to prevent intellij from screaming at us
                        // Call to 'Thread.sleep()' in a loop, probably busy-waiting
                        TimeUnit.MILLISECONDS.sleep(refreshInterval);

                        // refresh our credentials
                        AuthorizationCodeCredentials refreshed = api.authorizationCodePKCERefresh().build().execute();

                        // set them :)
                        api.setAccessToken(refreshed.getAccessToken());
                        api.setRefreshToken(refreshed.getRefreshToken());

                        // spotify gives us a refresh interval, so we'll abide by that so we dont get ratelimited/denied access cause of too many requests
                        // https://developer.spotify.com/documentation/general/guides/authorization/client-credentials/
                        // this time is in milliseconds
                        refreshInterval = refreshed.getExpiresIn();
                    } catch (InterruptedException ignored) {
                        // empty catch block - dont do anything
                    } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, "Spotify-Refresh-Thread");

            playbackUpdateThread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000);
                        playing = api.getUsersCurrentlyPlayingTrack().build().execute();
                    } catch (InterruptedException | IOException | ParseException | SpotifyWebApiException e ) {
                        e.printStackTrace();
                    }
                }
            }, "Spotify-Playback-Update-Thread");
            codeRefreshThread.start();
            playbackUpdateThread.start();
        } catch (Exception ignored) { }
    }
}
