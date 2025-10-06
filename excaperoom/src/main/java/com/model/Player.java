package com.model;

import java.util.ArrayList;
import java.util.UUID;

public class Player {
    private UUID id;
    private String displayName;
    private Progress progress;

    public static class Progress {
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

    public Player(String displayName, Progress progress) {
        this.id = UUID.randomUUID();
        this.displayName = displayName;
        this.progress = progress;
    }

    public Player(UUID id, String displayName, Progress progress) {
        this.id = id;
        this.displayName = displayName;
        this.progress = progress;
    }

    public UUID getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }
}