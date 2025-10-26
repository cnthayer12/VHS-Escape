package com.model;

import java.io.IOException;
import java.io.InputStream;
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

            // Load story file as a resource
            String intro = "";
            String outroWon = "";
            String outroLost = "";
            try (InputStream in = Driver.class.getResourceAsStream("story.txt")) {
                if (in == null) {
                    System.err.println("Could not find story.txt in package resources.");
                } else {
                    String story = new String(in.readAllBytes());
                    // Split sections by markers
                    String[] sections = story.split("Outro");
                    intro = sections[0].replace("Intro:", "").trim();
                    for (String s : sections) {
                        if (s.contains("(won):")) outroWon = s.replace("(won):", "").trim();
                        if (s.contains("(lost):")) outroLost = s.replace("(lost):", "").trim();
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading story file: " + e.getMessage());
            }

            // Play intro
            System.out.println("\n--- Intro ---");
            System.out.println(intro);
            Speak.speak(intro);

            // Start game
            facade.startGame(currentPlayer, Game.Difficulty.MEDIUM);

            // Run puzzles
            ArrayList<Puzzle> puzzles = facade.getAllPuzzles();
            for (int i = 0; i < puzzles.size(); i++) {
                facade.startPuzzle();
                facade.completePuzzle(); // auto-complete for now
                facade.nextPuzzle();
            }

            // Determine outcome
            String finalOutro;
            if (facade.getProgressPercent() >= 100.0) {
                finalOutro = outroWon;
            } else {
                finalOutro = outroLost;
            }

            // Play outro
            System.out.println("\n--- Outro ---");
            System.out.println(finalOutro);
            Speak.speak(finalOutro);

        } else {
            System.out.println("Invalid room. Exiting game.");
        }

        facade.saveProgress();
        }
}