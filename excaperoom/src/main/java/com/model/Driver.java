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

            // Create three puzzles BEFORE starting the game
            Riddle riddle = new Riddle();
            riddle.setRiddleText("""
                                 I haunt a maze, gobbling dots with a chomping sound.
                                 Four colorful ghosts chase me, but I can turn the tables around. Who am I?"""
            );
            riddle.setCorrectAnswer("Pacman");
            riddle.addHint(new Hint("Think of my insatiable appetite", 10));
            riddle.addHint(new Hint("I am the same color as the sun", 10));

            Cipher cipher = new Cipher();
            cipher.setCipherText("jotfsu dpjo up dpoujovf"); // "insert coin to continue"
            cipher.setCorrectAnswer("insert coin to continue");
            cipher.addHint(new Hint("Try shifting each letter back by 1.", 10));
            cipher.addHint(new Hint("It's a Caesar Cipher with a shift of 1.", 10));

            ItemPuzzle lockedBox = new ItemPuzzle("lockbox", "Locked Box");
            lockedBox.setRequiredItemName("Key");
            lockedBox.addHint(new Hint("The lock looks old… maybe a key would help.", 10));

            // Add puzzles to the PuzzlesManager 
            PuzzlesManager manager = PuzzlesManager.getInstance();
            manager.addPuzzle(cipher);
            manager.addPuzzle(riddle);
            manager.addPuzzle(lockedBox);

            // Start game
            facade.startGame(currentPlayer, Game.Difficulty.MEDIUM);

            // Acquire at least 2 items
            Item flashlight = new Item("Flashlight", "Illuminates the answer", "Found in the room", null);
            Item vhsTape = new Item("Vintage VHS Tape", "Plays the answer", "Found in the room", null);
            Item key = new Item("Key", "Unlocks the locked box", "Found in the room", null);
            facade.addItem(flashlight);
            facade.addItem(vhsTape);
            facade.addItem(key);
            System.out.println("Items acquired: Flashlight, Vintage VHS Tape, and Key\n");

            // Get current progress for tracking hints
            Progress progress = Players.getInstance().getCurrentPlayer().getProgress().get(
                Players.getInstance().getCurrentPlayer().getProgress().size() - 1
            );

            int totalPuzzlesSolved = 0;

            // Solve puzzles in a non-linear order 
            System.out.println("=== Puzzle 1: Cipher ===");
            cipher.startPuzzle();
            System.out.println("Cipher Text: " + cipher.getCipherText());
            boolean cipherSolved = false;
            int attempts = 0;
            
            while (attempts < 3 && !cipherSolved) {
                System.out.print("Enter your answer (" + (3 - attempts) + " tries left): ");
                String cipherAnswer = scanner.nextLine().trim();

                if (cipher.checkAnswer(cipherAnswer)) {
                    System.out.println("Correct!\n");
                    cipherSolved = true;
                    cipher.completePuzzle();
                    totalPuzzlesSolved++;
                } else {
                    attempts++;
                    System.out.println("Incorrect.");

                    // offer hint 1 after first failure
                    if (attempts == 1) {
                        System.out.print("Would you like to use your first hint? (yes/no): ");
                        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                            Hint usedHint = cipher.getHints().get(0);
                            System.out.println("Hint: " + usedHint.revealHint());
                            progress.addHint(usedHint);
                        }
                    }
                    // offer hint 2 after second failure
                    else if (attempts == 2) {
                        System.out.print("Would you like to use your second hint? (yes/no): ");
                        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                            Hint usedHint = cipher.getHints().get(1);
                            System.out.println("Hint: " + usedHint.revealHint());
                            progress.addHint(usedHint);
                        }
                    }
                    // third failure ends the puzzle
                    if (attempts == 3) {
                        System.out.println("Out of tries! The correct answer was: " + cipher.getCorrectAnswer() + "\n");
                    }
                }
            }

            System.out.println("=== Puzzle 2: Riddle ===");
            riddle.startPuzzle();
            System.out.println("Riddle: " + riddle.getRiddleText());

            boolean riddleSolved = false;
            int riddleAttempts = 0;

            while (riddleAttempts < 3 && !riddleSolved) {
                System.out.print("Enter your answer (" + (3 - riddleAttempts) + " tries left): ");
                String riddleAnswer = scanner.nextLine().trim();

                if (riddle.checkAnswer(riddleAnswer)) {
                    System.out.println("Correct!\n");
                    riddleSolved = true;
                    riddle.completePuzzle();
                    totalPuzzlesSolved++;
                } else {
                    riddleAttempts++;
                    System.out.println("Incorrect.");

                    Progress currentProgress = Players.getInstance().getCurrentPlayer().getProgress().get(
                        Players.getInstance().getCurrentPlayer().getProgress().size() - 1
                    );
                    
                    if (riddleAttempts == 1) {
                        System.out.print("Would you like to use your first hint? (yes/no): ");
                        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                            Hint usedHint = riddle.getHints().get(0);
                            System.out.println("Hint: " + usedHint.revealHint());
                            currentProgress.addHint(usedHint);
                        }
                    } else if (riddleAttempts == 2) {
                        System.out.print("Would you like to use your second hint? (yes/no): ");
                        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                            Hint usedHint = riddle.getHints().get(1);
                            System.out.println("Hint: " + usedHint.revealHint());
                            currentProgress.addHint(usedHint);
                        }
                    }
                    
                    if (riddleAttempts == 3) {
                        System.out.println("Out of tries! The correct answer was: " + riddle.getCorrectAnswer() + "\n");
                    }
                }
            }

            System.out.println("=== Puzzle 3: Item Puzzle (Locked Box) ===");
            lockedBox.startPuzzle();
            System.out.println("A locked box sits before you. You need the right item to open it.");
            System.out.print("Do you want to try to open it? (yes/no): ");
            
            if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                if (lockedBox.checkAnswer("")) {
                    System.out.println("You used the Key! The box opens with a satisfying click.\n");
                    lockedBox.completePuzzle();
                    totalPuzzlesSolved++;
                } else {
                    System.out.println("You don't have the required item (Key) to open this box.\n");
                }
            }

            // Calculate final progress
            int totalPuzzles = 3;
            double progressPercent = (totalPuzzlesSolved * 100.0) / totalPuzzles;

            // Determine outcome
            String finalOutro;
            if (progressPercent >= 100.0) {
                finalOutro = outroWon;
                System.out.println("🎉 You escaped! All puzzles solved!");
            } else {
                finalOutro = outroLost;
                System.out.println("⏰ Time's up or too many failures. You didn't escape this time.");
            }

            // Play outro
            System.out.println("\n--- Outro ---");
            System.out.println(finalOutro);
            Speak.speak(finalOutro);

            // Generate certificate if won
            if (progressPercent >= 100.0) {
                facade.generateCompletionCertificate();
                System.out.println("\n📜 Completion certificate generated!");
            }

        } else {
            System.out.println("Invalid room. Exiting game.");
        }

        facade.saveProgress();
        scanner.close();
    }
}




    


