package com.model;

import java.util.UUID;

public class User {
    private UUID id;
    private String displayName;
    private int hintsUsed;
    private String[] inventory; 
    private String[] storedHints;
    private int strikes;
    private int currentScore; 

    public User(String displayName, int hintsUsed, String[] inventory, String[] storedHints, int strikes, int currentScore) {
        this.id = UUID.randomUUID();
        this.displayName = displayName;
        this.hintsUsed = hintsUsed;
        this.inventory = inventory;
        this.storedHints = storedHints;
        this.strikes = strikes;
        this.currentScore = currentScore;
    }

    public User(UUID id, String displayName, int hintsUsed, String[] inventory, String[] storedHints, int strikes, int currentScore) {
        this.id = id; 
        this.displayName = displayName;
        this.hintsUsed = hintsUsed;
        this.inventory = inventory;
        this.storedHints = storedHints;
        this.strikes = strikes;
        this.currentScore = currentScore;
    }

    public UUID getId() {
		return id;
	}

    public String getDisplayName() {
        return displayName;
    }

    public String toString() {
		return displayName;
	}

    public int hintsUsed() {
        return hintsUsed;
    }

    public String[] getInventory() {
        return inventory;
    }

    public String[] getStoredHints() {
        return storedHints;
    }

    public int getStrikes() {
        return strikes;
    }

    public int getCurrentScore() {
        return currentScore;
    }

}
