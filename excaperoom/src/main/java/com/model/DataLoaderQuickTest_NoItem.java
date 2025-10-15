package com.model;

import java.util.ArrayList;

/**
 * Minimal DataLoader tester that ignores Item-related fields.
 */
public class DataLoaderQuickTest_NoItem {
    public static void main(String[] args) {
        System.out.println("=== DataLoader Quick Test (no Item) ===");
        ArrayList<Player> players = DataLoader.getPlayers();

        if (players == null || players.isEmpty()) {
            System.out.println("No players loaded.");
            return;
        }

        for (Player p : players) {
            System.out.println("\nPlayer: " + p.getDisplayName() + "  [id=" + p.getId() + "]");
            if (p.getProgress() == null || p.getProgress().isEmpty()) {
                System.out.println("  (no progress data)");
                continue;
            }

            Progress pr = p.getProgress().get(0);
            System.out.println("  Hints used: " + pr.getHintsUsed());
            System.out.println("  Strikes: " + pr.getStrikes());
            System.out.println("  Current score: " + pr.getCurrentScore());

            try {
                ArrayList<?> storedHints = pr.getStoredHints();
                if (storedHints == null || storedHints.isEmpty()) {
                    System.out.println("  Stored hints: []");
                } else {
                    System.out.println("  Stored hints:");
                    for (Object obj : storedHints) {
                        if (obj instanceof Hint) {
                            Hint h = (Hint) obj;
                            System.out.println("    - " + h.getText() + " (cost: " + h.getCost() + ")");
                        } else {
                            System.out.println("    - " + obj);
                        }
                    }
                }
            } catch (Throwable t) {
                System.out.println("  (could not read stored hints: " + t.getMessage() + ")");
            }
        }

        System.out.println("\n=== Test complete ===");
    }
}