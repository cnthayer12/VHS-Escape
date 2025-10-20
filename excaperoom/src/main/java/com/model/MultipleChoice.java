package com.model;

import java.util.ArrayList;
import java.util.Scanner;

public class MultipleChoice extends Puzzle {
    private String question;
    private ArrayList<String> options;
    private String correctAnswer;

    public MultipleChoice() {
        super();
        this.question = "";
        this.options = new ArrayList<>();
        this.correctAnswer = "";
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

    @Override
    public void startPuzzle() {
        super.startPuzzle();
        System.out.println(question);
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose 1-4: ");
        int choice = scanner.nextInt();
        String answer = options.get(choice - 1);

        if (answer.equalsIgnoreCase(correctAnswer)) {
            playSound(true);
            completePuzzle();
            System.out.println("Correct!");
        } else {
            playSound(false);
            System.out.println("Incorrect. The correct answer was: " + correctAnswer);
        }
    }

}
