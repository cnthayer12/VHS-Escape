package com.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestItemPuzzle {

    @Test
    public void defaultConstructor_initializesFields() {
        ItemPuzzle puzzle = new ItemPuzzle();

        // The default constructor sets:
        // puzzleType     = ""
        // requiredItemName = ""
        // type = "ItemPuzzle" (inherited from Puzzle)
        assertEquals("Default puzzleType should be empty string", "", puzzle.getPuzzleType());
        assertEquals("Default requiredItemName should be empty string", "", puzzle.getRequiredItemName());

        // We can't always access puzzle.type directly unless it's protected in Puzzle,
        // so instead we sanity check toString() formatting.
        assertTrue("toString() should start with 'ItemPuzzle:'",
                puzzle.toString().startsWith("ItemPuzzle:"));
    }

    @Test
    public void constructorWithArgs_setsFields() {
        ItemPuzzle puzzle = new ItemPuzzle("Key Door Puzzle", "Rusty Key");

        assertEquals("Key Door Puzzle", puzzle.getPuzzleType());
        assertEquals("Rusty Key", puzzle.getRequiredItemName());

        String desc = puzzle.toString();
        assertTrue("toString() should include puzzle type",
                desc.contains("Key Door Puzzle"));
        assertTrue("toString() should include required item name",
                desc.contains("Rusty Key"));
    }

    @Test
    public void checkAnswer_returnsFalseWhenNoPlayerOrNoInventory() {
        // We are intentionally NOT setting up Players.getInstance().getCurrentPlayer()
        // The code in ItemPuzzle.checkAnswer() should handle that safely and return false.
        ItemPuzzle puzzle = new ItemPuzzle("Door Lock", "Silver Key");

        boolean result = puzzle.checkAnswer("anything");

        assertFalse(
            "checkAnswer should return false (and not throw) if there is no current player / no matching item",
            result
        );
    }

}
