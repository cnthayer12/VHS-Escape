package com.model;

import java.util.ArrayList;

public class Progress {

    private int hintsUsed;
    private ArrayList<Item> inventory;
    private ArrayList<Hint> storedHints;
    private ArrayList<Puzzle> completedPuzzles;
    private Puzzle currentPuzzle;
    private int strikes;
    private int currentScore;

    public Progress(ArrayList<Item> inventory, ArrayList<Hint> storedHints, ArrayList<Puzzle> completedPuzzles, Puzzle currentPuzzle, int strikes, int currentScore) {
        this.hintsUsed = storedHints.size();
        this.inventory = inventory;
        this.storedHints = storedHints;
        this.completedPuzzles = completedPuzzles;
        this.currentPuzzle = currentPuzzle;
        this.strikes = strikes;
        this.currentScore = currentScore;
    }
    public Progress() {
        hintsUsed = 0;
        inventory = new ArrayList<>();
        storedHints = new ArrayList<>();
        completedPuzzles = new ArrayList<>();
        currentPuzzle = null;
        strikes = 0;
        currentScore = 0;
    }

     public static Progress loadProgress(Player player) {
        if (player == null) {
            return null;
        }
        
        ArrayList<Progress> progressList = player.getProgress();
        if (progressList == null || progressList.isEmpty()) {
            return null;
        }
        
        return progressList.get(progressList.size() - 1);
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

    public Puzzle getCurrentPuzzle() {
        return currentPuzzle;
    }

    public ArrayList<Puzzle> getCompletedPuzzles() {
        return completedPuzzles;
    }

    public void addCompletedPuzzle(Puzzle puzzle) {
        if(puzzle != null && completedPuzzles != null)
            completedPuzzles.add(puzzle);
    }
    
    public void addItem(Item item) {
        if(item != null && inventory != null)
            inventory.add(item);
    }

    public void addHint(Hint hint) {
        if(hint != null && storedHints != null) {
            storedHints.add(hint);
            hintsUsed++;
        }
    }

    public void setCompletedPuzzles(ArrayList<Puzzle> completedPuzzles) {
        this.completedPuzzles = completedPuzzles;
    }
    
    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }

    public void setStoredHints(ArrayList<Hint> storedHints) {
        this.storedHints = storedHints;
        hintsUsed = storedHints.size();
    }

    public void setScore(int score) {
        currentScore = score;
    }

    public void setStrikes(int strikes) {
        this.strikes = strikes;
    }

    public void setCurrentPuzzle(Puzzle puzzle) {
        currentPuzzle = puzzle;
    }

    @Override
    public String toString() {
        return "Percent Complete: " + calculatePercent() + "\nQuestions Answered and Hints Used: " + getPuzzlesInfo();
    }

    public double calculatePercent() {
        final int PUZZLECOUNT = 3;
        return (completedPuzzles.size() / (double) PUZZLECOUNT) * 100;
    }

    public String getPuzzlesInfo() {
        String returnString = "";
        for(Puzzle puzzle : completedPuzzles) {
            returnString += puzzle.toString();
            for(Hint hint : storedHints) {
                if(hint.getPuzzle().equals(puzzle))
                    returnString += "\n     Hint used: " + hint.toString();
            }
            returnString += "\n"; 
        }
        return returnString;
    }
}

