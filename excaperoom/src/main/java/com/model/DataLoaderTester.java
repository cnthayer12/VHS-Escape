package com.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DataLoaderTester {

    // Known accounts from your players.json
    private static final String[] EXPECTED_UUIDS = new String[] {
        "0c84e4ca-e25d-4f21-a409-c78a8543556a", // Carleigh
        "9a7411d6-45a7-4964-aefd-43f79b6a6fe7", // Lukin
        "17731615-a3ae-49a9-a77e-a578166e01da", // Bianca
        "045cb7de-25af-4bde-a0b6-f4aa91517154"  // Charlotte
    };
    private static final String[] EXPECTED_NAMES = new String[] {
        "Carleigh", "Lukin", "Bianca", "Charlotte"
    };

    public static void main(String[] args) {
        System.out.println("=== DataLoaderTester ===");

        try {
            ArrayList<Player> players = DataLoader.getPlayers();
            if (players == null) {
                System.err.println("ERROR: DataLoader.getPlayers() returned null");
                System.exit(2);
            }

            System.out.println("Total loaded players: " + players.size());
            if (players.size() < 4) {
                System.err.println("FAIL: expected at least 4 player accounts in json/players.json");
                System.exit(2);
            }

            System.out.println("\n--- All loaded players ---");
            for (int i = 0; i < players.size(); i++) {
                Player p = players.get(i);
                String id = (p.getId() == null) ? "<no-id>" : p.getId().toString();
                String name = (p.getDisplayName() == null) ? "<no-name>" : p.getDisplayName();
                System.out.printf("  [%d] id=%s, name=%s%n", i, id, name);
            }

            // Build sets for verification
            Set<String> foundUUIDs = new HashSet<>();
            Set<String> foundNames = new HashSet<>();
            for (Player p : players) {
                if (p.getId() != null) foundUUIDs.add(p.getId().toString());
                if (p.getDisplayName() != null) foundNames.add(p.getDisplayName());
            }

            boolean uuidAllPresent = true;
            for (String u : EXPECTED_UUIDS) {
                if (!foundUUIDs.contains(u)) {
                    System.err.println("MISSING expected UUID: " + u);
                    uuidAllPresent = false;
                } else {
                    System.out.println("FOUND expected UUID: " + u);
                }
            }

            boolean namesAllPresent = true;
            for (String n : EXPECTED_NAMES) {
                if (!foundNames.contains(n)) {
                    System.err.println("MISSING expected displayName: " + n);
                    namesAllPresent = false;
                } else {
                    System.out.println("FOUND expected displayName: " + n);
                }
            }

            if (!uuidAllPresent || !namesAllPresent) {
                System.err.println("\nFAIL: one or more expected accounts are missing.");
                System.exit(2);
            }

            // Non-fatal: try to call DataLoader.loadPuzzles() reflectively if present
            try {
                Method loadPuzzlesM = DataLoader.class.getMethod("loadPuzzles");
                Object result = loadPuzzlesM.invoke(null);
                if (result instanceof ArrayList) {
                    @SuppressWarnings("unchecked")
                    ArrayList<?> puzzles = (ArrayList<?>) result;
                    System.out.println("\nPuzzles loaded (via reflective call): " + puzzles.size());
                } else if (result == null) {
                    System.out.println("\nDataLoader.loadPuzzles() returned null (treated as 0).");
                } else {
                    System.out.println("\nDataLoader.loadPuzzles() returned: " + result.getClass().getName());
                }
            } catch (NoSuchMethodException nsme) {
                System.out.println("\nDataLoader.loadPuzzles() not present - skipping puzzle checks.");
            } catch (Throwable t) {
                System.out.println("\nWarning: invoking DataLoader.loadPuzzles() threw an exception (non-fatal):");
                t.printStackTrace(System.out);
            }

            System.out.println("\nPASS: All 4 expected accounts parsed successfully.");
            System.exit(0);

        } catch (Throwable t) {
            System.err.println("ERROR: unexpected exception:");
            t.printStackTrace(System.err);
            System.exit(3);
        }
    }
}
