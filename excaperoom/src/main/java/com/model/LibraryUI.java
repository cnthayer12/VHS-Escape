package com.model;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Text-based user interface for the VHS Escape Room game.
 * <p>
 * {@code LibraryUI} provides a console menu that allows a user to:
 * <ul>
 *     <li>Create a new player and start a new game</li>
 *     <li>Load an existing player and resume with chosen difficulty</li>
 *     <li>Play through puzzles using the {@link EscapeGameFacade}</li>
 *     <li>View final results once the game is complete</li>
 * </ul>
 * It acts as a thin presentation layer on top of the facade and core game model.
 */
public class LibraryUI {

    /** Facade used to interact with the core game, player, and puzzle systems. */
    private EscapeGameFacade facade;

    /** Scanner for reading user input from the console. */
    private Scanner scanner;

    /**
     * Constructs a new {@code LibraryUI} and initializes the facade
     * and input scanner.
     */
    public LibraryUI() {
        facade = EscapeGameFacade.getInstance();
        scanner = new Scanner(System.in);
    }

    /**
     * Main UI loop for the VHS Escape Room.
     * <p>
     * Displays a welcome message and repeatedly shows the main menu until
     * the user chooses to exit. Delegates user choices to helper methods
     * such as {@link #startNewGame()}, {@link #loadPlayer()}, and
     * {@link #playGame()}.
     */
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
                    running = false;
                    System.out.println("\nExiting VHS Escape Room. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        
        scanner.close();
    }

    /**
     * Prints the initial welcome banner for the VHS Escape Room.
     */
    private void displayWelcome() {
        System.out.println("           Welcome to the VHS Escape Room!");
        System.out.println("=".repeat(60) + "\n");
    }

    /**
     * Displays the main menu options to the user.
     * <p>
     * Note: The printed menu includes options 1–5, but only options 1–4
     * are currently handled in {@link #run()}.
     */
    private void displayMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Start New Game");
        System.out.println("2. Load Player");
        System.out.println("3. Play Game");
        System.out.println("4. View Leaderboard");
        System.out.println("5. Exit");
        System.out.print("\nEnter choice: ");
    }

    /**
     * Handles the flow for starting a brand-new game.
     * <p>
     * This method:
     * <ul>
     *     <li>Prompts for a player name and creates a new player account.</li>
     *     <li>Prompts for difficulty and starts a game at that difficulty.</li>
     *     <li>Displays the story, difficulty, time limit, and starting score.</li>
     * </ul>
     * The new player is looked up from {@link Players} after creation and passed
     * to {@link EscapeGameFacade#startGame(Player, Game.Difficulty)}.
     */
    private void startNewGame() {
        System.out.println("\n--- START NEW GAME ---");
        
        System.out.print("Enter player name: ");
        String playerName = scanner.nextLine().trim();
        
        if (playerName.isEmpty()) {
            System.out.println("Invalid name.");
            return;
        }

        facade.createPlayer(playerName, playerName);
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
        for (Player p : players.getPlayers()) {
            if (p.getDisplayName().equals(playerName)) {
                newPlayer = p;
                break;
            }
        }
        
        if (newPlayer == null) {
            System.out.println("✗ Error: Could not find created player");
            return;
        }
  
        facade.startGame(newPlayer, difficulty);
        System.out.println("✓ Game started successfully!");
        
        facade.displayStory();
        System.out.println("\nDifficulty: " + difficulty);
        System.out.println("Time Limit: " + (difficulty.getTimeLimit() / 60) + " minutes");
        System.out.println("Starting Score: " + facade.getScore());
    }

    /**
     * Handles the flow for loading an existing player and starting a game.
     * <p>
     * This method:
     * <ul>
     *     <li>Prompts for a player name.</li>
     *     <li>Loads progress from disk via {@link EscapeGameFacade#loadProgress()}.</li>
     *     <li>Finds the matching player within {@link Players}.</li>
     *     <li>Allows the user to choose a difficulty.</li>
     *     <li>Starts a game with the loaded player and prints existing progress stats.</li>
     * </ul>
     */
    private void loadPlayer() {
        System.out.println("\n--- LOAD PLAYER ---");
        
        System.out.print("Enter player name: ");
        String playerName = scanner.nextLine().trim();
        
        if (playerName.isEmpty()) {
            System.out.println("Invalid name.");
            return;
        }

        facade.loadProgress();
        System.out.println("✓ Progress loaded from file");

        Players players = Players.getInstance();
        Player loadedPlayer = null;
        
        for (Player p : players.getPlayers()) {
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
        
        facade.startGame(loadedPlayer, difficulty);
        System.out.println("✓ Game resumed with saved progress");
        
        if (loadedPlayer.getProgress() != null && !loadedPlayer.getProgress().isEmpty()) {
            Progress progress = loadedPlayer.getProgress().get(0);
            System.out.println("\nLoaded Progress:");
            System.out.println("- Hints Used: " + progress.getHintsUsed());
            System.out.println("- Strikes: " + progress.getStrikes());
            System.out.println("- Previous Score: " + progress.getCurrentScore());
        }
    }

    /**
     * Main interaction loop for a single game turn.
     * <p>
     * This method:
     * <ul>
     *     <li>Checks that a game is active and not over.</li>
     *     <li>Displays the current game status (time, score, progress, etc.).</li>
     *     <li>Provides options to solve a puzzle, get a hint, skip a puzzle, or pause.</li>
     *     <li>Updates strikes, score, and puzzle progression via the facade and {@link Game}.</li>
     * </ul>
     */
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
        System.out.println("- Progress: " + String.format("%.1f%%", facade.getProgressPercent()));
        System.out.println("- Score: " + facade.getScore());
        System.out.println("- Puzzle: " + (facade.getCurrentPuzzleIndex() + 1) + "/" + facade.getTotalPuzzles());
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
                    facade.completePuzzle();
                    System.out.println("✓ Puzzle completed!");
                    
                    if (!facade.nextPuzzle()) {
                        System.out.println("\n🎉 All puzzles completed! Game Over!");
                        displayFinalScore();
                    } else {
                        System.out.println("Moving to next puzzle...");
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
                
                if (hints == null || hints.isEmpty()) {
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
                            Hint hint = facade.revealHint();
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

    /**
     * Displays the final game results for the current player, including:
     * <ul>
     *     <li>Puzzles completed</li>
     *     <li>Final score (recalculated via {@link Game#calculateScore()})</li>
     *     <li>Time taken</li>
     *     <li>Strikes and hints used</li>
     * </ul>
     */
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
        System.out.println("Hints Used: " + facade.getHintsUsed());
        System.out.println("=".repeat(60));
    }

    /**
     * Formats a number of seconds into {@code mm:ss} format.
     *
     * @param seconds total seconds
     * @return formatted time string
     */
    private String formatTime(long seconds) {
        long minutes = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    /**
     * Entry point for running the VHS Escape Room console UI.
     *
     * @param args command-line arguments (ignored)
     */
    public static void main(String[] args) {
        LibraryUI ui = new LibraryUI();
        ui.run();
        System.out.println("\n--- Program Complete ---");
    }
}
