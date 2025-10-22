package com.model;

import java.util.UUID;

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
    
    public String revealHint() {
        if (!used) {
            used = true;
            EscapeGameFacade.getInstance().saveProgress();
        }
        return text;
    }
    
    public void markUsed() {
        if (!used) {
            used = true;
            EscapeGameFacade.getInstance().saveProgress();
        }
    }
    
    public void reset() {
        used = false;
    }
    
    public UUID getId() {
        return id;
    }
    
    public String getText() {
        return text;
    }

    public boolean isUsed() {
        return used;
    }
    
    public int getCost() {
        return cost;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public void setCost(int cost) {
        this.cost = cost;
    }
    
    @Override
    public String toString() {
        return "[Hint: " + id.toString() + ", used=" + used + ", cost=" + cost + "]";
    }
}
