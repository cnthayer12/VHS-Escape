package com.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataWriterTester extends DataConstants {

    public static void main(String[] args) {
        System.out.println("=== DataWriterTester ===");

        try {
            // 1) Back up files
            File playersFile = new File(USER_FILE_NAME);
            File roomsFile = new File(PUZZLES_FILE);
            String timeTag = String.valueOf(System.currentTimeMillis());
            File playersBackup = playersFile.exists()
                    ? new File(playersFile.getParent(), "players_backup_" + timeTag + ".json")
                    : null;
            File roomsBackup = roomsFile.exists()
                    ? new File(roomsFile.getParent(), "rooms_backup_" + timeTag + ".json")
                    : null;

            if (playersBackup != null) backupFile(playersFile, playersBackup);
            if (roomsBackup != null) backupFile(roomsFile, roomsBackup);

            if (playersBackup != null) System.out.println("Created backup: " + playersBackup.getPath());
            else System.out.println("No existing players.json to back up.");

            if (roomsBackup != null) System.out.println("Created backup: " + roomsBackup.getPath());
            else System.out.println("No existing rooms.json to back up.");

            // 2) Read existing players.json (or create empty array)
            JSONParser parser = new JSONParser();
            JSONArray existingPlayersJson = null;
            if (playersFile.exists()) {
                try (FileReader fr = new FileReader(playersFile)) {
                    Object parsed = parser.parse(fr);
                    if (parsed instanceof JSONArray) existingPlayersJson = (JSONArray) parsed;
                    else existingPlayersJson = new JSONArray();
                } catch (Throwable t) {
                    System.err.println("Warning: failed to parse existing players.json - will treat as empty array.");
                    existingPlayersJson = new JSONArray();
                }
            } else {
                existingPlayersJson = new JSONArray();
            }

            // Build a set of existing UUIDs to avoid duplicates
            HashSet<String> existingPlayerUUIDs = new HashSet<>();
            for (Object o : existingPlayersJson) {
                if (o instanceof JSONObject) {
                    JSONObject jo = (JSONObject) o;
                    Object uuid = jo.get("uuid");
                    if (uuid != null) existingPlayerUUIDs.add(uuid.toString());
                }
            }

            // 3) Obtain players to append (DataLoader or synthetic)
            ArrayList<Player> playersToAdd = null;
            try {
                playersToAdd = DataLoader.getPlayers();
                System.out.println("DataLoader.getPlayers() returned " + (playersToAdd == null ? "null" : playersToAdd.size()));
            } catch (Throwable t) {
                System.out.println("DataLoader.getPlayers() threw; using synthetic players. (" + t.getMessage() + ")");
            }
            if (playersToAdd == null || playersToAdd.size() == 0) {
                playersToAdd = createSyntheticPlayersForAppend();
                System.out.println("Using " + playersToAdd.size() + " synthetic player(s).");
            }

            // 4) Convert and append non-duplicate players
            int appendedPlayers = 0;
            for (Player p : playersToAdd) {
                String uuidStr = null;
                try {
                    UUID id = p.getId();
                    if (id != null) uuidStr = id.toString();
                } catch (Throwable ignored) {}
                // If player has no id, try to generate one (but we prefer existing id)
                if (uuidStr == null) {
                    try {
                        // create a deterministic-ish id from displayName to avoid accidental duplicates
                        uuidStr = UUID.randomUUID().toString();
                    } catch (Throwable ignored) { uuidStr = UUID.randomUUID().toString(); }
                }

                if (existingPlayerUUIDs.contains(uuidStr)) {
                    System.out.println("Skipping duplicate player uuid=" + uuidStr + " displayName=" + safeString(p.getDisplayName()));
                    continue;
                }

                JSONObject userObj = buildPlayerJsonObject(p, uuidStr);
                existingPlayersJson.add(userObj);
                existingPlayerUUIDs.add(uuidStr);
                appendedPlayers++;
            }

            // 5) Write merged players.json back to disk
            try (FileWriter fw = new FileWriter(playersFile)) {
                fw.write(existingPlayersJson.toJSONString());
                fw.flush();
            } catch (IOException ioe) {
                System.err.println("Failed to write players.json: " + ioe.getMessage());
                System.exit(2);
            }
            System.out.println("Appended " + appendedPlayers + " player(s). New players.json size: " + existingPlayersJson.size());

            // 6) Read existing rooms.json
            JSONObject roomsDoc = null;
            if (roomsFile.exists()) {
                try (FileReader fr = new FileReader(roomsFile)) {
                    Object parsed = parser.parse(fr);
                    if (parsed instanceof JSONObject) roomsDoc = (JSONObject) parsed;
                    else roomsDoc = new JSONObject();
                } catch (Throwable t) {
                    System.err.println("Warning: failed to parse existing rooms.json - will create minimal structure.");
                    roomsDoc = new JSONObject();
                }
            } else {
                roomsDoc = new JSONObject();
            }

            // Ensure minimal structure
            if (roomsDoc.get("global") == null) roomsDoc.put("global", new JSONObject());
            JSONObject global = (JSONObject) roomsDoc.get("global");
            if (global.get("puzzles") == null) global.put("puzzles", new JSONArray());
            JSONArray existingPuzzlesArr = (JSONArray) global.get("puzzles");

            // Build set of existing puzzle IDs to avoid duplicates
            HashSet<String> existingPuzzleIDs = new HashSet<>();
            for (Object o : existingPuzzlesArr) {
                if (o instanceof JSONObject) {
                    JSONObject pj = (JSONObject) o;
                    Object pid = pj.get("puzzleID");
                    if (pid == null) pid = pj.get("id");
                    if (pid != null) existingPuzzleIDs.add(pid.toString());
                }
            }

            // 7) Fetch puzzles from PuzzlesManager reflectively
            ArrayList<Puzzle> puzzlesToAdd = fetchPuzzlesFromManagerForTester();
            System.out.println("Fetched " + (puzzlesToAdd == null ? 0 : puzzlesToAdd.size()) + " puzzle(s) from PuzzlesManager.");

            // 8) Convert and append non-duplicate puzzles
            int appendedPuzzles = 0;
            if (puzzlesToAdd != null) {
                for (Puzzle pz : puzzlesToAdd) {
                    Object idObj = tryInvoke(pz, "getID", "getPuzzleID", "getId");
                    String pidStr = idObj == null ? null : idObj.toString();
                    if (pidStr == null) {
                        // if puzzle has no id, generate one to avoid collision
                        pidStr = UUID.randomUUID().toString();
                    }
                    if (existingPuzzleIDs.contains(pidStr)) {
                        System.out.println("Skipping duplicate puzzle pid=" + pidStr);
                        continue;
                    }
                    JSONObject pj = buildPuzzleJsonObjectForTester(pz, pidStr);
                    existingPuzzlesArr.add(pj);
                    existingPuzzleIDs.add(pidStr);
                    appendedPuzzles++;
                }
            }

            // 9) Write merged rooms.json back to disk
            try (FileWriter fw = new FileWriter(roomsFile)) {
                fw.write(roomsDoc.toJSONString());
                fw.flush();
            } catch (IOException ioe) {
                System.err.println("Failed to write rooms.json: " + ioe.getMessage());
                System.exit(2);
            }

            System.out.println("Appended " + appendedPuzzles + " puzzle(s). New global.puzzles size: " + existingPuzzlesArr.size());
            System.out.println("\nPASS: append test completed successfully.");
            System.exit(0);

        } catch (Throwable t) {
            System.err.println("ERROR: unexpected exception:");
            t.printStackTrace(System.err);
            System.exit(3);
        }
    }

    // -------------------------------------------------------------------------
    // Helpers used inside this tester (convert model objects -> JSON, fetch puzzles)
    // -------------------------------------------------------------------------

    private static void backupFile(File original, File backup) {
        try {
            Files.copy(original.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("WARNING: failed to back up " + original.getPath() + ": " + e.getMessage());
        }
    }

    private static ArrayList<Player> createSyntheticPlayersForAppend() {
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<Progress> pList = new ArrayList<>();
        Progress prog = new Progress();
        prog.setInventory(new ArrayList<Item>());
        prog.setStoredHints(new ArrayList<Hint>());
        prog.setStrikes(0);
        prog.setScore(0);
        pList.add(prog);
        Player p1 = new Player("append-user-1", pList, "password");
        try { p1.setId(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")); } catch (Throwable ignored) {}
        Player p2 = new Player("append-user-2", pList, "password");
        try { p2.setId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb")); } catch (Throwable ignored) {}
        players.add(p1);
        players.add(p2);
        return players;
    }

    // Build player JSON matching writer shape (password top-level, progress object)
    private static JSONObject buildPlayerJsonObject(Player p, String uuidStr) {
        JSONObject jo = new JSONObject();
        try { jo.put("uuid", uuidStr); } catch (Throwable ignored) {}
        try { jo.put(USER_ID, uuidStr); } catch (Throwable ignored) {}
        try { jo.put("displayName", p.getDisplayName()); } catch (Throwable ignored) {}
        try { jo.put(USER_NAME, p.getDisplayName()); } catch (Throwable ignored) {}

        // password: try getPassword() then empty string
        String password = "";
        try {
            Method gp = p.getClass().getMethod("getPassword");
            Object pw = gp.invoke(p);
            if (pw != null) password = pw.toString();
        } catch (Throwable ignored) {}

        jo.put("password", password);

        JSONObject progressObj = new JSONObject();
        try {
            ArrayList<Progress> progs = p.getProgress();
            Progress prog = (progs != null && progs.size() > 0) ? progs.get(0) : null;

            progressObj.put("hintsUsed", prog != null ? prog.getHintsUsed() : 0);

            JSONArray invArr = new JSONArray();
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
            progressObj.put("inventory", invArr);

            JSONArray shArr = new JSONArray();
            if (prog != null && prog.getStoredHints() != null) {
                for (Hint h : prog.getStoredHints()) {
                    JSONObject hj = new JSONObject();
                    try { hj.put("id", ""); } catch (Throwable ignored) {}
                    try { hj.put("text", h.getText()); } catch (Throwable ignored) {}
                    try { hj.put("cost", h.getCost()); } catch (Throwable ignored) {}
                    shArr.add(hj);
                }
            }
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
                Object curr = prog != null ? prog.getCurrentPuzzle() : null;
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

            progressObj.put("strikes", prog != null ? prog.getStrikes() : 0);
            progressObj.put("currentScore", prog != null ? prog.getCurrentScore() : 0);

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
        return jo;
    }

    // Fetch puzzles similarly to DataWriter's helper but exposed here
    @SuppressWarnings("unchecked")
    private static ArrayList<Puzzle> fetchPuzzlesFromManagerForTester() {
        ArrayList<Puzzle> puzzles = null;
        try {
            PuzzlesManager pm = null;
            try { pm = PuzzlesManager.getInstance(); } catch (Throwable ignored) {}
            if (pm == null) return new ArrayList<>();

            try {
                Method getList = pm.getClass().getMethod("getPuzzles");
                Object obj = getList.invoke(pm);
                if (obj instanceof List) puzzles = new ArrayList<>((List<Puzzle>) obj);
            } catch (NoSuchMethodException nsme) {
                try {
                    Method getAll = pm.getClass().getMethod("getAllPuzzles");
                    Object obj = getAll.invoke(pm);
                    if (obj instanceof List) puzzles = new ArrayList<>((List<Puzzle>) obj);
                } catch (NoSuchMethodException nsme2) {
                    try {
                        Method getCurrent = pm.getClass().getMethod("getCurrentPuzzle");
                        Object curr = getCurrent.invoke(pm);
                        if (curr instanceof Puzzle) {
                            puzzles = new ArrayList<>();
                            puzzles.add((Puzzle) curr);
                        }
                    } catch (NoSuchMethodException nsme3) {
                        puzzles = new ArrayList<>();
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            puzzles = new ArrayList<>();
        }
        if (puzzles == null) puzzles = new ArrayList<>();
        return puzzles;
    }

    // Convert Puzzle -> JSONObject used in rooms.json global.puzzles
    private static JSONObject buildPuzzleJsonObjectForTester(Puzzle p, String pidStr) {
        JSONObject pj = new JSONObject();
        try { pj.put("puzzleID", pidStr); } catch (Throwable ignored) {}
        try {
            Object typeObj = tryInvoke(p, "getType", "getPuzzleType");
            if (typeObj != null) pj.put("puzzleType", typeObj.toString());
        } catch (Throwable ignored) {}
        try {
            Object prompt = tryInvoke(p, "getPrompt", "getPromptText", "getTriviaText");
            if (prompt != null) pj.put("prompt", prompt.toString());
        } catch (Throwable ignored) {}
        try {
            Object ans = tryInvoke(p, "getAnswer", "getCombination", "getCorrectChoice", "getAnswerText");
            if (ans != null) pj.put("answer", ans.toString());
        } catch (Throwable ignored) {}

        JSONArray hintArr = new JSONArray();
        try {
            Object hintsObj = tryInvoke(p, "getHints", "getAllHints");
            if (hintsObj instanceof List) {
                List<?> hints = (List<?>) hintsObj;
                for (Object ho : hints) {
                    if (!(ho instanceof Hint)) continue;
                    Hint h = (Hint) ho;
                    JSONObject hj = new JSONObject();
                    try { hj.put("id", ""); } catch (Throwable ignored) {}
                    try { hj.put("text", h.getText()); } catch (Throwable ignored) {}
                    try { hj.put("cost", h.getCost()); } catch (Throwable ignored) {}
                    hintArr.add(hj);
                }
            }
        } catch (Throwable ignored) {}
        pj.put("hints", hintArr);

        return pj;
    }

    private static Object tryInvoke(Object target, String... methodNames) {
        if (target == null) return null;
        for (String name : methodNames) {
            try {
                Method m = target.getClass().getMethod(name);
                Object res = m.invoke(target);
                if (res != null) return res;
            } catch (NoSuchMethodException nsme) {
                // try next
            } catch (Throwable t) {
                // ignore and continue
            }
        }
        return null;
    }

    private static String safeString(String s) {
        return s == null ? "<null>" : s;
    }
}
