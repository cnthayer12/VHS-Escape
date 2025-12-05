package com.model;

import java.util.ArrayList;

/**
 * Singleton manager class that handles all puzzle-related operations in the VHS Escape game.
 * Manages the collection of puzzles, tracks the current puzzle, and handles hint revelation,
 * puzzle navigation, and answer submission.
 * 
 * @author VHS Escape Team
 * @version 1.0
 */
public class PuzzlesManager {
    /** Collection of all puzzles in the game */
    private ArrayList<Puzzle> puzzles;
    
    /** The puzzle currently being played */
    private Puzzle currentPuzzle;
    
    /** Index of the current puzzle in the puzzles collection */
    private int currentPuzzleIndex;
    
    /** Singleton instance of PuzzlesManager */
    private static PuzzlesManager instance;
    
    /**
     * Private constructor to enforce singleton pattern.
     * Initializes the puzzles collection and sets default values.
     */
    private PuzzlesManager() {
        this.puzzles = new ArrayList<>();
        this.currentPuzzle = null;
        this.currentPuzzleIndex = 0;
    }

    /**
     * Gets the singleton instance of PuzzlesManager.
     * Creates a new instance if one doesn't exist.
     * 
     * @return the singleton instance of PuzzlesManager
     */
    public static PuzzlesManager getInstance() {
        if (instance == null) {
            instance = new PuzzlesManager();
        }
        return instance;
    }
    
    /**
     * Plays a sound effect based on the correctness of an answer.
     * Delegates to the current puzzle's playSound method.
     * 
     * @param correct true to play success sound, false to play failure sound
     */
    public void playSound(boolean correct) {
        if (currentPuzzle != null) {
            currentPuzzle.playSound(correct);
        }
    }

    /**
     * Gets the puzzle currently being played.
     * 
     * @return the current Puzzle object
     */
    public Puzzle getCurrentPuzzle() {
        return currentPuzzle;
    }

    /**
     * Sets the current puzzle and updates the current puzzle index.
     * 
     * @param puzzle the puzzle to set as current
     */
    public void setCurrentPuzzle(Puzzle puzzle) {
        this.currentPuzzle = puzzle;

        if (puzzle != null && puzzles != null) {
            currentPuzzleIndex = puzzles.indexOf(puzzle);
            if (currentPuzzleIndex == -1) {
                currentPuzzleIndex = 0;
            }
        }
    }

    /**
     * Gets the index of the current puzzle in the puzzles collection.
     * 
     * @return the current puzzle index
     */
    public int getCurrentPuzzleIndex() {
        return currentPuzzleIndex;
    }

    /**
     * Gets the collection of all puzzles.
     * 
     * @return the ArrayList of all puzzles
     */
    public ArrayList<Puzzle> getPuzzles() {
        return puzzles;
    }

    /**
     * Gets the total number of puzzles in the game.
     * 
     * @return the count of puzzles
     */
    public int getTotalPuzzlesCount() {
        return puzzles != null ? puzzles.size() : 0;
    }

    /**
     * Adds a puzzle to the collection of puzzles.
     * 
     * @param puzzle the puzzle to add
     */
    public void addPuzzle(Puzzle puzzle) {
        if (puzzle != null && puzzles != null) {
            puzzles.add(puzzle);
        }
    }

    /**
     * Advances to the next puzzle in the sequence.
     * 
     * @return true if successfully advanced to next puzzle, false if already at the last puzzle
     */
    public boolean nextPuzzle() {
        if (puzzles == null || puzzles.isEmpty()) {
            return false;
        }
        
        if (currentPuzzleIndex < puzzles.size() - 1) {
            currentPuzzleIndex++;
            currentPuzzle = puzzles.get(currentPuzzleIndex);
            return true;
        }

        return false;
    }
 
    /**
     * Gets all hints available for the current puzzle.
     * 
     * @return ArrayList of available hints, or empty list if no current puzzle
     */
    public ArrayList<Hint> getAvailableHints() {
        if (currentPuzzle != null) {
            return currentPuzzle.getHints();
        }
        return new ArrayList<>();
    }

