package com.model;

import java.time.Instant;
import java.util.ArrayList;

/**
 * Game class - Represents a game session
 * STUB VERSION
 */
public class Game {
    private String gameID;
    private Players player;
    private Instant startTime;
    private Instant endTime;
    //private Difficulty difficulty;
    private int score;
    private boolean isPaused;
    private boolean isOver;
    private ArrayList<Puzzle> puzzles;
    private int completedCount;
    private String story;
    private static Game instance;
    private Progress progress;
    private Player currentPlayer;
    
    /**
     * Constructor
     */
    public Game() {
        this.gameID = "game-001";
        this.score = 0;
        this.isPaused = false;
        this.isOver = false;
        this.puzzles = new ArrayList<>();
        this.completedCount = 0;
        this.story = "VHS Escape Room Story";
    }
    
    /**
     * Get singleton instance - STUB
     */
    public static Game getInstance() {
        System.out.println("STUB Game.getInstance() called");
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }
    
    /**
     * Calculate score - STUB
     */
    public int calculateScore() {
        System.out.println("STUB Game.calculateScore() called");
        return score;
    }
    
    /**
     * Delete game - STUB
     */
    public void deleteGame() {
        System.out.println("STUB Game.deleteGame() called");
    }
    
    /**
     * Pause game - STUB
     */
    public void pause() {
        System.out.println("STUB Game.pause() called");
        isPaused = true;
    }
    
    /**
     * Resume game - STUB
     */
    public void resume() {
        System.out.println("STUB Game.resume() called");
        isPaused = false;
    }
    
    /**
     * Get progress percent - STUB
     */
    public double progressPercent() {
        System.out.println("STUB Game.progressPercent() called");
        return 0.0;
    }
    
    /**
     * Go to next puzzle - STUB
     */
    public boolean nextPuzzle() {
        System.out.println("STUB Game.nextPuzzle() called");
        return true;
    }
    
    /**
     * Exit to main menu - STUB
     */
    public void exitMain() {
        System.out.println("STUB Game.exitMain() called");
    }
    
    /**
     * Display story - STUB
     */
    public void displayStory() {
        System.out.println("STUB Game.displayStory() called");
        System.out.println(story);
    }
    
    /**
     * Get game ID - STUB
     */
    public String getGameID() {
        return gameID;
    }
    
    /**
     * Get score - STUB
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Set score - STUB
     */
    public void setScore(int score) {
        this.score = score;
    }
    
    /**
     * Check if game is over - STUB
     */
    public boolean isOver() {
        return isOver;
    }
}