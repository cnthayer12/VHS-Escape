package com.model;

import java.util.ArrayList;

public class PuzzlesManager {
    private ArrayList<Puzzle> puzzles;
    private Puzzle currentPuzzle;
    private static PuzzlesManager instance;
    
    private PuzzlesManager() {
        this.puzzles = new ArrayList<>();
        this.currentPuzzle = null;
    }

    public static PuzzlesManager getInstance() {
        if (instance == null) {
            instance = new PuzzlesManager();
        }
        return instance;
    }
    
    //Correct sound
    public void playSound(boolean correct) {
         if (currentPuzzle != null) {
            currentPuzzle.playSound(correct);
        }
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

    public ArrayList<Hint> getAvailableHints() {
        if(currentPuzzle != null)
            return currentPuzzle.getHints();
        return null;
    }

    public int getHintsUsed() {
        if(currentPuzzle != null)
        {
            Progress progress = Players.getCurrentPlayer().getProgress().get(Players.getCurrentPlayer().getProgress().size()-1);
            return progress.getHintsUsed();
        }
        return -1;
    }

    public Hint revealHint() {
        if(currentPuzzle != null)
        {
            Progress progress = Players.getCurrentPlayer().getProgress().get(Players.getCurrentPlayer().getProgress().size()-1);
            ArrayList<Hint> storedHints = progress.getStoredHints();
            if(storedHints.size() == currentPuzzle.getHints().size()) {
                System.out.println("No hints remaining on this puzzle!");
                return null;
            }
            progress.addHint(currentPuzzle.getHints().get(storedHints.size()));
            return currentPuzzle.getHints().get(storedHints.size());
        }
        return null;
    }

    public void skipCurrentPuzzle() {
        if(currentPuzzle != null)
        {
            currentPuzzle.completePuzzle();
            Progress progress = Players.getCurrentPlayer().getProgress().get(Players.getCurrentPlayer().getProgress().size()-1);
            progress.setStrikes(progress.getStrikes()+1);
        }
    }

    public boolean submitAnswer(String answer) {
        return currentPuzzle.checkAnswer(answer);
    }

    public void startPuzzle() {
        if(currentPuzzle != null)
            currentPuzzle.startPuzzle();
    }
}