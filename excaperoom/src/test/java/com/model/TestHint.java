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
}
