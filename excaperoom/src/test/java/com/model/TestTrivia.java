package com.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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

    // --------------------------------------------------
    // Constructor / Initialization Behavior
    // --------------------------------------------------

    @Test
    public void testDefaultConstructorSetsType() {
        assertEquals("Trivia", defaultTrivia.getType());
    }

    @Test
    public void testCustomTriviaInitializationStoresValues() {
        assertEquals("What year was the VHS invented?", customTrivia.getTriviaText());
        assertEquals("1976", customTrivia.getCorrectAnswer());
    }

    // --------------------------------------------------
    // checkAnswer() - Correctness and Normalization
    // --------------------------------------------------

    @Test
    public void testCheckAnswerExactMatch() {
        assertTrue(customTrivia.checkAnswer("1976"));
    }

    @Test
    public void testCheckAnswerDifferentCase() {
        customTrivia.setCorrectAnswer("VHS");
        assertTrue(customTrivia.checkAnswer("vhs"));
    }

    @Test
    public void testCheckAnswerMixedCase() {
        customTrivia.setCorrectAnswer("VHS");
        assertTrue(customTrivia.checkAnswer("VhS"));
    }

    @Test
    public void testCheckAnswerLeadingWhitespace() {
        assertTrue(customTrivia.checkAnswer("   1976"));
    }

    @Test
    public void testCheckAnswerTrailingWhitespace() {
        assertTrue(customTrivia.checkAnswer("1976   "));
    }

    @Test
    public void testCheckAnswerLeadingAndTrailingWhitespace() {
        assertTrue(customTrivia.checkAnswer("   1976   "));
    }

    @Test
    public void testCheckAnswerTrimsAndIgnoresCaseTogether() {
        customTrivia.setCorrectAnswer("VHS");
        assertTrue(customTrivia.checkAnswer("   vHs   "));
    }

    @Test
    public void testCheckAnswerWithExtraInternalSpacesFails() {
        customTrivia.setCorrectAnswer("the vhs");
        assertFalse(customTrivia.checkAnswer("the   vhs"));
    }

    @Test
    public void testCheckAnswerPartialMatchFails() {
        customTrivia.setCorrectAnswer("VHS");
        assertFalse(customTrivia.checkAnswer("VHS tape"));
    }

    // --------------------------------------------------
    // checkAnswer() - Error and Edge Cases
    // --------------------------------------------------

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

    @Test
    public void testCheckAnswerWithSpecialCharacters() {
        customTrivia.setCorrectAnswer("VHS!");
        assertTrue(customTrivia.checkAnswer("vhs!"));
        assertFalse(customTrivia.checkAnswer("vhs"));
    }

    @Test
    public void testCheckAnswerWithNumericAnswerDifferentFormatting() {
        customTrivia.setCorrectAnswer("1,000");
        assertFalse(customTrivia.checkAnswer("1000"));  // commas matter
    }

    // --------------------------------------------------
    // toString() Behavior
    // --------------------------------------------------

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
        customTrivia.setTriviaText("This is a very long trivia question that tests string output consistency.");
        assertEquals("Trivia: This is a very long trivia question that tests string output consistency.",
                     customTrivia.toString());
    }

    @Test
    public void testToStringIncludesTypePrefix() {
        assertTrue(customTrivia.toString().startsWith("Trivia:"));
    }

    // --------------------------------------------------
    // Integration / Full Workflow Tests
    // --------------------------------------------------

    @Test
    public void testCompleteTriviaWorkflow() {
        Trivia trivia = new Trivia();
        trivia.setTriviaText("What color is the sky?");
        trivia.setCorrectAnswer("blue");

        // Validate stored state
        assertEquals("Trivia", trivia.getType());

        // Validate behavior flow
        assertFalse(trivia.checkAnswer("red"));
        assertTrue(trivia.checkAnswer("BLUE"));
        assertTrue(trivia.checkAnswer("  Blue  "));
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

    @Test
    public void testTriviaHandlesSequentialAnswerChecks() {
        customTrivia.setCorrectAnswer("1976");
        assertTrue(customTrivia.checkAnswer("1976"));
        assertFalse(customTrivia.checkAnswer("wrong"));
        assertTrue(customTrivia.checkAnswer("1976"));
    }

    @Test
    public void testTriviaMaintainsDataIntegrityAfterChecks() {
        String textBefore = customTrivia.getTriviaText();
        String answerBefore = customTrivia.getCorrectAnswer();

        customTrivia.checkAnswer("1976");
        customTrivia.checkAnswer("wrong");

        assertEquals(textBefore, customTrivia.getTriviaText());
        assertEquals(answerBefore, customTrivia.getCorrectAnswer());
    }
}

