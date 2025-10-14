package com.model;

import java.util.ArrayList;
import java.util.UUID;

public class Player {
    private UUID id;
    private String displayName;
    private ArrayList<Progress> progress;

    public Player(String displayName, ArrayList<Progress> progress) {
        this.id = UUID.randomUUID();
        this.displayName = displayName;
        this.progress = progress;
    }

    public Player(UUID id, String displayName, ArrayList<Progress> progress) {
        this.id = id;
        this.displayName = displayName;
        this.progress = progress;
    }

    public UUID getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ArrayList<Progress> getProgress() {
        return progress;
    }

    public ArrayList<Progress> setProgress(ArrayList<Progress> progress) {
        return progress;
    }
}