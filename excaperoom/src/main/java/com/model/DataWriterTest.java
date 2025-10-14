package com.model;

import java.io.File;
import java.util.ArrayList;

public class DataWriterTest {

private ArrayList<Player> players;
    private static final String TEST_PLAYERS_FILE = "players.json";

    public static void main(String[] args) {
        DataWriterTest tester = new DataWriterTest();
        
        System.out.println("Starting DataWriter Tests...\n");
        
        tester.testSavePlayersNull();
        tester.testSavePlayersEmpty();
        tester.testSavePlayersSinglePlayer();
        tester.testSavePlayersMultiplePlayers();
        tester.testSavePlayerWithProgress();
        
        System.out.println("\nAll tests completed!");
    }  
    
    public void setUp() {
        players = new ArrayList<>();
    
    }
 public void tearDown() {
        File file = new File(TEST_PLAYERS_FILE);
        if (file.exists()) {
            file.delete();
        }
        if (players != null) {
            players.clear();
        }
    }

    public void testSavePlayersNull() {
        setUp();
        System.out.println("Test 1: Save null players");
        
        boolean result = DataWriter.savePlayers(null);
        
        if (!result) {
            System.out.println("  PASSED: Null players returned false as expected\n");
        } else {
            System.out.println("  FAILED: Expected false but got true\n");
        }
        
        tearDown();
    }

    public void testSavePlayersEmpty() {
        setUp();
        System.out.println("Test 2: Save empty player list");
        
        // TODO: Test saving an empty ArrayList
       
        
        System.out.println("  TODO: Implement this test\n");
        
        tearDown();
    }

    public void testSavePlayersSinglePlayer() {
        setUp();
        System.out.println("Test 3: Save single player");
        
        // TODO: Create a player with username only
       
        
        System.out.println("  TODO: Implement this test\n");
        
        tearDown();
    }

    public void testSavePlayersMultiplePlayers() {
        setUp();
        System.out.println("Test 4: Save multiple players");
        
        // TODO: Create 2-3 test players
      
        
        // Check if result is true and file exists
        
        System.out.println("  TODO: Implement this test\n");
        
        tearDown();
    }

    public void testSavePlayerWithProgress() {
        setUp();
        System.out.println("Test 5: Save player with progress");
        
        // TODO: Create a player
       
        
        System.out.println("  TODO: Implement this test\n");
        
        tearDown();
    }

    // Add more test methods as needed:
    
    
}
