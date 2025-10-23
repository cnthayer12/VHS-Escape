package com.model;

import java.io.File;
import java.util.ArrayList;

public class DataWriterTest {

    private ArrayList<Player> players;
    private static final String TEST_PLAYERS_FILE = "json/players.json";

    public static void main(String[] args) {
        DataWriterTest tester = new DataWriterTest();
        
        System.out.println("Starting DataWriter Tests...\n");
        
        tester.testSavePlayersNull();
        tester.testSavePlayersEmpty();
        tester.testSavePlayersSinglePlayer();
        tester.testSavePlayersMultiplePlayers();
        tester.testSavePlayerWithProgress();
        tester.testSavePlayerWithHints();
        
        System.out.println("\nAll tests completed!");
    }  
    
    public void setUp() {
        players = new ArrayList<>();
    }
    
    public void tearDown() {
        if (players != null) {
            players.clear();
        }
        // Note: We don't delete the file to allow inspection
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
        
        // Create test inventory (empty Item objects since Item class is empty)
        ArrayList<Item> inventory = new ArrayList<>();
        inventory.add(new Item());
        inventory.add(new Item());
        
        // Create empty hints list
        ArrayList<Hint> storedHints = new ArrayList<>();
        
        // Create progress with test data
        Progress progress = new Progress(inventory, storedHints, new ArrayList<>(), null, 1, 150);
        progressList.add(progress);
        
        Player player = new Player("progressUser", progressList);
        players.add(player);
        boolean result = DataWriter.savePlayers(players);
        File file = new File(TEST_PLAYERS_FILE);
        
        if (result && file.exists()) {
            System.out.println("  PASSED: Player with progress saved successfully\n");
            System.out.println("  Player has " + progress.getHintsUsed() + " hints used");
            System.out.println("  Player has " + progress.getStrikes() + " strikes");
            System.out.println("  Player has score of " + progress.getCurrentScore());
        } else {
            System.out.println("  FAILED: Could not save player with progress\n");
        }
        
        tearDown();
    }

    public void testSavePlayerWithHints() {
        setUp();
        System.out.println("Test 6: Save player with stored hints");
        
        ArrayList<Progress> progressList = new ArrayList<>();
        
        // Create test inventory
        ArrayList<Item> inventory = new ArrayList<>();
        
        // Create stored hints
        ArrayList<Hint> storedHints = new ArrayList<>();
        Hint hint1 = new Hint("This is a helpful hint", 10);
        Hint hint2 = new Hint("Another clue", 15);
        hint1.markUsed(); // Mark first hint as used
        storedHints.add(hint1);
        storedHints.add(hint2);
        
        // Create progress with hints
        Progress progress = new Progress(inventory, storedHints, new ArrayList<>(), null, 0, 100);
        progressList.add(progress);
        
        Player player = new Player("hintUser", progressList);
        players.add(player);
        boolean result = DataWriter.savePlayers(players);
        File file = new File(TEST_PLAYERS_FILE);
        
        if (result && file.exists()) {
            System.out.println("  PASSED: Player with hints saved successfully\n");
            System.out.println("  Player has " + storedHints.size() + " stored hints");
            System.out.println("  First hint is used: " + hint1.isUsed());
            System.out.println("  Second hint is used: " + hint2.isUsed());
        } else {
            System.out.println("  FAILED: Could not save player with hints\n");
        }
        
        tearDown();
    }
}
