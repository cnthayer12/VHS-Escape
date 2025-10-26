package com.model;

public class ItemPuzzle extends Puzzle {
    private String requiredItemName;
    private String puzzleType;

    public ItemPuzzle() {
        super();
        this.puzzleType = type;
        this.requiredItemName = "";
        type = "ItemPuzzle";
    }

    public String getRequiredItemName() {
        return requiredItemName;
    }

    public void setRequiredItemName(String requiredItemName) {
        this.requiredItemName = requiredItemName;
    }

    public String getPuzzleType() {
        return puzzleType;
    }

    public void setPuzzleType(String type) {
        this.puzzleType = type;
    }

    @Override
    public boolean checkAnswer(String answer) {
        // Check if player has the required item in inventory
        Player currentPlayer = Players.getInstance().getCurrentPlayer();
        if (currentPlayer != null && currentPlayer.getProgress() != null && 
            !currentPlayer.getProgress().isEmpty()) {
            Progress progress = currentPlayer.getProgress().get(
                currentPlayer.getProgress().size() - 1);
            
            for (Item item : progress.getInventory()) {
                if (item.getName().equalsIgnoreCase(requiredItemName)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "ItemPuzzle: " + puzzleType + " (requires " + requiredItemName + ")";
    }
}
