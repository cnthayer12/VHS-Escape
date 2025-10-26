package com.model;

public class Trivia extends Puzzle {
    private String triviaText;
    private String correctAnswer;

    public Trivia() {
        super();
        this.triviaText = "";
        this.correctAnswer = "";
    }

    public String getTriviaText() {
        return triviaText;
    }

    public void setTriviaText(String triviaText) {
        this.triviaText = triviaText;
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
}