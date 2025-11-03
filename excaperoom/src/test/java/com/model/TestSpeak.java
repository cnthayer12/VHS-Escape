package com.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

public class TestSpeak {

    @Test
    public void speak_withNormalText_shouldNotThrow() {
        try {
            Speak.speak("This is a test line.");
            // If no exception, we consider this a pass.
            assertTrue("Speak.speak(\"text\") should run without throwing", true);
        } catch (Exception e) {
            fail("Speak.speak(\"text\") should not throw, but threw: " + e.getClass().getName() + " - " + e.getMessage());
        }
    }

    @Test
    public void speak_withNullText_shouldNotThrowOrCrash() {
        try {
            Speak.speak(null);

            // Our expectation: even if text is null, Speak should degrade gracefully
            // (e.g. print or skip), not crash the game.
            assertTrue("Speak.speak(null) should return without throwing", true);
        } catch (Exception e) {
            fail("Speak.speak(null) should not throw, but threw: " + e.getClass().getName() + " - " + e.getMessage());
        }
    }

    @Test
    public void speak_withEmptyString_shouldNotThrow() {
        try {
            Speak.speak("");
            assertTrue("Speak.speak(\"\") should not crash TTS", true);
        } catch (Exception e) {
            fail("Speak.speak(\"\") should not throw, but threw: " + e.getClass().getName());
        }
    }

}
