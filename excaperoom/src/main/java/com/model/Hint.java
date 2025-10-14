package com.model;

import java.util.UUID;

/**
 * Hint class - Represents a hint for a puzzle
 * STUB VERSION
 */
public class Hint {
    private UUID id;
    private String text;
    private boolean used;
    private int cost;
    
    /**
     * Constructor
     */
    public Hint() {
        this.id = UUID.randomUUID();
        this.text = "";
        this.used = false;
        this.cost = 10;
    }
    
    /**
     * Constructor with parameters
     */
    public Hint(String text, int cost) {
        this.id = UUID.randomUUID();
        this.text = text;
        this.used = false;
        this.cost = cost;
    }
    
    /**
     * Reveal hint - STUB
     */
    public String revealHint() {
        System.out.println("STUB Hint.revealHint() called");
        used = true;
        return text;
    }
    
    /**
     * Mark as used - STUB
     */
    public void markUsed() {
        System.out.println("STUB Hint.markUsed() called");
        used = true;
    }
    
    /**
     * Reset hint - STUB
     */
    public void reset() {
        System.out.println("STUB Hint.reset() called");
        used = false;
    }
    
    /**
     * Get ID - STUB
     */
    public UUID getId() {
        return id;
    }
    
    /**
     * Get text - STUB
     */
    public String getText() {
        return text;
    }
    
    /**
     * Is used - STUB
     */
    public boolean isUsed() {
        return used;
    }
    
    /**
     * Get cost - STUB
     */
    public int getCost() {
        return cost;
    }
    
    /**
     * Set text - STUB
     */
    public void setText(String text) {
        this.text = text;
    }
    
    /**
     * Set cost - STUB
     */
    public void setCost(int cost) {
        this.cost = cost;
    }
    
    @Override
    public String toString() {
        return id.toString();
    }
}