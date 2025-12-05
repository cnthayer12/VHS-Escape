package com.model;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Represents a player in the VHS Escape game.
 * Each player has a unique identifier, display name, password, and tracks their progress.
 * 
 * @author VHS Escape Team
 * @version 1.0
 */
public class Player {
    /** Unique identifier for the player */
    private UUID uuid;
    
    /** The player's display name */
    private String displayName;
    
    /** List of progress records for the player across multiple game sessions */
    private ArrayList<Progress> progress;
    
    /** The player's password for authentication */
    private String password;

    /**
     * Constructs a new Player with an auto-generated UUID.
     * 
     * @param displayName the player's display name
     * @param progress the list of progress records
     * @param password the player's password
     */
    public Player(String displayName, ArrayList<Progress> progress, String password) {
        this.uuid = UUID.randomUUID();
        this.displayName = displayName;
        this.password = password;
        this.progress = progress;
    }

    /**
     * Constructs a new Player with a specified UUID.
     * If the provided UUID is null, a new one will be generated.
     * 
     * @param uuid the unique identifier (null to auto-generate)
     * @param displayName the player's display name
     * @param progress the list of progress records
     * @param password the player's password
     */
    public Player(UUID uuid, String displayName, ArrayList<Progress> progress, String password) {
        this.uuid = uuid == null ? UUID.randomUUID() : uuid;
        this.displayName = displayName;
        this.progress = progress;
        this.password = password;
    }

    /**
     * Gets the player's unique identifier.
     * 
     * @return the UUID of the player
     */
    public UUID getId() {
        return uuid;
    }

    /**
     * Sets the player's unique identifier.
     * 
     * @param uuid the UUID to set
     */
    public void setId(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Gets the player's display name.
     * 
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the player's display name.
     * 
     * @param displayName the display name to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the list of all progress records for this player.
     * 
     * @return the ArrayList of Progress objects
     */
    public ArrayList<Progress> getProgress() {
        return progress;
    }

    /**
     * Sets the list of progress records for this player.
     * 
     * @param progress the ArrayList of Progress objects to set
     */
    public void setProgress(ArrayList<Progress> progress) {
        this.progress = progress;
    }

    /**
     * Sets the player's password.
     * Prevents setting an empty password and prints an error message if attempted.
     * 
     * @param password the password to set
     */
    public void setPassword(String password) {
        if(password.equals("")) {
            System.out.println("Password cannot be empty");
            return;
        }
        this.password = password;
    }

    /**
     * Gets the player's password.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Checks if the provided password matches the player's password.
     * 
     * @param password the password to verify
     * @return true if the password matches, false otherwise
     */
    public Boolean checkPassword(String password) {
        return password.equals(this.password);
    }

    /**
     * Returns a string representation of this Player.
     * 
     * @return a string containing the player's UUID, display name, and progress
     */
    @Override
    public String toString() {
        return "Player{" +
                "uuid=" + uuid +
                ", displayName='" + displayName + '\'' +
                ", progress=" + progress +
                '}';
    }
}