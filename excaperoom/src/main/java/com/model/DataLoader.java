package com.model;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataLoader extends DataConstants {

    @SuppressWarnings("unchecked")
    public static ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<Player>();

        File f = new File(USER_FILE_NAME);
        if (!f.exists()) {
            return players;
        }

        try (FileReader reader = new FileReader(f)) {
            Object parsed = new JSONParser().parse(reader);

            // normalize to a JSONArray called peopleJSON
            JSONArray peopleJSON = null;
            if (parsed instanceof JSONArray) {
                peopleJSON = (JSONArray) parsed;
            } else if (parsed instanceof JSONObject) {
                JSONObject root = (JSONObject) parsed;

                // NEW: check schemaVersion if present
                try {
                    Object sv = root.get("schemaVersion");
                    if (sv != null) {
                        int svn = -1;
                        try {
                            if (sv instanceof Number) svn = ((Number) sv).intValue();
                            else svn = Integer.parseInt(sv.toString());
                        } catch (Exception ignore) {}
                        if (svn != -1 && svn != 1) {
                            System.out.println("Warning: players.json schemaVersion=" + svn + " (expected 1). Proceeding to parse 'users'/'players'.");
                        }
                    }
                } catch (Throwable ignore) {}

                // FIRST: accept "users" (preferred wrapper)
                Object maybeUsers = root.get("users");
                if (maybeUsers instanceof JSONArray) {
                    peopleJSON = (JSONArray) maybeUsers;
                } else {
                    // SECOND: accept "players" wrapper (other variants)
                    Object maybePlayers = root.get("players");
                    if (maybePlayers instanceof JSONArray) {
                        peopleJSON = (JSONArray) maybePlayers;
                    } else {
                        // If root *looks like* a single player (has displayName/uuid/progress),
                        // treat it as a single-player object. Otherwise produce an empty array.
                        if (root.containsKey(USER_NAME) || root.containsKey("uuid") || root.containsKey("progress")) {
                            peopleJSON = new JSONArray();
                            peopleJSON.add(root);
                        } else {
                            peopleJSON = new JSONArray(); // empty
                        }
                    }
                }
            } else {
                // unexpected structure -> empty list
                peopleJSON = new JSONArray();
            }

            for (int i = 0; i < peopleJSON.size(); i++) {
                Object entry = peopleJSON.get(i);
                if (!(entry instanceof JSONObject)) continue;
                JSONObject personJSON = (JSONObject) entry;

                // id/uuid (support both "uuid" and USER_ID)
                UUID id = null;
                try {
                    Object maybeUuid = personJSON.get("uuid");
                    if (maybeUuid == null) maybeUuid = personJSON.get(USER_ID);
                    if (maybeUuid != null) {
                        id = UUID.fromString(maybeUuid.toString());
                    }
                } catch (Exception ex) {
                    // ignore; leave id null
                }

                String displayName = personJSON.get(USER_NAME) == null ? "" : personJSON.get(USER_NAME).toString();

                // Prefer top-level password. If missing, fall back to progress.password (legacy).
                String password = "";
                try {
                    Object pwTop = personJSON.get("password");
                    if (pwTop != null) password = pwTop.toString();
                } catch (Throwable ignored) {}

                if (password == null || password.length() == 0) {
                    // fallback: check inside progress object (legacy shape some files had)
                    try {
                        Object progRaw = personJSON.get("progress");
                        if (progRaw instanceof JSONObject) {
                            JSONObject progJ = (JSONObject) progRaw;
                            Object pwProg = progJ.get("password");
                            if (pwProg != null) password = pwProg.toString();
                        }
                    } catch (Throwable ignored) {}
                }

                // Build Progress object (use no-arg constructor and setters for robustness)
                Progress progress = new Progress();

                // inventory: parse into Item objects (accept simple string or object)
                ArrayList<Item> inventory = new ArrayList<>();

                try {
                    Object progObj = personJSON.get("progress");
                    if (progObj instanceof JSONObject) {
                        JSONObject progJ = (JSONObject) progObj;

                        // inventory
                        try {
                            Object inv = progJ.get(USER_INVENTORY);
                            if (inv instanceof JSONArray) {
                                JSONArray invA = (JSONArray) inv;
                                for (Object itRaw : invA) {
                                    if (itRaw instanceof JSONObject) {
                                        JSONObject itJ = (JSONObject) itRaw;
                                        Item it = new Item();
                                        try { if (itJ.get("name") != null) it.setName(itJ.get("name").toString()); } catch (Throwable ignore) {}
                                        try { if (itJ.get("description") != null) it.setDescription(itJ.get("description").toString()); } catch (Throwable ignore) {}
                                        try { if (itJ.get("location") != null) it.setLocation(itJ.get("location").toString()); } catch (Throwable ignore) {}
                                        inventory.add(it);
                                    }
                                }
                            }
                        } catch (Throwable ignored) {}
                        progress.setInventory(inventory);

                        // storedHints
                        ArrayList<Hint> stored = new ArrayList<>();
                        try {
                            Object sh = progJ.get(USER_STORED_HINTS);
                            if (sh instanceof JSONArray) {
                                for (Object hj : (JSONArray) sh) {
                                    if (!(hj instanceof JSONObject)) continue;
                                    JSONObject hjo = (JSONObject) hj;
                                    Hint hint = new Hint();
                                    try { if (hjo.get("text") != null) hint.setText(hjo.get("text").toString()); } catch (Throwable ignore) {}
                                    try { if (hjo.get("cost") != null) hint.setCost(((Long) hjo.get("cost")).intValue()); } catch (Throwable ignore) {}
                                    try {
                                        if (hjo.get("used") != null) {
                                            Method setUsed = hint.getClass().getMethod("setUsed", boolean.class);
                                            setUsed.invoke(hint, ((Boolean) hjo.get("used")).booleanValue());
                                        }
                                    } catch (Throwable ignore) {}
                                    stored.add(hint);
                                }
                            }
                        } catch (Throwable ignored) {}
                        progress.setStoredHints(stored);

                        // strikes / score
                        try { if (progJ.get(USER_STRIKES) != null) progress.setStrikes(((Long) progJ.get(USER_STRIKES)).intValue()); } catch (Exception ignored) {}
                        try { if (progJ.get(USER_CURRENT_SCORE) != null) progress.setScore(((Long) progJ.get(USER_CURRENT_SCORE)).intValue()); } catch (Exception ignored) {}

                        // currentPuzzle and completedPuzzles left empty (loader does not instantiate puzzles here)
                        // completed puzzles could be read if needed (but the original loader leaves them out)
                    }
                } catch (Throwable ignored) {}

                ArrayList<Progress> progressList = new ArrayList<>();
                progressList.add(progress);

                Player player;
                if (id != null) {
                    // Use constructor with id if available
                    try {
                        player = new Player(id, displayName, progressList, password);
                    } catch (NoSuchMethodError | NoClassDefFoundError ex) {
                        player = new Player(displayName, progressList, password);
                        try { player.setId(id); } catch (Exception ignore) {}
                    }
                } else {
                    player = new Player(displayName, progressList, password);
                }

                players.add(player);
            }

            return players;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return players;
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Puzzle> loadPuzzles() {
        ArrayList<Puzzle> loaded = new ArrayList<>();
        File pf = new File(PUZZLES_FILE);
        if (!pf.exists()) return loaded;

        try (FileReader reader = new FileReader(pf)) {
            Object parsed = new JSONParser().parse(reader);
            JSONArray puzzlesJSON;
            if (parsed instanceof JSONArray) {
                puzzlesJSON = (JSONArray) parsed;
            } else if (parsed instanceof JSONObject) {
                JSONObject root = (JSONObject) parsed;
                Object maybe = root.get("puzzles");
                if (maybe instanceof JSONArray) {
                    puzzlesJSON = (JSONArray) maybe;
                } else {
                    puzzlesJSON = new JSONArray();
                    puzzlesJSON.add(root);
                }
            } else {
                puzzlesJSON = new JSONArray();
            }

            for (Object obj : puzzlesJSON) {
                if (!(obj instanceof JSONObject)) continue;
                JSONObject pj = (JSONObject) obj;

                Puzzle puzzle = new Puzzle();

                // set ID if present (support "id" or "uuid")
                if (pj.get("id") != null) {
                    try { puzzle.setID(UUID.fromString(pj.get("id").toString())); } catch (Exception ignored) {}
                } else if (pj.get("uuid") != null) {
                    try { puzzle.setID(UUID.fromString(pj.get("uuid").toString())); } catch (Exception ignored) {}
                }

                // hints
                ArrayList<Hint> hints = new ArrayList<>();
                Object hObj = pj.get("hints");
                if (hObj instanceof JSONArray) {
                    for (Object h : (JSONArray) hObj) {
                        if (h instanceof JSONObject) {
                            JSONObject hj = (JSONObject) h;
                            Hint hint = new Hint();
                            try { if (hj.get("text") != null) hint.setText(hj.get("text").toString()); } catch (Throwable ignore) {}
                            try { if (hj.get("cost") != null) hint.setCost(((Long) hj.get("cost")).intValue()); } catch (Throwable ignore) {}
                            if (hj.get("used") != null) {
                                try {
                                    Method setUsed = hint.getClass().getMethod("setUsed", boolean.class);
                                    setUsed.invoke(hint, ((Boolean) hj.get("used")).booleanValue());
                                } catch (Exception ignore) { /* optional */ }
                            }
                            hints.add(hint);
                        }
                    }
                }
                try { puzzle.getClass().getMethod("setHints", java.util.List.class).invoke(puzzle, hints); }
                catch (Exception ignore) {
                    // setHints does not exist: try to add via addHint(Hint)
                    try {
                        Method addHint = puzzle.getClass().getMethod("addHint", Hint.class);
                        for (Hint hh : hints) addHint.invoke(puzzle, hh);
                    } catch (Exception ignore2) { /* unable to populate hints */ }
                }

                // other puzzle metadata can be read here if needed (prompt, combination, meta fields etc.)
                loaded.add(puzzle);

                // attempt to register puzzle with PuzzlesManager if available
                try {
                    PuzzlesManager pm = PuzzlesManager.getInstance();
                    try {
                        Method addM = pm.getClass().getMethod("addPuzzle", Puzzle.class);
                        addM.invoke(pm, puzzle);
                    } catch (NoSuchMethodException nsme) {
                        // try getPuzzles() and add to returned list
                        try {
                            Method getList = pm.getClass().getMethod("getPuzzles");
                            Object listObj = getList.invoke(pm);
                            if (listObj instanceof java.util.List) {
                                ((java.util.List) listObj).add(puzzle);
                            }
                        } catch (Exception ignore) {
                            // last resort: try to set currentPuzzle if "current" flag present
                            if (pj.get("current") != null && (pj.get("current") instanceof Boolean) && ((Boolean) pj.get("current"))) {
                                try {
                                    Method setCurrent = pm.getClass().getMethod("setCurrentPuzzle", Puzzle.class);
                                    setCurrent.invoke(pm, puzzle);
                                } catch (Exception ignored) {}
                            }
                        }
                    }
                } catch (Throwable t) {
                    // if PuzzlesManager is not available or methods differ, ignore quietly
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return loaded;
    }

    // Convenience: loads both players and puzzles and returns players
    public static ArrayList<Player> loadAllWithPuzzles() {
        ArrayList<Player> players = getPlayers();
        loadPuzzles(); // populates PuzzlesManager if possible
        return players;
    }
}
