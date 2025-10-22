package com.model;

import java.util.ArrayList;
import java.util.Scanner;

public class LibraryUI {
    private EscapeGameFacade facade;
    private Scanner scanner;
    
    public LibraryUI() {
        facade = EscapeGameFacade.getInstance();
        scanner = new Scanner(System.in);
    }

    public void run() {
        displayWelcome();
        
        boolean running = true;
        while (running) {
            displayMainMenu();
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    startNewGame();
                    break;
                case "2":
                    loadPlayer();
                    break;
                case "3":
                    playGame();
                    break;
                case "4":
                    saveGame();
                    break;
                case "5":
                    running = false;
                    System.out.println("\nExiting VHS Escape Room. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        
        scanner.close();
    }

    private void displayWelcome() {
        System.out.println("Welcome to the VHS Escape Room!");
    }
 
    private void displayMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Start New Game");
        System.out.println("2. Load Player");
        System.out.println("3. Play Game");
        System.out.println("4. Save Progress");
        System.out.println("5. Exit");
        System.out.print("\nEnter choice: ");
    }
  
    private void startNewGame() {
        System.out.println("\n--- START NEW GAME ---");
        
        System.out.print("Enter player name: ");
        String playerName = scanner.nextLine().trim();
        
        if (playerName.isEmpty()) {
            System.out.println("Invalid name.");
            return;
        }

        if (!facade.createPlayer(playerName)) {
            System.out.println("✗ Failed to create player");
            return;
        }
        System.out.println("✓ Player created: " + playerName);

        System.out.println("\nSelect Difficulty:");
        System.out.println("1. Easy (30 minutes)");
        System.out.println("2. Medium (20 minutes)");
        System.out.println("3. Hard (15 minutes)");
        System.out.print("Enter choice: ");
        
        String diffChoice = scanner.nextLine().trim();
        Game.Difficulty difficulty;
        
        switch (diffChoice) {
            case "1":
                difficulty = Game.Difficulty.EASY;
                break;
            case "2":
                difficulty = Game.Difficulty.MEDIUM;
                break;
            case "3":
                difficulty = Game.Difficulty.HARD;
                break;
            default:
                System.out.println("Invalid choice. Using Medium difficulty.");
                difficulty = Game.Difficulty.MEDIUM;
        }
    
        Players players = Players.getInstance();
        Player newPlayer = null;
        for (Player p : Players.getPlayers()) {
            if (p.getDisplayName().equals(playerName)) {
                newPlayer = p;
                break;
            }
        }
        
        if (newPlayer == null) {
            System.out.println("✗ Error: Could not find created player");
            return;
        }

        Game game = Game.getInstance();
        game.initializeGame(newPlayer, difficulty);

        if (facade.startGame()) {
            System.out.println("✓ Game started successfully!");
            game.displayStory();
            System.out.println("\nDifficulty: " + difficulty);
            System.out.println("Time Limit: " + (difficulty.getTimeLimit() / 60) + " minutes");
            System.out.println("Starting Score: " + game.getScore());
        } else {
            System.out.println("✗ Failed to start game");
        }
    }

    private void loadPlayer() {
        System.out.println("\n--- LOAD PLAYER ---");
        
        System.out.print("Enter player name: ");
        String playerName = scanner.nextLine().trim();
        
        if (playerName.isEmpty()) {
            System.out.println("Invalid name.");
            return;
        }

        if (facade.loadProgress()) {
            System.out.println("✓ Progress loaded from file");
            

            Players players = Players.getInstance();
            Player loadedPlayer = null;
            
            for (Player p : Players.getPlayers()) {
                if (p.getDisplayName().equals(playerName)) {
                    loadedPlayer = p;
                    break;
                }
            }
            
            if (loadedPlayer == null) {
                System.out.println("✗ Player '" + playerName + "' not found in loaded data");
                return;
            }
            
            System.out.println("✓ Player '" + playerName + "' found");

            System.out.println("\nSelect Difficulty:");
            System.out.println("1. Easy (30 minutes)");
            System.out.println("2. Medium (20 minutes)");
            System.out.println("3. Hard (15 minutes)");
            System.out.print("Enter choice: ");
            
            String diffChoice = scanner.nextLine().trim();
            Game.Difficulty difficulty;
            
            switch (diffChoice) {
                case "1":
                    difficulty = Game.Difficulty.EASY;
                    break;
                case "2":
                    difficulty = Game.Difficulty.MEDIUM;
                    break;
                case "3":
                    difficulty = Game.Difficulty.HARD;
                    break;
                default:
                    difficulty = Game.Difficulty.MEDIUM;
            }

            Game game = Game.getInstance();
            game.initializeGame(loadedPlayer, difficulty);
            
            if (facade.startGame()) {
                System.out.println("✓ Game resumed with saved progress");

                if (loadedPlayer.getProgress() != null && !loadedPlayer.getProgress().isEmpty()) {
                    Progress progress = loadedPlayer.getProgress().get(0);
                    System.out.println("\nLoaded Progress:");
                    System.out.println("- Hints Used: " + progress.getHintsUsed());
                    System.out.println("- Strikes: " + progress.getStrikes());
                    System.out.println("- Previous Score: " + progress.getCurrentScore());
                }
            }
        } else {
            System.out.println("✗ Failed to load progress");
        }
    }

    private void playGame() {
        System.out.println("\n--- PLAY GAME ---");
        
        Game game = Game.getInstance();
        
        if (game.getCurrentPlayer() == null) {
            System.out.println("No game in progress. Please start or load a game first.");
            return;
        }
        
        if (game.isOver()) {
            System.out.println("Game is already over!");
            displayFinalScore();
            return;
        }
        
        Puzzle currentPuzzle = facade.getCurrentPuzzle();
        
        if (currentPuzzle == null) {
            System.out.println("No puzzle available.");
            return;
        }
 
        System.out.println("\nCurrent Status:");
        System.out.println("- Player: " + game.getCurrentPlayer().getDisplayName());
        System.out.println("- Time Remaining: " + formatTime(game.getRemainingTime()));
        System.out.println("- Progress: " + String.format("%.1f%%", game.progressPercent()));
        System.out.println("- Score: " + game.getScore());
        System.out.println("- Puzzle: " + (game.getCurrentPuzzleIndex() + 1) + "/" + facade.getTotalPuzzles());
        System.out.println("- Strikes: " + facade.getStrikes());
        
        System.out.println("\n1. Solve Puzzle");
        System.out.println("2. Get Hint");
        System.out.println("3. Skip Puzzle");
        System.out.println("4. Pause Game");
        System.out.print("\nEnter choice: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                System.out.println("\nAttempting to solve puzzle...");
                facade.startPuzzle();
                
                System.out.print("Enter answer (or press Enter to simulate correct answer): ");
                String answer = scanner.nextLine().trim();
                
                boolean correct = true; 
                if (!answer.isEmpty()) {
                    correct = facade.submitAnswer(answer);
                }
                
                if (correct) {
                    if (facade.completePuzzle()) {
                        System.out.println("✓ Puzzle completed!");
                        game.completePuzzle();
                        
                        if (!facade.nextPuzzle()) {
                            System.out.println("\n🎉 All puzzles completed! Game Over!");
                            displayFinalScore();
                        } else {
                            System.out.println("Moving to next puzzle...");
                        }
                    }
                } else {
                    System.out.println("✗ Incorrect answer!");
                    facade.addStrike();
                    System.out.println("Strike added. Total strikes: " + facade.getStrikes());
                }
                break;
                
            case "2":
                System.out.println("\nAvailable Hints:");
                ArrayList<Hint> hints = facade.getAvailableHints();
                
                if (hints.isEmpty()) {
                    System.out.println("No hints available for this puzzle.");
                } else {
                    for (int i = 0; i < hints.size(); i++) {
                        Hint h = hints.get(i);
                        System.out.println((i + 1) + ". " + (h.isUsed() ? "[USED] " : "") + 
                                         "Cost: " + h.getCost() + " points");
                    }
                    
                    System.out.print("\nEnter hint number (or 0 to cancel): ");
                    try {
                        int hintNum = Integer.parseInt(scanner.nextLine().trim());
                        if (hintNum > 0 && hintNum <= hints.size()) {
                            Hint hint = facade.revealHint(hintNum - 1);
                            if (hint != null) {
                                System.out.println("\nHint: " + hint.getText());
                                System.out.println("Score penalty applied: -" + hint.getCost());
                                game.calculateScore();
                            } else {
                                System.out.println("Hint already used or unavailable.");
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                }
                break;
                
            case "3":
                System.out.print("\nAre you sure you want to skip? (y/n): ");
                String confirm = scanner.nextLine().trim().toLowerCase();
                if (confirm.equals("y")) {
                    facade.skipPuzzle();
                    System.out.println("✓ Puzzle skipped");
                }
                break;
                
            case "4":
                facade.pauseGame();
                System.out.println("✓ Game paused");
                System.out.print("Press Enter to resume...");
                scanner.nextLine();
                facade.resumeGame();
                System.out.println("✓ Game resumed");
                break;
                
            default:
                System.out.println("Invalid choice");
        }
    }

    private void saveGame() {
        System.out.println("\n--- SAVE PROGRESS ---");
        
        if (facade.saveProgress()) {
            System.out.println("✓ Progress saved successfully");
        } else {
            System.out.println("✗ Failed to save progress");
        }
    }

    private void displayFinalScore() {
        Game game = Game.getInstance();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("FINAL RESULTS");
        System.out.println("=".repeat(60));
        System.out.println("Player: " + game.getCurrentPlayer().getDisplayName());
        System.out.println("Puzzles Completed: " + game.getCompletedCount() + "/" + game.getPuzzles().size());
        System.out.println("Final Score: " + game.calculateScore());
        System.out.println("Time Taken: " + formatTime(game.getElapsedTime()));
        System.out.println("Strikes: " + facade.getStrikes());
        if (game.getProgress() != null) {
            System.out.println("Hints Used: " + game.getProgress().getHintsUsed());
        }
        System.out.println("=".repeat(60));

        facade.saveProgress();
    }
 
    private String formatTime(long seconds) {
        long minutes = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }
    
    public static void main(String[] args) {
        LibraryUI ui = new LibraryUI();
        ui.run();
        System.out.println("\n--- Program Complete ---");
    }
}