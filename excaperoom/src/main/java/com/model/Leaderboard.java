package com.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Leaderboard {
    private static Leaderboard instance;
    private ArrayList<LeaderboardEntry> entries;

    public static class LeaderboardEntry {
        private String playerName;
        private int score;
        private int puzzlesCompleted;
        private long timeTaken; // in seconds
        private Game.Difficulty difficulty;
        private String timestamp;
        
        public LeaderboardEntry(String playerName, int score, int puzzlesCompleted, 
                              long timeTaken, Game.Difficulty difficulty) {
            this.playerName = playerName;
            this.score = score;
            this.puzzlesCompleted = puzzlesCompleted;
            this.timeTaken = timeTaken;
            this.difficulty = difficulty;
            this.timestamp = java.time.Instant.now().toString();
        }
        
        public String getPlayerName() { return playerName; }
        public int getScore() { return score; }
        public int getPuzzlesCompleted() { return puzzlesCompleted; }
        public long getTimeTaken() { return timeTaken; }
        public Game.Difficulty getDifficulty() { return difficulty; }
        public String getTimestamp() { return timestamp; }
    }
 
    private Leaderboard() {
        this.entries = new ArrayList<>();
        loadLeaderboard();
    }
    
    public static Leaderboard getInstance() {
        if (instance == null) {
            instance = new Leaderboard();
        }
        return instance;
    }
  
    public void addScore(String playerName, int score, int puzzlesCompleted, 
                        long timeTaken, Game.Difficulty difficulty) {
        LeaderboardEntry entry = new LeaderboardEntry(playerName, score, puzzlesCompleted, 
                                                      timeTaken, difficulty);
        entries.add(entry);
        sortLeaderboard();
    }

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
    
    private void sortLeaderboard() {
        Collections.sort(entries, new Comparator<LeaderboardEntry>() {
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
    

    public ArrayList<LeaderboardEntry> getTopEntries(int n) {
        ArrayList<LeaderboardEntry> topEntries = new ArrayList<>();
        int count = Math.min(n, entries.size());
        
        for (int i = 0; i < count; i++) {
            topEntries.add(entries.get(i));
        }
        
        return topEntries;
    }

    public ArrayList<LeaderboardEntry> getAllEntries() {
        return new ArrayList<>(entries);
    }

    public ArrayList<LeaderboardEntry> getEntriesByDifficulty(Game.Difficulty difficulty) {
        ArrayList<LeaderboardEntry> filtered = new ArrayList<>();
        
        for (LeaderboardEntry entry : entries) {
            if (entry.getDifficulty() == difficulty) {
                filtered.add(entry);
            }
        }
        
        return filtered;
    }
   
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

    public int getPlayerRank(String playerName) {
        LeaderboardEntry playerBest = getPlayerBestScore(playerName);
        if (playerBest == null) {
            return -1;
        }
        
        int rank = 1;
        for (LeaderboardEntry entry : entries) {
            if (entry.getScore() > playerBest.getScore()) {
                rank++;
            } else if (entry.getScore() == playerBest.getScore() && 
                      entry.getTimeTaken() < playerBest.getTimeTaken()) {
                rank++;
            }
        }
        
        return rank;
    }

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
        
        ArrayList<LeaderboardEntry> topEntries = getTopEntries(topN);
        
        for (int i = 0; i < topEntries.size(); i++) {
            LeaderboardEntry entry = topEntries.get(i);
            String rankDisplay = (i + 1) + ".";
            
            if (i == 0) rankDisplay = "🥇 1.";
            else if (i == 1) rankDisplay = "🥈 2.";
            else if (i == 2) rankDisplay = "🥉 3.";
            
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
            String rankDisplay = (i + 1) + ".";
            
            if (i == 0) rankDisplay = "🥇 1.";
            else if (i == 1) rankDisplay = "🥈 2.";
            else if (i == 2) rankDisplay = "🥉 3.";
            
            System.out.printf("%-5s %-20s %-10d %-10d %-12s%n",
                            rankDisplay,
                            entry.getPlayerName(),
                            entry.getScore(),
                            entry.getPuzzlesCompleted(),
                            formatTime(entry.getTimeTaken()));
        }
        
        System.out.println("=".repeat(80));
    }
    
 
    private String formatTime(long seconds) {
        long minutes = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

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
    
    public void clearLeaderboard() {
        entries.clear();
    }
    

    public int size() {
        return entries.size();
    }
}