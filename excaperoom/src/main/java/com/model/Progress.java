package com.model;

import java.util.ArrayList;

/**
 * Tracks a player's progress through the VHS Escape game.
 * Maintains information about completed puzzles, inventory, hints used,
 * strikes accumulated, and current score.
 * 
 * @author VHS Escape Team
 * @version 1.0
 */
public class Progress {
    /** Number of hints used by the player */
    private int hintsUsed;
    
    /** Collection of items in the player's inventory */
    private ArrayList<Item> inventory;
    
    /** Collection of hints that have been revealed to the player */
    private ArrayList<Hint> storedHints;
    
    /** Collection of puzzles the player has completed */
    private ArrayList<Puzzle> completedPuzzles;
    
    /** The puzzle the player is currently working on */
    private Puzzle currentPuzzle;
    
    /** Number of strikes (incorrect attempts or skips) accumulated */
    private int strikes;
    
    /** The player's current score */
    private int currentScore;

    /**
     * Constructs a Progress object with specified values.
     * 
     * @param inventory the items in the player's inventory
     * @param storedHints the hints that have been revealed
     * @param completedPuzzles the puzzles that have been completed
     * @param currentPuzzle the puzzle currently being worked on
     * @param strikes the number of strikes accumulated
     * @param currentScore the player's current score
     */
    public Progress(ArrayList<Item> inventory, ArrayList<Hint> storedHints, ArrayList<Puzzle> completedPuzzles, 
                    Puzzle currentPuzzle, int strikes, int currentScore) {
        this.hintsUsed = storedHints.size();
        this.inventory = inventory;
        this.storedHints = storedHints;
        this.completedPuzzles = completedPuzzles;
        this.currentPuzzle = currentPuzzle;
        this.strikes = strikes;
        this.currentScore = currentScore;
    }

    /**
     * Constructs a new Progress object with default values.
     * Initializes empty collections and sets all counters to 0.
     */
    public Progress() {
        hintsUsed = 0;
        inventory = new ArrayList<>();
        storedHints = new ArrayList<>();
        completedPuzzles = new ArrayList<>();
        currentPuzzle = null;
        strikes = 0;
        currentScore = 0;
    }

    /**
     * Loads the most recent progress record for a given player.
     * 
     * @param player the player whose progress to load
     * @return the most recent Progress object, or null if none exists
     */
    public static Progress loadProgress(Player player) {
        if (player == null) {
            return null;
        }
        
        ArrayList<Progress> progressList = player.getProgress();
        if (progressList == null || progressList.isEmpty()) {
            return null;
        }
        
        return progressList.get(progressList.size() - 1);
    }

    /**
     * Gets the number of hints used.
     * 
     * @return the number of hints used
     */
    public int getHintsUsed() {
        return hintsUsed;
    }

    /**
     * Gets the player's inventory.
     * 
     * @return the ArrayList of items in inventory
     */
    public ArrayList<Item> getInventory() {
        return inventory;
    }

    /**
     * Gets the collection of hints that have been revealed.
     * 
     * @return the ArrayList of stored hints
     */
    public ArrayList<Hint> getStoredHints() {
        return storedHints;
    }

    /**
     * Gets the number of strikes accumulated.
     * 
     * @return the number of strikes
     */
    public int getStrikes() {
        return strikes;
    }

    /**
     * Gets the player's current score.
     * 
     * @return the current score
     */
    public int getCurrentScore() {
        return currentScore;
    }

    /**
     * Gets the puzzle currently being worked on.
     * 
     * @return the current Puzzle object
     */
    public Puzzle getCurrentPuzzle() {
        return currentPuzzle;
    }

    /**
     * Gets the collection of completed puzzles.
     * 
     * @return the ArrayList of completed puzzles
     */
    public ArrayList<Puzzle> getCompletedPuzzles() {
        return completedPuzzles;
    }

    /**
     * Adds a puzzle to the collection of completed puzzles.
     * 
     * @param puzzle the puzzle to add
     */
    public void addCompletedPuzzle(Puzzle puzzle) {
        if(puzzle != null && completedPuzzles != null)
            completedPuzzles.add(puzzle);
    }
    
    /**
     * Adds an item to the player's inventory.
     * 
     * @param item the item to add
     */
    public void addItem(Item item) {
        if(item != null && inventory != null)
            inventory.add(item);
    }

    /**
     * Adds a hint to the stored hints and increments the hints used counter.
     * 
     * @param hint the hint to add
     */
    public void addHint(Hint hint) {
        if(hint != null && storedHints != null) {
            storedHints.add(hint);
            hintsUsed++;
        }
    }

    /**
     * Sets the collection of completed puzzles.
     * 
     * @param completedPuzzles the ArrayList of completed puzzles to set
     */
    public void setCompletedPuzzles(ArrayList<Puzzle> completedPuzzles) {
        this.completedPuzzles = completedPuzzles;
    }
    
    /**
     * Sets the player's inventory.
     * 
     * @param inventory the ArrayList of items to set
     */
    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }

    /**
     * Sets the stored hints and updates the hints used counter.
     * 
     * @param storedHints the ArrayList of hints to set
     */
    public void setStoredHints(ArrayList<Hint> storedHints) {
        this.storedHints = storedHints;
        hintsUsed = storedHints.size();
    }

    /**
     * Sets the player's current score.
     * 
     * @param score the score to set
     */
    public void setScore(int score) {
        currentScore = score;
    }

    /**
     * Sets the number of strikes.
     * 
     * @param strikes the number of strikes to set
     */
    public void setStrikes(int strikes) {
        this.strikes = strikes;
    }

    /**
     * Sets the current puzzle being worked on.
     * 
     * @param puzzle the puzzle to set as current
     */
    public void setCurrentPuzzle(Puzzle puzzle) {
        currentPuzzle = puzzle;
    }

    /**
     * Returns a string representation of this Progress.
     * Includes completion percentage and information about completed puzzles and hints.
     * 
     * @return a formatted string describing the progress
     */
    @Override
    public String toString() {
        return "Percent Complete: " + calculatePercent() + "\nQuestions Answered and Hints Used: " + getPuzzlesInfo();
    }

    /**
     * Calculates the percentage of puzzles completed out of total puzzles.
     * 
     * @return the completion percentage as a double
     */
    public double calculatePercent() {
        final int PUZZLECOUNT = 3;
        return (completedPuzzles.size() / (double) PUZZLECOUNT) * 100;
    }

    /**
     * Generates a formatted string with information about completed puzzles
     * and the hints used for each puzzle.
     * 
     * @return a string listing completed puzzles and associated hints
     */
    public String getPuzzlesInfo() {
        String returnString = "";
        for(Puzzle puzzle : completedPuzzles) {
            returnString += puzzle.toString();
            for(Hint hint : storedHints) {
                if(hint.getPuzzle().equals(puzzle))
                    returnString += "\n     Hint used: " + hint.toString();
            }
            returnString += "\n"; 
        }
        return returnString;
    }
}

