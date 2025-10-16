package com.model;

import java.util.ArrayList;

public class Room {
    private String roomID;
    private String title;
    private String story;
    private PuzzlesManager puzzleManager;
    private ArrayList<ItemPuzzle> hidden;

    public Room(String roomID, String title, String story, PuzzlesManager puzzleManager, ArrayList<ItemPuzzle> hidden) {
        this.roomID = roomID;
        this.title = title;
        this.story = story;
        this.puzzleManager = puzzleManager;
        this.hidden = hidden;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public PuzzlesManager getPuzzleManager() {
        return puzzleManager;
    }

    public void setPuzzleManager(PuzzlesManager puzzleManager) {
        this.puzzleManager = puzzleManager;
    }

    public ArrayList<ItemPuzzle> getHidden() {
        return hidden;
    }

    public void setHidden(ArrayList<ItemPuzzle> hidden) {
        this.hidden = hidden;
    }
}

