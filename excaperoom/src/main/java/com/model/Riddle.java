package com.model;

/**
 * Represents a riddle puzzle where the player must provide a text answer to a riddle.
 * Extends the base Puzzle class with riddle-specific functionality.
 * Answers are checked case-insensitively with trimmed whitespace.
 * 
 * @author VHS Escape Team
 * @version 1.0
 */
public class Riddle extends Puzzle {
    /** The text of the riddle presented to the player */
    private String riddleText;
    
    /** The correct answer to the riddle */
    private String correctAnswer;

    /**
     * Constructs a new Riddle with empty text and answer.
     * Sets the puzzle type to "Riddle".
     */
    public Riddle() {
        super();
        this.riddleText = "";
        this.correctAnswer = "";
        this.type = "Riddle";
    }

    /**
     * Constructs a new Riddle with specified text and answer.
     * 
     * @param riddleText the text of the riddle
     * @param correctAnswer the correct answer to the riddle
     */
    public Riddle(String riddleText, String correctAnswer) {
        super();
        this.riddleText = riddleText;
        this.correctAnswer = correctAnswer;
    }

    /**
     * Gets the text of the riddle.
     * 
     * @return the riddle text
     */
    public String getRiddleText() {
        return riddleText;
    }

    /**
     * Sets the text of the riddle.
     * 
     * @param riddleText the riddle text to set
     */
    public void setRiddleText(String riddleText) {
        this.riddleText = riddleText;
    }

    /**
     * Gets the correct answer to the riddle.
     * 
     * @return the correct answer
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Sets the correct answer to the riddle.
     * 
     * @param correctAnswer the correct answer to set
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    /**
     * Checks if the provided answer matches the correct answer.
     * Comparison is case-insensitive and ignores leading/trailing whitespace.
     * 
     * @param answer the answer to check
     * @return true if the answer is correct, false otherwise
     */
    @Override
    public boolean checkAnswer(String answer) {
        if (answer == null || correctAnswer == null) {
            return false;
        }
        return answer.trim().equalsIgnoreCase(correctAnswer.trim());
    }

    /**
     * Returns a string representation of this riddle.
     * 
     * @return a string containing the riddle text
     */
    @Override
    public String toString() {
        return "Riddle: " + riddleText;
    }
}