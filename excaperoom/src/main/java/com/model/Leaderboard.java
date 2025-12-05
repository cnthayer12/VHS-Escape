package com.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Manages all leaderboard data for the escape room game.
 * <p>
 * The {@code Leaderboard} follows a singleton pattern, ensuring that all game
 * components reference a single leaderboard instance. Scores are sorted
 * primarily by total score (descending) and secondarily by completion time
 * (ascending).
 * <p>
 * The leaderboard supports:
 * <ul>
 *     <li>Adding new scores</li>
 *     <li>Retrieving top N entries</li>
 *     <li>Filtering by difficulty</li>
 *     <li>Determining a player’s best score and rank</li>
 *     <li>Displaying formatted leaderboards</li>
 * </ul>
 */
public class Leaderboard {

    /** Singleton instance of the leaderboard. */
    private static Leaderboard instance;

    /** List of all leaderboard entries, sorted automatically when modified. */
    private ArrayList<LeaderboardEntry> entries;

    /**
     * Represents a single player's leaderboard record.
     * Stores score, puzzles completed, time taken, difficulty, and creation timestamp.
     */
    public static class LeaderboardEntry {

        private String playerName;
        private int score;
        private int puzzlesCompleted;
        private long timeTaken;
        private Game.Difficulty difficulty;
        private String timestamp;

        /**
         * Constructs a new leaderboard entry.
         *
         * @param playerName        the player's display name
         * @param score             the player's final score
         * @param puzzlesCompleted  number of puzzles solved
         * @param timeTaken         total time taken (in seconds)
         * @param difficulty        game difficulty level
         */
        public LeaderboardEntry(String playerName, int score, int puzzlesCompleted,
                                long timeTaken, Game.Difficulty difficulty) {
            this.playerName = playerName;
            this.score = score;
            this.puzzlesCompleted = puzzlesCompleted;
            this.timeTaken = timeTaken;
            this.difficulty = difficulty;
            this.timestamp = java.time.Instant.now().toString();
        }

        /** @return the player's display name */
        public String getPlayerName() { return playerName; }

        /** @return the final score earned */
        public int getScore() { return score; }

        /** @return number of puzzles completed */
        public int getPuzzlesCompleted() { return puzzlesCompleted; }

        /** @return total time taken in seconds */
        public long getTimeTaken() { return timeTaken; }

        /** @return the difficulty level played */
        public Game.Difficulty getDifficulty() { return difficulty; }

        /** @return ISO timestamp representing when the entry was recorded */
        public String getTimestamp() { return timestamp; }

        /**
         * Returns a formatted summary of the leaderboard entry.
         */
        @Override
        public String toString() {
            long minutes = timeTaken / 60;
            long seconds = timeTaken % 60;
            return String.format("%s - Score: %d | Puzzles: %d | Time: %02d:%02d | Difficulty: %s",
                    playerName, score, puzzlesCompleted, minutes, seconds, difficulty);
        }
    }

    /**
     * Private constructor to enforce singleton instance.
     * Initializes the leaderboard and loads data from player progress files.
     */
    private Leaderboard() {
        this.entries = new ArrayList<>();
        loadLeaderboard();
    }

    /**
     * Retrieves the singleton leaderboard instance.
     *
     * @return the global {@code Leaderboard} instance
     */
    public static Leaderboard getInstance() {
        if (instance == null) {
            instance = new Leaderboard();
        }
        return instance;
    }

    /**
     * Adds a new score entry to the leaderboard and resorts the list.
     *
     * @param playerName        the player's name
     * @param score             final score earned
     * @param puzzlesCompleted  number of puzzles solved
     * @param timeTaken         total time taken (seconds)
     * @param difficulty        difficulty setting
     */
    public void addScore(String playerName, int score, int puzzlesCompleted,
                         long timeTaken, Game.Difficulty difficulty) {
        LeaderboardEntry entry = new LeaderboardEntry(
                playerName, score, puzzlesCompleted, timeTaken, difficulty);
        entries.add(entry);
        sortLeaderboard();
    }

