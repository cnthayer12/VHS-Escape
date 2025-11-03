package com.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestRiddle {

    @Test
    public void defaultConstructor_initializesFields() {
        Riddle r = new Riddle();

        assertEquals("Default constructor should set riddleText to empty string",
                "", r.getRiddleText());

        assertEquals("Default constructor should set correctAnswer to empty string",
                "", r.getCorrectAnswer());

        // We can't guarantee access to r.type here (depends on Puzzle),
        // but we CAN at least assert toString starts correctly so we know object is usable.
        assertTrue("toString() should start with 'Riddle:'",
                r.toString().startsWith("Riddle:"));
    }

    @Test
    public void constructorWithArgs_setsFields() {
        Riddle r = new Riddle("What has keys but can't open locks?", "piano");

        assertEquals("Constructor should store riddle text",
                "What has keys but can't open locks?", r.getRiddleText());

        assertEquals("Constructor should store correct answer",
                "piano", r.getCorrectAnswer());
    }

    @Test
    public void checkAnswer_exactMatchIsTrue() {
        Riddle r = new Riddle("2+2?", "4");

        assertTrue("checkAnswer should return true on exact match",
                r.checkAnswer("4"));
    }

    @Test
    public void checkAnswer_caseInsensitiveMatchIsTrue() {
        Riddle r = new Riddle("Capital of France?", "Paris");

        assertTrue("checkAnswer should be case-insensitive (lowercase guess)",
                r.checkAnswer("paris"));

        assertTrue("checkAnswer should be case-insensitive (uppercase guess)",
                r.checkAnswer("PARIS"));
    }

    @Test
    public void checkAnswer_trimsWhitespace() {
        Riddle r = new Riddle("Speak friend and enter", "mellon");

        assertTrue("checkAnswer should trim leading/trailing whitespace in the guess",
                r.checkAnswer("  mellon  "));
    }

    @Test
    public void checkAnswer_wrongAnswerIsFalse() {
        Riddle r = new Riddle("Riddle me this", "correct");

        assertFalse("checkAnswer should be false for wrong guesses",
                r.checkAnswer("incorrect"));
    }

    @Test
    public void checkAnswer_nullGuessIsFalse() {
        Riddle r = new Riddle("Riddle me this", "correct");

        assertFalse("checkAnswer should return false when guess is null",
                r.checkAnswer(null));
    }

    @Test
    public void checkAnswer_nullCorrectAnswerIsFalse() {
        Riddle r = new Riddle("Edge case riddle", null);

        assertFalse("checkAnswer should return false if correctAnswer is null",
                r.checkAnswer("anything"));
    }

    @Test
    public void toString_includesRiddleText() {
        Riddle r = new Riddle("What walks on four legs in the morning?", "man");
        String result = r.toString();

        assertNotNull("toString() should not return null", result);
        assertTrue("toString() should include the riddle text",
                result.contains("What walks on four legs in the morning?"));
        assertTrue("toString() should start with 'Riddle:'",
                result.startsWith("Riddle:"));
    }
}
