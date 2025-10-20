package com.model;

/**
 * LibraryUI handles the user interface for testing the VHS Escape Room
 */
public class LibraryUI {
    private EscapeGameFacade facade;
    
    public LibraryUI() {
        facade = EscapeGameFacade.getInstance();
    }
    
    public void run() {
        startUp();
        loadPlayer();
    }
    // well named method for each thing "startUp" "loadPlayer" "playGame" "saveGame" add scanner and based on what user says call methods in facade
// Scenario 1: New Player Starting Game
    public void startUp() {
        System.out.println("\n--- Scenario 1: New Player Starting Game ---");
        
        if (!facade.startGame()) {
            System.out.println("Failed to start game");
            return;
        }
        System.out.println("Game started successfully for player1");
        
        System.out.println("Player discovers a VHS tape...");
        
        System.out.println("Player attempts to solve first puzzle...");
        
        if (!facade.saveProgress()) {
            System.out.println("Failed to save progress");
        } else {
            System.out.println("Progress saved successfully");
        }
    }
    
// Scenario 2: Continuing Player
    public void loadPlayer() {
        System.out.println("\n--- Scenario 2: Continuing Player ---");
        
        if (!facade.loadProgress()) {
            System.out.println("Failed to get player instance for player2");
            return;
        }
        System.out.println("Player 'player2' instance retrieved");
        
        if (!facade.startGame()) {
            System.out.println("Failed to start game");
            return;
        }
        System.out.println("Game resumed with saved progress");
        
        System.out.println("Player requests a hint...");
        
        System.out.println("Player made incorrect choice - strike added");
        
        // continues to next puzzle
        System.out.println("Player moves to next puzzle...");
        
        // save data
        if (!facade.saveProgress()) {
            System.out.println("Failed to save progress");
        } else {
            System.out.println("All progress saved successfully");
        }
        
        System.out.println("Player's current progress preserved");
    }
    
    /**
     * Main method to run the UI
     */
    public static void main(String[] args) {
        LibraryUI ui = new LibraryUI();
        ui.run();
        System.out.println("\n--- Test Complete ---");
    }
}