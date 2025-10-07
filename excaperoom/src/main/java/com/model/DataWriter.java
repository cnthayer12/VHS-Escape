package com.model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class DataWriter {

    private static final String PLAYERS_FILE = "players.json";
    private static final String ROOMS_FILE = "rooms.json";

    public static boolean savePlayers(ArrayList<Player> players) {
        if (players == null) {
            return false;
        }

        JSONArray playersArray = new JSONArray();

        for (Player player : players) {
            JSONObject playerJSON = new JSONObject();
            playerJSON.put("username", player.getDisplayName());

            // Save full progress details
            JSONArray progressArray = new JSONArray();
            if (player.getProgress() != null) {
                for (Progress progress : player.getProgress()) {
                    if (progress != null) {
                        JSONObject progressJSON = new JSONObject();
                        
                        progressJSON.put("hintsUsed", progress.getHintsUsed());
                        progressJSON.put("strikes", progress.getStrikes());
                        progressJSON.put("currentScore", progress.getCurrentScore());

                        // Save inventory
                        JSONArray inventoryArray = new JSONArray();
                        if (progress.getInventory() != null) {
                            for (Item item : progress.getInventory()) {
                                if (item != null) {
                                    inventoryArray.add(item.getName());
                                }
                            }
                        }
                        progressJSON.put("inventory", inventoryArray);

                        // Save stored hints
                        JSONArray hintsArray = new JSONArray();
                        if (progress.getStoredHints() != null) {
                            for (Hint hint : progress.getStoredHints()) {
                                if (hint != null) {
                                    hintsArray.add(hint.getID().toString());
                                }
                            }
                        }
                        progressJSON.put("storedHints", hintsArray);

                        progressArray.add(progressJSON);
                    }
                }
            }
            playerJSON.put("progress", progressArray);
            playersArray.add(playerJSON);
        }

        try (FileWriter file = new FileWriter(PLAYERS_FILE)) {
            file.write(playersArray.toJSONString());
            file.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean saveRooms(ArrayList<Room> rooms) {
        //placeholder for future implementation
        return true;
    }

    public static boolean saveAll (ArrayList<Player> players) {
        return savePlayers(players);
    }
}
