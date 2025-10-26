package com.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * DataWriter - writes players with canonical "uuid" only (no "id" keys).
 *
 * - Players saved with keys: "uuid", "displayName", "password", "progress"
 * - Never writes "id" or USER_ID into players.json
 * - Keeps rooms.json merging behavior for puzzles (dedupes by puzzleID)
 *
 * Defensive: uses reflective getters where appropriate; tolerant I/O.
 */
public class DataWriter {

    private static final String USER_FILE_NAME = "json/players.json";
    private static final String ROOMS_FILE = "json/rooms.json";
    private static final String PUZZLES_FILE = "json/puzzles.json";

    // -------------------------
    // Players writer (canonical uuid only)
    // -------------------------
    @SuppressWarnings("unchecked")
    public static boolean savePlayers(ArrayList<Player> players) {
        JSONArray out = new JSONArray();
        try {
            if (players != null) {
                for (Player p : players) {
                    JSONObject jo = new JSONObject();

                    // --- Identifier: write only "uuid" (canonical) if available ---
                    try {
                        UUID id = null;
                        try { id = p.getId(); } catch (Throwable ignored) {}
                        if (id != null) {
                            jo.put("uuid", id.toString());
                        }
                        // intentionally NOT writing "id" or any alternate id key
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

            // write players file
            try (FileWriter fw = new FileWriter(USER_FILE_NAME)) {
                fw.write(out.toJSONString());
                fw.flush();
            }

            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    // -------------------------
    // Rooms / puzzles writer (merge w/ dedupe by puzzleID)
    // -------------------------
    @SuppressWarnings("unchecked")
    public static boolean savePuzzlesIntoRoomsJson() {
        try {
            // 1) Fetch puzzles from manager (best-effort)
            ArrayList<Puzzle> puzzles = fetchPuzzlesFromManager();
            if (puzzles == null) puzzles = new ArrayList<>();

            // 2) Read existing rooms.json and preserve structure
            JSONObject roomsDoc = null;
            File f = new File(ROOMS_FILE);
            if (f.exists()) {
                try (FileReader fr = new FileReader(f)) {
                    Object parsed = new JSONParser().parse(fr);
                    if (parsed instanceof JSONObject) roomsDoc = (JSONObject) parsed;
                } catch (Throwable t) {
                    System.err.println("Warning: failed to parse existing rooms.json; will create minimal structure:");
                    t.printStackTrace();
                }
            }

            if (roomsDoc == null) {
                roomsDoc = new JSONObject();
                roomsDoc.put("schemaVersion", 1L);
                roomsDoc.put("global", new JSONObject());
                roomsDoc.put("rooms", new JSONArray());
            }

            JSONObject global = (JSONObject) roomsDoc.get("global");
            if (global == null) {
                global = new JSONObject();
                roomsDoc.put("global", global);
            }
            JSONArray existingPuzzles = (JSONArray) global.get("puzzles");
            if (existingPuzzles == null) {
                existingPuzzles = new JSONArray();
                global.put("puzzles", existingPuzzles);
            }

            // Build set of existing puzzleIDs
            Set<String> existingIds = new HashSet<>();
            for (Object o : existingPuzzles) {
                if (!(o instanceof JSONObject)) continue;
                JSONObject ep = (JSONObject) o;
                Object idv = ep.get("puzzleID");
                if (idv == null) idv = ep.get("id");
                if (idv != null) existingIds.add(idv.toString());
            }

            // Convert fetched puzzles to JSON and append only new ones
            Set<String> seenNew = new HashSet<>();
            int appended = 0;
            for (Puzzle p : puzzles) {
                try {
                    Object idObj = tryInvoke(p, "getID", "getPuzzleID", "getId");
                    if (idObj == null) continue;
                    String pid = idObj.toString();
                    if (pid == null || pid.length() == 0) continue;

                    if (existingIds.contains(pid) || seenNew.contains(pid)) continue;
                    seenNew.add(pid);

                    JSONObject pj = new JSONObject();
                    pj.put("puzzleID", pid);

                    Object typeObj = tryInvoke(p, "getType", "getPuzzleType");
                    if (typeObj != null) pj.put("puzzleType", typeObj.toString());

                    Object prompt = tryInvoke(p, "getPrompt", "getPromptText", "getTriviaText", "getRiddleText");
                    if (prompt != null) pj.put("prompt", prompt.toString());

                    Object ans = tryInvoke(p, "getAnswer", "getCombination", "getCorrectChoice", "getAnswerText");
                    if (ans != null) pj.put("answer", ans.toString());

                    // hints
                    JSONArray hintArr = new JSONArray();
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
                            try {
                                Method isUsedM = h.getClass().getMethod("isUsed");
                                Object used = isUsedM.invoke(h);
                                if (used instanceof Boolean) hj.put("used", (Boolean) used);
                            } catch (NoSuchMethodException nsme) {}
                            hintArr.add(hj);
                        }
                    }
                    pj.put("hints", hintArr);

                    existingPuzzles.add(pj);
                    existingIds.add(pid);
                    appended++;
                } catch (Throwable t) {
                    // skip problematic puzzle and continue
                }
            }

            // Optionally update rooms[].hidden puzzle.type where we can match IDs
            try {
                JSONArray roomsArr = (JSONArray) roomsDoc.get("rooms");
                if (roomsArr != null) {
                    java.util.Map<String, Object> typeById = new java.util.HashMap<>();
                    for (Object o : existingPuzzles) {
                        if (!(o instanceof JSONObject)) continue;
                        JSONObject pjson = (JSONObject) o;
                        Object pid = pjson.get("puzzleID");
                        Object ptype = pjson.get("puzzleType");
                        if (pid != null && ptype != null) typeById.put(pid.toString(), ptype);
                    }
                    for (Object rObj : roomsArr) {
                        if (!(rObj instanceof JSONObject)) continue;
                        JSONObject rj = (JSONObject) rObj;
                        Object hiddenObj = rj.get("hidden");
                        if (hiddenObj instanceof JSONArray) {
                            JSONArray hidden = (JSONArray) hiddenObj;
                            for (Object hid : hidden) {
                                if (!(hid instanceof JSONObject)) continue;
                                JSONObject hItem = (JSONObject) hid;
                                Object puzzleRef = hItem.get("puzzle");
                                if (puzzleRef instanceof JSONObject) {
                                    JSONObject pRef = (JSONObject) puzzleRef;
                                    Object pid = pRef.get("id");
                                    if (pid != null) {
                                        Object ptype = typeById.get(pid.toString());
                                        if (ptype != null) pRef.put("type", ptype.toString());
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Throwable ignored) {}

            // write rooms.json back
            try (FileWriter fw = new FileWriter(ROOMS_FILE)) {
                fw.write(roomsDoc.toJSONString());
                fw.flush();
            }

            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    // -------------------------
    // Helpers
    // -------------------------
    @SuppressWarnings("unchecked")
    private static ArrayList<Puzzle> fetchPuzzlesFromManager() {
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
                // method exists but failed; continue to next
            }
        }
        return null;
    }

    // Convenience combined save (players list only; puzzles writer uses manager)
    public static boolean saveAll(ArrayList<Player> players) {
        boolean okPlayers = savePlayers(players);
        boolean okPuzzles = savePuzzlesIntoRoomsJson();
        return okPlayers && okPuzzles;
    }
}
