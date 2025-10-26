package com.model;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    /**
     * Helper: attempt to call a setter on target object.
     * Tries the declared setter name with argument type matching value when possible.
     * Returns true if a setter was invoked.
     */
    private static boolean trySet(Object target, String setterName, Object value) {
        if (target == null || setterName == null || value == null) return false;
        Class<?> cls = target.getClass();
        try {
            // direct match first
            Method m = null;
            for (Method mm : cls.getMethods()) {
                if (mm.getName().equals(setterName) && mm.getParameterCount() == 1) {
                    m = mm;
                    break;
                }
            }
            if (m == null) {
                return false;
            }
            Class<?> param = m.getParameterTypes()[0];
            Object arg = convertArg(param, value);
            if (arg == null && value != null) {
                // if conversion failed, try passing the raw string (if param is String)
                if (param == String.class) arg = value.toString();
                else return false;
            }
            m.invoke(target, arg);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Convert value (primitive wrapper / String / JSONArray / JSONObject / Number) to an instance of param class when possible.
     */
    private static Object convertArg(Class<?> param, Object value) {
        if (value == null) return null;
        if (param.isInstance(value)) return value;
        String s = value.toString();
        try {
            if (param == String.class) return s;
            if (param == int.class || param == Integer.class) return Integer.parseInt(s);
            if (param == long.class || param == Long.class) return Long.parseLong(s);
            if (param == boolean.class || param == Boolean.class) {
                if (value instanceof Boolean) return value;
                return Boolean.parseBoolean(s);
            }
            if (param == List.class || param == java.util.List.class) {
                if (value instanceof JSONArray) {
                    // convert JSONArray to List<String> or List<Object>
                    JSONArray a = (JSONArray) value;
                    List<Object> lst = new ArrayList<>();
                    for (Object o : a) lst.add(o);
                    return lst;
                }
            }
            if (param == Map.class || param == java.util.Map.class) {
                if (value instanceof JSONObject) {
                    JSONObject jo = (JSONObject) value;
                    Map<String,Object> map = new HashMap<>();
                    for (Object k : jo.keySet()) map.put(k.toString(), jo.get(k));
                    return map;
                }
            }
            // if param is enum, try valueOf
            if (param.isEnum()) {
                @SuppressWarnings({ "unchecked", "rawtypes" })
                Enum ev = Enum.valueOf((Class<Enum>) param, s);
                return ev;
            }
        } catch (Throwable ignored) {}
        // Last resort: try common boxed types
        if ((param == Integer.class || param == int.class) && value instanceof Number) return ((Number) value).intValue();
        if ((param == Long.class || param == long.class) && value instanceof Number) return ((Number) value).longValue();
        if ((param == Double.class || param == double.class) && value instanceof Number) return ((Number) value).doubleValue();
        return null;
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

                // Determine puzzle type (check several common keys)
                String typeStr = null;
                try {
                    Object t = pj.get("type");
                    if (t == null) t = pj.get("puzzleType");
                    if (t == null) t = pj.get("puzzle_type");
                    if (t != null) typeStr = t.toString();
                } catch (Throwable ignored) {}

                // Instantiate a concrete subclass based on type
                Puzzle puzzle;
                if (typeStr != null) {
                    switch (typeStr) {
                        case "Trivia":
                            puzzle = new Trivia();
                            break;
                        case "Riddle":
                            puzzle = new Riddle();
                            break;
                        case "PixelHunt":
                            puzzle = new PixelHunt();
                            break;
                        case "MultipleChoice":
                            puzzle = new MultipleChoice();
                            break;
                        case "Cipher":
                            puzzle = new Cipher();
                            break;
                        case "ItemPuzzle":
                            puzzle = new ItemPuzzle();
                            break;
                        default:
                            puzzle = new Puzzle();
                            break;
                    }
                } else {
                    puzzle = new Puzzle();
                }

                // set type on object
                try { puzzle.setType(typeStr == null ? "" : typeStr); } catch (Throwable ignored) {}

                // set ID if present (puzzleID, id, uuid, puzzleID)
                try {
                    Object idVal = pj.get("puzzleID");
                    if (idVal == null) idVal = pj.get("id");
                    if (idVal == null) idVal = pj.get("uuid");
                    if (idVal != null) {
                        try { puzzle.setID(UUID.fromString(idVal.toString())); } catch (Throwable ignored) {}
                    }
                } catch (Throwable ignored) {}

                // HINTS: parse to Hint objects
                ArrayList<Hint> hints = new ArrayList<>();
                try {
                    Object hObj = pj.get("hints");
                    if (hObj instanceof JSONArray) {
                        JSONArray hArr = (JSONArray) hObj;
                        for (Object hh : hArr) {
                            if (!(hh instanceof JSONObject)) continue;
                            JSONObject hJ = (JSONObject) hh;
                            Hint hint = new Hint();
                            try { if (hJ.get("id") != null) hint.setId(hJ.get("id").toString()); } catch (Throwable ignored) {}
                            try { if (hJ.get("text") != null) hint.setText(hJ.get("text").toString()); } catch (Throwable ignored) {}
                            try { if (hJ.get("cost") != null) hint.setCost(((Number) hJ.get("cost")).intValue()); } catch (Throwable ignored) {}
                            try {
                                if (hJ.get("used") != null) {
                                    Method setUsed = hint.getClass().getMethod("setUsed", boolean.class);
                                    setUsed.invoke(hint, ((Boolean) hJ.get("used")).booleanValue());
                                }
                            } catch (Throwable ignore) {}
                            hints.add(hint);
                        }
                    }
                } catch (Throwable ignored) {}

                // Attach hints to puzzle (prefer setHints, else addHint)
                try {
                    Method setHints = puzzle.getClass().getMethod("setHints", java.util.List.class);
                    setHints.invoke(puzzle, hints);
                } catch (Throwable t) {
                    try {
                        Method addHint = puzzle.getClass().getMethod("addHint", Hint.class);
                        for (Hint hh : hints) {
                            try { addHint.invoke(puzzle, hh); } catch (Throwable ignored) {}
                        }
                    } catch (Throwable ignored) {}
                }

                // Map common fields from JSON to puzzle via trySet (uses reflection)
                // define mapping of jsonKey -> candidate setter names (tries each)
                Map<String, String[]> mapping = new LinkedHashMap<>();
                mapping.put("question", new String[] {"setQuestion", "setPrompt", "setTriviaText", "setRiddleText"});
                mapping.put("triviaText", new String[] {"setTriviaText", "setPrompt", "setQuestion"});
                mapping.put("riddleText", new String[] {"setRiddleText", "setPrompt", "setQuestion"});
                mapping.put("cipherText", new String[] {"setCipherText", "setPrompt", "setQuestion"});
                mapping.put("correctAnswer", new String[] {"setCorrectAnswer", "setAnswer", "setSolution"});
                mapping.put("correctChoice", new String[] {"setCorrectChoice", "setCorrectIndex"});
                mapping.put("choices", new String[] {"setChoices", "setOptions"});
                mapping.put("choicesList", new String[] {"setChoices", "setOptions"});
                mapping.put("meta", new String[] {"setMeta", "setProperties"});
                mapping.put("correctSound", new String[] {"setCorrectSound"});
                mapping.put("incorrectSound", new String[] {"setIncorrectSound"});
                mapping.put("combination", new String[] {"setCombination"});
                mapping.put("questionText", new String[] {"setQuestion", "setPrompt"});
                mapping.put("answer", new String[] {"setAnswer", "setCorrectAnswer", "setSolution"});
                mapping.put("prompt", new String[] {"setPrompt", "setQuestion"});
                mapping.put("promptText", new String[] {"setPrompt", "setQuestion"});
                mapping.put("puzzleText", new String[] {"setPrompt", "setQuestion"});
                mapping.put("correct", new String[] {"setCorrect", "setSolved"});
                mapping.put("correctAnswer", new String[] {"setCorrectAnswer", "setAnswer"});
                // Also include 'question' synonyms like 'triviaText' or 'riddleText' above

                // For each json key, try to set using the candidate setter names
                for (Map.Entry<String,String[]> me : mapping.entrySet()) {
                    String jsonKey = me.getKey();
                    Object jsonVal = pj.get(jsonKey);
                    if (jsonVal == null) continue;
                    for (String setter : me.getValue()) {
                        if (trySet(puzzle, setter, jsonVal)) break;
                    }
                }

                // special handling for 'choices' to ensure list conversion
                try {
                    Object choicesObj = pj.get("choices");
                    if (choicesObj instanceof JSONArray) {
                        List<String> choices = new ArrayList<>();
                        for (Object c : (JSONArray) choicesObj) choices.add(c == null ? null : c.toString());
                        // try setter names
                        trySet(puzzle, "setChoices", choices);
                        trySet(puzzle, "setOptions", choices);
                    }
                } catch (Throwable ignored) {}

                // special handling for meta (JSONObject) - try setMeta(Map) or setMethod/setShift etc.
                try {
                    Object metaObj = pj.get("meta");
                    if (metaObj instanceof JSONObject) {
                        JSONObject metaJ = (JSONObject) metaObj;
                        // try setMeta(Map)
                        Map<String,Object> metaMap = new HashMap<>();
                        for (Object k : metaJ.keySet()) metaMap.put(k.toString(), metaJ.get(k));
                        trySet(puzzle, "setMeta", metaMap);
                        // also try specific meta fields
                        if (metaJ.get("method") != null) trySet(puzzle, "setMethod", metaJ.get("method").toString());
                        if (metaJ.get("shift") != null) trySet(puzzle, "setShift", metaJ.get("shift"));
                        if (metaJ.get("note") != null) trySet(puzzle, "setNote", metaJ.get("note").toString());
                    }
                } catch (Throwable ignored) {}

                // some puzzles use 'correctAnswer' key vs 'correctChoice' -> attempt to set both
                try {
                    Object ca = pj.get("correctAnswer");
                    if (ca != null) {
                        trySet(puzzle, "setCorrectAnswer", ca);
                        trySet(puzzle, "setAnswer", ca);
                        trySet(puzzle, "setSolution", ca);
                    }
                } catch (Throwable ignored) {}

                // attempt to set correctChoice (int) if present
                try {
                    Object cc = pj.get("correctChoice");
                    if (cc != null) {
                        trySet(puzzle, "setCorrectChoice", cc);
                        trySet(puzzle, "setCorrectIndex", cc);
                    }
                } catch (Throwable ignored) {}

                // attempt to set sound files
                try {
                    if (pj.get("correctSound") != null) trySet(puzzle, "setCorrectSound", pj.get("correctSound").toString());
                    if (pj.get("incorrectSound") != null) trySet(puzzle, "setIncorrectSound", pj.get("incorrectSound").toString());
                } catch (Throwable ignored) {}

                // Attempt to set other likely fields directly
                String[] directKeys = new String[] {"question", "triviaText", "riddleText", "cipherText", "prompt", "answer", "combination"};
                for (String k : directKeys) {
                    try {
                        Object val = pj.get(k);
                        if (val != null) {
                            trySet(puzzle, "set" + capitalize(k), val);
                        }
                    } catch (Throwable ignored) {}
                }

                // Add puzzle to loaded list
                loaded.add(puzzle);

                // try to register with PuzzlesManager (best-effort)
                try {
                    PuzzlesManager pm = PuzzlesManager.getInstance();
                    try {
                        Method addM = pm.getClass().getMethod("addPuzzle", Puzzle.class);
                        addM.invoke(pm, puzzle);
                    } catch (NoSuchMethodException nsme) {
                        try {
                            Method getList = pm.getClass().getMethod("getPuzzles");
                            Object listObj = getList.invoke(pm);
                            if (listObj instanceof java.util.List) {
                                ((java.util.List) listObj).add(puzzle);
                            }
                        } catch (Exception ignore) {
                            if (pj.get("current") instanceof Boolean && ((Boolean) pj.get("current"))) {
                                try {
                                    Method setCurrent = pm.getClass().getMethod("setCurrentPuzzle", Puzzle.class);
                                    setCurrent.invoke(pm, puzzle);
                                } catch (Exception ignored) {}
                            }
                        }
                    }
                } catch (Throwable t) {
                    // ignore if PuzzlesManager is not present or incompatible
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return loaded;
    }

    // convenience: capitalize first letter
    private static String capitalize(String s) {
        if (s == null || s.length() == 0) return s;
        if (s.length() == 1) return s.toUpperCase();
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }

    // Convenience: loads both players and puzzles and returns players
    public static ArrayList<Player> loadAllWithPuzzles() {
        ArrayList<Player> players = getPlayers();
        loadPuzzles(); // populates PuzzlesManager if possible
        return players;
    }
}