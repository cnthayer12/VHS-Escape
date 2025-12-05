package com.model;

/**
 * Represents a PixelHunt puzzle where the player must click on a specific location
 * on the screen within a defined tolerance range.
 * 
 * @author VHS Escape Team
 * @version 1.0
 */
public class PixelHunt extends Puzzle {
    /** The x-coordinate of the correct click location */
    private int correctX;
    
    /** The y-coordinate of the correct click location */
    private int correctY;
    
    /** The pixel tolerance range for accepting a click as correct */
    private int closeness;

    /**
     * Constructs a new PixelHunt puzzle with default values.
     * Sets correctX and correctY to 0, and closeness to 5 pixels.
     */
    public PixelHunt() {
        super();
        this.correctX = 0;
        this.correctY = 0;
        this.closeness = 5; 
        this.type = "PixelHunt";
    }

    /**
     * Sets the x-coordinate of the correct click location.
     * 
     * @param correctX the x-coordinate to set
     */
    public void setCorrectX(int correctX) {
        this.correctX = correctX;
    }

    /**
     * Sets the y-coordinate of the correct click location.
     * 
     * @param correctY the y-coordinate to set
     */
    public void setCorrectY(int correctY) {
        this.correctY = correctY;
    }

    /**
     * Sets the tolerance range in pixels for accepting a click as correct.
     * 
     * @param closeness the pixel tolerance to set
     */
    public void setCloseness(int closeness) {
        this.closeness = closeness;
    }

    /**
     * Gets the x-coordinate of the correct click location.
     * 
     * @return the x-coordinate
     */
    public int getCorrectX() {
        return correctX;
    }

    /**
     * Gets the y-coordinate of the correct click location.
     * 
     * @return the y-coordinate
     */
    public int getCorrectY() {
        return correctY;
    }

    /**
     * Gets the tolerance range in pixels for accepting clicks.
     * 
     * @return the closeness value
     */
    public int getCloseness() {
        return closeness;
    }

    /**
     * Checks if a user's click is within the acceptable range of the correct location.
     * Plays appropriate sound feedback and completes the puzzle if correct.
     * 
     * @param x the x-coordinate of the user's click
     * @param y the y-coordinate of the user's click
     * @return true if the click is within the acceptable range, false otherwise
     */
    public boolean checkClick(int x, int y) {
        boolean correct = Math.abs(x - correctX) <= closeness && Math.abs(y - correctY) <= closeness;
        playSound(correct);
        if (correct) {
            completePuzzle();
        }
        return correct;
    }

    /**
     * Starts the PixelHunt puzzle and displays instructions to the player.
     */
    @Override
    public void startPuzzle() {
        super.startPuzzle();
        System.out.println("PixelHunt Puzzle started. Click around the screen to find the hidden spot!");
    }
    
    /**
     * Returns a string representation of this PixelHunt puzzle.
     * 
     * @return a descriptive string for this puzzle type
     */
    @Override
    public String toString() {
        return "A PixelHunt puzzle";
    }
}
