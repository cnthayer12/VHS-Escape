package com.model;

public class Cipher extends Puzzle {
    private String cipherText;
    private String correctAnswer;

    public Cipher() {
        super();
        this.cipherText = "";
        this.correctAnswer = "";
        this.type = "Cipher";
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

    @Override
    public String toString() {
        return "Cipher: " + cipherText;
    }
}