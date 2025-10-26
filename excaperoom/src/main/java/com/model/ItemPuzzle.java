package com.model;

<<<<<<< HEAD
public class ItemPuzzle extends Puzzle{

    public ItemPuzzle() {
        super();
=======
public class ItemPuzzle extends Puzzle {
    private String requiredItemName;
    private String puzzleType;

    public ItemPuzzle(String id, String type) {
        super();
        this.puzzleType = type;
        this.requiredItemName = "";
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
>>>>>>> 060f5f37ae567bd71bccbbe598e1ae01fec35893
    }

    @Override
    public String toString() {
        return "ItemPuzzle: " + puzzleType + " (requires " + requiredItemName + ")";
    }
}
