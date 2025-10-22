package com.model;

import java.io.File;
import java.util.ArrayList;


public class Puzzle {
    private ArrayList<Hint> hints;
    private File correctSound;
    private File incorrectSound;
    

    public Puzzle() {
        this.hints = new ArrayList<>();
        this.correctSound = null;
        this.incorrectSound = null;
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
    
    public void playSound(boolean correct) {
     System.out.println("STUB Puzzle.playSound() called - correct: " + correct);
    }
}