package com.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Singleton manager class that handles all player-related operations in the VHS Escape game.
 * Manages player accounts, authentication, progress tracking, and certificate generation.
 * 
 * @author VHS Escape Team
 * @version 1.0
 */
public class Players {
    /** Singleton instance of Players */
    private static Players instance = null;
    
    /** Collection of all registered players */
    private static ArrayList<Player> players;
    
    /** The currently logged-in player */
    private static Player currentPlayer = null;

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes the players collection.
     */
    private Players() {
        players = new ArrayList<>();
    }

    /**
     * Gets the singleton instance of Players.
     * Creates a new instance if one doesn't exist.
     * 
     * @return the singleton instance of Players
     */
    public static Players getInstance() {
        if (instance == null) {
            instance = new Players();
        }
        return instance;
    }

    /**
     * Gets the collection of all registered players.
     * 
     * @return the ArrayList of all players
     */
    public static ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the currently logged-in player.
     * 
     * @return the current Player object, or null if no one is logged in
     */
    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the collection of players.
     * 
     * @param players the ArrayList of players to set
     */
    public void setPlayers(ArrayList<Player> players) {
        Players.players = players;
    }

    /**
     * Adds a player to the collection.
     * 
     * @param player the player to add
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Gets a player by their unique identifier.
     * 
     * @param id the UUID of the player to find
     * @return the Player object if found, null otherwise
     */
    public Player getPlayer(UUID id) {
        for (Player player : players) {
            if (player.getId().equals(id)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Removes a player from the collection by their unique identifier.
     * 
     * @param id the UUID of the player to remove
     * @return true if the player was found and removed, false otherwise
     */
    public boolean removePlayer(UUID id) {
        Player player = getPlayer(id);
        if (player != null) {
            players.remove(player);
            return true;
        }
        return false;
    }

    /**
     * Logs in a player with the provided display name and password.
     * Prevents login if a player is already logged in.
     * 
     * @param display the player's display name
     * @param pass the player's password
     */
    public void login(String display, String pass) {
        if(currentPlayer != null)
        {
            System.out.println("Could not log in, already logged in.");
            return;
        }
        for(int i = 0; i < players.size(); i++){
            Player search = players.get(i);
            if(search.getDisplayName().equals(display) && search.checkPassword(pass)){
                currentPlayer = search;
                System.out.println("Successfully logged in!");
                return;
            }
        }
        System.out.println("Could not log in, invalid username or password.");
    }

    /**
     * Logs out the currently logged-in player.
     * Prints an error message if no player is logged in.
     */
    public void logout() {
        if(currentPlayer != null) {
            currentPlayer = null;
            System.out.println("Successfully logged out.");
        } else {
            System.out.println("Could not log out, no player is current logged in.");
        }
    }

    /**
     * Creates a new player account with the provided display name and password.
     * Automatically logs in the new player upon successful creation.
     * 
     * @param displayName the display name for the new account
     * @param pass the password for the new account
     * @return 0 if already logged in, 1 if password is empty, 2 if name already exists,
     *         3 if account was created successfully
     */
    public int createAccount(String displayName, String pass) {
        if(currentPlayer != null) {
            System.out.println("Could not create account, a user is already logged in.");
            return 0;
        }
        if(pass.equals("")) {
            System.out.println("Could not create account, password cannot be empty.");
            return 1;
        }
        for(Player player : players) {
            if(player.getDisplayName().equals(displayName)) {
                System.out.println("Could not create account, one with this name already exists.");
                return 2;
            }
        }
        Progress progressInstance = new Progress();
        ArrayList<Progress> progress = new ArrayList<>();
        progress.add(progressInstance);
        Player newPlayer = new Player(displayName, progress, pass);
        players.add(newPlayer);
        System.out.println("Account created successfully! Logging in now.");
        Players.getInstance().login(displayName, pass);
        return 3;
    }

    /**
     * Loads player data and puzzle data from persistent storage.
     * 
     * @return true if data was loaded successfully, false otherwise
     */
    public boolean loadProgress() {
        ArrayList<Player> loadedPlayers = DataLoader.getPlayers();
        if (loadedPlayers != null) {
            players = loadedPlayers;
            DataLoader.loadPuzzles();
            return true;
        }
        return false;
    }

    /**
     * Gets the number of strikes accumulated by the current player.
     * 
     * @return the number of strikes, or 0 if no player is logged in
     */
    public int getStrikes() {
        Player currentPlayer = Players.getCurrentPlayer();
        if (currentPlayer != null && currentPlayer.getProgress() != null && !currentPlayer.getProgress().isEmpty()) {
            return currentPlayer.getProgress().get(0).getStrikes();
        }
        return 0;
    }

    /**
     * Adds one strike to the current player's progress.
     */
    public void addStrike() {
        Player currentPlayer = Players.getCurrentPlayer();
        if (currentPlayer != null && currentPlayer.getProgress() != null && !currentPlayer.getProgress().isEmpty()) {
            Progress progress = currentPlayer.getProgress().get(0);
            int currentStrikes = progress.getStrikes();
            progress.setStrikes(currentStrikes + 1);
        }
    }

    /**
     * Resets the current player's strikes to zero.
     */
    public void resetStrikes() {
        Player currentPlayer = Players.getCurrentPlayer();
        if (currentPlayer != null && currentPlayer.getProgress() != null && !currentPlayer.getProgress().isEmpty()) {
            currentPlayer.getProgress().get(0).setStrikes(0);
        }
    }

    /**
     * Saves all player data to persistent storage.
     * Ensures the current player is synchronized with the players list before saving.
     */
    public void saveProgress() {
        // Make sure currentPlayer is synced with the players list
        if (currentPlayer != null) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getId().equals(currentPlayer.getId())) {
                    players.set(i, currentPlayer);  // Update the reference
                    break;
                }
            }
        }
        DataWriter.savePlayers(players);
    }

    /**
     * Prints the current player's progress to the console.
     */
    public void checkProgress() {
        System.out.println(currentPlayer.getProgress().get(currentPlayer.getProgress().size()-1));
    }

    /**
     * Generates a completion certificate for the current player and saves it to a text file.
     * The certificate includes the player's name, difficulty level, hints used, and final score.
     * 
     * @param difficulty the game difficulty level completed
     * @param score the final score achieved
     */
    public void generateCertificate(Game.Difficulty difficulty, int score) {
        Player player = getCurrentPlayer();
        Progress progress = player.getProgress().get(0);

        String content =
            "              VHS ESCAPE COMPLETION CERTIFICATE\n" +
            "\n" +
            "------------------------------------------------------------\n" +
            " Player Name   : " + player.getDisplayName() + "\n" +
            " Game Name     : VHS Escape\n" +
            " Difficulty    : " + difficulty + "\n" +
            " Hints Used    : " + progress.getHintsUsed() + "\n" +
            " Final Score   : " + score + "\n" +
            "------------------------------------------------------------\n" +
            "\n" +
            "🏆 You escaped the tape and unlocked the final mystery!\n" +
            "Thanks for playing – your courage and curiosity are unmatched.\n" +
            "\n" +
            "============================================================\n";
        try {
            Files.write(Paths.get("certificate_" + player.getDisplayName() + ".txt"), content.getBytes());
        } catch (IOException e) {
            System.out.println("Error saving certificate: " + e.getMessage());
        }
    }

    /**
     * Adds an item to the current player's inventory.
     * 
     * @param item the item to add to the inventory
     */
    public void addItem(Item item) {
        if(currentPlayer != null) {
            Progress progress = currentPlayer.getProgress().get(currentPlayer.getProgress().size()-1);
            progress.addItem(item);
        }
    }
}


