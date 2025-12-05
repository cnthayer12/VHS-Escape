package com.model;

/**
 * Represents a trivia puzzle where the player must answer a factual question.
 * Extends the base Puzzle class with trivia-specific functionality.
 * Automatically plays sound feedback and completes the puzzle on correct answer.
 * 
 * @author VHS Escape Team
 * @version 1.0
 */
public class Trivia extends Puzzle {
    /** The trivia question text */
    private String triviaText;
    
    /** The correct answer to the trivia question */
    private String correctAnswer;

    /**
     * Constructs a new Trivia puzzle with empty text and answer.
     * Sets the puzzle type to "Trivia".
     */
    public Trivia() {
        super();
        this.triviaText = "";
        this.correctAnswer = "";
        this.type = "Trivia";
    }

    /**
     * Gets the trivia question text.
     * 
     * @return the trivia question
     */
    public String getTriviaText() {
        return triviaText;
    }

    /**
     * Sets the trivia question text.
     * 
     * @param triviaText the trivia question to set
     */
    public void setTriviaText(String triviaText) {
        this.triviaText = triviaText;
    }

    /**
     * Gets the correct answer to the trivia question.
     * 
     * @return the correct answer
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Sets the correct answer to the trivia question.
     * 
     * @param correctAnswer the correct answer to set
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    /**
     * Checks if the user's answer is correct.
     * Comparison is case-insensitive and ignores leading/trailing whitespace.
     * Plays appropriate sound feedback and completes the puzzle if correct.
     * 
     * @param userAnswer the answer provided by the user
     * @return true if the answer is correct, false otherwise
     */
    @Override
    public boolean checkAnswer(String userAnswer) {
        if (userAnswer == null || userAnswer.trim().isEmpty()) {
            playSound(false);
            return false;
        }

        if (userAnswer.trim().equalsIgnoreCase(correctAnswer.trim())) {
            playSound(true);
            completePuzzle();
            return true;
        } else {
            playSound(false);
            return false;
        }
    }

    /**
     * Returns a string representation of this trivia puzzle.
     * 
     * @return a string containing the trivia question
     */
    @Override
    public String toString() {
        return "Trivia: " + triviaText;
    }
}