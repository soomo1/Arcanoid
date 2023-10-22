package org.soomo.controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SoundManager {
    private static final List<MediaPlayer> activePlayers = new ArrayList<>();
    private static final Media hitBrickMedia;
    private static final MediaPlayer hitBrickPlayer;
    private static boolean isMuted = false;

    static {
        String hitBrickPath = Paths.get("src/main/resources/audio/hitBrick.wav").toUri().toString();
        hitBrickMedia = new Media(hitBrickPath);
        hitBrickPlayer = new MediaPlayer(hitBrickMedia);
        activePlayers.add(hitBrickPlayer);
    }

    public static boolean getIsMuted() {
        return isMuted;
    }

    public static void setIsMuted(boolean mute) {
        isMuted = mute;
    }

    public static void playHitBrickSound() {
        if (!isMuted) {
            hitBrickPlayer.seek(Duration.ZERO); // Rewind to the beginning
            hitBrickPlayer.play();
        }
    }

    // Load and play background music
    public static void playTheme() {
        Media media = new Media(Paths.get("src/main/resources/audio/mainTheme2.mp3").toUri().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely
        activePlayers.add(mediaPlayer);
        if (!isMuted) {
            mediaPlayer.play();
        }
    }

    /**
     * Mutes or unmutes all active MediaPlayers.
     */
    public static void setSound() {
        for (MediaPlayer player : activePlayers) {
            player.setMute(isMuted);
        }
    }
}
