package com.model;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Abstract base class for all puzzle types in the VHS Escape game.
 * Provides common functionality for puzzle management, hints, sounds, and lifecycle methods.
 * 
 * @author VHS Escape Team
 * @version 1.0
 */
public class Puzzle {
    /** Collection of hints available for this puzzle */
    private ArrayList<Hint> hints;
    
    /** Sound file to play when the puzzle is solved correctly */
    private File correctSound;
    
    /** Sound file to play when an incorrect answer is given */
    private File incorrectSound;
    
    /** Unique identifier for this puzzle */
    private UUID puzzleID;
    
    /** The type of puzzle (e.g., "Trivia", "Riddle", "PixelHunt") */
    protected String type;

    /**
     * Constructs a new Puzzle with default values.
     * Initializes hints list, generates a unique puzzle ID, and sets null sound files.
     */
    public Puzzle() {
        this.hints = new ArrayList<>();
        this.correctSound = null;
        this.incorrectSound = null;
        this.puzzleID = UUID.randomUUID();
        this.type = "";
    }

    /**
     * Gets the type of this puzzle.
     * 
     * @return the puzzle type as a String
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of this puzzle.
     * 
     * @param type the puzzle type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Starts the puzzle by setting it as the current puzzle, resuming the game,
     * and saving progress.
     */
    public void startPuzzle() {
        PuzzlesManager.getInstance().setCurrentPuzzle(this);
        Game.getInstance().resume();
        EscapeGameFacade.getInstance().saveProgress();
    }
 
    /**
     * Completes the puzzle by playing the success sound, saving progress,
     * and advancing to the next puzzle.
     * 
     * @return true when the puzzle is successfully completed
     */
    public boolean completePuzzle() {
        playSound(true);
        EscapeGameFacade.getInstance().saveProgress();
        EscapeGameFacade.getInstance().nextPuzzle();
        return true;
    }

    /**
     * Skips the current puzzle and advances to the next one.
     * Saves progress after skipping.
     */
    public void skip() {
        EscapeGameFacade.getInstance().nextPuzzle();
        EscapeGameFacade.getInstance().saveProgress();
    }
    
    /**
     * Gets the collection of hints available for this puzzle.
     * 
     * @return the ArrayList of Hint objects
     */
    public ArrayList<Hint> getHints() {
        return hints;
    }
    
    /**
     * Adds a hint to this puzzle's collection of available hints.
     * 
     * @param hint the hint to add
     */
    public void addHint(Hint hint) {
        if (hint != null && hints != null) {
            hints.add(hint);
        }
    }

    /**
     * Sets the collection of hints for this puzzle.
     * 
     * @param hints the ArrayList of hints to set
     */
    public void setHints(ArrayList<Hint> hints) {
        this.hints = hints;
    }

    /**
     * Gets the unique identifier for this puzzle.
     * 
     * @return the puzzle's UUID
     */
    public UUID getID() {
        return puzzleID;
    }

    /**
     * Sets the unique identifier for this puzzle.
     * 
     * @param puzzleID the UUID to set
     */
    public void setID(UUID puzzleID) {
        this.puzzleID = puzzleID;
    }
    
    /**
     * Plays a sound effect based on whether the answer is correct or incorrect.
     * Currently a stub implementation that prints to console.
     * 
     * @param correct true to play success sound, false to play failure sound
     */
    public void playSound(boolean correct) {
        System.out.println("STUB Puzzle.playSound() called - correct: " + correct);
    }

    /**
     * Checks if the provided answer is correct for this puzzle.
     * This is a base implementation that always returns false.
     * Subclasses should override this method with specific logic.
     * 
     * @param answer the answer to check
     * @return true if the answer is correct, false otherwise
     */
    public boolean checkAnswer(String answer) {
        return false;
    }

    /**
     * Returns a string representation of this puzzle.
     * Uses dynamic dispatch based on the puzzle type to return
     * the appropriate string representation from subclasses.
     * 
     * @return a string describing the puzzle
     */
    @Override
    public String toString() {
        String returnString = "";
        if(type.equals("Trivia"))
            returnString = ((Trivia) this).toString();
        if(type.equals("Riddle"))
            returnString = ((Riddle) this).toString();
        if(type.equals("MultipleChoice"))
            returnString = ((MultipleChoice) this).toString();
        if(type.equals("PixelHunt"))
            returnString = ((PixelHunt) this).toString();
        if(type.equals("ItemPuzzle"))
            returnString = ((ItemPuzzle) this).toString();
        return returnString;
    }
}