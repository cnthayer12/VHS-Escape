package com.model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataWriterTester {

    public static void main(String[] args) {
        System.out.println("=== DataWriterTester ===");

        Path playersPath = Paths.get("json/players.json");
        Path roomsPath = Paths.get("json/rooms.json");
        Path backup = Paths.get("json/players_backup.json");

        try {
            // --- Back up players.json ---
            if (Files.exists(playersPath)) {
                Files.copy(playersPath, backup, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Backed up existing players.json -> players_backup.json");
            }

            // --- Read rooms.json before ---
            String roomsBefore = readFile(roomsPath);
            if (roomsBefore == null) {
                System.out.println("WARNING: rooms.json not found, skipping integrity check.");
            }

            // --- Create sample data: 4 accounts (Carleigh, Lukin, Bianca, Charlotte) ---
            ArrayList<Player> players = new ArrayList<>();

            // Carleigh (use known UUID from your fixtures)
            Player carleigh = new Player(
                    UUID.fromString("0c84e4ca-e25d-4f21-a409-c78a8543556a"),
                    "Carleigh",
                    new ArrayList<>(),
                    "pass1"
            );
            players.add(carleigh);

            // Lukin
            Player lukin = new Player(
                    UUID.fromString("9a7411d6-45a7-4964-aefd-43f79b6a6fe7"),
                    "Lukin",
                    new ArrayList<>(),
                    "lukinpw"
            );
            players.add(lukin);

            // Bianca (give one Progress entry with some sample values)
            Progress biancaProg = new Progress();
            biancaProg.setStrikes(1);
            biancaProg.setScore(200);
            ArrayList<Progress> biancaProgressList = new ArrayList<>();
            biancaProgressList.add(biancaProg);
            Player bianca = new Player(
                    UUID.fromString("17731615-a3ae-49a9-a77e-a578166e01da"),
                    "Bianca",
                    biancaProgressList,
                    "secret"
            );
            players.add(bianca);

            // Charlotte
            Player charlotte = new Player(
                    UUID.fromString("045cb7de-25af-4bde-a0b6-f4aa91517154"),
                    "Charlotte",
                    new ArrayList<>(),
                    "pw3"
            );
            players.add(charlotte);

            // --- Invoke DataWriter ---
            System.out.println("Writing players.json via DataWriter...");
            boolean result = DataWriter.savePlayers(players);
            System.out.println("DataWriter.savePlayers(...) returned: " + result);

            // --- Validate file exists ---
            File pf = playersPath.toFile();
            if (!pf.exists()) {
                System.err.println("FAIL: players.json was not created!");
                System.exit(1);
            }

            // --- Parse written file and assert wrapper presence (try-with-resources) ---
            JSONParser parser = new JSONParser();
            JSONObject rootObj = null;
            try (FileReader fr = new FileReader(pf)) {
                Object parsed = parser.parse(fr);
                if (!(parsed instanceof JSONObject)) {
                    System.err.println("FAIL: players.json root is not a JSON object; expected wrapper {schemaVersion, users}.");
                    System.exit(2);
                }
                rootObj = (JSONObject) parsed;
            }

            // Check schemaVersion exists and equals 1 (best-effort)
            Object sv = rootObj.get("schemaVersion");
            if (sv == null) {
                System.err.println("FAIL: players.json missing 'schemaVersion' field.");
                System.exit(3);
            } else {
                int svn = -1;
                try {
                    if (sv instanceof Number) svn = ((Number) sv).intValue();
                    else svn = Integer.parseInt(sv.toString());
                } catch (Exception ex) { /* leave svn as -1 */ }
                if (svn != 1) {
                    System.err.println("FAIL: players.json 'schemaVersion' expected 1 but found: " + sv);
                    System.exit(4);
                }
            }

            // Check users array exists
            Object usersRaw = rootObj.get("users");
            if (!(usersRaw instanceof JSONArray)) {
                System.err.println("FAIL: players.json 'users' is missing or not an array.");
                System.exit(5);
            }
            JSONArray usersArr = (JSONArray) usersRaw;
            System.out.println("players.json written successfully with wrapper; users array size = " + usersArr.size());

            // Print small summary of users
            for (Object o : usersArr) {
                if (o instanceof JSONObject) {
                    JSONObject jo = (JSONObject) o;
                    System.out.printf("  -> uuid=%s, name=%s, password=%s%n",
                                      jo.get("uuid"),
                                      jo.get("displayName"),
                                      jo.get("password"));
                }
            }

            // --- Ensure DataLoader can still load players from this file ---
            try {
                ArrayList<Player> loaded = DataLoader.getPlayers();
                if (loaded == null) {
                    System.err.println("FAIL: DataLoader.getPlayers() returned null after DataWriter wrote wrapper file.");
                    System.exit(6);
                } else {
                    System.out.println("DataLoader.getPlayers() loaded " + loaded.size() + " players from written file.");
                    if (loaded.size() < 4) {
                        System.err.println("FAIL: expected DataLoader to load 4 players but loaded " + loaded.size());
                        System.exit(7);
                    }
                }
            } catch (Throwable t) {
                System.err.println("FAIL: DataLoader.getPlayers() threw an exception after writing wrapper file.");
                t.printStackTrace(System.err);
                System.exit(8);
            }

            // --- Verify rooms.json unchanged ---
            String roomsAfter = readFile(roomsPath);
            if (roomsBefore != null && roomsBefore.equals(roomsAfter)) {
                System.out.println("PASS: rooms.json unchanged.");
            } else if (roomsBefore != null) {
                System.err.println("FAIL: rooms.json was modified!");
            }

            // --- Restore original players.json ---
            if (Files.exists(backup)) {
                // Ensure file handles closed, then replace
                Files.move(backup, playersPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                System.out.println("Restored original players.json from backup.");
            }

            System.out.println("\nPASS: DataWriter test completed successfully.");
        } catch (Throwable t) {
            System.err.println("ERROR during DataWriter test:");
            t.printStackTrace();
            try {
                if (Files.exists(backup)) {
                    Files.move(backup, playersPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Restored original players.json after error.");
                }
            } catch (IOException ignored) {}
            System.exit(2);
        }
    }

    private static String readFile(Path path) {
        if (!Files.exists(path)) return null;
        try {
            return Files.readString(path);
        } catch (IOException e) {
            return null;
        }
    }
}
