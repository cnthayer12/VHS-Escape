package com.model;

import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Utility class responsible for serializing {@link Player} data to disk.
 * <p>
 * This writer converts players and their associated progress, inventory,
 * hints, and puzzle state into a JSON structure and saves it to
 * {@code json/players.json}. The output format is:
 * <pre>
 * {
 *   "schemaVersion": 1,
 *   "users": [
 *     { ... player object ... },
 *     ...
 *   ]
 * }
 * </pre>
 */
public class DataWriter {

    /** Path to the JSON file where player data is stored. */
    private static final String USER_FILE_NAME = "json/players.json";

    /**
     * Saves the given list of players to the {@code players.json} file.
     * <p>
     * Behavior:
     * <ul>
     *     <li>Writes a top-level object with {@code schemaVersion} and
     *         a {@code users} array.</li>
     *     <li>Each user entry contains:
     *         <ul>
     *             <li>{@code uuid} – player ID, if available</li>
     *             <li>{@code displayName}</li>
     *             <li>{@code password} – top-level password string</li>
     *             <li>{@code progress} – sub-object containing:
     *                 <ul>
     *                     <li>{@code hintsUsed}</li>
     *                     <li>{@code inventory}</li>
     *                     <li>{@code storedHints}</li>
     *                     <li>{@code completedPuzzles}</li>
     *                     <li>{@code currentPuzzle}</li>
     *                     <li>{@code strikes}</li>
     *                     <li>{@code currentScore}</li>
     *                 </ul>
     *             </li>
     *         </ul>
     *     </li>
     *     <li>Is tolerant of missing getters and null values, filling
     *         in reasonable defaults.</li>
     * </ul>
     *
     * @param players the list of players to be saved; may be {@code null}
     * @return {@code true} if the data was written successfully;
     *         {@code false} if an error occurred
     */
    @SuppressWarnings("unchecked")
    public static boolean savePlayers(ArrayList<Player> players) {
        JSONArray out = new JSONArray();
        try {
            if (players != null) {
                for (Player p : players) {
                    JSONObject jo = new JSONObject();

                    // --- uuid ---
                    try {
                        UUID id = null;
                        try { id = p.getId(); } catch (Throwable ignored) {}
                        if (id != null) {
                            jo.put("uuid", id.toString());
                        }
                    } catch (Throwable ignored) {}

                    // --- displayName ---
                    try {
                        String name = null;
                        try { name = p.getDisplayName(); } catch (Throwable ignored) {}
                        if (name != null) jo.put("displayName", name);
                    } catch (Throwable ignored) {}

                    // --- top-level password ---
                    try {
                        String password = "";
                        try {
                            Method gp = p.getClass().getMethod("getPassword");
                            Object pw = gp.invoke(p);
                            if (pw != null) password = pw.toString();
                        } catch (NoSuchMethodException nsme) {
                            // no getter: leave empty string
                        } catch (Throwable ignored) {}
                        jo.put("password", password == null ? "" : password);
                    } catch (Throwable ignored) {
                        try { jo.put("password", ""); } catch (Throwable ignored2) {}
                    }

                    // --- progress object (kept shape) ---
                    JSONObject progressObj = new JSONObject();
                    try {
                        ArrayList<Progress> progs = null;
                        try { progs = p.getProgress(); } catch (Throwable ignored) {}
                        Progress prog = (progs != null && progs.size() > 0) ? progs.get(0) : null;

                        // hintsUsed
                        try { progressObj.put("hintsUsed", prog != null ? prog.getHintsUsed() : 0); } catch (Throwable ignored) { progressObj.put("hintsUsed", 0); }

                        // inventory
                        JSONArray invArr = new JSONArray();
                        System.out.println("Saving player: " + p.getDisplayName()); //
                        System.out.println("Progress object: " + prog); //
                        System.out.println("Inventory size: " + (prog != null && prog.getInventory() != null ? prog.getInventory().size() : "null")); //
                        try {
                            if (prog != null && prog.getInventory() != null) {
                                for (Item it : prog.getInventory()) {
                                    JSONObject itemObj = new JSONObject();
                                    try { itemObj.put("uuid", ""); } catch (Throwable ignored) {}
                                    try { itemObj.put("name", it.getName()); } catch (Throwable ignored) {}
                                    try { itemObj.put("description", it.getDescription()); } catch (Throwable ignored) {}
                                    try { itemObj.put("location", it.getLocation()); } catch (Throwable ignored) {}
                                    invArr.add(itemObj);
                                }
                            }
                        } catch (Throwable ignored) {}
                        progressObj.put("inventory", invArr);

                        // storedHints
                        JSONArray shArr = new JSONArray();
                        try {
                            if (prog != null && prog.getStoredHints() != null) {
                                for (Hint h : prog.getStoredHints()) {
                                    JSONObject hj = new JSONObject();
                                    try { hj.put("id", ""); } catch (Throwable ignored) {}
                                    try { hj.put("text", h.getText()); } catch (Throwable ignored) {}
                                    try { hj.put("cost", h.getCost()); } catch (Throwable ignored) {}
                                    try {
                                        Method isUsedM = h.getClass().getMethod("isUsed");
                                        Object used = isUsedM.invoke(h);
                                        if (used instanceof Boolean) hj.put("used", (Boolean) used);
                                    } catch (NoSuchMethodException nsme) {
                                        // ignore
                                    } catch (Throwable ignored) {}
                                    shArr.add(hj);
                                }
                            }
                        } catch (Throwable ignored) {}
                        progressObj.put("storedHints", shArr);

                        // completedPuzzles
                        JSONArray completedArr = new JSONArray();
                        try {
                            if (prog != null && prog.getCompletedPuzzles() != null) {
                                for (Object cp : prog.getCompletedPuzzles()) {
                                    try {
                                        Method gid = cp.getClass().getMethod("getID");
                                        Object idv = gid.invoke(cp);
                                        completedArr.add(idv != null ? idv.toString() : cp.toString());
                                    } catch (Throwable t) {
                                        completedArr.add(cp.toString());
                                    }
                                }
                            }
                        } catch (Throwable ignored) {}
                        progressObj.put("completedPuzzles", completedArr);

                        // currentPuzzle
                        try {
                            Object curr = (prog != null) ? prog.getCurrentPuzzle() : null;
                            if (curr == null) progressObj.put("currentPuzzle", null);
                            else {
                                JSONObject currObj = new JSONObject();
                                try {
                                    Method gid = curr.getClass().getMethod("getID");
                                    Object idv = gid.invoke(curr);
                                    if (idv != null) currObj.put("puzzleID", idv.toString());
                                } catch (Throwable ignored) {}
                                progressObj.put("currentPuzzle", currObj);
                            }
                        } catch (Throwable ignored) { progressObj.put("currentPuzzle", null); }

                        // strikes & currentScore
                        try { progressObj.put("strikes", prog != null ? prog.getStrikes() : 0); } catch (Throwable ignored) { progressObj.put("strikes", 0); }
                        try { progressObj.put("currentScore", prog != null ? prog.getCurrentScore() : 0); } catch (Throwable ignored) { progressObj.put("currentScore", 0); }

                    } catch (Throwable ignored) {
                        progressObj.put("hintsUsed", 0);
                        progressObj.put("inventory", new JSONArray());
                        progressObj.put("storedHints", new JSONArray());
                        progressObj.put("completedPuzzles", new JSONArray());
                        progressObj.put("currentPuzzle", null);
                        progressObj.put("strikes", 0);
                        progressObj.put("currentScore", 0);
                    }

                    jo.put("progress", progressObj);

                    out.add(jo);
                }
            }

            // --- NEW: preserve wrapper with schemaVersion and users ---
            JSONObject root = new JSONObject();
            root.put("schemaVersion", 1);
            root.put("users", out);

            // write players file (wrapped)
            try (FileWriter fw = new FileWriter(USER_FILE_NAME)) {
                fw.write(root.toJSONString());
                fw.flush();
            }

            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }
}

