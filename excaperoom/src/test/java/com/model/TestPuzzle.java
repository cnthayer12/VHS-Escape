package com.model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class TestPuzzle {
    
    private Puzzle defaultPuzzle;
    private Puzzle puzzleWithHints;
    
    @Before
    public void setUp() {
        defaultPuzzle = new Puzzle();
        puzzleWithHints = new Puzzle();
        puzzleWithHints.addHint(new Hint("First hint", 10));
        puzzleWithHints.addHint(new Hint("Second hint", 15));
    }
    
    // Constructor Tests
    
    @Test
    public void testDefaultConstructorInitializesHintsAsEmptyList() {
        assertNotNull(defaultPuzzle.getHints());
        assertEquals(0, defaultPuzzle.getHints().size());
    }
    
    @Test
    public void testDefaultConstructorInitializesType() {
        assertEquals("", defaultPuzzle.getType());
    }
    
    @Test
    public void testDefaultConstructorInitializesPuzzleID() {
        assertNotNull(defaultPuzzle.getID());
    }
    
    @Test
    public void testDefaultConstructorCreatesUniqueIDs() {
        Puzzle puzzle1 = new Puzzle();
        Puzzle puzzle2 = new Puzzle();
        assertFalse(puzzle1.getID().equals(puzzle2.getID()));
    }
    
    // addHint Tests
    
    @Test
    public void testAddHintWithValidHint() {
        Hint hint = new Hint("Test hint", 5);
        defaultPuzzle.addHint(hint);
        assertEquals(1, defaultPuzzle.getHints().size());
        assertEquals(hint, defaultPuzzle.getHints().get(0));
    }
    
    @Test
    public void testAddHintWithMultipleHints() {
        Hint hint1 = new Hint("Hint 1", 5);
        Hint hint2 = new Hint("Hint 2", 10);
        defaultPuzzle.addHint(hint1);
        defaultPuzzle.addHint(hint2);
        assertEquals(2, defaultPuzzle.getHints().size());
    }
    
    @Test
    public void testAddHintWithNullHint() {
        int initialSize = defaultPuzzle.getHints().size();
        defaultPuzzle.addHint(null);
        assertEquals(initialSize, defaultPuzzle.getHints().size());
    }
    
    @Test
    public void testAddHintToExistingHints() {
        int initialSize = puzzleWithHints.getHints().size();
        Hint newHint = new Hint("Third hint", 20);
        puzzleWithHints.addHint(newHint);
        assertEquals(initialSize + 1, puzzleWithHints.getHints().size());
    }
    
    // getHints Tests
    
    @Test
    public void testGetHintsReturnsCorrectList() {
        assertEquals(2, puzzleWithHints.getHints().size());
    }
    
    @Test
    public void testGetHintsReturnsEmptyListForNewPuzzle() {
        assertEquals(0, defaultPuzzle.getHints().size());
    }
    
    // setHints Tests
    
    @Test
    public void testSetHintsWithNewList() {
        java.util.ArrayList<Hint> newHints = new java.util.ArrayList<>();
        newHints.add(new Hint("New hint 1", 5));
        newHints.add(new Hint("New hint 2", 10));
        
        defaultPuzzle.setHints(newHints);
        assertEquals(2, defaultPuzzle.getHints().size());
    }
    
    @Test
    public void testSetHintsWithEmptyList() {
        java.util.ArrayList<Hint> emptyHints = new java.util.ArrayList<>();
        puzzleWithHints.setHints(emptyHints);
        assertEquals(0, puzzleWithHints.getHints().size());
    }
    
    @Test
    public void testSetHintsReplacesExistingHints() {
        java.util.ArrayList<Hint> newHints = new java.util.ArrayList<>();
        newHints.add(new Hint("Replacement hint", 25));
        
        puzzleWithHints.setHints(newHints);
        assertEquals(1, puzzleWithHints.getHints().size());
        assertEquals("Replacement hint", puzzleWithHints.getHints().get(0).getText());
    }
    
    // setType and getType Tests
    
    @Test
    public void testSetTypeWithValidString() {
        defaultPuzzle.setType("Trivia");
        assertEquals("Trivia", defaultPuzzle.getType());
    }
    
    @Test
    public void testSetTypeWithDifferentTypes() {
        defaultPuzzle.setType("Riddle");
        assertEquals("Riddle", defaultPuzzle.getType());
        
        defaultPuzzle.setType("MultipleChoice");
        assertEquals("MultipleChoice", defaultPuzzle.getType());
    }
    
    @Test
    public void testSetTypeWithEmptyString() {
        defaultPuzzle.setType("");
        assertEquals("", defaultPuzzle.getType());
    }
    
    // checkAnswer Tests
    
    @Test
    public void testCheckAnswerReturnsFalseByDefault() {
        assertFalse(defaultPuzzle.checkAnswer("any answer"));
    }
    
    @Test
    public void testCheckAnswerWithNullInput() {
        assertFalse(defaultPuzzle.checkAnswer(null));
    }
    
    @Test
    public void testCheckAnswerWithEmptyString() {
        assertFalse(defaultPuzzle.checkAnswer(""));
    }
    
    // toString Tests
    
    @Test
    public void testToStringWithEmptyType() {
        String result = defaultPuzzle.toString();
        assertEquals("", result);
    }
    
    @Test
    public void testToStringWithTriviaType() {
        Trivia trivia = new Trivia();
        trivia.setTriviaText("Test question");
        String result = trivia.toString();
        assertEquals("Trivia: Test question", result);
    }
    
    // Integration Tests
    
    @Test
    public void testPuzzleWithMultipleHintsWorkflow() {
        Puzzle puzzle = new Puzzle();
        puzzle.setType("TestPuzzle");
        
        Hint hint1 = new Hint("First clue", 5);
        Hint hint2 = new Hint("Second clue", 10);
        Hint hint3 = new Hint("Third clue", 15);
        
        puzzle.addHint(hint1);
        puzzle.addHint(hint2);
        puzzle.addHint(hint3);
        
        assertEquals(3, puzzle.getHints().size());
        assertEquals("TestPuzzle", puzzle.getType());
        assertEquals("First clue", puzzle.getHints().get(0).getText());
        assertEquals("Third clue", puzzle.getHints().get(2).getText());
    }
    
    @Test
    public void testPuzzleIDUniqueness() {
        Puzzle puzzle1 = new Puzzle();
        Puzzle puzzle2 = new Puzzle();
        Puzzle puzzle3 = new Puzzle();
        
        assertFalse(puzzle1.getID().equals(puzzle2.getID()));
        assertFalse(puzzle2.getID().equals(puzzle3.getID()));
        assertFalse(puzzle1.getID().equals(puzzle3.getID()));
    }
    
    @Test
    public void testHintAssociationWithPuzzle() {
        Puzzle puzzle = new Puzzle();
        Hint hint = new Hint("Associated hint", 10);
        hint.setPuzzle(puzzle);
        
        puzzle.addHint(hint);
        
        assertEquals(1, puzzle.getHints().size());
        assertEquals(puzzle, puzzle.getHints().get(0).getPuzzle());
    }
}
