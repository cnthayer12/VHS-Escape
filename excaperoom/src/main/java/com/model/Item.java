package com.model;

import java.io.File;

public class Item {
    private String name;
    private String description;
    private String location;
    private File sound;

    public Item() {
        this.name = "";
        this.description = "";
        this.location = "";
        this.sound = null;
    }

    public Item(String name, String description, String location, File sound) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.sound = sound;
    }

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

    public boolean addToInventory() {
        System.out.println(name + " added to inventory");
        
        if (sound != null && sound.exists()) {
            playSound();
        }
        
        return true;
    }
    
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

    private void playSound() {
        System.out.println("STUB: Playing sound for item: " + name);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public File getSound() {
        return sound;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSound(File sound) {
        this.sound = sound;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Item item = (Item) obj;
        return name.equals(item.name) && location.equals(item.location);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }
}