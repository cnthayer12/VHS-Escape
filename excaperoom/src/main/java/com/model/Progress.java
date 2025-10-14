package com.model;

import java.util.ArrayList;

public class Progress {

    private int hintsUsed;
    private ArrayList<Item> inventory;
    private ArrayList<Hint> storedHints;
    private int strikes;
    private int currentScore;

    public Progress(int hintsUsed,ArrayList<Item> inventory, ArrayList<Hint> storedHints, int strikes, int currentScore) {
        this.hintsUsed = hintsUsed;
        this.inventory = inventory;
        this.storedHints = storedHints;
        this.strikes = strikes;
        this.currentScore = currentScore;
    }

    public int getHintsUsed() {
        return hintsUsed;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public ArrayList<Hint> getStoredHints() {
        return storedHints;
    }

    public int getStrikes() {
        return strikes;
    }

    public int getCurrentScore() {
        return currentScore;
    }
}

