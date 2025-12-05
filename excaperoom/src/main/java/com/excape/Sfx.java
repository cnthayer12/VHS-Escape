package com.excape;

import javafx.scene.media.AudioClip;

public final class Sfx {
    private static double volume = 0.8;

    private Sfx() {}

    public static void play(String resourcePath) {
        var url = Sfx.class.getResource(resourcePath);
        if (url == null) {
            System.err.println("SFX not found: " + resourcePath);
            return;
        }

        AudioClip clip = new AudioClip(url.toExternalForm());
        clip.setVolume(volume);
        clip.play();
    }

    public static void setVolume(double v) {
        volume = Math.max(0.0, Math.min(1.0, v));
    }
}
