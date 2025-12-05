package com.model;

/**
 * A puzzle that requires the player to possess a specific item in order to solve it.
 * <p>
 * Unlike riddles or trivia puzzles, an {@code ItemPuzzle} does not validate text input;
 * instead, it checks the player's inventory to determine whether the required item is present.
 * <p>
 * The puzzle is considered solved automatically if the player has an item whose name matches
 * the required item name (case-insensitive).
 */
public class ItemPuzzle extends Puzzle {

    /** The name of the item required to solve this puzzle. */
    private String requiredItemName;

    /** Optional label describing the type or category of item puzzle. */
    private String puzzleType;

    /**
     * Default constructor that initializes an empty item puzzle.
     * <ul>
     *     <li>{@code puzzleType = ""}</li>
     *     <li>{@code requiredItemName = ""}</li>
     *     <li>{@code type = "ItemPuzzle"}</li>
     * </ul>
     */
    public ItemPuzzle() {
        super();
        this.puzzleType = "";
        this.requiredItemName = "";
        type = "ItemPuzzle";
    }

    /**
     * Constructs an item puzzle with the given type and required item name.
     *
     * @param puzzleType       descriptive label for the puzzle
     * @param requiredItemName the name of the item needed to complete the puzzle
     */
    public ItemPuzzle(String puzzleType, String requiredItemName) {
        super();
        this.puzzleType = puzzleType;
        this.requiredItemName = requiredItemName;
        type = "ItemPuzzle";
    }

    /**
     * Returns the name of the item required to solve this puzzle.
     *
     * @return the required item's name
     */
    public String getRequiredItemName() {
        return requiredItemName;
    }

    /**
     * Sets the name of the item required to solve this puzzle.
     *
     * @param requiredItemName the required item's name
     */
    public void setRequiredItemName(String requiredItemName) {
        this.requiredItemName = requiredItemName;
    }

    /**
     * Returns the type label describing this item puzzle.
     *
     * @return the puzzle type
     */
    public String getPuzzleType() {
        return puzzleType;
    }

    /**
     * Sets the descriptive type label for this item puzzle.
     *
     * @param puzzleType the new type value
     */
    public void setPuzzleType(String puzzleType) {
        this.puzzleType = puzzleType;
    }

    /**
     * Determines whether the puzzle is solved by checking the player's inventory
     * for the required item.
     * <p>
     * The {@code answer} parameter is ignored because solving the puzzle depends
     * solely on whether the player possesses the correct item.
     * <p>
     * The method:
     * <ul>
     *     <li>Retrieves the current player</li>
     *     <li>Accesses their most recent {@link Progress} object</li>
     *     <li>Searches the inventory for an item with a matching name</li>
     * </ul>
     *
     * @param answer ignored for this puzzle type
     * @return {@code true} if the player has the required item; {@code false} otherwise
     */
    @Override
    public boolean checkAnswer(String answer) {
        Player currentPlayer = Players.getInstance().getCurrentPlayer();
        if (currentPlayer != null &&
            currentPlayer.getProgress() != null &&
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

    /**
     * Returns a formatted string representation of this item puzzle.
     *
     * @return a descriptive string containing the puzzle type and required item
     */
    @Override
    public String toString() {
        return "ItemPuzzle: " + puzzleType + " (requires " + requiredItemName + ")";
    }
}
