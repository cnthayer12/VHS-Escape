package com.model;

public class Riddle extends Puzzle {
    private String riddleText;
    private String correctAnswer;

    public Riddle() {
        super();
        this.riddleText = "";
        this.correctAnswer = "";
    }

    public String getRiddleText() {
        return riddleText;
    }

    public void setRiddleText(String riddleText) {
        this.riddleText = riddleText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

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
}