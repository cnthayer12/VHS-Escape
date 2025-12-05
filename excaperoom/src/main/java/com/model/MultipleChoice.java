package com.model;

import java.util.ArrayList;

public class MultipleChoice extends Puzzle {
    private String question;
    private ArrayList<String> options;
    private String correctAnswer;

    public MultipleChoice() {
        super();
        this.question = "";
        this.options = new ArrayList<>();
        this.correctAnswer = "";
        this.type = "MultipleChoice";
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public ArrayList<String> getOptions(){
        return options;
    }

    public String getQuestion() {
        return question;
    }

    @Override
    public void startPuzzle() {
        super.startPuzzle();
        System.out.println(question);
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }
    }

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

    @Override
    public String toString() {
        String returnString = "";
        for(String option : options)
            returnString += option + "\n";
        return "Multiple Choice:\n" + returnString;
    }
}
