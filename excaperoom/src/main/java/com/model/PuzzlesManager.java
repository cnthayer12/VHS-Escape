package com.model;

import java.util.ArrayList;

/**
 * PuzzlesManager class - STUB
 */
public class PuzzlesManager {
    private ArrayList<Puzzle> puzzles;
    private Puzzle currentPuzzle;
    private static PuzzlesManager instance;
    
    private PuzzlesManager() {
        this.puzzles = new ArrayList<>();
        this.currentPuzzle = null;
    }

    public static PuzzlesManager getInstance() {
        System.out.println("[STUB] PuzzlesManager.getInstance() called");
        if (instance == null) {
            instance = new PuzzlesManager();
        }
        return instance;
    }
    
    //Correct sound
    public void playSound(boolean correct) {
        System.out.println("[STUB] PuzzlesManager.playSound() called - correct: " + correct);
    }
    
    // get current puzzle
    public Puzzle getCurrentPuzzle() {
        return currentPuzzle;
    }
    
    public void setCurrentPuzzle(Puzzle puzzle) {
        this.currentPuzzle = puzzle;
    }

    public ArrayList<Puzzle> getPuzzles() {
        return puzzles;
    }

    public void addPuzzle(Puzzle puzzle) {
        if (puzzle != null && puzzles != null) {
            puzzles.add(puzzle);
        }
    }
}