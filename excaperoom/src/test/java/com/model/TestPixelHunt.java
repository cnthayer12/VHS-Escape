package com.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class TestPixelHunt {

    private PixelHunt defaultHunt;
    private PixelHunt customHunt;

    @Before
    public void setUp() {
        defaultHunt = new PixelHunt();
        customHunt = new PixelHunt();
        customHunt.setCorrectX(100);
        customHunt.setCorrectY(200);
        customHunt.setCloseness(10);
    }

    // ---------------- Constructor Tests ----------------

    @Test
    public void testDefaultConstructorInitializesValues() {
        // Arrange handled by setUp()
        // Assert
        assertEquals(0, defaultHunt.getCorrectX());
        assertEquals(0, defaultHunt.getCorrectY());
        assertEquals(5, defaultHunt.getCloseness());
        assertEquals("PixelHunt", defaultHunt.getType());
    }

    // ---------------- Accessor / Mutator Tests ----------------

    @Test
    public void testSetAndGetCorrectX() {
        // Arrange
        defaultHunt.setCorrectX(45);
        // Assert
        assertEquals(45, defaultHunt.getCorrectX());
    }

    @Test
    public void testSetAndGetCorrectY() {
        defaultHunt.setCorrectY(78);
        assertEquals(78, defaultHunt.getCorrectY());
    }

    @Test
    public void testSetAndGetCloseness() {
        defaultHunt.setCloseness(12);
        assertEquals(12, defaultHunt.getCloseness());
    }

    @Test
    public void testSetNegativeCloseness() {
        defaultHunt.setCloseness(-10);
        assertEquals(-10, defaultHunt.getCloseness());
    }

    // ---------------- checkClick() Normal Cases ----------------

    @Test
    public void testCheckClickExactlyOnCorrectSpotReturnsTrue() {
        assertTrue(customHunt.checkClick(100, 200));
    }

    @Test
    public void testCheckClickWithinClosenessRangeReturnsTrue() {
        assertTrue(customHunt.checkClick(105, 205));  // within ±10
    }

    @Test
    public void testCheckClickOutsideClosenessRangeReturnsFalse() {
        assertFalse(customHunt.checkClick(120, 230)); // outside ±10
    }

    @Test
    public void testCheckClickAtBoundaryOfClosenessReturnsTrue() {
        assertTrue(customHunt.checkClick(110, 200));  // on x boundary
        assertTrue(customHunt.checkClick(100, 210));  // on y boundary
    }

    // ---------------- checkClick() Edge / Error Cases ----------------

    @Test
    public void testCheckClickNegativeCoordinates() {
        customHunt.setCorrectX(-5);
        customHunt.setCorrectY(-5);
        customHunt.setCloseness(10);

        assertTrue(customHunt.checkClick(-10, -10)); // within range
        assertFalse(customHunt.checkClick(-30, -40)); // outside range
    }

    @Test
    public void testCheckClickWithZeroCloseness() {
        customHunt.setCloseness(0);
        assertTrue(customHunt.checkClick(100, 200));
        assertFalse(customHunt.checkClick(101, 200));
    }

    @Test
    public void testCheckClickWithLargeClosenessAlwaysTrueForNearbyPoints() {
        customHunt.setCloseness(1000);
        assertTrue(customHunt.checkClick(50, 100));
    }

    // ---------------- startPuzzle() and toString() Tests ----------------

    @Test
    public void testStartPuzzleDoesNotThrowException() {
        // This method prints to console; just ensure it runs
        defaultHunt.startPuzzle();
    }

    @Test
    public void testToStringReturnsExpectedText() {
        assertEquals("A PixelHunt puzzle", defaultHunt.toString());
    }

    // ---------------- Integration / Workflow Tests ----------------

    @Test
    public void testFullWorkflowClickSequence() {
        PixelHunt hunt = new PixelHunt();
        hunt.setCorrectX(10);
        hunt.setCorrectY(15);
        hunt.setCloseness(3);

        assertFalse(hunt.checkClick(0, 0));   // incorrect
        assertTrue(hunt.checkClick(12, 16));  // correct
    }

    @Test
    public void testTypeRemainsPixelHuntAfterFieldUpdates() {
        customHunt.setCorrectX(500);
        customHunt.setCloseness(50);
        assertEquals("PixelHunt", customHunt.getType());
    }
    
}
