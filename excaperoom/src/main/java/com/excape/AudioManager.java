package com.excape;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public final class AudioManager {
    private static MediaPlayer bgmPlayer;

    private AudioManager() {}

    public static void playBgm(String resourcePath) {
        try {
            var url = AudioManager.class.getResource(resourcePath);
            if (url == null) {
                System.err.println("BGM not found: " + resourcePath);
                return;
            }

            if (bgmPlayer != null) {
                bgmPlayer.stop();
            }

            Media media = new Media(url.toExternalForm());
            bgmPlayer = new MediaPlayer(media);
            bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            bgmPlayer.setVolume(0.35);
            bgmPlayer.play();

        } catch (Exception e) {
            System.err.println("Failed to play BGM: " + resourcePath);
            e.printStackTrace();
        }
    }

    public static void stopBgm() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }
    }

    public static void setVolume(double volume) {
        if (bgmPlayer != null) {
            bgmPlayer.setVolume(volume);
        }
    }
}
