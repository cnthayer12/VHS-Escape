package com.model;

import java.util.ArrayList;

/**
 * Represents a multiple-choice puzzle that presents a question with several
 * answer options and checks the user's response against the correct answer.
 * This class extends {@link Puzzle} and adds functionality specific to
 * multiple-choice interactions.
 */
public class MultipleChoice extends Puzzle {

    /** The text of the question being asked. */
    private String question;

    /** The list of answer choices available to the user. */
    private ArrayList<String> options;

    /** The correct answer for this multiple-choice puzzle. */
    private String correctAnswer;

    /**
     * Constructs an empty MultipleChoice puzzle with default values and sets the
     * puzzle type to {@code "MultipleChoice"}.
     */
    public MultipleChoice() {
        super();
        this.question = "";
        this.options = new ArrayList<>();
        this.correctAnswer = "";
        this.type = "MultipleChoice";
    }

    /**
     * Sets the question text for this puzzle.
     *
     * @param question the question to display
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Sets the list of answer options for this puzzle.
     *
     * @param options a list of possible answer choices
     */
    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    /**
     * Sets the correct answer for this puzzle.
     *
     * @param correctAnswer the answer that should be considered correct
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    /**
     * Returns the list of available answer choices.
     *
     * @return the list of answer options
     */
    public ArrayList<String> getOptions() {
        return options;
    }

    /**
     * Returns the question text for this puzzle.
     *
     * @return the question string
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Starts the puzzle by displaying the question and all available answer options.
     * Also calls {@link Puzzle#startPuzzle()} to trigger any parent-class behaviors.
     */
    @Override
    public void startPuzzle() {
        super.startPuzzle();
        System.out.println(question);
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }
    }

    /**
     * Checks whether the provided answer matches the correct answer. If the answer is
     * correct, a success sound is played and the puzzle is marked complete.
     *
     * @param answer the user's submitted answer
     * @return {@code true} if the answer matches the correct answer (case-insensitive),
     *         {@code false} otherwise
     */
    @Override
    public boolean checkAnswer(String answer) {
        if (answer.equalsIgnoreCase(correctAnswer)) {
            playSound(true);
            completePuzzle();
            return true;
        } else {
            playSound(false);
            return false;
        }
    }

    /**
     * Returns a formatted string representation of the multiple-choice puzzle,
     * including all available answer options.
     *
     * @return a string describing the puzzle and its options
     */
    @Override
    public String toString() {
        String returnString = "";
        for (String option : options) {
            returnString += option + "\n";
        }
        return "Multiple Choice:\n" + returnString;
    }
}
