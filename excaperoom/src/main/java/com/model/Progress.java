package com.model;

import java.util.ArrayList;

public class Progress {

    private int hintsUsed;
    private ArrayList<String> inventory;
    private ArrayList<String> storedHints;
    private int strikes;
    private int currentScore;

    public Progress(int hintsUsed,ArrayList<String> inventory, ArrayList<String> storedHints, int strikes, int currentScore) {
        this.hintsUsed = hintsUsed;
        this.inventory = inventory;
        this.storedHints = storedHints;
        this.strikes = strikes;
        this.currentScore = currentScore;
    }

    public int getHintsUsed() {
        return hintsUsed;
    }

    public ArrayList<String> getInventory() {
        return inventory;
    }

    public ArrayList<String> getStoredHints() {
        return storedHints;
    }

    public int getStrikes() {
        return strikes;
    }

    public int getCurrentScore() {
        return currentScore;
    }
}

