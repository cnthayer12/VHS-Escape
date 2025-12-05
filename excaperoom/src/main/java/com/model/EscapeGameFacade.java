package com.model;

import java.util.ArrayList;

import com.model.Leaderboard.LeaderboardEntry;

/**
 * Facade for the VHS Escape game system.
 * <p>
 * This class provides a simplified, high-level API for interacting with the core
 * subsystems:
 * <ul>
 *     <li>{@link Game} – overall game flow, timing, and scoring</li>
 *     <li>{@link PuzzlesManager} – current puzzle, hints, and puzzle progression</li>
 *     <li>{@link Players} – player accounts, inventory, strikes, and persistence</li>
 *     <li>{@link Leaderboard} – ranking and score entries</li>
 * </ul>
 * Client code (such as {@link Driver}) should use this facade instead of
 * calling subsystem classes directly.
 */
public class EscapeGameFacade {

    /** The active game instance being played. */
    private Game game;

    /** Manager responsible for current puzzle and puzzle list. */
    private PuzzlesManager puzzlesManager;

    /** Manager for player accounts, current player, and progress. */
    private Players players;

    /** Singleton instance of the facade. */
    private static EscapeGameFacade instance;

    /**
     * Name or identifier of the current item being interacted with.
     * This can be used by the UI to track contextual item actions.
     */
    private String item = "";

    /**
     * Private constructor to enforce the singleton pattern.
     * Initializes references to the shared {@link Game}, {@link PuzzlesManager},
     * and {@link Players} instances.
     */
    private EscapeGameFacade() {
        this.game = Game.getInstance();
        this.puzzlesManager = PuzzlesManager.getInstance();
        this.players = Players.getInstance();
    }

    /**
     * Returns the singleton instance of {@code EscapeGameFacade}.
     *
     * @return the shared {@code EscapeGameFacade} instance
     */
    public static EscapeGameFacade getInstance() {
        if (instance == null) {
            instance = new EscapeGameFacade();
        }
        return instance;
    }

    /**
     * Starts a new game session for the given player at the specified difficulty.
     *
     * @param player     the player who is starting the game
     * @param difficulty the selected game difficulty
     */
    public void startGame(Player player, Game.Difficulty difficulty) {
        game.initializeGame(player, difficulty);
    }

    /**
     * Loads player progress and state from persistent storage.
     *
     * @return {@code true} if progress was loaded successfully; {@code false} otherwise
     */
    public boolean loadProgress() {
        return players.loadProgress();
    }

    /**
     * Pauses the current game, if one is running.
     */
    public void pauseGame() {
        game.pause();
    }

    /**
     * Resumes a previously paused game.
     */
    public void resumeGame() {
        game.resume();
    }

    /**
     * Ends the current game and exits the main game loop.
     * Marks the game as having ended successfully before exiting.
     */
    public void endGame() {
        game.endGame(true);
        game.exitMain();
    }

    /**
     * Returns the remaining time in the current game session.
     *
     * @return remaining time in milliseconds
     */
    public long getRemainingTime() {
        return game.getRemainingTime();
    }

    /**
     * Returns the elapsed time in the current game session.
     *
     * @return elapsed time in milliseconds
     */
    public long getElapsedTime() {
        return game.getElapsedTime();
    }

    /**
     * Starts the currently selected puzzle using the {@link PuzzlesManager}.
     */
    public void startPuzzle() {
        puzzlesManager.startCurrentPuzzle();
    }

    /**
     * Marks the current puzzle as completed within the {@link Game}.
     */
    public void completePuzzle() {
        game.completePuzzle();
    }

    /**
     * Skips the current puzzle and moves on according to the puzzle manager's logic.
     */
    public void skipPuzzle() {
        puzzlesManager.skipCurrentPuzzle();
    }

    /**
     * Advances to the next puzzle in the game, if available.
     *
     * @return {@code true} if there was a next puzzle to move to;
     *         {@code false} if the current puzzle was already the last one
     */
    public boolean nextPuzzle() {
        return game.nextPuzzle();
    }

    /**
     * Submits an answer for the current puzzle.
     *
     * @param answer the player's answer to the current puzzle
     * @return {@code true} if the answer is correct; {@code false} otherwise
     */
    public boolean submitAnswer(String answer) {
        return puzzlesManager.submitAnswer(answer);
    }

    /**
     * Returns overall game progress as a percentage.
     *
     * @return progress from {@code 0.0} to {@code 100.0}
     */
    public double getProgressPercent() {
        return game.progressPercent();
    }

    /**
     * Creates a new player account and immediately saves progress.
     *
     * @param username the desired username or display name
     * @param pass     the desired password
     * @return a status code from {@link Players#createAccount(String, String)}
     */
    public int createPlayer(String username, String pass) {
        int returnVal = players.createAccount(username, pass);
        saveProgress();
        return returnVal;
    }

