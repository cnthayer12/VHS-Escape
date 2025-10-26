package com.model;

public class PixelHunt extends Puzzle {
    private int correctX;
    private int correctY;
    private int closeness;

    public PixelHunt() {
        super();
        this.correctX = 0;
        this.correctY = 0;
        this.closeness = 5; 
        this.type = "PixelHunt";
    }

    public void setCorrectX(int correctX) {
        this.correctX = correctX;
    }

    public void setCorrectY(int correctY) {
        this.correctY = correctY;
    }

    public void setCloseness(int closeness) {
        this.closeness = closeness;
    }

    public int getCorrectX() {
        return correctX;
    }

    public int getCorrectY() {
        return correctY;
    }

    public int getCloseness() {
        return closeness;
    }

    /**
     * Called when the user clicks on the screen
     */
    public boolean checkClick(int x, int y) {
        boolean correct = Math.abs(x - correctX) <= closeness && Math.abs(y - correctY) <= closeness;
        playSound(correct);
        if (correct) {
            completePuzzle();
        }
        return correct;
    }

    @Override
    public void startPuzzle() {
        super.startPuzzle();
        System.out.println("PixelHunt Puzzle started. Click around the screen to find the hidden spot!");
    }
    
    @Override
    public String toString() {
        return "A PixelHunt puzzle";
    }
}
