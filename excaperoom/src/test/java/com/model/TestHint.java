package com.model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class TestHint {
    
    private Hint defaultHint;
    private Hint parameterizedHint;
    private Hint hintWithPuzzle;
    private Puzzle testPuzzle;
    
    @Before
    public void setUp() {
        defaultHint = new Hint();
        parameterizedHint = new Hint("This is a helpful hint", 15);
        testPuzzle = new Trivia();
        hintWithPuzzle = new Hint("Hint with puzzle", 20, testPuzzle);
    }
    
    // Constructor Tests
    
    @Test
    public void testDefaultConstructorInitializesId() {
        assertEquals("0", defaultHint.getId());
    }
    
    @Test
    public void testDefaultConstructorInitializesText() {
        assertEquals("", defaultHint.getText());
    }
    
    @Test
    public void testDefaultConstructorInitializesUsed() {
        assertFalse(defaultHint.isUsed());
    }
    
    @Test
    public void testDefaultConstructorInitializesCost() {
        assertEquals(10, defaultHint.getCost());
    }
    
    @Test
    public void testDefaultConstructorInitializesPuzzleAsNull() {
        assertNull(defaultHint.getPuzzle());
    }
    
    @Test
    public void testParameterizedConstructorSetsText() {
        assertEquals("This is a helpful hint", parameterizedHint.getText());
    }
    
    @Test
    public void testParameterizedConstructorSetsCost() {
        assertEquals(15, parameterizedHint.getCost());
    }
    
    @Test
    public void testParameterizedConstructorSetsUsedToFalse() {
        assertFalse(parameterizedHint.isUsed());
    }
    
    @Test
    public void testThreeParameterConstructorSetsPuzzle() {
        assertEquals(testPuzzle, hintWithPuzzle.getPuzzle());
    }
    
    @Test
    public void testThreeParameterConstructorSetsText() {
        assertEquals("Hint with puzzle", hintWithPuzzle.getText());
    }
    
    @Test
    public void testThreeParameterConstructorSetsCost() {
        assertEquals(20, hintWithPuzzle.getCost());
    }
    
    // Constructor Edge Cases
    
    @Test
    public void testConstructorWithNullText() {
        Hint hint = new Hint(null, 10);
        assertNull(hint.getText());
    }
    
    @Test
    public void testConstructorWithEmptyText() {
        Hint hint = new Hint("", 10);
        assertEquals("", hint.getText());
    }
    
    @Test
    public void testConstructorWithNegativeCost() {
        Hint hint = new Hint("Test", -5);
        assertEquals(-5, hint.getCost());
    }
    
    @Test
    public void testConstructorWithZeroCost() {
        Hint hint = new Hint("Free hint", 0);
        assertEquals(0, hint.getCost());
    }
    
    @Test
    public void testConstructorWithVeryLargeCost() {
        Hint hint = new Hint("Expensive", 999999);
        assertEquals(999999, hint.getCost());
    }
    
    @Test
    public void testConstructorWithNullPuzzle() {
        Hint hint = new Hint("Text", 10, null);
        assertNull(hint.getPuzzle());
    }
    
    @Test
    public void testConstructorWithWhitespaceText() {
        Hint hint = new Hint("   ", 10);
        assertEquals("   ", hint.getText());
    }
    
    @Test
    public void testConstructorWithSpecialCharacters() {
        Hint hint = new Hint("!@#$%^&*()", 10);
        assertEquals("!@#$%^&*()", hint.getText());
    }
    
    @Test
    public void testConstructorWithVeryLongText() {
        String longText = "A".repeat(1000);
        Hint hint = new Hint(longText, 10);
        assertEquals(longText, hint.getText());
    }
    
    // revealHint Tests
    
    @Test
    public void testRevealHintReturnsText() {
        String result = parameterizedHint.revealHint();
        assertEquals("This is a helpful hint", result);
    }
    
    @Test
    public void testRevealHintMarksAsUsed() {
        parameterizedHint.revealHint();
        assertTrue(parameterizedHint.isUsed());
    }
    
    @Test
    public void testRevealHintOnAlreadyUsedHint() {
        parameterizedHint.revealHint();
        String result = parameterizedHint.revealHint();
        assertEquals("This is a helpful hint", result);
        assertTrue(parameterizedHint.isUsed());
    }
    
    @Test
    public void testRevealHintWithEmptyText() {
        String result = defaultHint.revealHint();
        assertEquals("", result);
    }
    
    @Test
    public void testRevealHintWithNullText() {
        Hint hint = new Hint(null, 10);
        String result = hint.revealHint();
        assertNull(result);
    }
    
    @Test
    public void testRevealHintDoesNotChangeText() {
        String originalText = parameterizedHint.getText();
        parameterizedHint.revealHint();
        assertEquals(originalText, parameterizedHint.getText());
    }
    
    @Test
    public void testRevealHintDoesNotChangeCost() {
        int originalCost = parameterizedHint.getCost();
        parameterizedHint.revealHint();
        assertEquals(originalCost, parameterizedHint.getCost());
    }
    
    @Test
    public void testRevealHintMultipleTimes() {
        String first = parameterizedHint.revealHint();
        String second = parameterizedHint.revealHint();
        String third = parameterizedHint.revealHint();
        assertEquals(first, second);
        assertEquals(second, third);
    }
    
    // markUsed Tests
    
    @Test
    public void testMarkUsedChangesUsedToTrue() {
        assertFalse(parameterizedHint.isUsed());
        parameterizedHint.markUsed();
        assertTrue(parameterizedHint.isUsed());
    }
    
    @Test
    public void testMarkUsedOnAlreadyUsedHint() {
        parameterizedHint.markUsed();
        parameterizedHint.markUsed();
        assertTrue(parameterizedHint.isUsed());
    }
    
    @Test
    public void testMarkUsedDoesNotChangeText() {
        String originalText = parameterizedHint.getText();
        parameterizedHint.markUsed();
        assertEquals(originalText, parameterizedHint.getText());
    }
    
    @Test
    public void testMarkUsedDoesNotChangeCost() {
        int originalCost = parameterizedHint.getCost();
        parameterizedHint.markUsed();
        assertEquals(originalCost, parameterizedHint.getCost());
    }
    
    @Test
    public void testMarkUsedDoesNotChangePuzzle() {
        Puzzle originalPuzzle = hintWithPuzzle.getPuzzle();
        hintWithPuzzle.markUsed();
        assertEquals(originalPuzzle, hintWithPuzzle.getPuzzle());
    }
    
    // reset Tests
    
    @Test
    public void testResetChangesUsedToFalse() {
        parameterizedHint.markUsed();
        assertTrue(parameterizedHint.isUsed());
        parameterizedHint.reset();
        assertFalse(parameterizedHint.isUsed());
    }
    
    @Test
    public void testResetOnUnusedHint() {
        assertFalse(parameterizedHint.isUsed());
        parameterizedHint.reset();
        assertFalse(parameterizedHint.isUsed());
    }
    
    @Test
    public void testResetDoesNotChangeText() {
        String originalText = parameterizedHint.getText();
        parameterizedHint.markUsed();
        parameterizedHint.reset();
        assertEquals(originalText, parameterizedHint.getText());
    }
    
    @Test
    public void testResetDoesNotChangeCost() {
        int originalCost = parameterizedHint.getCost();
        parameterizedHint.markUsed();
        parameterizedHint.reset();
        assertEquals(originalCost, parameterizedHint.getCost());
    }
    
    @Test
    public void testResetDoesNotChangePuzzle() {
        Puzzle originalPuzzle = hintWithPuzzle.getPuzzle();
        hintWithPuzzle.markUsed();
        hintWithPuzzle.reset();
        assertEquals(originalPuzzle, hintWithPuzzle.getPuzzle());
    }
    
    @Test
    public void testResetAfterRevealHint() {
        parameterizedHint.revealHint();
        assertTrue(parameterizedHint.isUsed());
        parameterizedHint.reset();
        assertFalse(parameterizedHint.isUsed());
    }
    
    @Test
    public void testMultipleResetCalls() {
        parameterizedHint.markUsed();
        parameterizedHint.reset();
        parameterizedHint.reset();
        parameterizedHint.reset();
        assertFalse(parameterizedHint.isUsed());
    }
    
    // setPuzzle and getPuzzle Tests
    
    @Test
    public void testSetPuzzleWithValidPuzzle() {
        defaultHint.setPuzzle(testPuzzle);
        assertEquals(testPuzzle, defaultHint.getPuzzle());
    }
    
    @Test
    public void testSetPuzzleWithNull() {
        hintWithPuzzle.setPuzzle(null);
        assertNull(hintWithPuzzle.getPuzzle());
    }
    
    @Test
    public void testSetPuzzleReplacesExistingPuzzle() {
        Puzzle newPuzzle = new Trivia();
        Puzzle oldPuzzle = hintWithPuzzle.getPuzzle();
        hintWithPuzzle.setPuzzle(newPuzzle);
        assertEquals(newPuzzle, hintWithPuzzle.getPuzzle());
        assertFalse(oldPuzzle == hintWithPuzzle.getPuzzle());
    }
    
    @Test
    public void testSetPuzzleMultipleTimes() {
        Puzzle puzzle1 = new Trivia();
        Puzzle puzzle2 = new Trivia();
        Puzzle puzzle3 = new Trivia();
        
        defaultHint.setPuzzle(puzzle1);
        defaultHint.setPuzzle(puzzle2);
        defaultHint.setPuzzle(puzzle3);
        
        assertEquals(puzzle3, defaultHint.getPuzzle());
    }
    
    // setText Tests
    
    @Test
    public void testSetTextWithValidString() {
        defaultHint.setText("New hint text");
        assertEquals("New hint text", defaultHint.getText());
    }
    
    @Test
    public void testSetTextWithEmptyString() {
        parameterizedHint.setText("");
        assertEquals("", parameterizedHint.getText());
    }
    
    @Test
    public void testSetTextWithNull() {
        parameterizedHint.setText(null);
        assertNull(parameterizedHint.getText());
    }
    
    @Test
    public void testSetTextReplacesExistingText() {
        String oldText = parameterizedHint.getText();
        parameterizedHint.setText("Completely different");
        assertFalse(oldText.equals(parameterizedHint.getText()));
    }
    
    @Test
    public void testSetTextWithSpecialCharacters() {
        defaultHint.setText("!@#$%^&*()_+-=[]{}|;:',.<>?/~`");
        assertEquals("!@#$%^&*()_+-=[]{}|;:',.<>?/~`", defaultHint.getText());
    }
    
    @Test
    public void testSetTextWithUnicode() {
        defaultHint.setText("日本語 한국어 中文");
        assertEquals("日本語 한국어 中文", defaultHint.getText());
    }
    
    @Test
    public void testSetTextWithNewlines() {
        defaultHint.setText("Line 1\nLine 2\nLine 3");
        assertEquals("Line 1\nLine 2\nLine 3", defaultHint.getText());
    }
    
    // setCost Tests
    
    @Test
    public void testSetCostWithPositiveValue() {
        defaultHint.setCost(25);
        assertEquals(25, defaultHint.getCost());
    }
    
    @Test
    public void testSetCostWithZero() {
        parameterizedHint.setCost(0);
        assertEquals(0, parameterizedHint.getCost());
    }
    
    @Test
    public void testSetCostWithNegativeValue() {
        parameterizedHint.setCost(-10);
        assertEquals(-10, parameterizedHint.getCost());
    }
    
    @Test
    public void testSetCostWithVeryLargeValue() {
        defaultHint.setCost(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, defaultHint.getCost());
    }
    
    @Test
    public void testSetCostWithVerySmallValue() {
        defaultHint.setCost(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, defaultHint.getCost());
    }
    
    @Test
    public void testSetCostReplacesExistingCost() {
        int oldCost = parameterizedHint.getCost();
        parameterizedHint.setCost(100);
        assertFalse(oldCost == parameterizedHint.getCost());
    }
    
    // setId Tests
    
    @Test
    public void testSetIdWithValidString() {
        defaultHint.setId("hint-123");
        assertEquals("hint-123", defaultHint.getId());
    }
    
    @Test
    public void testSetIdWithEmptyString() {
        parameterizedHint.setId("");
        assertEquals("", parameterizedHint.getId());
    }
    
    @Test
    public void testSetIdWithNull() {
        parameterizedHint.setId(null);
        assertNull(parameterizedHint.getId());
    }
    
    @Test
    public void testSetIdWithNumericString() {
        defaultHint.setId("12345");
        assertEquals("12345", defaultHint.getId());
    }
    
    @Test
    public void testSetIdReplacesExistingId() {
        String oldId = parameterizedHint.getId();
        parameterizedHint.setId("new-id");
        assertFalse(oldId.equals(parameterizedHint.getId()));
    }
    
    // toString Tests
    
    @Test
    public void testToStringWithDefaultHint() {
        String result = defaultHint.toString();
        assertEquals("[Hint: 0, used=false, cost=10]", result);
    }
    
    @Test
    public void testToStringWithUsedHint() {
        parameterizedHint.markUsed();
        String result = parameterizedHint.toString();
        assertEquals("[Hint: 0, used=true, cost=15]", result);
    }
    
    @Test
    public void testToStringWithCustomId() {
        parameterizedHint.setId("hint-123");
        String result = parameterizedHint.toString();
        assertEquals("[Hint: hint-123, used=false, cost=15]", result);
    }
    
    @Test
    public void testToStringWithNegativeCost() {
        Hint hint = new Hint("Test", -5);
        String result = hint.toString();
        assertEquals("[Hint: 0, used=false, cost=-5]", result);
    }
    
    @Test
    public void testToStringWithZeroCost() {
        Hint hint = new Hint("Test", 0);
        String result = hint.toString();
        assertEquals("[Hint: 0, used=false, cost=0]", result);
    }
    
    @Test
    public void testToStringAfterMultipleChanges() {
        defaultHint.setId("custom-123");
        defaultHint.setCost(50);
        defaultHint.markUsed();
        String result = defaultHint.toString();
        assertEquals("[Hint: custom-123, used=true, cost=50]", result);
    }
    
    @Test
    public void testToStringWithNullId() {
        parameterizedHint.setId(null);
        String result = parameterizedHint.toString();
        assertEquals("[Hint: null, used=false, cost=15]", result);
    }
    
    // Integration Tests
    
    @Test
    public void testCompleteHintLifecycle() {
        Hint hint = new Hint("Look at the clock", 10);
        assertFalse(hint.isUsed());
        
        String revealed = hint.revealHint();
        assertEquals("Look at the clock", revealed);
        assertTrue(hint.isUsed());
        
        hint.reset();
        assertFalse(hint.isUsed());
    }
    
    @Test
    public void testHintWithPuzzleAssociation() {
        Hint hint = new Hint("Try the red button", 5);
        assertNull(hint.getPuzzle());
        
        Puzzle puzzle = new Trivia();
        hint.setPuzzle(puzzle);
        assertNotNull(hint.getPuzzle());
        assertEquals(puzzle, hint.getPuzzle());
    }
    
    @Test
    public void testHintModificationDoesNotAffectOtherHints() {
        Hint hint1 = new Hint("Hint 1", 10);
        Hint hint2 = new Hint("Hint 2", 20);
        
        hint1.markUsed();
        hint1.setCost(50);
        hint1.setText("Modified");
        
        assertEquals("Hint 2", hint2.getText());
        assertEquals(20, hint2.getCost());
        assertFalse(hint2.isUsed());
    }
    
    @Test
    public void testHintStateAfterMultipleOperations() {
        Hint hint = new Hint("Original", 15);
        
        hint.setText("Modified");
        hint.setCost(30);
        hint.setId("custom-id");
        hint.markUsed();
        hint.reset();
        hint.markUsed();
        
        assertEquals("Modified", hint.getText());
        assertEquals(30, hint.getCost());
        assertEquals("custom-id", hint.getId());
        assertTrue(hint.isUsed());
    }
    
    @Test
    public void testUsedStateTransitionsCorrectly() {
        assertFalse(defaultHint.isUsed());
        
        defaultHint.markUsed();
        assertTrue(defaultHint.isUsed());
        
        defaultHint.reset();
        assertFalse(defaultHint.isUsed());
        
        defaultHint.revealHint();
        assertTrue(defaultHint.isUsed());
        
        defaultHint.reset();
        assertFalse(defaultHint.isUsed());
    }
}
