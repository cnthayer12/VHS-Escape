package com.model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestTrivia {
    
    private Trivia defaultTrivia;
    private Trivia customTrivia;
    
    @Before
    public void setUp() {
        defaultTrivia = new Trivia();
        customTrivia = new Trivia();
        customTrivia.setTriviaText("What year was the VHS invented?");
        customTrivia.setCorrectAnswer("1976");
    }
    
    // Constructor Tests
    
    @Test
    public void testDefaultConstructorInitializesTriviaText() {
        assertEquals("", defaultTrivia.getTriviaText());
    }
    
    @Test
    public void testDefaultConstructorInitializesCorrectAnswer() {
        assertEquals("", defaultTrivia.getCorrectAnswer());
    }
    
    @Test
    public void testDefaultConstructorSetsType() {
        assertEquals("Trivia", defaultTrivia.getType());
    }
    
    // checkAnswer Tests - Normal Cases
    
    @Test
    public void testCheckAnswerWithCorrectAnswer() {
        assertTrue(customTrivia.checkAnswer("1976"));
    }
    
    @Test
    public void testCheckAnswerWithCorrectAnswerDifferentCase() {
        customTrivia.setCorrectAnswer("VHS");
        assertTrue(customTrivia.checkAnswer("vhs"));
    }
    
    @Test
    public void testCheckAnswerWithCorrectAnswerMixedCase() {
        customTrivia.setCorrectAnswer("VHS");
        assertTrue(customTrivia.checkAnswer("VhS"));
    }
    
    @Test
    public void testCheckAnswerWithLeadingWhitespace() {
        assertTrue(customTrivia.checkAnswer("  1976"));
    }
    
    @Test
    public void testCheckAnswerWithTrailingWhitespace() {
        assertTrue(customTrivia.checkAnswer("1976  "));
    }
    
    @Test
    public void testCheckAnswerWithLeadingAndTrailingWhitespace() {
        assertTrue(customTrivia.checkAnswer("  1976  "));
    }
    
    @Test
    public void testCheckAnswerWithIncorrectAnswer() {
        assertFalse(customTrivia.checkAnswer("1980"));
    }
    
    @Test
    public void testCheckAnswerWithCompletelyWrongAnswer() {
        assertFalse(customTrivia.checkAnswer("wrong"));
    }
    
    // checkAnswer Tests - Error Cases
    
    @Test
    public void testCheckAnswerWithNullInput() {
        assertFalse(customTrivia.checkAnswer(null));
    }
    
    @Test
    public void testCheckAnswerWithEmptyString() {
        assertFalse(customTrivia.checkAnswer(""));
    }
    
    @Test
    public void testCheckAnswerWithWhitespaceOnly() {
        assertFalse(customTrivia.checkAnswer("   "));
    }
    
    @Test
    public void testCheckAnswerWithEmptyCorrectAnswer() {
        Trivia trivia = new Trivia();
        trivia.setCorrectAnswer("");
        assertFalse(trivia.checkAnswer("anything"));
    }
    
    // toString Tests
    
    @Test
    public void testToStringWithTriviaText() {
        assertEquals("Trivia: What year was the VHS invented?", customTrivia.toString());
    }
    
    @Test
    public void testToStringWithEmptyTriviaText() {
        assertEquals("Trivia: ", defaultTrivia.toString());
    }
    
    @Test
    public void testToStringWithLongTriviaText() {
        customTrivia.setTriviaText("This is a very long trivia question with lots of text");
        assertEquals("Trivia: This is a very long trivia question with lots of text", customTrivia.toString());
    }
    
    // Integration Tests
    
    @Test
    public void testCompleteTriviaWorkflow() {
        Trivia trivia = new Trivia();
        trivia.setTriviaText("What color is the sky?");
        trivia.setCorrectAnswer("blue");
        
        assertEquals("What color is the sky?", trivia.getTriviaText());
        assertEquals("blue", trivia.getCorrectAnswer());
        assertEquals("Trivia", trivia.getType());
        
        assertFalse(trivia.checkAnswer("red"));
        assertTrue(trivia.checkAnswer("BLUE"));
    }
    
    @Test
    public void testTriviaWithNumericAnswer() {
        Trivia trivia = new Trivia();
        trivia.setTriviaText("How many planets are in our solar system?");
        trivia.setCorrectAnswer("8");
        
        assertTrue(trivia.checkAnswer("8"));
        assertFalse(trivia.checkAnswer("9"));
    }
    
    @Test
    public void testTriviaAnswerCaseSensitivity() {
        customTrivia.setCorrectAnswer("AbCdEf");
        assertTrue(customTrivia.checkAnswer("ABCDEF"));
        assertTrue(customTrivia.checkAnswer("abcdef"));
        assertTrue(customTrivia.checkAnswer("AbCdEf"));
    }
}
