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

            // Play intro (using facade)
            System.out.println("\n--- Intro ---");
            System.out.println(intro);
            Speak.speak(intro);
            facade.displayStory(); // Display game story through facade

            // Create three puzzles BEFORE starting the game
            Riddle riddle = new Riddle();
            riddle.setRiddleText("""
                                 I haunt a maze, gobbling dots with a chomping sound.
                                 Four colorful ghosts chase me, but I can turn the tables around. Who am I?"""
            );
            riddle.setCorrectAnswer("Pacman");
            riddle.addHint(new Hint("Think of my insatiable appetite", 10, riddle));
            riddle.addHint(new Hint("I am the same color as the sun", 10, riddle));

            Cipher cipher = new Cipher();
            cipher.setCipherText("jotfsu dpjo up dpoujovf"); // "insert coin to continue"
            cipher.setCorrectAnswer("insert coin to continue");
            cipher.addHint(new Hint("Try shifting each letter back by 1.", 10, cipher));
            cipher.addHint(new Hint("It's a Caesar Cipher with a shift of 1.", 10, cipher));

            ItemPuzzle lockedBox = new ItemPuzzle("lockbox", "Locked Box");
            lockedBox.setRequiredItemName("Key");
            lockedBox.addHint(new Hint("The lock looks old… maybe a key would help.", 10, lockedBox));

            // Add puzzles through the facade (this adds to PuzzlesManager)
            PuzzlesManager manager = PuzzlesManager.getInstance();
            manager.addPuzzle(cipher);
            manager.addPuzzle(riddle);
            manager.addPuzzle(lockedBox);

            // Start game through facade
            facade.startGame(Players.getInstance().getCurrentPlayer(), Game.Difficulty.MEDIUM);

            // Acquire items through facade
            Item flashlight = new Item("Flashlight", "Illuminates the answer", "Found in the room", null);
            Item vhsTape = new Item("Vintage VHS Tape", "Plays the answer", "Found in the room", null);
            Item key = new Item("Key", "Unlocks the locked box", "Found in the room", null);
            facade.addItem(flashlight);
            facade.addItem(vhsTape);
            facade.addItem(key);
            System.out.println("Items acquired: Flashlight, Vintage VHS Tape, and Key\n");

            int totalPuzzlesSolved = 0;
            int totalPuzzles = facade.getTotalPuzzles();

            // Solve puzzles in a non-linear order 
            System.out.println("=== Puzzle 1: Cipher ===");
            facade.startPuzzle(); // Start through facade
            Puzzle currentPuzzle = facade.getCurrentPuzzle();
            if (currentPuzzle instanceof Cipher) {
                Cipher cipherPuzzle = (Cipher) currentPuzzle;
                System.out.println("Cipher Text: " + cipherPuzzle.getCipherText());
                
                boolean cipherSolved = false;
                int attempts = 0;
                
                while (attempts < 3 && !cipherSolved) {
                    System.out.print("Enter your answer (" + (3 - attempts) + " tries left): ");
                    String cipherAnswer = scanner.nextLine().trim();

                    // Submit answer through facade
                    if (facade.submitAnswer(cipherAnswer)) {
                        System.out.println("Correct!\n");
                        cipherSolved = true;
                        facade.completePuzzle(); // Complete through facade
                        totalPuzzlesSolved++;
                    } else {
                        attempts++;
                        facade.addStrike(); // Add strike through facade
                        System.out.println("Incorrect.");

                        // Offer hints through facade
                        ArrayList<Hint> availableHints = facade.getAvailableHints();
                        if (attempts == 1 && availableHints.size() > 0) {
                            System.out.print("Would you like to use your first hint? (yes/no): ");
                            if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                                Hint usedHint = facade.revealHint(); // Reveal through facade
                                if (usedHint != null) {
                                    System.out.println("Hint: " + usedHint.getText());
                                }
                            }
                        } else if (attempts == 2 && availableHints.size() > 1) {
                            System.out.print("Would you like to use your second hint? (yes/no): ");
                            if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                                Hint usedHint = facade.revealHint(); // Reveal through facade
                                if (usedHint != null) {
                                    System.out.println("Hint: " + usedHint.getText());
                                }
                            }
                        }
                        
                        if (attempts == 3) {
                            System.out.println("Out of tries! The correct answer was: " + cipherPuzzle.getCorrectAnswer() + "\n");
                        }
                    }
                }
            }

            facade.nextPuzzle(); // Move to next puzzle through facade
            facade.resetStrikes(); // Reset strikes through facade

            System.out.println("=== Puzzle 2: Riddle ===");
            facade.startPuzzle(); // Start through facade
            currentPuzzle = facade.getCurrentPuzzle();
            if (currentPuzzle instanceof Riddle) {
                Riddle riddlePuzzle = (Riddle) currentPuzzle;
                System.out.println("Riddle: " + riddlePuzzle.getRiddleText());

                boolean riddleSolved = false;
                int riddleAttempts = 0;

                while (riddleAttempts < 3 && !riddleSolved) {
                    System.out.print("Enter your answer (" + (3 - riddleAttempts) + " tries left): ");
                    String riddleAnswer = scanner.nextLine().trim();

                    // Submit answer through facade
                    if (facade.submitAnswer(riddleAnswer)) {
                        System.out.println("Correct!\n");
                        riddleSolved = true;
                        facade.completePuzzle(); // Complete through facade
                        totalPuzzlesSolved++;
                    } else {
                        riddleAttempts++;
                        facade.addStrike(); // Add strike through facade
                        System.out.println("Incorrect.");

                        // Offer hints through facade
                        ArrayList<Hint> availableHints = facade.getAvailableHints();
                        if (riddleAttempts == 1 && availableHints.size() > 0) {
                            System.out.print("Would you like to use your first hint? (yes/no): ");
                            if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                                Hint usedHint = facade.revealHint();
                                if (usedHint != null) {
                                    System.out.println("Hint: " + usedHint.getText());
                                }
                            }
                        } else if (riddleAttempts == 2 && availableHints.size() > 1) {
                            System.out.print("Would you like to use your second hint? (yes/no): ");
                            if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                                Hint usedHint = facade.revealHint();
                                if (usedHint != null) {
                                    System.out.println("Hint: " + usedHint.getText());
                                }
                            }
                        }
                        
                        if (riddleAttempts == 3) {
                            System.out.println("Out of tries! The correct answer was: " + riddlePuzzle.getCorrectAnswer() + "\n");
                        }
                    }
                }
            }

            facade.nextPuzzle(); // Move to next puzzle through facade
            facade.resetStrikes(); // Reset strikes through facade

            System.out.println("=== Puzzle 3: Item Puzzle (Locked Box) ===");
            facade.startPuzzle(); // Start through facade
            currentPuzzle = facade.getCurrentPuzzle();
            if (currentPuzzle instanceof ItemPuzzle) {
                System.out.println("A locked box sits before you. You need the right item to open it.");
                System.out.print("Do you want to try to open it? (yes/no): ");
                
                if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                    // Submit answer through facade (ItemPuzzle checks inventory automatically)
                    if (facade.submitAnswer("")) {
                        System.out.println("You used the Key! The box opens with a satisfying click.\n");
                        facade.completePuzzle(); // Complete through facade
                        totalPuzzlesSolved++;
                    } else {
                        System.out.println("You don't have the required item (Key) to open this box.\n");
                        facade.addStrike(); // Add strike through facade
                    }
                }
            }

            // Get progress through facade
            double progressPercent = facade.getProgressPercent();
            int finalScore = facade.getScore();

            // Determine outcome
            String finalOutro;
            if (totalPuzzlesSolved == totalPuzzles) {
                finalOutro = outroWon;
                System.out.println("🎉 You escaped! All puzzles solved!");
                System.out.println("Final Score: " + finalScore);
                System.out.println("Hints Used: " + facade.getHintsUsed());
                System.out.println("Strikes: " + facade.getStrikes());
            } else {
                finalOutro = outroLost;
                System.out.println("⏰ Time's up or too many failures. You didn't escape this time.");
                System.out.println("Final Score: " + finalScore);
            }

            // Play outro
            System.out.println("\n--- Outro ---");
            System.out.println(finalOutro);
            Speak.speak(finalOutro);

            // Generate certificate if won (through facade)
            if (totalPuzzlesSolved == totalPuzzles) {
                facade.generateCompletionCertificate();
                System.out.println("\n📜 Completion certificate generated!");
            }

            // Display leaderboard (through facade)
            System.out.println("\n=== Leaderboard ===");
            ArrayList<Leaderboard.LeaderboardEntry> leaderboard = facade.getLeaderboard();
            if (leaderboard != null && !leaderboard.isEmpty()) {
                for (int i = 0; i < Math.min(5, leaderboard.size()); i++) {
                    Leaderboard.LeaderboardEntry entry = leaderboard.get(i);
                    System.out.println((i + 1) + ". " + entry);
                }
            }

        } else {
            System.out.println("Invalid room. Exiting game.");
        }

        // Save progress through facade
        facade.saveProgress();
        scanner.close();
    }
}




    

