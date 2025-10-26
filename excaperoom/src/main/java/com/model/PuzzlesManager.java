package com.model;

import java.util.ArrayList;

public class PuzzlesManager {
    private ArrayList<Puzzle> puzzles;
    private Puzzle currentPuzzle;
    private int currentPuzzleIndex;
    private static PuzzlesManager instance;
    
    private PuzzlesManager() {
        this.puzzles = new ArrayList<>();
        this.currentPuzzle = null;
        this.currentPuzzleIndex = 0;
    }

    public static PuzzlesManager getInstance() {
        if (instance == null) {
            instance = new PuzzlesManager();
        }
        return instance;
    }
    
    public void playSound(boolean correct) {
        if (currentPuzzle != null) {
            currentPuzzle.playSound(correct);
        }
    }

    public Puzzle getCurrentPuzzle() {
        return currentPuzzle;
    }

    public void setCurrentPuzzle(Puzzle puzzle) {
        this.currentPuzzle = puzzle;

        if (puzzle != null && puzzles != null) {
            currentPuzzleIndex = puzzles.indexOf(puzzle);
            if (currentPuzzleIndex == -1) {
                currentPuzzleIndex = 0;
            }
        }
    }

    public int getCurrentPuzzleIndex() {
        return currentPuzzleIndex;
    }

    public ArrayList<Puzzle> getPuzzles() {
        return puzzles;
    }

    public int getTotalPuzzlesCount() {
        return puzzles != null ? puzzles.size() : 0;
    }

    public void addPuzzle(Puzzle puzzle) {
        if (puzzle != null && puzzles != null) {
            puzzles.add(puzzle);
        }
    }

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
 
    public ArrayList<Hint> getAvailableHints() {
        if (currentPuzzle != null) {
            return currentPuzzle.getHints();
        }
        return new ArrayList<>();
    }

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
 
    public boolean submitAnswer(String answer) {
        if (currentPuzzle != null) {
            return currentPuzzle.checkAnswer(answer);
        }
        return false;
    }

    public void startCurrentPuzzle() {
        if (currentPuzzle != null) {
            currentPuzzle.startPuzzle();
        }
    }

    public void reset() {
        currentPuzzleIndex = 0;
        if (puzzles != null && !puzzles.isEmpty()) {
            currentPuzzle = puzzles.get(0);
        } else {
            currentPuzzle = null;
        }
    }
}