package com.model;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

/**
 * Utility class for text-to-speech functionality using FreeTTS.
 * Provides static methods to convert text to speech using the Kevin16 voice.
 * 
 * @author VHS Escape Team
 * @version 1.0
 */
public class Speak {
    /** The name of the voice to use for text-to-speech */
    private static final String VOICE_NAME = "kevin16";

    /**
     * Converts the provided text to speech and plays it.
     * Uses the FreeTTS library with the Kevin16 voice.
     * If the voice cannot be found, an error message is printed to stderr.
     * 
     * @param text the text to convert to speech
     */
    public static void speak(String text) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice voice = voiceManager.getVoice(VOICE_NAME);

        if(voice == null) {
            System.err.println("Voice not found: " + VOICE_NAME);
            return;
        }

        voice.allocate();
        voice.speak(text);
        voice.deallocate();
    }
}
