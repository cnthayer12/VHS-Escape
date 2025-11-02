package com.model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestCipher {
    
    private Cipher defaultCipher;
    private Cipher parameterizedCipher;
    
    @Before
    public void setUp() {
        defaultCipher = new Cipher();
        parameterizedCipher = new Cipher("KHOOR", "HELLO", 3);
    }
    
    // Constructor Tests
    
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
    public void testParameterizedConstructorSetsCipherText() {
        assertEquals("KHOOR", parameterizedCipher.getCipherText());
    }
    
    @Test
    public void testParameterizedConstructorSetsCorrectAnswer() {
        assertEquals("HELLO", parameterizedCipher.getCorrectAnswer());
    }
    
    @Test
    public void testParameterizedConstructorSetsShift() {
        assertEquals(3, parameterizedCipher.getShift());
    }
    
    // checkAnswer Tests - Normal Cases
    
    @Test
    public void testCheckAnswerWithExactMatch() {
        assertTrue(parameterizedCipher.checkAnswer("HELLO"));
    }
    
    @Test
    public void testCheckAnswerWithLowercaseInput() {
        assertTrue(parameterizedCipher.checkAnswer("hello"));
    }
    
    @Test
    public void testCheckAnswerWithMixedCase() {
        assertTrue(parameterizedCipher.checkAnswer("HeLLo"));
    }
    
    @Test
    public void testCheckAnswerWithLeadingWhitespace() {
        assertTrue(parameterizedCipher.checkAnswer("  HELLO"));
    }
    
    @Test
    public void testCheckAnswerWithTrailingWhitespace() {
        assertTrue(parameterizedCipher.checkAnswer("HELLO  "));
    }
    
    @Test
    public void testCheckAnswerWithLeadingAndTrailingWhitespace() {
        assertTrue(parameterizedCipher.checkAnswer("  HELLO  "));
    }
    
    @Test
    public void testCheckAnswerWithIncorrectAnswer() {
        assertFalse(parameterizedCipher.checkAnswer("WORLD"));
    }
    
    @Test
    public void testCheckAnswerWithEmptyString() {
        assertFalse(parameterizedCipher.checkAnswer(""));
    }
    
    @Test
    public void testCheckAnswerWithEmptyCorrectAnswer() {
        Cipher cipher = new Cipher("XYZ", "", 1);
        assertTrue(cipher.checkAnswer(""));
    }
    
    @Test
    public void testCheckAnswerWithWhitespaceOnlyInput() {
        assertFalse(parameterizedCipher.checkAnswer("   "));
    }
    
    // checkAnswer Tests - Error Cases
    
    @Test
    public void testCheckAnswerWithNullInput() {
        assertFalse(parameterizedCipher.checkAnswer(null));
    }
    
    @Test
    public void testCheckAnswerWithNullCorrectAnswer() {
        Cipher cipher = new Cipher("ABC", null, 1);
        assertFalse(cipher.checkAnswer("HELLO"));
    }
    
    @Test
    public void testCheckAnswerWithBothNull() {
        Cipher cipher = new Cipher("ABC", null, 1);
        assertFalse(cipher.checkAnswer(null));
    }
    
    // toString Tests
    
    @Test
    public void testToStringWithCipherText() {
        assertEquals("Cipher Puzzle: KHOOR", parameterizedCipher.toString());
    }
    
    @Test
    public void testToStringWithEmptyCipherText() {
        assertEquals("Cipher Puzzle: ", defaultCipher.toString());
    }
    
    @Test
    public void testToStringWithNullCipherText() {
        defaultCipher.setCipherText(null);
        assertEquals("Cipher Puzzle: null", defaultCipher.toString());
    }
    
    // Integration Tests
    
    @Test
    public void testCompleteWorkflowWithValidData() {
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
    public void testCipherTextModificationDoesNotAffectAnswer() {
        parameterizedCipher.setCipherText("NEWCIPHER");
        assertTrue(parameterizedCipher.checkAnswer("HELLO"));
    }
    
    @Test
    public void testShiftModificationDoesNotAffectAnswerChecking() {
        parameterizedCipher.setShift(10);
        assertTrue(parameterizedCipher.checkAnswer("HELLO"));
    }
}
