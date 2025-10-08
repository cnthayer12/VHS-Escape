package com.model;

import java.util.ArrayList;

public class Rooms {
    private static Rooms rooms = null;
    private ArrayList<Room> roomList;

    // Private constructor for Singleton pattern
    private Rooms() {
        // Load existing rooms from JSON via DataLoader
        roomList = DataLoader.loadRooms();
    }

    // Singleton accessor
    public static Rooms getInstance() {
        if (rooms == null) {
            rooms = new Rooms();
        }
        return rooms;
    }

    // Return all rooms
    public ArrayList<Room> getRooms() {
        return roomList;
    }

    // Get a room by its ID
    public Room getRoomByID(String roomID) {
        for (Room room : roomList) {
            if (room.getRoomID().equals(roomID)) {
                return room;
            }
        }
        return null;
    }

    // Add a new room and save using DataWriter
    public void addRoom(Room room) {
        roomList.add(room);
        DataWriter.saveRooms(roomList);
    }

    // Remove a room by ID and save
    public boolean removeRoom(String roomID) {
        for (Room room : roomList) {
            if (room.getRoomID().equals(roomID)) {
                roomList.remove(room);
                DataWriter.saveRooms(roomList);
                return true;
            }
        }
        return false;
    }

    // Save all rooms manually (optional helper)
    public void saveRooms() {
        DataWriter.saveRooms(roomList);
    }
}