    /**
     * Adds the current game's results to the leaderboard.
     *
     * @param game the completed game whose results are being added
     */
    public void addCurrentGameScore(Game game) {
        if (game == null || game.getCurrentPlayer() == null) {
            return;
        }

        addScore(
                game.getCurrentPlayer().getDisplayName(),
                game.getScore(),
                game.getCompletedCount(),
                game.getElapsedTime(),
                game.getDifficulty()
        );
    }

    /**
     * Sorts leaderboard entries by:
     * <ol>
     *     <li>Highest score first</li>
     *     <li>If tied, lowest time first</li>
     * </ol>
     */
    private void sortLeaderboard() {
        Collections.sort(entries, new Comparator<>() {
            @Override
            public int compare(LeaderboardEntry e1, LeaderboardEntry e2) {
                int scoreCompare = Integer.compare(e2.getScore(), e1.getScore());
                if (scoreCompare != 0) {
                    return scoreCompare;
                }
                return Long.compare(e1.getTimeTaken(), e2.getTimeTaken());
            }
        });
    }

    /**
     * Retrieves the top N leaderboard entries.
     *
     * @param n number of entries to retrieve
     * @return list of up to {@code n} top entries
     */
    public ArrayList<LeaderboardEntry> getTopEntries(int n) {
        ArrayList<LeaderboardEntry> top = new ArrayList<>();
        int limit = Math.min(n, entries.size());

        for (int i = 0; i < limit; i++) {
            top.add(entries.get(i));
        }

        return top;
    }

    /**
     * Returns all leaderboard entries in sorted order.
     *
     * @return copy of the current leaderboard
     */
    public ArrayList<LeaderboardEntry> getAllEntries() {
        return new ArrayList<>(entries);
    }

    /**
     * Returns all leaderboard entries filtered by game difficulty.
     *
     * @param difficulty difficulty level to filter by
     * @return list of matching entries
     */
    public ArrayList<LeaderboardEntry> getEntriesByDifficulty(Game.Difficulty difficulty) {
        ArrayList<LeaderboardEntry> filtered = new ArrayList<>();

        for (LeaderboardEntry entry : entries) {
            if (entry.getDifficulty() == difficulty) {
                filtered.add(entry);
            }
        }

        return filtered;
    }

    /**
     * Retrieves the best score achieved by a specified player.
     *
     * @param playerName the player's display name
     * @return best leaderboard entry for that player, or {@code null} if none exist
     */
    public LeaderboardEntry getPlayerBestScore(String playerName) {
        LeaderboardEntry best = null;

        for (LeaderboardEntry entry : entries) {
            if (entry.getPlayerName().equals(playerName)) {
                if (best == null || entry.getScore() > best.getScore()) {
                    best = entry;
                }
            }
        }

        return best;
    }

    /**
     * Determines a player's leaderboard rank based on:
     * <ul>
     *     <li>Higher scores rank better</li>
     *     <li>If scores tie, faster time ranks better</li>
     * </ul>
     *
     * @param playerName the player's name
     * @return the player's rank, or -1 if no entries exist for them
     */
    public int getPlayerRank(String playerName) {
        LeaderboardEntry best = getPlayerBestScore(playerName);
        if (best == null) {
            return -1;
        }

        int rank = 1;
        for (LeaderboardEntry entry : entries) {
            if (entry.getScore() > best.getScore()) {
                rank++;
            } else if (entry.getScore() == best.getScore() &&
                    entry.getTimeTaken() < best.getTimeTaken()) {
                rank++;
            }
        }

        return rank;
    }

    /**
     * Prints a formatted leaderboard to the console, showing the top N players.
     *
     * @param topN number of entries to display
     */
    public void displayLeaderboard(int topN) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                           🏆 LEADERBOARD 🏆");
        System.out.println("=".repeat(80));

