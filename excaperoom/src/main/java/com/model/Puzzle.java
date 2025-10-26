package com.model;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;


public class Puzzle {
    private ArrayList<Hint> hints;
    private File correctSound;
    private File incorrectSound;
    private UUID puzzleID;
    

    public Puzzle() {
        this.hints = new ArrayList<>();
        this.correctSound = null;
        this.incorrectSound = null;
        this.puzzleID = UUID.randomUUID();
    }
    
    // Puzzle actions
    public void startPuzzle() {
        PuzzlesManager.getInstance().setCurrentPuzzle(this);
        Game.getInstance().resume();
        EscapeGameFacade.getInstance().saveProgress();
    }
 
    public boolean completePuzzle() {
        playSound(true);
        EscapeGameFacade.getInstance().saveProgress();
        EscapeGameFacade.getInstance().nextPuzzle();
        return true;
    }

    public void skip() {
        EscapeGameFacade.getInstance().nextPuzzle();
        EscapeGameFacade.getInstance().saveProgress();
    }
    
    // Hints
    public ArrayList<Hint> getHints() {
        return hints;
    }
    
    public void addHint(Hint hint) {
        if (hint != null && hints != null) {
            hints.add(hint);
        }
    }

    public void setHints(ArrayList<Hint> hints) {
        this.hints = hints;
    }

    public UUID getID() {
        return puzzleID;
    }

    public void setID(UUID puzzleID) {
        this.puzzleID = puzzleID;
    }
    
    public void playSound(boolean correct) {
     System.out.println("STUB Puzzle.playSound() called - correct: " + correct);
    }

    public boolean checkAnswer(String answer) {
        return false;
    }
}