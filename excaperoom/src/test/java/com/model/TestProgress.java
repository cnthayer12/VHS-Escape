package com.model;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class TestProgress {

    private Progress defaultProgress;
    private Progress parameterizedProgress;
    private ArrayList<Item> inventory;
    private ArrayList<Hint> storedHints;
    private ArrayList<Puzzle> completedPuzzles;
    private Puzzle puzzle;

    @Before
    public void setUp() {
        inventory = new ArrayList<>();
        storedHints = new ArrayList<>();
        completedPuzzles = new ArrayList<>();
        puzzle = new Riddle();
        parameterizedProgress = new Progress(inventory, storedHints, completedPuzzles, puzzle, 2, 100);
        defaultProgress = new Progress();
    }

    // ---------------- Constructor Tests ----------------

    @Test
    public void testDefaultConstructorInitializesEmptyLists() {
        assertTrue(defaultProgress.getInventory().isEmpty());
        assertTrue(defaultProgress.getStoredHints().isEmpty());
        assertTrue(defaultProgress.getCompletedPuzzles().isEmpty());
    }

    @Test
    public void testDefaultConstructorInitializesValuesToZero() {
        assertEquals(0, defaultProgress.getHintsUsed());
        assertEquals(0, defaultProgress.getStrikes());
        assertEquals(0, defaultProgress.getCurrentScore());
        assertNull(defaultProgress.getCurrentPuzzle());
    }

    @Test
    public void testParameterizedConstructorSetsFieldsProperly() {
        assertEquals(inventory, parameterizedProgress.getInventory());
        assertEquals(storedHints, parameterizedProgress.getStoredHints());
        assertEquals(completedPuzzles, parameterizedProgress.getCompletedPuzzles());
        assertEquals(puzzle, parameterizedProgress.getCurrentPuzzle());
        assertEquals(2, parameterizedProgress.getStrikes());
        assertEquals(100, parameterizedProgress.getCurrentScore());
    }

    // ---------------- Accessor Tests ----------------

    @Test
    public void testGetHintsUsedReflectsStoredHintsCount() {
        storedHints.add(new Hint("hint", 0));
        Progress progress = new Progress(inventory, storedHints, completedPuzzles, puzzle, 0, 0);
        assertEquals(1, progress.getHintsUsed());
    }

    @Test
    public void testGetInventoryReturnsCorrectList() {
        assertNotNull(defaultProgress.getInventory());
    }

    @Test
    public void testGetCompletedPuzzlesReturnsEmptyInitially() {
        assertTrue(defaultProgress.getCompletedPuzzles().isEmpty());
    }

    // ---------------- Mutator Tests ----------------

    @Test
    public void testAddCompletedPuzzleAddsPuzzle() {
        defaultProgress.addCompletedPuzzle(puzzle);
        assertEquals(1, defaultProgress.getCompletedPuzzles().size());
    }

    @Test
    public void testAddCompletedPuzzleIgnoresNull() {
        defaultProgress.addCompletedPuzzle(null);
        assertEquals(0, defaultProgress.getCompletedPuzzles().size());
    }

    @Test
    public void testAddItemAddsToInventory() {
        Item item = new Item("Key", null, null, null);
        defaultProgress.addItem(item);
        assertEquals(1, defaultProgress.getInventory().size());
    }

    @Test
    public void testAddItemIgnoresNull() {
        defaultProgress.addItem(null);
        assertEquals(0, defaultProgress.getInventory().size());
    }

    @Test
    public void testAddHintAddsToStoredHintsAndIncrementsUsed() {
        Hint hint = new Hint("Use flashlight", 0);
        defaultProgress.addHint(hint);
        assertEquals(1, defaultProgress.getStoredHints().size());
        assertEquals(1, defaultProgress.getHintsUsed());
    }

    @Test
    public void testAddHintIgnoresNull() {
        defaultProgress.addHint(null);
        assertEquals(0, defaultProgress.getStoredHints().size());
    }

    @Test
    public void testSetScoreUpdatesCurrentScore() {
        defaultProgress.setScore(250);
        assertEquals(250, defaultProgress.getCurrentScore());
    }

    @Test
    public void testSetStrikesUpdatesValue() {
        defaultProgress.setStrikes(3);
        assertEquals(3, defaultProgress.getStrikes());
    }

    @Test
    public void testSetInventoryReplacesInventoryList() {
        ArrayList<Item> newList = new ArrayList<>();
        newList.add(new Item("Lantern", null, null, null));
        defaultProgress.setInventory(newList);
        assertEquals(newList, defaultProgress.getInventory());
    }

    @Test
    public void testSetStoredHintsUpdatesCount() {
        ArrayList<Hint> hints = new ArrayList<>();
        hints.add(new Hint("hint1", 0));
        hints.add(new Hint("hint2", 0));
        defaultProgress.setStoredHints(hints);
        assertEquals(2, defaultProgress.getHintsUsed());
    }

    // ---------------- Functional Tests ----------------

    @Test
    public void testCalculatePercentReturnsZeroInitially() {
        assertEquals(0.0, defaultProgress.calculatePercent(), 0.001);
    }

    @Test
    public void testCalculatePercentAfterAddingOnePuzzle() {
        defaultProgress.addCompletedPuzzle(puzzle);
        double expected = (1 / 3.0) * 100;
        assertEquals(expected, defaultProgress.calculatePercent(), 0.001);
    }

    @Test
    public void testToStringIncludesPercentAndHintsInfo() {
        defaultProgress.addCompletedPuzzle(puzzle);
        String result = defaultProgress.toString();
        assertTrue(result.contains("Percent Complete"));
        assertTrue(result.contains("Questions Answered"));
    }

    @Test
    public void testLoadProgressReturnsNullWhenPlayerIsNull() {
        assertNull(Progress.loadProgress(null));
    }

    @Test
    public void testLoadProgressReturnsNullWhenProgressListEmpty() {
        Player player = new Player("test", null, "pass");
        player.getProgress().clear();
        assertNull(Progress.loadProgress(player));
    }

    @Test
    public void testLoadProgressReturnsLatestProgress() {
        Player player = new Player("test", null, "pass");
        Progress first = new Progress();
        Progress second = new Progress();
        player.getProgress().add(first);
        player.getProgress().add(second);
        assertEquals(second, Progress.loadProgress(player));
    }

    // ---------------- Edge Case Tests ----------------

    @Test
    public void testSetCompletedPuzzlesWithNullList() {
        defaultProgress.setCompletedPuzzles(null);
        assertNull(defaultProgress.getCompletedPuzzles());
    }

    @Test
    public void testSetInventoryWithNullList() {
        defaultProgress.setInventory(null);
        assertNull(defaultProgress.getInventory());
    }

    @Test
    public void testSetStoredHintsWithEmptyListResetsHintCount() {
        ArrayList<Hint> emptyHints = new ArrayList<>();
        defaultProgress.setStoredHints(emptyHints);
        assertEquals(0, defaultProgress.getHintsUsed());
    }

    @Test
    public void testGetPuzzlesInfoReturnsEmptyStringInitially() {
        assertEquals("", defaultProgress.getPuzzlesInfo());
    }

    @Test
    public void testGetPuzzlesInfoListsHintsUsedForPuzzle() {
        Riddle riddle = new Riddle();
        Hint hint = new Hint("Try again", 0);
        hint.setPuzzle(riddle);
        defaultProgress.addCompletedPuzzle(riddle);
        defaultProgress.addHint(hint);
        String info = defaultProgress.getPuzzlesInfo();
        assertTrue(info.contains("Hint used"));
    }
    
}
