package com.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
    static EscapeGameFacade facade = EscapeGameFacade.getInstance();

    public static void main(String[] args) {
        facade.loadProgress();
        System.out.println("Welcome to VHS Escape!");
        System.out.println("Creating a new account for Leni Rivers (display name: lrivers) with the password password1234, lrivers is an account that already exists for her brother though!");
        facade.createPlayer("lrivers", "password1234");
        System.out.println("Creating a new account for Leni Rivers (display name: lerivers) with the password password1234, since lrivers was taken");
        facade.createPlayer("lerivers", "password1234");

        System.out.println("\nLeni logs into her account...");
        facade.login("lerivers", "password1234");

        Player currentPlayer = Players.getInstance().getCurrentPlayer();

        Scanner scanner = new Scanner(System.in);
        System.out.println("\nAvailable Escape Rooms:");
        System.out.println("1. VHS Escape");
        System.out.print("Which room do you want to enter? ");
        String roomChoice = scanner.nextLine();

        if (roomChoice.equalsIgnoreCase("VHS Escape") || roomChoice.equals("1")) {
            System.out.println("\nLeni enters 'VHS Escape'...");
            facade.startGame(currentPlayer, Game.Difficulty.MEDIUM);

            String storyFile = "story.txt";
            try {
                String storyText = new String(Files.readAllBytes(Paths.get(storyFile)));
                facade.displayStory();
                System.out.println("\n--- Story ---");
                System.out.println(storyText);
                Speak.speak(storyText);
            } catch (IOException e) {
                System.err.println("Error reading story file: " + e.getMessage());
            }

            ArrayList<Puzzle> puzzles = facade.getAllPuzzles();
            for (int i = 0; i < puzzles.size(); i++) {
                facade.startPuzzle();
                facade.completePuzzle();
                facade.nextPuzzle();
            }

            String outroWon = "Congratulations!!!! You completed all the puzzles and escaped the VHS tape before you were stuck in the story forever!";
            System.out.println("\n--- Outro ---");
            System.out.println(outroWon);
            Speak.speak(outroWon);

        } else {
            System.out.println("Invalid room. Exiting game.");
        }

        facade.saveProgress();

        
        scanner.close();
    }
}

