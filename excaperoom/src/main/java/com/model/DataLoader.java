package com.model;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataLoader extends DataConstants {

    public static ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<Player>();

        try {
            FileReader reader = new FileReader(USER_FILE_NAME);
            Object parsed = new JSONParser().parse(reader);

            // normalize to a JSONArray called peopleJSON
            JSONArray peopleJSON = null;
            if (parsed instanceof JSONArray) {
                peopleJSON = (JSONArray) parsed;
            } else if (parsed instanceof JSONObject) {
                JSONObject root = (JSONObject) parsed;
                // try common wrapper keys: "users" or "players"
                Object maybeUsers = root.get("users");
                if (maybeUsers instanceof JSONArray) {
                    peopleJSON = (JSONArray) maybeUsers;
                } else {
                    Object maybePlayers = root.get("players");
                    if (maybePlayers instanceof JSONArray) {
                        peopleJSON = (JSONArray) maybePlayers;
                    } else {
                        // treat the root as a single player object
                        peopleJSON = new JSONArray();
                        peopleJSON.add(root);
                    }
                }
            } else {
                // unexpected structure -> empty list
                peopleJSON = new JSONArray();
            }

            for (int i = 0; i < peopleJSON.size(); i++) {
                JSONObject personJSON = (JSONObject) peopleJSON.get(i);

                // id/uuid (support both "uuid" and "id")
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

                String displayName = personJSON.get(USER_NAME) == null ? null : personJSON.get(USER_NAME).toString();

                // nested progress object (this matches your uploaded players.json schema)
                int hintsUsed = 0;
                ArrayList<Item> inventory = new ArrayList<Item>();
                ArrayList<Hint> storedHints = new ArrayList<Hint>();
                int strikes = 0;
                int currentScore = 0;

                Object progObj = personJSON.get("progress");
                if (progObj instanceof JSONObject) {
                    JSONObject progJSON = (JSONObject) progObj;
                    if (progJSON.get(USER_HINTS_USED) != null) {
                        hintsUsed = ((Long) progJSON.get(USER_HINTS_USED)).intValue();
                    }

                    // inventory: parse into Item objects
                    Object invObj = progJSON.get(USER_INVENTORY);
                    if (invObj instanceof JSONArray) {
                        JSONArray invArr = (JSONArray) invObj;
                        for (Object it : invArr) {
                            if (it instanceof JSONObject) {
                                JSONObject itemObj = (JSONObject) it;
                                String name = itemObj.get("name") == null ? null : itemObj.get("name").toString();
                                String desc = itemObj.get("description") == null ? null : itemObj.get("description").toString();
                                inventory.add(new Item());
                            } else if (it != null) {
                                // inventory element is a primitive/string — wrap into Item with name only
                                inventory.add(new Item());
                            }
                        }
                    }

                    // storedHints: parse into Hint objects
                    Object shObj = progJSON.get(USER_STORED_HINTS);
                    if (shObj instanceof JSONArray) {
                        JSONArray shArr = (JSONArray) shObj;
                        for (Object h : shArr) {
                            if (h instanceof JSONObject) {
                                JSONObject hJ = (JSONObject) h;
                                String text = hJ.get("text") == null ? null : hJ.get("text").toString();
                                int cost = 0;
                                if (hJ.get("cost") != null) {
                                    cost = ((Long) hJ.get("cost")).intValue();
                                }
                                storedHints.add(new Hint(text, cost));
                            } else if (h != null) {
                                // plain string hint
                                storedHints.add(new Hint());
                            }
                        }
                    }

                    if (progJSON.get(USER_STRIKES) != null) {
                        strikes = ((Long) progJSON.get(USER_STRIKES)).intValue();
                    }
                    if (progJSON.get(USER_CURRENT_SCORE) != null) {
                        currentScore = ((Long) progJSON.get(USER_CURRENT_SCORE)).intValue();
                    }
                } else {
                    // backward-compat: top-level fields
                    if (personJSON.get(USER_HINTS_USED) != null) {
                        hintsUsed = ((Long) personJSON.get(USER_HINTS_USED)).intValue();
                    }

                    Object invObj = personJSON.get(USER_INVENTORY);
                    if (invObj instanceof JSONArray) {
                        JSONArray invArr = (JSONArray) invObj;
                        for (Object it : invArr) {
                            if (it instanceof JSONObject) {
                                JSONObject itemObj = (JSONObject) it;
                                String name = itemObj.get("name") == null ? null : itemObj.get("name").toString();
                                String desc = itemObj.get("description") == null ? null : itemObj.get("description").toString();
                                inventory.add(new Item());
                            } else if (it != null) {
                                inventory.add(new Item());
                            }
                        }
                    }

                    Object shObj = personJSON.get(USER_STORED_HINTS);
                    if (shObj instanceof JSONArray) {
                        JSONArray shArr = (JSONArray) shObj;
                        for (Object h : shArr) {
                            if (h instanceof JSONObject) {
                                JSONObject hJ = (JSONObject) h;
                                String text = hJ.get("text") == null ? null : hJ.get("text").toString();
                                int cost = 0;
                                if (hJ.get("cost") != null) {
                                    cost = ((Long) hJ.get("cost")).intValue();
                                }
                                storedHints.add(new Hint(text, cost));
                            } else if (h != null) {
                                storedHints.add(new Hint());
                            }
                        }
                    }

                    if (personJSON.get(USER_STRIKES) != null) {
                        strikes = ((Long) personJSON.get(USER_STRIKES)).intValue();
                    }
                    if (personJSON.get(USER_CURRENT_SCORE) != null) {
                        currentScore = ((Long) personJSON.get(USER_CURRENT_SCORE)).intValue();
                    }
                }

                // Build Progress (with typed lists)
                Progress progress = new Progress(hintsUsed, inventory, storedHints, strikes, currentScore);
                ArrayList<Progress> progressList = new ArrayList<Progress>();
                progressList.add(progress);

                Player player = (id != null) ? new Player(id, displayName, progressList) : new Player(displayName, progressList);
                players.add(player);
            }

            return players;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return players;
    }

    public static void main(String[] args) {
        ArrayList<Player> players = DataLoader.getPlayers();

        for (Player player : players) {
            System.out.println(player);
        }
    }
}