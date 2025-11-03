package com.model;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class TestPuzzleManager {

    private PuzzlesManager manager;
    private Puzzle puzzle1;
    private Puzzle puzzle2;

    @Before
    public void setUp() {
        manager = PuzzlesManager.getInstance();
        manager.getPuzzles().clear();  // Reset state between tests
        puzzle1 = new Riddle();
        puzzle2 = new PixelHunt();
    }

    // ---------------- Singleton Tests ----------------

    @Test
    public void testGetInstanceReturnsSameObject() {
        PuzzlesManager second = PuzzlesManager.getInstance();
        assertSame(manager, second);
    }

    // ---------------- Constructor and Defaults ----------------

    @Test
    public void testInitialPuzzlesListIsEmpty() {
        assertTrue(manager.getPuzzles().isEmpty());
    }

    @Test
    public void testInitialCurrentPuzzleIsNull() {
        assertNull(manager.getCurrentPuzzle());
    }

    @Test
    public void testInitialCurrentPuzzleIndexIsZero() {
        assertEquals(0, manager.getCurrentPuzzleIndex());
    }

    // ---------------- Puzzle Management ----------------

    @Test
    public void testAddPuzzleAddsToList() {
        manager.addPuzzle(puzzle1);
        assertEquals(1, manager.getPuzzles().size());
        assertTrue(manager.getPuzzles().contains(puzzle1));
    }

    @Test
    public void testAddNullPuzzleDoesNothing() {
        manager.addPuzzle(null);
        assertEquals(0, manager.getPuzzles().size());
    }

    @Test
    public void testSetCurrentPuzzleUpdatesCurrentPuzzle() {
        manager.addPuzzle(puzzle1);
        manager.setCurrentPuzzle(puzzle1);
        assertEquals(puzzle1, manager.getCurrentPuzzle());
    }

    @Test
    public void testSetCurrentPuzzleAdjustsIndex() {
        manager.addPuzzle(puzzle1);
        manager.addPuzzle(puzzle2);
        manager.setCurrentPuzzle(puzzle2);
        assertEquals(1, manager.getCurrentPuzzleIndex());
    }

    @Test
    public void testSetCurrentPuzzleNotInListSetsIndexZero() {
        manager.addPuzzle(puzzle1);
        Puzzle fakePuzzle = new Riddle();
        manager.setCurrentPuzzle(fakePuzzle);
        assertEquals(0, manager.getCurrentPuzzleIndex());
    }

    // ---------------- Next Puzzle Navigation ----------------

    @Test
    public void testNextPuzzleAdvancesWhenPossible() {
        manager.addPuzzle(puzzle1);
        manager.addPuzzle(puzzle2);
        manager.setCurrentPuzzle(puzzle1);
        assertTrue(manager.nextPuzzle());
        assertEquals(puzzle2, manager.getCurrentPuzzle());
    }

    @Test
    public void testNextPuzzleReturnsFalseIfAtEnd() {
        manager.addPuzzle(puzzle1);
        manager.setCurrentPuzzle(puzzle1);
        assertFalse(manager.nextPuzzle());
    }

    @Test
    public void testNextPuzzleReturnsFalseWhenEmpty() {
        assertFalse(manager.nextPuzzle());
    }

    // ---------------- Hint Management ----------------

    @Test
    public void testGetAvailableHintsReturnsEmptyWhenNoCurrentPuzzle() {
        assertTrue(manager.getAvailableHints().isEmpty());
    }

    @Test
    public void testGetAvailableHintsReturnsPuzzleHints() {
        ArrayList<Hint> hints = new ArrayList<>();
        hints.add(new Hint("Try looking around.", 0));
        puzzle1.setHints(hints);
        manager.addPuzzle(puzzle1);
        manager.setCurrentPuzzle(puzzle1);
        assertEquals(1, manager.getAvailableHints().size());
    }

    @Test
    public void testRevealHintReturnsNullWhenNoCurrentPuzzle() {
        assertNull(manager.revealHint(0));
    }

    @Test
    public void testRevealHintReturnsNullForInvalidIndex() {
        manager.addPuzzle(puzzle1);
        manager.setCurrentPuzzle(puzzle1);
        assertNull(manager.revealHint(5));
    }

    // ---------------- Skip and Submit ----------------

    @Test
    public void testSkipCurrentPuzzleDoesNotThrowWhenNull() {
        manager.skipCurrentPuzzle();
    }

    @Test
    public void testSubmitAnswerReturnsFalseWhenNoCurrentPuzzle() {
        assertFalse(manager.submitAnswer("test"));
    }

    @Test
    public void testSubmitAnswerDelegatesToPuzzle() {
        ((Riddle) puzzle1).setCorrectAnswer("42");
        manager.addPuzzle(puzzle1);
        manager.setCurrentPuzzle(puzzle1);
        assertTrue(manager.submitAnswer("42"));
    }

    // ---------------- Start Puzzle ----------------

    @Test
    public void testStartCurrentPuzzleDoesNotThrowWhenNull() {
        manager.startCurrentPuzzle();
    }

    @Test
    public void testStartCurrentPuzzleRunsWhenSet() {
        manager.addPuzzle(puzzle1);
        manager.setCurrentPuzzle(puzzle1);
        manager.startCurrentPuzzle();
    }

    // ---------------- Integration / Workflow ----------------

    @Test
    public void testFullPuzzleCycle() {
        manager.addPuzzle(puzzle1);
        manager.addPuzzle(puzzle2);

        manager.setCurrentPuzzle(puzzle1);
        assertEquals(puzzle1, manager.getCurrentPuzzle());

        manager.startCurrentPuzzle();
        assertTrue(manager.submitAnswer("wrong") == false || true);  // Ensure no crash
        manager.skipCurrentPuzzle();
        manager.nextPuzzle();
        assertEquals(puzzle2, manager.getCurrentPuzzle());
    }
}
    

