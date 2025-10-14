package com.model;

import java.util.ArrayList;

public class Rooms {
    private static Rooms rooms = null;
    private ArrayList<Room> roomList;

    public static Rooms getInstance() {
        if (rooms == null) {
            rooms = new Rooms();
        }
        return rooms;
    }

    public ArrayList<Room> getRooms() {
        return roomList;
    }

    public Room getRoomByID(String roomID) {
        for (Room room : roomList) {
            if (room.getRoomID().equals(roomID)) {
                return room;
            }
        }
        return null;
    }

    public void addRoom(Room room) {
        roomList.add(room);
        DataWriter.saveRooms(roomList);
    }

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

    public void saveRooms() {
        DataWriter.saveRooms(roomList);
    }
}
