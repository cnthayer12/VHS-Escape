package com.model;

import java.util.ArrayList;
import java.util.UUID;

public class Player {
    private UUID uuid;
    private String displayName;
    private ArrayList<Progress> progress;
    private String password;

    public Player(String displayName, ArrayList<Progress> progress, String password) {
        this.uuid = UUID.randomUUID();
        this.displayName = displayName;
        this.password = password;
        this.progress = progress;
    }

    public Player(UUID uuid, String displayName, ArrayList<Progress> progress, String password) {
        this.uuid = uuid == null ? UUID.randomUUID() : uuid;
        this.displayName = displayName;
        this.progress = progress;
        this.password = password;
    }

    public UUID getId() {
        return uuid;
    }

    public void setId(UUID uuid) {
        this.uuid = uuid;
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

    public void setProgress(ArrayList<Progress> progress) {
        this.progress = progress;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean checkPassword(String password) {
        return password.equals(this.password);
    }

    @Override
    public String toString() {
        return "Player{" +
                "uuid=" + uuid +
                ", displayName='" + displayName + '\'' +
                ", progress=" + progress +
                '}';
    }
}