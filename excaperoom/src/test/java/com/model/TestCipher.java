package com.model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class TestCipher {

    private Cipher defaultCipher;
    private Cipher parameterizedCipher;

    @Before
    public void setUp() {
        defaultCipher = new Cipher();
        parameterizedCipher = new Cipher("KHOOR", "HELLO", 3);
    }

    // ---------------- Constructor Tests ----------------

     @Test
    public void testParameterizedConstructorWithNullValues() {
        Cipher cipher = new Cipher(null, null, 0);
        assertNull(cipher.getCipherText());
        assertNull(cipher.getCorrectAnswer());
        assertEquals(0, cipher.getShift());
    }
    @Test
    public void testParameterizedConstructorSetsType() {
        Cipher cipher = new Cipher("KHOOR", "HELLO", 3);
        assertEquals("Cipher", cipher.getType());
    }
    
    @Test
    public void testDefaultConstructorInitializesCipherText() {
        assertEquals("", defaultCipher.getCipherText());
    }

    @Test
    public void testDefaultConstructorInitializesCorrectAnswer() {
        assertEquals("", defaultCipher.getCorrectAnswer());
    }

    @Test
    public void testDefaultConstructorInitializesShift() {
        assertEquals(1, defaultCipher.getShift());
    }

    @Test
    public void testDefaultConstructorSetsType() {
        assertEquals("Cipher", defaultCipher.getType());
    }

    @Test
    public void testParameterizedConstructorSetsFields() {
        assertEquals("KHOOR", parameterizedCipher.getCipherText());
        assertEquals("HELLO", parameterizedCipher.getCorrectAnswer());
        assertEquals(3, parameterizedCipher.getShift());
    }

    // ---------------- Accessor / Mutator Tests ----------------

    @Test
    public void testSetCipherTextToNullThenGet() {
        defaultCipher.setCipherText(null);
        assertNull(defaultCipher.getCipherText());
    }

    @Test
    public void testSetCorrectAnswerToNullThenGet() {
        defaultCipher.setCorrectAnswer(null);
        assertNull(defaultCipher.getCorrectAnswer());
    }

    @Test
    public void testSetShiftToNegative() {
        defaultCipher.setShift(-5);
        assertEquals(-5, defaultCipher.getShift());
    }

    @Test
    public void testSetShiftToZero() {
        defaultCipher.setShift(0);
        assertEquals(0, defaultCipher.getShift());
    }

    @Test
    public void testSetShiftToLargeValue() {
        defaultCipher.setShift(999);
        assertEquals(999, defaultCipher.getShift());
    }

    // ---------------- checkAnswer Normal Cases ----------------

    @Test
    public void testCheckAnswerExactMatch() {
        assertTrue(parameterizedCipher.checkAnswer("HELLO"));
    }

    @Test
    public void testCheckAnswerLowercase() {
        assertTrue(parameterizedCipher.checkAnswer("hello"));
    }

    @Test
    public void testCheckAnswerMixedCase() {
        assertTrue(parameterizedCipher.checkAnswer("HeLLo"));
    }

    @Test
    public void testCheckAnswerWithWhitespace() {
        assertTrue(parameterizedCipher.checkAnswer("  HELLO  "));
    }

    @Test
    public void testCheckAnswerIncorrect() {
        assertFalse(parameterizedCipher.checkAnswer("WORLD"));
    }

    @Test
    public void testCheckAnswerEmptyString() {
        assertFalse(parameterizedCipher.checkAnswer(""));
    }

    @Test
    public void testCheckAnswerWithSymbolsInInput() {
        assertFalse(parameterizedCipher.checkAnswer("HELLO!"));
    }

    @Test
    public void testCheckAnswerWithSpacesInMiddle() {
        assertFalse(parameterizedCipher.checkAnswer("HE LLO"));
    }

    @Test
    public void testCheckAnswerEmptyCorrectAnswer() {
        Cipher cipher = new Cipher("XYZ", "", 1);
        assertTrue(cipher.checkAnswer(""));
    }

    @Test
    public void testCheckAnswerWhitespaceOnly() {
        assertFalse(parameterizedCipher.checkAnswer("   "));
    }

    // ---------------- checkAnswer Error / Edge Cases ----------------

    @Test
    public void testCheckAnswerNullInput() {
        assertFalse(parameterizedCipher.checkAnswer(null));
    }

    @Test
    public void testCheckAnswerNullCorrectAnswer() {
        Cipher cipher = new Cipher("ABC", null, 1);
        assertFalse(cipher.checkAnswer("HELLO"));
    }

    @Test
    public void testCheckAnswerBothNull() {
        Cipher cipher = new Cipher(null, null, 1);
        assertFalse(cipher.checkAnswer(null));
    }

    @Test
    public void testCheckAnswerCipherTextNullDoesNotCrash() {
        Cipher cipher = new Cipher(null, "HELLO", 1);
        assertTrue(cipher.checkAnswer("HELLO"));
    }

    // ---------------- toString Tests ----------------

    @Test
    public void testToStringWithCipherText() {
        assertEquals("Cipher Puzzle: KHOOR", parameterizedCipher.toString());
    }

    @Test
    public void testToStringEmptyCipherText() {
        assertEquals("Cipher Puzzle: ", defaultCipher.toString());
    }

    @Test
    public void testToStringNullCipherText() {
        defaultCipher.setCipherText(null);
        assertEquals("Cipher Puzzle: null", defaultCipher.toString());
    }

    @Test
    public void testToStringWithSpecialCharacters() {
        defaultCipher.setCipherText("@#$$%");
        assertEquals("Cipher Puzzle: @#$$%", defaultCipher.toString());
    }

    // ---------------- Integration / Workflow Tests ----------------

    @Test
    public void testFullWorkflow() {
        Cipher cipher = new Cipher();
        cipher.setCipherText("BCD");
        cipher.setCorrectAnswer("ABC");
        cipher.setShift(1);

        assertEquals("BCD", cipher.getCipherText());
        assertEquals("ABC", cipher.getCorrectAnswer());
        assertEquals(1, cipher.getShift());
        assertTrue(cipher.checkAnswer("abc"));
    }

    @Test
    public void testCipherTextChangeDoesNotAffectCheckAnswer() {
        parameterizedCipher.setCipherText("NEWVALUE");
        assertTrue(parameterizedCipher.checkAnswer("HELLO"));
    }

    @Test
    public void testShiftChangeDoesNotAffectCheckAnswer() {
        parameterizedCipher.setShift(99);
        assertTrue(parameterizedCipher.checkAnswer("HELLO"));
    }

    // ---------------- Potential Bug Probes ----------------

    @Test
    public void testCheckAnswerTrimsTabsAndNewlines() {
        assertTrue(parameterizedCipher.checkAnswer("\n\tHELLO\t"));
    }

    @Test
    public void testCheckAnswerUnicodeCaseInsensitive() {
        // simulate lowercase with accents or unicode letters
        assertFalse(parameterizedCipher.checkAnswer("héllo")); // may reveal lack of Unicode normalization
    }

    @Test
    public void testCheckAnswerPartialMatch() {
        assertFalse(parameterizedCipher.checkAnswer("HELL"));
    }

    @Test
    public void testShiftAffectsCipherInternallyIfImplemented() {
        Cipher cipher = new Cipher("KHOOR", "HELLO", 3);
        cipher.setShift(5);
        // Even if shift changes, check if answer logic ignores it (to catch inconsistent logic)
        cipher.checkAnswer("HELLO");
    }
}

