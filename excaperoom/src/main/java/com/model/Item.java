package com.model;

import java.io.File;

/**
 * Represents an item that can be found, collected, or used within the VHS Escape game.
 * <p>
 * Items may have:
 * <ul>
 *     <li>A name describing the item</li>
 *     <li>A description for narrative or gameplay context</li>
 *     <li>A location indicating where it was found</li>
 *     <li>An optional associated sound file</li>
 * </ul>
 * Items can be added to a player's inventory through {@link Progress}, and may
 * interact with puzzles via {@link #use(Puzzle)}.
 */
public class Item {

    /** The display name of the item. */
    private String name;

    /** A narrative description of the item. */
    private String description;

    /** The location where the item was discovered. */
    private String location;

    /** Optional sound file to play when the item is used or collected. */
    private File sound;

    /**
     * Default constructor initializing all fields to empty or null.
     */
    public Item() {
        this.name = "";
        this.description = "";
        this.location = "";
        this.sound = null;
    }

    /**
     * Constructs an item with the given details.
     *
     * @param name        the name of the item
     * @param description the description of the item
     * @param location    where the item was found
     * @param sound       optional sound file to play on interaction
     */
    public Item(String name, String description, String location, File sound) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.sound = sound;
    }

    /**
     * Attempts to use this item on the specified puzzle.
     * <p>
     * This method:
     * <ul>
     *     <li>Prints feedback to the console</li>
     *     <li>Attempts to play the item's sound if available</li>
     * </ul>
     *
     * @param puzzle the puzzle to use the item on
     * @return {@code true} if the item was used successfully,
     *         {@code false} if no puzzle was provided
     */
    public boolean use(Puzzle puzzle) {
        if (puzzle == null) {
            System.out.println("Cannot use " + name + " - no puzzle selected");
            return false;
        }
        
        System.out.println("Using " + name + " on puzzle");
        
        if (sound != null && sound.exists()) {
            playSound();
        }
        
        return true;
    }

    /**
     * Adds this item to the current player’s inventory.
     * <p>
     * Also attempts to play the item's sound file if present.
     *
     * @return {@code true} indicating the item was added
     */
    public boolean addToInventory() {
        System.out.println(name + " added to inventory");
        
        if (sound != null && sound.exists()) {
            playSound();
        }
        
        return true;
    }

    /**
     * Adds this item to the specified {@link Progress} inventory.
     * <p>
     * Performs validation on the progress object and prints error messages if
     * the inventory or progress object is missing.
     *
     * @param progress the progress object whose inventory will receive the item
     * @return {@code true} if successfully added; {@code false} otherwise
     */
    public boolean addToInventory(Progress progress) {
        if (progress == null) {
            System.out.println("Error: Progress is null");
            return false;
        }
        
        if (progress.getInventory() == null) {
            System.out.println("Error: Inventory not initialized");
            return false;
        }
        
        progress.getInventory().add(this);
        System.out.println(name + " added to inventory");
        
        if (sound != null && sound.exists()) {
            playSound();
        }
        
        return true;
    }

    /**
     * Plays the item's associated sound, if present.
     * <p>
     * Currently implemented as a console stub.
     */
    private void playSound() {
        System.out.println("STUB: Playing sound for item: " + name);
    }

    /**
     * Returns the name of the item.
     *
     * @return item name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the item's description text.
     *
     * @return item description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the location where the item was found.
     *
     * @return item location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the sound file associated with this item.
     *
     * @return sound file, or {@code null} if none exists
     */
    public File getSound() {
        return sound;
    }

    /**
     * Sets the item's name.
     *
     * @param name new name value
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the item's description.
     *
     * @param description new description value
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the location where this item is found.
     *
     * @param location new location value
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Sets the sound file for this item.
     *
     * @param sound the new sound file
     */
    public void setSound(File sound) {
        this.sound = sound;
    }

    /**
     * Returns a human-readable string representation of this item.
     *
     * @return formatted item string
     */
    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    /**
     * Determines whether another object is equal to this item.
     * <p>
     * Items are considered equal if both their names and locations match.
     *
     * @param obj the object to compare with
     * @return {@code true} if equal; {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Item item = (Item) obj;
        return name.equals(item.name) && location.equals(item.location);
    }

    /**
     * Computes the hash code for this item.
     *
     * @return hash code based on name and location
     */
    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }
}
