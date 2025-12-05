package com.model;

/**
 * Represents a hint associated with a puzzle in the VHS Escape game.
 * <p>
 * Each hint contains:
 * <ul>
 *     <li>A unique identifier</li>
 *     <li>The hint text to display</li>
 *     <li>A cost value representing how many points it deducts</li>
 *     <li>A reference to the {@link Puzzle} it belongs to</li>
 *     <li>A flag indicating whether the hint has already been used</li>
 * </ul>
 * <p>
 * When a hint is revealed or marked as used, player progress is saved automatically
 * through the {@link EscapeGameFacade}.
 */
public class Hint {

    /** Unique identifier for the hint. */
    private String id;

    /** The text content of the hint. */
    private String text;

    /** Whether this hint has already been used by the player. */
    private boolean used;

    /** Point cost for revealing this hint. */
    private int cost;

    /** Reference to the puzzle this hint is associated with. */
    private Puzzle puzzle;
    
    /**
     * Default constructor initializing a blank hint with default values.
     * <ul>
     *     <li>id = "0"</li>
     *     <li>text = ""</li>
     *     <li>used = false</li>
     *     <li>cost = 10</li>
     *     <li>puzzle = null</li>
     * </ul>
     */
    public Hint() {
        this.id = "0";
        this.text = "";
        this.used = false;
        this.cost = 10;
        this.puzzle = null;
    }
    
    /**
     * Creates a hint with the specified text and point cost.
     *
     * @param text the hint text to display
     * @param cost the point cost for revealing the hint
     */
    public Hint(String text, int cost) {
        this.id = "0";
        this.text = text;
        this.used = false;
        this.cost = cost;
        this.puzzle = null;
    }

    /**
     * Creates a hint associated with a specific puzzle.
     *
     * @param text   the hint text
     * @param cost   the point cost
     * @param puzzle the puzzle this hint belongs to
     */
    public Hint(String text, int cost, Puzzle puzzle) {
        this.id = "0";
        this.text = text;
        this.used = false;
        this.cost = cost;
        this.puzzle = puzzle;
    }
    
    /**
     * Assigns this hint to a puzzle.
     *
     * @param puzzle the puzzle to associate with this hint
     */
    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    /**
     * Returns the puzzle this hint is associated with.
     *
     * @return the puzzle, or {@code null} if not assigned
     */
    public Puzzle getPuzzle() {
        return puzzle;
    }

    /**
     * Reveals the hint text and marks it as used.
     * <p>
     * When revealed for the first time, the hint is marked as used and
     * player progress is saved automatically.
     *
     * @return the hint text
     */
    public String revealHint() {
        if (!used) {
            used = true;
            EscapeGameFacade.getInstance().saveProgress();
        }
        return text;
    }
    
    /**
     * Marks this hint as used without returning the text.
     * <p>
     * Progress is saved only if this is the first time the hint is marked used.
     */
    public void markUsed() {
        if (!used) {
            used = true;
            EscapeGameFacade.getInstance().saveProgress();
        }
    }
    
    /**
     * Resets the hint to an unused state.
     */
    public void reset() {
        used = false;
    }
    
    /**
     * Returns the unique ID of the hint.
     *
     * @return the hint ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique ID for the hint.
     *
     * @param id the new ID value
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Returns the text content of the hint.
     *
     * @return the hint text
     */
    public String getText() {
        return text;
    }

    /**
     * Returns whether this hint has already been used.
     *
     * @return {@code true} if used, {@code false} otherwise
     */
    public boolean isUsed() {
        return used;
    }
    
    /**
     * Returns the point cost of this hint.
     *
     * @return the hint cost
     */
    public int getCost() {
        return cost;
    }
    
    /**
     * Sets the text content of this hint.
     *
     * @param text the new hint text
     */
    public void setText(String text) {
        this.text = text;
    }
    
    /**
     * Sets the cost of this hint.
     *
     * @param cost the new point cost
     */
    public void setCost(int cost) {
        this.cost = cost;
    }
    
    /**
     * Returns a brief string representation of the hint for debugging.
     *
     * @return a formatted string containing ID, cost, and used state
     */
    @Override
    public String toString() {
        return "[Hint: " + id + ", used=" + used + ", cost=" + cost + "]";
    }
}

