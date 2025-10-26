package com.model;

// Cipher.java
public class Cipher extends Puzzle {
    private String cipherText;
    private String correctAnswer;
    private int shift;

    public Cipher() {
        super();
        this.cipherText = "";
        this.correctAnswer = "";
        this.shift = 1; // default Caesar cipher shift
    }

    public Cipher(String cipherText, String correctAnswer, int shift) {
        super();
        this.cipherText = cipherText;
        this.correctAnswer = correctAnswer;
        this.shift = shift;
    }

    public String getCipherText() {
        return cipherText;
    }

    public void setCipherText(String cipherText) {
        this.cipherText = cipherText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    @Override
    public boolean checkAnswer(String answer) {
        if (answer == null || correctAnswer == null) {
            return false;
        }
        return answer.trim().equalsIgnoreCase(correctAnswer.trim());
    }

    @Override
    public String toString() {
        return "Cipher Puzzle: " + cipherText;
    }
}