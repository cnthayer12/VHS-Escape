package com.model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class DataWriter extends DataConstants {
    public static boolean savePlayers(ArrayList<Player> players) {
        if (players == null) {
            return false;
        }

        JSONArray playersArray = new JSONArray();

        for (Player player : players) {
            JSONObject playerJSON = new JSONObject();
            playerJSON.put(USER_ID, player.getId().toString());
            playerJSON.put(USER_NAME, player.getDisplayName());

            // Save full progress details
            JSONArray progressArray = new JSONArray();
            if (player.getProgress() != null) {
                for (Progress progress : player.getProgress()) {
                    if (progress != null) {
                        JSONObject progressJSON = new JSONObject();
                        
                        progressJSON.put(USER_HINTS_USED, progress.getHintsUsed());
                        progressJSON.put(USER_STRIKES, progress.getStrikes());
                        progressJSON.put(USER_CURRENT_SCORE, progress.getCurrentScore());

                        // Save inventory (just save empty array since Item class is empty)
                        JSONArray inventoryArray = new JSONArray();
                        if (progress.getInventory() != null) {
                            // For now, just save count since Item has no properties
                            for (Item item : progress.getInventory()) {
                                if (item != null) {
                                    inventoryArray.add(new JSONObject()); // empty object placeholder
                                }
                            }
                        }
                        progressJSON.put(USER_INVENTORY, inventoryArray);

                        // Save stored hints
                        JSONArray hintsArray = new JSONArray();
                        if (progress.getStoredHints() != null) {
                            for (Hint hint : progress.getStoredHints()) {
                                if (hint != null) {
                                    JSONObject hintJSON = new JSONObject();
                                    hintJSON.put("id", hint.getId().toString());
                                    hintJSON.put("text", hint.getText());
                                    hintJSON.put("used", hint.isUsed());
                                    hintJSON.put("cost", hint.getCost());
                                    hintsArray.add(hintJSON);
                                }
                            }
                        }
                        progressJSON.put(USER_STORED_HINTS, hintsArray);

                        progressArray.add(progressJSON);
                    }
                }
            }
            playerJSON.put("progress", progressArray);
            playersArray.add(playerJSON);
        }

        try (FileWriter file = new FileWriter(USER_FILE_NAME)) {
            file.write(playersArray.toJSONString());
            file.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean savePuzzlesState(PuzzlesManager puzzlesManager) {
        if (puzzlesManager == null) {
            return false;
        }

        JSONObject stateJSON = new JSONObject();
        JSONArray puzzlesArray = new JSONArray();

        // Save all puzzles and their hints state
        if (puzzlesManager.getPuzzles() != null) {
            for (Puzzle puzzle : puzzlesManager.getPuzzles()) {
                if (puzzle == null) continue;
                
                JSONObject puzzleJSON = new JSONObject();
                
                // Save hints state (only thing that changes at runtime)
                JSONArray hintsArray = new JSONArray();
                if (puzzle.getHints() != null) {
                    for (Hint hint : puzzle.getHints()) {
                        if (hint != null) {
                            JSONObject hintJSON = new JSONObject();
                            hintJSON.put("id", hint.getId().toString());
                            hintJSON.put("used", hint.isUsed());
                            hintsArray.add(hintJSON);
                        }
                    }
                }
                puzzleJSON.put("hints", hintsArray);
                
                puzzlesArray.add(puzzleJSON);
            }
        }

        // Save current puzzle index or identifier
        if (puzzlesManager.getCurrentPuzzle() != null) {
            int currentIndex = puzzlesManager.getPuzzles().indexOf(puzzlesManager.getCurrentPuzzle());
            stateJSON.put("currentPuzzleIndex", currentIndex);
        }
        stateJSON.put("puzzles", puzzlesArray);

        try (FileWriter file = new FileWriter(PUZZLES_STATE_FILE)) {
            file.write(stateJSON.toJSONString());
            file.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveRooms(ArrayList<Room> rooms) {
        // Placeholder - rooms are loaded from config, not saved at runtime
        // This method exists to prevent errors in Rooms.java
        System.out.println("saveRooms() called - rooms are configuration only, no runtime save needed");
        return true;
    }

    public static boolean saveAll(ArrayList<Player> players, PuzzlesManager puzzlesManager) {
        boolean playersSaved = savePlayers(players);
        boolean puzzlesStateSaved = savePuzzlesState(puzzlesManager);
        return playersSaved && puzzlesStateSaved;
    }
}
