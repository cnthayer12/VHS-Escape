package com.model;

/**
 * Represents a Caesar Cipher puzzle where a user must decode a shifted message.
 * This class stores the encrypted text, the correct decoded answer, and
 * the shift value used for the cipher. It extends the {@link Puzzle} class.
 */
public class Cipher extends Puzzle {

    /** The encrypted text shown to the user. */
    private String cipherText;

    /** The correct decoded answer to the cipher. */
    private String correctAnswer;

    /** The shift value used in the Caesar cipher. */
    private int shift;

    /**
     * Default constructor that initializes a Cipher puzzle with empty values
     * and a default Caesar cipher shift of 1.
     */
    public Cipher() {
        super();
        this.cipherText = "";
        this.correctAnswer = "";
        this.type = "Cipher";
        this.shift = 1;
    }

    /**
     * Constructs a Cipher puzzle with the provided encrypted text, correct answer,
     * and shift value.
     *
     * @param cipherText     the encrypted message
     * @param correctAnswer  the correct decoded answer
     * @param shift          the Caesar cipher shift used
     */
    public Cipher(String cipherText, String correctAnswer, int shift) {
        super();
        this.cipherText = cipherText;
        this.correctAnswer = correctAnswer;
        this.shift = shift;
    }

    /**
     * Returns the encrypted cipher text.
     *
     * @return the cipher text
     */
    public String getCipherText() {
        return cipherText;
    }

    /**
     * Sets the encrypted cipher text.
     *
     * @param cipherText the message to encrypt
     */
    public void setCipherText(String cipherText) {
        this.cipherText = cipherText;
    }

    /**
     * Returns the correct answer for the puzzle.
     *
     * @return the decoded correct answer
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Sets the correct decoded answer for this cipher.
     *
     * @param correctAnswer the correct solution
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    /**
     * Returns the Caesar cipher shift value.
     *
     * @return the shift amount
     */
    public int getShift() {
        return shift;
    }

    /**
     * Sets the Caesar cipher shift value.
     *
     * @param shift the shift amount to use
     */
    public void setShift(int shift) {
        this.shift = shift;
    }

    /**
     * Checks whether the given answer matches the correct decoded answer.
     * Comparison is case-insensitive and trims surrounding whitespace.
     *
     * @param answer the user's submitted answer
     * @return true if the answer matches, false otherwise
     */
    @Override
    public boolean checkAnswer(String answer) {
        if (answer == null || correctAnswer == null) {
            return false;
        }
        return answer.trim().equalsIgnoreCase(correctAnswer.trim());
    }

    /**
     * Returns a simple string representation of the Cipher puzzle.
     *
     * @return a string showing the cipher text
     */
    @Override
    public String toString() {
        return "Cipher Puzzle: " + cipherText;
    }
}