    /**
     * Attempts to log in with the given credentials and immediately saves progress.
     *
     * @param username the username to log in with
     * @param pass     the password for the account
     * @return {@code false} (current implementation does not return the login result)
     */
    public boolean login(String username, String pass) {
        players.login(username, pass);
        saveProgress();
        return false;
    }

    /**
     * Returns the currently logged-in player.
     *
     * @return the current {@link Player}, or {@code null} if no player is logged in
     */
    public Player getCurrentPlayer() {
        return Players.getCurrentPlayer();
    }

    /**
     * Logs out the current player and saves progress.
     */
    public void logout() {
        players.logout();
        saveProgress();
    }

    /**
     * Displays the game story using the {@link Game} subsystem.
     */
    public void displayStory() {
        game.displayStory();
    }

    /**
     * Returns the list of hints currently available for the active puzzle.
     *
     * @return a list of available {@link Hint} objects, possibly empty
     */
    public ArrayList<Hint> getAvailableHints() {
        return puzzlesManager.getAvailableHints();
    }

    /**
     * Reveals the next available hint for the current puzzle.
     *
     * @return the {@link Hint} that was revealed, or {@code null} if none remain
     */
    public Hint revealHint() {
        return puzzlesManager.revealHint();
    }

    /**
     * Returns the number of hints used in the current puzzle or game.
     *
     * @return the count of hints used
     */
    public int getHintsUsed() {
        return puzzlesManager.getHintsUsed();
    }

    /**
     * Returns the number of strikes accumulated by the current player.
     *
     * @return the player's strike count
     */
    public int getStrikes() {
        return players.getStrikes();
    }

    /**
     * Adds a strike to the current player's record.
     */
    public void addStrike() {
        players.addStrike();
    }

    /**
     * Resets the current player's strikes back to zero.
     */
    public void resetStrikes() {
        players.resetStrikes();
    }

    /**
     * Updates the game's score to the specified value.
     *
     * @param points the new score value
     */
    public void updateScore(int points) {
        game.setScore(points);
    }

    /**
     * Returns the current game score.
     *
     * @return the current score
     */
    public int getScore() {
        return game.getScore();
    }

    /**
     * Returns the currently active puzzle.
     *
     * @return the current {@link Puzzle}, or {@code null} if none is active
     */
    public Puzzle getCurrentPuzzle() {
        return puzzlesManager.getCurrentPuzzle();
    }

    /**
     * Returns all puzzles managed by the {@link PuzzlesManager}.
     *
     * @return a list of all {@link Puzzle} instances currently loaded
     */
    public ArrayList<Puzzle> getAllPuzzles() {
        return puzzlesManager.getPuzzles();
    }

    /**
     * Returns the total number of puzzles in the current game or session.
     *
     * @return the total puzzle count
     */
    public int getTotalPuzzles() {
        return puzzlesManager.getTotalPuzzlesCount();
    }

    /**
     * Returns the index of the current puzzle in the game sequence.
     *
     * @return the zero-based index of the current puzzle
     */
    public int getCurrentPuzzleIndex() {
        return game.getCurrentPuzzleIndex();
    }

    /**
     * Saves all player progress and state to persistent storage.
     */
    public void saveProgress() {
        players.saveProgress();
    }

    /**
     * Returns the current leaderboard entries.
     *
     * @return a list of {@link LeaderboardEntry} objects representing the leaderboard
     */
    public ArrayList<LeaderboardEntry> getLeaderboard() {
         return Leaderboard.getInstance().getAllEntries();
    }

    /**
     * Adds an item to the current player's inventory.
     *
     * @param item the {@link Item} to add
     */
    public void addItem(Item item) {
        players.addItem(item);
    }

    /**
     * Sets the identifier or name of the current item being interacted with.
     *
     * @param item the item name or identifier
     */
    public void setCurrentItem(String item) {
        this.item = item;
    }

    /**
     * Returns the identifier or name of the current item being interacted with.
     *
     * @return the current item name or identifier
     */
    public String getCurrentItem() {
        return item;
    }

    /**
     * Generates a completion certificate for the current player based on
     * the game's difficulty and score.
     */
    public void generateCompletionCertificate() {
        players.generateCertificate(game.getDifficulty(), game.getScore());
    }

    /**
     * Displays or logs the current player's progress using the {@link Players} subsystem.
     */
    public void checkProgress() {
        players.checkProgress();
    }

    /**
     * Gives an item directly to the current player by adding it to their inventory.
     * This is functionally equivalent to {@link #addItem(Item)}.
     *
     * @param item the {@link Item} to grant to the player
     */
    public void giveItem(Item item) {
        players.addItem(item);
    }
}
