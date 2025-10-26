// Riddle.java
package com.model;

public class Riddle extends Puzzle {
    private String riddleText;
    private String correctAnswer;

    public Riddle() {
        super();
        this.riddleText = "";
        this.correctAnswer = "";
        this.type = "Riddle";
    }

    public Riddle(String riddleText, String correctAnswer) {
        super();
        this.riddleText = riddleText;
        this.correctAnswer = correctAnswer;
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

    @Override
    public boolean checkAnswer(String answer) {
        if (answer == null || correctAnswer == null) {
            return false;
        }
        return answer.trim().equalsIgnoreCase(correctAnswer.trim());
    }

    @Override
    public String toString() {
        return "Riddle: " + riddleText;
    }
}