    /**
     * Gets the number of hints used by the current player.
     * 
     * @return the number of hints used, or 0 if no player or progress exists
     */
    public int getHintsUsed() {
        if (currentPuzzle != null) {
            Player currentPlayer = Players.getCurrentPlayer();
            if (currentPlayer != null && currentPlayer.getProgress() != null && 
                !currentPlayer.getProgress().isEmpty()) {
                Progress progress = currentPlayer.getProgress().get(
                    currentPlayer.getProgress().size() - 1);
                return progress.getHintsUsed();
            }
        }
        return 0;
    }

    /**
     * Reveals a specific hint by index and marks it as used.
     * Adds the hint to the player's progress if not already used.
     * 
     * @param hintIndex the index of the hint to reveal
     * @return the revealed Hint object, or null if invalid index or no current puzzle
     */
    public Hint revealHint(int hintIndex) {
        if (currentPuzzle == null) {
            return null;
        }
        
        ArrayList<Hint> hints = currentPuzzle.getHints();
        if (hints == null || hintIndex < 0 || hintIndex >= hints.size()) {
            return null;
        }
        
        Hint hint = hints.get(hintIndex);
        
        if (!hint.isUsed()) {
            hint.markUsed();
            
            Player currentPlayer = Players.getCurrentPlayer();
            if (currentPlayer != null && currentPlayer.getProgress() != null && 
                !currentPlayer.getProgress().isEmpty()) {
                Progress progress = currentPlayer.getProgress().get(
                    currentPlayer.getProgress().size() - 1);
                progress.addHint(hint);
            }
        }
        
        return hint;
    }

    /**
     * Reveals the next unused hint for the current puzzle.
     * Automatically selects the next hint based on how many have been used.
     * 
     * @return the revealed Hint object, or null if no hints remain or no current puzzle
     */
    public Hint revealHint() {
        if (currentPuzzle != null) {
            Player currentPlayer = Players.getCurrentPlayer();
            if (currentPlayer != null && currentPlayer.getProgress() != null && 
                !currentPlayer.getProgress().isEmpty()) {
                Progress progress = currentPlayer.getProgress().get(
                    currentPlayer.getProgress().size() - 1);
                ArrayList<Hint> storedHints = progress.getStoredHints();
                ArrayList<Hint> availableHints = currentPuzzle.getHints();
                
                if (storedHints.size() >= availableHints.size()) {
                    System.out.println("No hints remaining on this puzzle!");
                    return null;
                }
                
                Hint nextHint = availableHints.get(storedHints.size());
                nextHint.markUsed();
                progress.addHint(nextHint);
                return nextHint;
            }
        }
        return null;
    }
 
    /**
     * Skips the current puzzle by completing it and adding a strike to the player's record.
     */
    public void skipCurrentPuzzle() {
        if (currentPuzzle != null) {
            currentPuzzle.completePuzzle();
            
            Player currentPlayer = Players.getCurrentPlayer();
            if (currentPlayer != null && currentPlayer.getProgress() != null && 
                !currentPlayer.getProgress().isEmpty()) {
                Progress progress = currentPlayer.getProgress().get(
                    currentPlayer.getProgress().size() - 1);
                progress.setStrikes(progress.getStrikes() + 1);
            }
        }
    }
 
    /**
     * Submits an answer for the current puzzle and checks if it's correct.
     * 
     * @param answer the answer to submit
     * @return true if the answer is correct, false otherwise
     */
    public boolean submitAnswer(String answer) {
        if (currentPuzzle != null) {
            return currentPuzzle.checkAnswer(answer);
        }
        return false;
    }

    /**
     * Starts the current puzzle.
     * Delegates to the current puzzle's startPuzzle method.
     */
    public void startCurrentPuzzle() {
        if (currentPuzzle != null) {
            currentPuzzle.startPuzzle();
        }
    }

    /**
     * Resets the puzzle manager to the first puzzle.
     * Sets the current puzzle index to 0 and the current puzzle to the first puzzle in the list.
     */
    public void reset() {
        currentPuzzleIndex = 0;
        if (puzzles != null && !puzzles.isEmpty()) {
            currentPuzzle = puzzles.get(0);
        } else {
            currentPuzzle = null;
        }
    }
}