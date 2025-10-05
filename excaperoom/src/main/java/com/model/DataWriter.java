package com.model;

    import java.io.FileWriter;
    import java.io.IOException;
    import java.util.ArrayList;
    import org.json.simple.JSONArray;
    import org.json.simple.JSONObject;

// DataWriter class handles writing player data and progress to JSON files
public class DataWriter {

        private static final String PLAYERS_FILE = "players.json";
        private static final String PROGRESS_FILE = "progress.json";

    public static boolean savePlayers(ArrayList<Player> players) {
        if ( players == null) {
            return false;
        }

        JSONArray playersArray = new JSONArray();

        for (Player player : players) {
            JSONObject playerJSON = new JSONObject();
            playerJSON.put("username", player.getUsername());

            JSONArray progressArray = new JSONArray();
            if (player.getProgress() != null) {
                for (Progress progress : player.getProgress()) {
                    if (progress != null) {
                        progressArray.add(progress.toString());
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

public static boolean saveProgress(ArrayList<Progress> progressList) {
    if (progressList == null) {
        return false;
    }

    JSONArray progressArray = new JSONArray();

    for (Progress progress : progressList) {
        JSONObject progressJSON = new JSONObject();

        progressJSON.put("hintsUsed", progress.getHintsUsed());
        progressJSON.put("strikes", progress.getStrikes());
        progressJSON.put("currentScore", progress.getCurrentScore());

        JSONArray inventoryArray = new JSONArray();
        if (progress.getInventory() != null) {
            for (Item item : progress.getInventory()) {
                if (item != null) {
                    inventoryArray.add(item.getName());
                }
            }
        }
        progressJSON.put("inventory", inventoryArray);

        JSONArray hintsArray = new JSONArray();
        if (progress.getStoredHints() != null) {
            for (Hint hint : progress.getStoredHints()) {
                if (hint != null) {
                    hintsArray.add(hint.getId().toString());
                }
            }
        }
        progressJSON.put("storedHints", hintsArray);
        progressArray.add(progressJSON);
    }

    try (FileWriter file = new FileWriter(PROGRESS_FILE)) {
        file.write(progressArray.toJSONString());
        file.flush();
        return true;
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
}

public static boolean saveAll(ArrayList<Player> players, ArrayList<Progress> progressList) {
    boolean playersSuccess = savePlayers(players);
    boolean progressSuccess = saveProgress(progressList);
    return playersSuccess && progressSuccess;
    }
}
