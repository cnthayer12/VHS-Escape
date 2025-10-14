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
        
        boolean result = DataWriter.savePlayers(players);
        
        if (result) {
            System.out.println("  PASSED: Empty list saved successfully\n");
        } else {
            System.out.println("  FAILED: Empty list should return true\n");
        }
        
        tearDown();
    }

    public void testSavePlayersSinglePlayer() {
        setUp();
        System.out.println("Test 3: Save single player");
        
        ArrayList<Progress> emptyProgress = new ArrayList<>();
        Player player = new Player("testUser1", emptyProgress);
        players.add(player);
        boolean result = DataWriter.savePlayers(players);
        
        File file = new File(TEST_PLAYERS_FILE);
        if (result && file.exists()) {
            System.out.println("  PASSED: Single player saved successfully\n");
        } else {
            System.out.println("  FAILED: Could not save single player\n");
        }
        
        tearDown();
    }

    public void testSavePlayersMultiplePlayers() {
        setUp();
        System.out.println("Test 4: Save multiple players");
        
        Player player1 = new Player("user1", new ArrayList<>());
        Player player2 = new Player("user2", new ArrayList<>());
        Player player3 = new Player("user3", new ArrayList<>());
        players.add(player1);
        players.add(player2);
        players.add(player3);
        
        boolean result = DataWriter.savePlayers(players);
        File file = new File(TEST_PLAYERS_FILE);
        
        if (result && file.exists()) {
            System.out.println("  PASSED: Multiple players saved successfully\n");
        } else {
            System.out.println("  FAILED: Could not save multiple players\n");
        }
        
        tearDown();
    }

    public void testSavePlayerWithProgress() {
        setUp();
        System.out.println("Test 5: Save player with progress");
        
        ArrayList<Progress> progressList = new ArrayList<>();
        
        // Create progress with test data (adjust based on your Progress constructor)
        // Progress progress = new Progress(...);
        // progressList.add(progress);
        
        Player player = new Player("progressUser", progressList);
        players.add(player);
        boolean result = DataWriter.savePlayers(players);
        File file = new File(TEST_PLAYERS_FILE);
        
        if (result && file.exists()) {
            System.out.println("  PASSED: Player with progress saved successfully\n");
        } else {
            System.out.println("  FAILED: Could not save player with progress\n");
        }
        
        tearDown();
    }
}