        if (entries.isEmpty()) {
            System.out.println("No scores yet. Be the first to complete the escape room!");
            System.out.println("=".repeat(80));
            return;
        }

        System.out.printf("%-5s %-20s %-10s %-10s %-12s %-10s%n",
                "Rank", "Player", "Score", "Puzzles", "Time", "Difficulty");
        System.out.println("-".repeat(80));

        ArrayList<LeaderboardEntry> top = getTopEntries(topN);

        for (int i = 0; i < top.size(); i++) {
            LeaderboardEntry entry = top.get(i);

            String rankDisplay = switch (i) {
                case 0 -> "🥇 1.";
                case 1 -> "🥈 2.";
                case 2 -> "🥉 3.";
                default -> (i + 1) + ".";
            };

            System.out.printf("%-5s %-20s %-10d %-10d %-12s %-10s%n",
                    rankDisplay,
                    entry.getPlayerName(),
                    entry.getScore(),
                    entry.getPuzzlesCompleted(),
                    formatTime(entry.getTimeTaken()),
                    entry.getDifficulty());
        }

        System.out.println("=".repeat(80));
    }

    /**
     * Displays leaderboard entries filtered by a specific difficulty.
     *
     * @param difficulty difficulty level to display
     * @param topN       number of top entries to show
     */
    public void displayLeaderboardByDifficulty(Game.Difficulty difficulty, int topN) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                   🏆 LEADERBOARD - " + difficulty + " 🏆");
        System.out.println("=".repeat(80));

        ArrayList<LeaderboardEntry> filtered = getEntriesByDifficulty(difficulty);

        if (filtered.isEmpty()) {
            System.out.println("No scores for " + difficulty + " difficulty yet!");
            System.out.println("=".repeat(80));
            return;
        }

        System.out.printf("%-5s %-20s %-10s %-10s %-12s%n",
                "Rank", "Player", "Score", "Puzzles", "Time");
        System.out.println("-".repeat(80));

        int count = Math.min(topN, filtered.size());
        for (int i = 0; i < count; i++) {
            LeaderboardEntry entry = filtered.get(i);

            String rankDisplay = switch (i) {
                case 0 -> "🥇 1.";
                case 1 -> "🥈 2.";
                case 2 -> "🥉 3.";
                default -> (i + 1) + ".";
            };

            System.out.printf("%-5s %-20s %-10d %-10d %-12s%n",
                    rankDisplay,
                    entry.getPlayerName(),
                    entry.getScore(),
                    entry.getPuzzlesCompleted(),
                    formatTime(entry.getTimeTaken()));
        }

        System.out.println("=".repeat(80));
    }

    /**
     * Formats a time (in seconds) into a MM:SS string.
     *
     * @param seconds total seconds elapsed
     * @return formatted string in mm:ss format
     */
    private String formatTime(long seconds) {
        long minutes = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    /**
     * Loads leaderboard data from existing player progress,
     * adding entries for players with non-zero stored scores.
     */
    private void loadLeaderboard() {
        Players players = Players.getInstance();

        if (players == null || players.getPlayers() == null) {
            return;
        }

        for (Player player : players.getPlayers()) {
            if (player.getProgress() != null && !player.getProgress().isEmpty()) {
                Progress progress = player.getProgress().get(0);

                if (progress.getCurrentScore() > 0) {
                    entries.add(new LeaderboardEntry(
                            player.getDisplayName(),
                            progress.getCurrentScore(),
                            0,
                            0,
                            Game.Difficulty.MEDIUM
                    ));
                }
            }
        }

        sortLeaderboard();
    }

    /** Removes all leaderboard entries. */
    public void clearLeaderboard() {
        entries.clear();
    }

    /**
     * Returns the number of entries currently stored in the leaderboard.
     *
     * @return leaderboard size
     */
    public int size() {
        return entries.size();
    }
}
