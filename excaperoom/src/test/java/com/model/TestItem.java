package com.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

public class TestItem {

    @Test
    public void defaultConstructor_initializesEmptyFields() {
        Item item = new Item();

        assertEquals("Default name should be empty string", "", item.getName());
        assertEquals("Default description should be empty string", "", item.getDescription());
        assertEquals("Default location should be empty string", "", item.getLocation());
        assertNull("Default sound should be null", item.getSound());
    }

    @Test
    public void constructorWithArgs_setsAllFields() {
        File fakeSound = new File("not-a-real-file.wav");
        Item item = new Item("Key", "Opens a locked door", "Basement", fakeSound);

        assertEquals("Key", item.getName());
        assertEquals("Opens a locked door", item.getDescription());
        assertEquals("Basement", item.getLocation());
        assertEquals(fakeSound, item.getSound());
    }

    @Test
    public void use_withNullPuzzle_returnsFalseAndDoesNotCrash() {
        Item item = new Item("Key", "Opens a locked door", "Basement", null);

        boolean result = item.use(null);

        assertFalse("use(null) should return false and print an error instead of crashing", result);
    }

    @Test
    public void use_withValidPuzzle_returnsTrue() {
        Item item = new Item("Key", "Opens a locked door", "Basement", null);
        Puzzle dummyPuzzle = new Riddle("Some riddle?", "answer"); // Riddle extends Puzzle

        boolean result = item.use(dummyPuzzle);

        assertTrue("use(puzzle) should return true when a puzzle is provided", result);
    }

    @Test
    public void addToInventory_noArgs_alwaysReturnsTrue() {
        Item item = new Item("Key", "Opens a locked door", "Basement", null);

        boolean result = item.addToInventory();

        assertTrue("addToInventory() with no Progress should return true and not crash", result);
    }

    @Test
    public void addToInventory_nullProgress_returnsFalseAndDoesNotCrash() {
        Item item = new Item("Key", "Opens a locked door", "Basement", null);

        boolean result = item.addToInventory((Progress) null);

        assertFalse("addToInventory(null) should return false and not throw", result);
    }

    @Test
    public void addToInventory_validProgress_addsItemToInventory() {
        // This test assumes Progress has:
        // - a default constructor
        // - setInventory(ArrayList<Item>)
        // - getInventory() that returns that list
        // If your Progress looks different, you may need to tweak this test accordingly.

        Item item = new Item("Key", "Opens a locked door", "Basement", null);

        Progress progress = new Progress();
        progress.setInventory(new ArrayList<Item>());

        boolean result = item.addToInventory(progress);

        assertTrue("addToInventory(progress) should return true when progress and its inventory are valid", result);
        assertTrue("Item should now be in the player's inventory",
                progress.getInventory().contains(item));
    }

    @Test
    public void equals_sameNameAndLocation_returnsTrue() {
        Item a = new Item("Key", "foo", "Basement", null);
        Item b = new Item("Key", "bar", "Basement", null); // desc differs but equals() ignores description

        assertTrue("Items with same name and location should be equal", a.equals(b));
        assertEquals("Equal items should have the same hashCode", a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_differentNameOrLocation_returnsFalse() {
        Item a = new Item("Key", "foo", "Basement", null);
        Item b = new Item("Key", "foo", "Attic", null);
        Item c = new Item("Hammer", "foo", "Basement", null);

        assertFalse("Different location should make items not equal", a.equals(b));
        assertFalse("Different name should make items not equal", a.equals(c));
        assertFalse("Item should not equal an unrelated type", a.equals("not-an-item"));
        assertFalse("Item should not equal null", a.equals(null));
    }

    @Test
    public void toString_includesNameAndLocationAndDoesNotCrash() {
        Item item = new Item("Key", "Opens a locked door", "Basement", null);

        String result = item.toString();

        assertNotNull("toString() should never return null", result);
        assertTrue("toString() should include the item name", result.contains("Key"));
        assertTrue("toString() should include the item location", result.contains("Basement"));
    }

    @Test
    public void toString_doesNotIncludeSoundReference() {
        File fakeSound = new File("not-a-real-file.wav");
        Item item = new Item("Key", "Opens a locked door", "Basement", fakeSound);

        String result = item.toString();

        assertFalse("toString() should NOT include the file path / sound object",
                result.contains("not-a-real-file.wav"));
    }
}
