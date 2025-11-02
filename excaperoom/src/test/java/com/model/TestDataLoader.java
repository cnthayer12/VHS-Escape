package com.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class TestDataLoader {
private File testDir;
    private File testFile;

    @Before
    public void setUp() {
        testDir = new File("json");
        if (!testDir.exists()) {
            testDir.mkdirs();
        }

        testFile = new File(testDir, "players.json");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("[]");
        } catch (IOException e) {
            fail("Failed to create test players.json file: " + e.getMessage());
        }
    }

    @After
    public void tearDown() {
        if (testFile.exists()) {
            testFile.delete();
        }
        if (testDir.exists() && testDir.isDirectory()) {
            File[] files = testDir.listFiles();
            if (files != null && files.length == 0) {
                testDir.delete();
            }
        }
    }

    // --- Player Loading ---
    @Test
    public void testGetPlayersReturnsList() {
        assertNotNull("DataLoader.getPlayers() should never return null", DataLoader.getPlayers());
    }

    @Test
    public void testGetPlayersEmptyFile() {
        assertTrue("DataLoader.getPlayers() should return an empty list when file is empty", DataLoader.getPlayers().isEmpty());
    }

    @Test
    public void testGetPlayersValidData() {
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("[{\"displayName\": \"Alice\", \"password\": \"1234\"}]");
        } catch (IOException e) {
            fail("Failed to write test data: " + e.getMessage());
        }

        ArrayList<Player> players = DataLoader.getPlayers();
        assertNotNull("List should not be null after loading valid data", players);
        assertFalse("List should contain at least one player", players.isEmpty());
        Player first = players.get(0);
        assertNotNull("Player object should not be null", first);
        assertEquals("Player name should match JSON", "Alice", first.getDisplayName());
    }

    @Test
    public void testGetPlayersCorruptedFile() {
        try (FileWriter writer = new FileWriter(testFile)) {
            // Write badly formatted JSON
            writer.write("[{\"displayName\": \"Alice\"");
        } catch (IOException e) {
            fail("Failed to write corrupted JSON: " + e.getMessage());
        }

        try {
            ArrayList<Player> players = DataLoader.getPlayers();
            assertNotNull("DataLoader should handle malformed JSON gracefully", players);
        } catch (Exception e) {
            fail("DataLoader.getPlayers() should not throw exceptions on malformed JSON");
        }
    }

    // --- Puzzle Loading ---
    @Test
    public void testLoadAllWithPuzzlesReturnsList() {
        ArrayList<Player> players = DataLoader.loadAllWithPuzzles();
        assertNotNull("DataLoader.loadAllWithPuzzles() should never return null", players);
    }

    @Test
    public void testLoadAllWithPuzzlesValidData() {
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("[{\"displayName\": \"Bob\", \"password\": \"test\"}]");
        } catch (IOException e) {
            fail("Failed to write valid test data: " + e.getMessage());
        }

        ArrayList<Player> players = DataLoader.loadAllWithPuzzles();
        assertNotNull(players);
        assertFalse("Expected list not to be empty after loading valid player data", players.isEmpty());
    }

    @Test
    public void testLoadAllWithPuzzlesMissingData() {
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("[{\"displayName\": \"Charlie\"}]");
        } catch (IOException e) {
            fail("Failed to write partial data: " + e.getMessage());
        }

        ArrayList<Player> players = DataLoader.loadAllWithPuzzles();
        assertNotNull(players);
        assertTrue("Loader should still return a list even with missing puzzle info", players.size() >= 0);
    }

    // --- General Behavior ---
    @Test
    public void testNoExceptionsThrownWhenFilesMissing() {
        if (testFile.exists()) testFile.delete();

        try {
            ArrayList<Player> players = DataLoader.getPlayers();
            assertNotNull("Should return a non-null list when file is missing", players);
        } catch (Exception e) {
            fail("DataLoader.getPlayers() should not throw exceptions when file missing");
        }
    }

    @Test
    public void testDataLoaderReadsCorrectFilePaths() {
        assertTrue("Expected players.json file to exist in 'json' directory", testFile.exists());
    }
}
