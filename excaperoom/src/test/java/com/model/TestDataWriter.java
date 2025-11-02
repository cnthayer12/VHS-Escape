package com.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class TestDataWriter {

    private File testDir;
    private File testFile;

    @Before
    public void setUp() {
        testDir = new File("json");
        if (!testDir.exists()) {
            testDir.mkdirs();
        }
        testFile = new File(testDir, "players.json");
        if (testFile.exists()) {
            testFile.delete();
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

    // --- Saving Players ---
     @Test
    public void testSavePlayersCreatesFile() {
        ArrayList<Player> emptyList = new ArrayList<>();
        boolean success = DataWriter.savePlayers(emptyList);

        assertTrue("savePlayers() should return true", success);
        assertTrue("players.json file should be created", testFile.exists());
        assertTrue("players.json file should not be empty", testFile.length() > 0);
    }

    @Test
    public void testSavePlayersNonEmptyList() {
        ArrayList<Progress> progs = new ArrayList<>();
        progs.add(new Progress());

        Player p = new Player("TestUser", progs, "password123");

        ArrayList<Player> list = new ArrayList<>();
        list.add(p);

        boolean success = DataWriter.savePlayers(list);
        assertTrue("savePlayers() should return true for non-empty list", success);
        assertTrue("players.json should exist after saving non-empty list", testFile.exists());

        try {
            String content = new String(Files.readAllBytes(testFile.toPath()), "UTF-8");
            assertTrue("Saved JSON should contain the saved player's displayName", content.contains("TestUser"));
        } catch (IOException e) {
            fail("Failed to read players.json: " + e.getMessage());
        }
    }

    @Test
    public void testSavePlayersEmptyList() {
        ArrayList<Player> emptyList = new ArrayList<>();
        boolean success = DataWriter.savePlayers(emptyList);
        assertTrue("savePlayers() should return true for empty list", success);
        assertTrue("players.json should be created when saving empty list", testFile.exists());

        try {
            String content = new String(Files.readAllBytes(testFile.toPath()), "UTF-8");
            assertTrue("Empty-list output should be JSON array-ish", content.trim().startsWith("[") || content.trim().startsWith("{"));
        } catch (IOException e) {
            fail("Failed to read players.json after saving empty list: " + e.getMessage());
        }
    }

    @Test
    public void testSavePlayersNullList() {
        boolean success = DataWriter.savePlayers(null);
        assertTrue("savePlayers() should handle null input gracefully", success);
        assertTrue("players.json should still be created", testFile.exists());
    }

    @Test
    public void testSavePlayersOverwritesExistingFile() {
        try (FileWriter w = new FileWriter(testFile)) {
            w.write("ORIGINAL_CONTENT_DO_NOT_OVERWRITE");
        } catch (IOException e) {
            fail("Failed to write initial content for overwrite test: " + e.getMessage());
        }

        ArrayList<Player> emptyList = new ArrayList<>();
        boolean success = DataWriter.savePlayers(emptyList);

        assertTrue("savePlayers should return true when overwriting an existing file", success);

        try {
            String content = new String(Files.readAllBytes(testFile.toPath()), "UTF-8");
            assertFalse("File content should have been replaced (should not contain ORIGINAL_CONTENT)", content.contains("ORIGINAL_CONTENT_DO_NOT_OVERWRITE"));
        } catch (IOException e) {
            fail("Failed to read players.json after overwrite: " + e.getMessage());
        }
    }

    @Test
    public void testSavePlayersFilePermissionsError() {
        try (FileWriter w = new FileWriter(testFile)) {
            w.write("INITIAL");
        } catch (IOException e) {
            fail("Failed to write initial content for permissions test: " + e.getMessage());
        }

        boolean setReadOnlyResult = testFile.setReadOnly();

        ArrayList<Player> emptyList = new ArrayList<>();
        boolean success = false;
        try {
            success = DataWriter.savePlayers(emptyList);
        } catch (Throwable t) {
            fail("DataWriter.savePlayers threw an exception when file was read-only: " + t.getMessage());
        }

        try {
            String content = new String(Files.readAllBytes(testFile.toPath()), "UTF-8");
            if (!setReadOnlyResult) {
                assertTrue("Could not change file permissions on this platform; savePlayers completed", testFile.exists());
            } else {
                boolean contentUnchanged = content.contains("INITIAL");
                if (!success) {
                    assertTrue("If savePlayers() failed due to permissions, file should still contain original content", contentUnchanged);
                } else {
                    assertFalse("If savePlayers() succeeded despite read-only, content should have been replaced", contentUnchanged);
                }
            }
        } catch (IOException e) {
            fail("Failed to read players.json after permission test: " + e.getMessage());
        } finally {
            testFile.setWritable(true);
        }
    }

    // --- Data Integrity ---
    @Test
    public void testSaveAndReloadConsistency() {
        ArrayList<Progress> progs = new ArrayList<>();
        progs.add(new Progress());
        Player p = new Player("RoundTripUser", progs, "pw");
        ArrayList<Player> list = new ArrayList<>();
        list.add(p);

        boolean saveOk = DataWriter.savePlayers(list);
        assertTrue("savePlayers should report success for round-trip test", saveOk);
        assertTrue("players.json should exist after save", testFile.exists());

        ArrayList<Player> loaded = DataLoader.getPlayers();
        assertNotNull("DataLoader.getPlayers() should return non-null after save", loaded);

        boolean found = false;
        for (Player lp : loaded) {
            if (lp != null && "RoundTripUser".equals(lp.getDisplayName())) {
                found = true;
                break;
            }
        }
        assertTrue("Loaded data should contain the user saved by DataWriter", found);
    }

    @Test
    public void testSavePlayersFormatting() {
        ArrayList<Progress> progs = new ArrayList<>();
        progs.add(new Progress());
        Player p = new Player("FormatUser", progs, "pw");
        ArrayList<Player> list = new ArrayList<>();
        list.add(p);

        boolean ok = DataWriter.savePlayers(list);
        assertTrue("savePlayers should succeed when testing formatting", ok);

        try {
            String content = new String(Files.readAllBytes(testFile.toPath()), "UTF-8");
            assertTrue("Saved JSON should contain 'displayName' key", content.contains("displayName"));
            assertTrue("Saved JSON should contain 'password' key", content.contains("password"));
        } catch (IOException e) {
            fail("Failed to read players.json for formatting check: " + e.getMessage());
        }
    }
}
