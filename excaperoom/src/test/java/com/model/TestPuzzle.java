package com.model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import java.util.ArrayList;
import java.util.UUID;

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
    
    @Test
    public void testDefaultConstructorCreatesNonNullHintsList() {
        Puzzle puzzle = new Puzzle();
        assertNotNull(puzzle.getHints());
    }
    
    @Test
    public void testDefaultConstructorHintsListIsMutable() {
        Puzzle puzzle = new Puzzle();
        puzzle.getHints().add(new Hint("Test", 10));
        assertEquals(1, puzzle.getHints().size());
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
    
    @Test
    public void testAddHintMaintainsOrder() {
        Hint hint1 = new Hint("First", 5);
        Hint hint2 = new Hint("Second", 10);
        Hint hint3 = new Hint("Third", 15);
        
        defaultPuzzle.addHint(hint1);
        defaultPuzzle.addHint(hint2);
        defaultPuzzle.addHint(hint3);
        
        assertEquals(hint1, defaultPuzzle.getHints().get(0));
        assertEquals(hint2, defaultPuzzle.getHints().get(1));
        assertEquals(hint3, defaultPuzzle.getHints().get(2));
    }
    
    @Test
    public void testAddHintWithSameHintMultipleTimes() {
        Hint hint = new Hint("Duplicate", 10);
        defaultPuzzle.addHint(hint);
        defaultPuzzle.addHint(hint);
        assertEquals(2, defaultPuzzle.getHints().size());
        assertSame(hint, defaultPuzzle.getHints().get(0));
        assertSame(hint, defaultPuzzle.getHints().get(1));
    }
    
    @Test
    public void testAddHintWithZeroCost() {
        Hint hint = new Hint("Free hint", 0);
        defaultPuzzle.addHint(hint);
        assertEquals(1, defaultPuzzle.getHints().size());
        assertEquals(0, defaultPuzzle.getHints().get(0).getCost());
    }
    
    @Test
    public void testAddHintWithNegativeCost() {
        Hint hint = new Hint("Negative cost", -10);
        defaultPuzzle.addHint(hint);
        assertEquals(1, defaultPuzzle.getHints().size());
        assertEquals(-10, defaultPuzzle.getHints().get(0).getCost());
    }
    
    @Test
    public void testAddHintWithEmptyText() {
        Hint hint = new Hint("", 10);
        defaultPuzzle.addHint(hint);
        assertEquals(1, defaultPuzzle.getHints().size());
        assertEquals("", defaultPuzzle.getHints().get(0).getText());
    }
    
    @Test
    public void testAddHintWithNullText() {
        Hint hint = new Hint(null, 10);
        defaultPuzzle.addHint(hint);
        assertEquals(1, defaultPuzzle.getHints().size());
        assertNull(defaultPuzzle.getHints().get(0).getText());
    }
    
    @Test
    public void testAddManyHints() {
        for (int i = 0; i < 100; i++) {
            defaultPuzzle.addHint(new Hint("Hint " + i, i));
        }
        assertEquals(100, defaultPuzzle.getHints().size());
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
    
    @Test
    public void testGetHintsReturnsModifiableList() {
        ArrayList<Hint> hints = defaultPuzzle.getHints();
        hints.add(new Hint("Direct add", 10));
        assertEquals(1, defaultPuzzle.getHints().size());
    }
    
    @Test
    public void testGetHintsReturnsSameListReference() {
        ArrayList<Hint> hints1 = defaultPuzzle.getHints();
        ArrayList<Hint> hints2 = defaultPuzzle.getHints();
        assertSame(hints1, hints2);
    }
    
    // setHints Tests
    
    @Test
    public void testSetHintsWithNewList() {
        ArrayList<Hint> newHints = new ArrayList<>();
        newHints.add(new Hint("New hint 1", 5));
        newHints.add(new Hint("New hint 2", 10));
        
        defaultPuzzle.setHints(newHints);
        assertEquals(2, defaultPuzzle.getHints().size());
    }
    
    @Test
    public void testSetHintsWithEmptyList() {
        ArrayList<Hint> emptyHints = new ArrayList<>();
        puzzleWithHints.setHints(emptyHints);
        assertEquals(0, puzzleWithHints.getHints().size());
    }
    
    @Test
    public void testSetHintsReplacesExistingHints() {
        ArrayList<Hint> newHints = new ArrayList<>();
        newHints.add(new Hint("Replacement hint", 25));
        
        puzzleWithHints.setHints(newHints);
        assertEquals(1, puzzleWithHints.getHints().size());
        assertEquals("Replacement hint", puzzleWithHints.getHints().get(0).getText());
    }
    
    @Test
    public void testSetHintsWithNull() {
        puzzleWithHints.setHints(null);
        assertNull(puzzleWithHints.getHints());
    }
    
    @Test
    public void testSetHintsWithListContainingNulls() {
        ArrayList<Hint> hintsWithNulls = new ArrayList<>();
        hintsWithNulls.add(new Hint("Valid", 10));
        hintsWithNulls.add(null);
        hintsWithNulls.add(new Hint("Also valid", 15));
        
        defaultPuzzle.setHints(hintsWithNulls);
        assertEquals(3, defaultPuzzle.getHints().size());
        assertNotNull(defaultPuzzle.getHints().get(0));
        assertNull(defaultPuzzle.getHints().get(1));
        assertNotNull(defaultPuzzle.getHints().get(2));
    }
    
    @Test
    public void testSetHintsMultipleTimes() {
        ArrayList<Hint> hints1 = new ArrayList<>();
        hints1.add(new Hint("First set", 10));
        
        ArrayList<Hint> hints2 = new ArrayList<>();
        hints2.add(new Hint("Second set", 20));
        hints2.add(new Hint("Second set item 2", 25));
        
        defaultPuzzle.setHints(hints1);
        assertEquals(1, defaultPuzzle.getHints().size());
        
        defaultPuzzle.setHints(hints2);
        assertEquals(2, defaultPuzzle.getHints().size());
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
    
    @Test
    public void testSetTypeWithNull() {
        defaultPuzzle.setType(null);
        assertNull(defaultPuzzle.getType());
    }
    
    @Test
    public void testSetTypeWithWhitespace() {
        defaultPuzzle.setType("   ");
        assertEquals("   ", defaultPuzzle.getType());
    }
    
    @Test
    public void testSetTypeWithSpecialCharacters() {
        defaultPuzzle.setType("Type!@#$%");
        assertEquals("Type!@#$%", defaultPuzzle.getType());
    }
    
    @Test
    public void testSetTypeReplacesExistingType() {
        defaultPuzzle.setType("Original");
        defaultPuzzle.setType("Replaced");
        assertEquals("Replaced", defaultPuzzle.getType());
    }
    
    @Test
    public void testSetTypeWithVeryLongString() {
        String longType = "A".repeat(1000);
        defaultPuzzle.setType(longType);
        assertEquals(longType, defaultPuzzle.getType());
    }
    
    @Test
    public void testSetTypeCaseSensitive() {
        defaultPuzzle.setType("Trivia");
        assertFalse("trivia".equals(defaultPuzzle.getType()));
        assertFalse("TRIVIA".equals(defaultPuzzle.getType()));
    }
    
    // setID and getID Tests
    
    @Test
    public void testSetIDWithValidUUID() {
        UUID newId = UUID.randomUUID();
        defaultPuzzle.setID(newId);
        assertEquals(newId, defaultPuzzle.getID());
    }
    
    @Test
    public void testSetIDWithNull() {
        defaultPuzzle.setID(null);
        assertNull(defaultPuzzle.getID());
    }
    
    @Test
    public void testSetIDReplacesExistingID() {
        UUID originalId = defaultPuzzle.getID();
        UUID newId = UUID.randomUUID();
        defaultPuzzle.setID(newId);
        assertNotSame(originalId, defaultPuzzle.getID());
        assertEquals(newId, defaultPuzzle.getID());
    }
    
    @Test
    public void testSetIDMultipleTimes() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();
        
        defaultPuzzle.setID(id1);
        defaultPuzzle.setID(id2);
        defaultPuzzle.setID(id3);
        
        assertEquals(id3, defaultPuzzle.getID());
    }
    
    @Test
    public void testGetIDReturnsConsistentValue() {
        UUID id1 = defaultPuzzle.getID();
        UUID id2 = defaultPuzzle.getID();
        assertEquals(id1, id2);
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
    
    @Test
    public void testCheckAnswerWithWhitespace() {
        assertFalse(defaultPuzzle.checkAnswer("   "));
    }
    
    @Test
    public void testCheckAnswerWithSpecialCharacters() {
        assertFalse(defaultPuzzle.checkAnswer("!@#$%^&*()"));
    }
    
    @Test
    public void testCheckAnswerWithNumbers() {
        assertFalse(defaultPuzzle.checkAnswer("12345"));
    }
    
    @Test
    public void testCheckAnswerWithVeryLongString() {
        assertFalse(defaultPuzzle.checkAnswer("A".repeat(10000)));
    }
    
    @Test
    public void testCheckAnswerMultipleTimes() {
        assertFalse(defaultPuzzle.checkAnswer("test1"));
        assertFalse(defaultPuzzle.checkAnswer("test2"));
        assertFalse(defaultPuzzle.checkAnswer("test3"));
    }
    
    @Test
    public void testCheckAnswerDoesNotModifyPuzzle() {
        String originalType = defaultPuzzle.getType();
        int originalHintCount = defaultPuzzle.getHints().size();
        UUID originalId = defaultPuzzle.getID();
        
        defaultPuzzle.checkAnswer("test");
        
        assertEquals(originalType, defaultPuzzle.getType());
        assertEquals(originalHintCount, defaultPuzzle.getHints().size());
        assertEquals(originalId, defaultPuzzle.getID());
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
    
    @Test
    public void testToStringWithNullType() {
        defaultPuzzle.setType(null);
        String result = defaultPuzzle.toString();
        assertEquals("", result);
    }
    
    @Test
    public void testToStringWithUnknownType() {
        defaultPuzzle.setType("UnknownType");
        String result = defaultPuzzle.toString();
        assertEquals("", result);
    }
    
    @Test
    public void testToStringDoesNotModifyPuzzle() {
        String originalType = defaultPuzzle.getType();
        int originalHintCount = defaultPuzzle.getHints().size();
        
        defaultPuzzle.toString();
        
        assertEquals(originalType, defaultPuzzle.getType());
        assertEquals(originalHintCount, defaultPuzzle.getHints().size());
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
    
    @Test
    public void testPuzzleModificationDoesNotAffectOtherPuzzles() {
        Puzzle puzzle1 = new Puzzle();
        Puzzle puzzle2 = new Puzzle();
        
        puzzle1.setType("Type1");
        puzzle1.addHint(new Hint("Hint1", 10));
        
        assertEquals("", puzzle2.getType());
        assertEquals(0, puzzle2.getHints().size());
    }
    
    @Test
    public void testPuzzleStateAfterMultipleOperations() {
        Puzzle puzzle = new Puzzle();
        
        puzzle.setType("Original");
        puzzle.addHint(new Hint("Hint1", 10));
        puzzle.setType("Modified");
        puzzle.addHint(new Hint("Hint2", 20));
        
        ArrayList<Hint> newHints = new ArrayList<>();
        newHints.add(new Hint("NewHint", 30));
        puzzle.setHints(newHints);
        
        assertEquals("Modified", puzzle.getType());
        assertEquals(1, puzzle.getHints().size());
        assertEquals("NewHint", puzzle.getHints().get(0).getText());
    }
    
    @Test
    public void testHintsListIndependence() {
        ArrayList<Hint> externalList = new ArrayList<>();
        externalList.add(new Hint("External", 10));
        
        defaultPuzzle.setHints(externalList);
        
        externalList.add(new Hint("Added externally", 20));
        
        assertEquals(2, defaultPuzzle.getHints().size());
    }
    
    @Test
    public void testAddingHintsAfterSettingHints() {
        ArrayList<Hint> initialHints = new ArrayList<>();
        initialHints.add(new Hint("Initial", 10));
        
        defaultPuzzle.setHints(initialHints);
        defaultPuzzle.addHint(new Hint("Added", 20));
        
        assertEquals(2, defaultPuzzle.getHints().size());
    }
    
    @Test
    public void testPuzzleWithNoHintsButWithType() {
        defaultPuzzle.setType("Trivia");
        assertEquals("Trivia", defaultPuzzle.getType());
        assertEquals(0, defaultPuzzle.getHints().size());
    }
    
    @Test
    public void testPuzzleWithHintsButNoType() {
        defaultPuzzle.addHint(new Hint("Test", 10));
        assertEquals("", defaultPuzzle.getType());
        assertEquals(1, defaultPuzzle.getHints().size());
    }
    
    @Test
    public void testPuzzleIDPersistsThroughModifications() {
        UUID originalId = defaultPuzzle.getID();
        
        defaultPuzzle.setType("Modified");
        defaultPuzzle.addHint(new Hint("Test", 10));
        defaultPuzzle.setHints(new ArrayList<>());
        
        assertEquals(originalId, defaultPuzzle.getID());
    }
    
    @Test
    public void testHintOrderMaintainedThroughGetHints() {
        Hint h1 = new Hint("First", 1);
        Hint h2 = new Hint("Second", 2);
        Hint h3 = new Hint("Third", 3);
        
        defaultPuzzle.addHint(h1);
        defaultPuzzle.addHint(h2);
        defaultPuzzle.addHint(h3);
        
        ArrayList<Hint> hints = defaultPuzzle.getHints();
        assertEquals("First", hints.get(0).getText());
        assertEquals("Second", hints.get(1).getText());
        assertEquals("Third", hints.get(2).getText());
    }
    
    @Test
    public void testMultipleInstancesHaveIndependentHintLists() {
        Puzzle p1 = new Puzzle();
        Puzzle p2 = new Puzzle();
        
        p1.addHint(new Hint("P1 Hint", 10));
        
        assertEquals(1, p1.getHints().size());
        assertEquals(0, p2.getHints().size());
    }
    
    @Test
    public void testSetHintsWithSameListReference() {
        ArrayList<Hint> hints = new ArrayList<>();
        hints.add(new Hint("Test", 10));
        
        defaultPuzzle.setHints(hints);
        puzzleWithHints.setHints(hints);
        
        hints.add(new Hint("Added to shared", 20));
        
        assertEquals(defaultPuzzle.getHints().size(), puzzleWithHints.getHints().size());
    }
    
    @Test
    public void testClearingHintsBySettingEmptyList() {
        puzzleWithHints.setHints(new ArrayList<>());
        assertEquals(0, puzzleWithHints.getHints().size());
    }
    
    @Test
    public void testPuzzleTypeCasePreservation() {
        defaultPuzzle.setType("TrIvIa");
        assertEquals("TrIvIa", defaultPuzzle.getType());
    }
    
    @Test
    public void testAddingNullHintDoesNotCrash() {
        try {
            defaultPuzzle.addHint(null);
            // Should not throw exception
        } catch (Exception e) {
            // If it does throw, that's a bug
            assertTrue("Adding null hint should not throw exception", false);
        }
    }
    
    @Test
    public void testCheckAnswerWithSameInputMultipleTimes() {
        boolean result1 = defaultPuzzle.checkAnswer("test");
        boolean result2 = defaultPuzzle.checkAnswer("test");
        boolean result3 = defaultPuzzle.checkAnswer("test");
        
        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }
    
    @Test
    public void testPuzzleRemainsValidAfterNullOperations() {
        defaultPuzzle.setType(null);
        defaultPuzzle.setID(null);
        defaultPuzzle.setHints(null);
        
        // Puzzle should still exist and be queryable
        assertNull(defaultPuzzle.getType());
        assertNull(defaultPuzzle.getID());
        assertNull(defaultPuzzle.getHints());
    }
}
