package com.model;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class TestLeaderboard {

    private Leaderboard leaderboard;

    @Before
    public void setUp() {
        leaderboard = Leaderboard.getInstance();
        leaderboard.clearLeaderboard(); // ensure clean state
    }

    // ---------------- Singleton Tests ----------------

    @Test
    public void testGetInstanceReturnsSameObject() {
        Leaderboard second = Leaderboard.getInstance();
        assertSame(leaderboard, second);
    }

    // ---------------- Constructor and Defaults ----------------

    @Test
    public void testInitialLeaderboardIsEmpty() {
        assertEquals(0, leaderboard.size());
    }

    @Test
    public void testAddScoreIncreasesSize() {
        leaderboard.addScore("Alice", 100, 3, 60, Game.Difficulty.EASY);
        assertEquals(1, leaderboard.size());
    }

    @Test
    public void testAddScoreStoresCorrectData() {
        leaderboard.addScore("Bob", 200, 4, 90, Game.Difficulty.MEDIUM);
        Leaderboard.LeaderboardEntry entry = leaderboard.getAllEntries().get(0);
        assertEquals("Bob", entry.getPlayerName());
        assertEquals(200, entry.getScore());
        assertEquals(4, entry.getPuzzlesCompleted());
        assertEquals(90, entry.getTimeTaken());
        assertEquals(Game.Difficulty.MEDIUM, entry.getDifficulty());
    }

    // ---------------- Sorting Behavior ----------------

    @Test
    public void testHigherScoreComesFirst() {
        leaderboard.addScore("PlayerA", 50, 1, 100, Game.Difficulty.MEDIUM);
        leaderboard.addScore("PlayerB", 200, 2, 120, Game.Difficulty.MEDIUM);
        ArrayList<Leaderboard.LeaderboardEntry> entries = leaderboard.getAllEntries();
        assertEquals("PlayerB", entries.get(0).getPlayerName());
    }

    @Test
    public void testTieBreakerShorterTimeComesFirst() {
        leaderboard.addScore("FastPlayer", 100, 2, 30, Game.Difficulty.HARD);
        leaderboard.addScore("SlowPlayer", 100, 2, 90, Game.Difficulty.HARD);
        ArrayList<Leaderboard.LeaderboardEntry> entries = leaderboard.getAllEntries();
        assertEquals("FastPlayer", entries.get(0).getPlayerName());
    }

    @Test
    public void testSortingStableForEqualEntries() {
        leaderboard.addScore("A", 100, 1, 60, Game.Difficulty.EASY);
        leaderboard.addScore("B", 100, 1, 60, Game.Difficulty.EASY);
        ArrayList<Leaderboard.LeaderboardEntry> entries = leaderboard.getAllEntries();
        assertTrue(entries.size() >= 2);
    }

    // ---------------- Retrieval Tests ----------------

    @Test
    public void testGetTopEntriesLimitsResults() {
        for (int i = 1; i <= 5; i++) {
            leaderboard.addScore("Player" + i, i * 10, 1, 60, Game.Difficulty.EASY);
        }
        ArrayList<Leaderboard.LeaderboardEntry> top3 = leaderboard.getTopEntries(3);
        assertEquals(3, top3.size());
    }

    @Test
    public void testGetAllEntriesReturnsCopy() {
        leaderboard.addScore("Test", 123, 2, 45, Game.Difficulty.MEDIUM);
        ArrayList<Leaderboard.LeaderboardEntry> entries = leaderboard.getAllEntries();
        entries.clear();
        assertEquals(1, leaderboard.size());
    }

    @Test
    public void testGetEntriesByDifficultyFiltersCorrectly() {
        leaderboard.addScore("EasyGuy", 80, 3, 70, Game.Difficulty.EASY);
        leaderboard.addScore("HardGuy", 90, 3, 90, Game.Difficulty.HARD);
        ArrayList<Leaderboard.LeaderboardEntry> filtered = leaderboard.getEntriesByDifficulty(Game.Difficulty.HARD);
        assertEquals(1, filtered.size());
        assertEquals("HardGuy", filtered.get(0).getPlayerName());
    }

    // ---------------- Player Rank and Score ----------------

    @Test
    public void testGetPlayerBestScoreReturnsHighest() {
        leaderboard.addScore("Zoe", 100, 1, 70, Game.Difficulty.MEDIUM);
        leaderboard.addScore("Zoe", 250, 2, 90, Game.Difficulty.HARD);
        Leaderboard.LeaderboardEntry best = leaderboard.getPlayerBestScore("Zoe");
        assertEquals(250, best.getScore());
    }

    @Test
    public void testGetPlayerBestScoreReturnsNullIfNoEntry() {
        assertNull(leaderboard.getPlayerBestScore("Nonexistent"));
    }

    @Test
    public void testGetPlayerRankHigherScoreGetsRank1() {
        leaderboard.addScore("Top", 300, 3, 60, Game.Difficulty.HARD);
        leaderboard.addScore("Low", 100, 1, 60, Game.Difficulty.EASY);
        assertEquals(1, leaderboard.getPlayerRank("Top"));
    }

    @Test
    public void testGetPlayerRankReturnsMinusOneForUnknown() {
        assertEquals(-1, leaderboard.getPlayerRank("Nobody"));
    }

    @Test
    public void testGetPlayerRankConsidersTimeOnTie() {
        leaderboard.addScore("Quick", 100, 2, 50, Game.Difficulty.MEDIUM);
        leaderboard.addScore("Slow", 100, 2, 120, Game.Difficulty.MEDIUM);
        assertTrue(leaderboard.getPlayerRank("Quick") < leaderboard.getPlayerRank("Slow"));
    }

    // ---------------- Display and Utility ----------------

    @Test
    public void testToStringFormattingOfEntry() {
        Leaderboard.LeaderboardEntry entry =
            new Leaderboard.LeaderboardEntry("Tester", 150, 4, 130, Game.Difficulty.EASY);
        String output = entry.toString();
        assertTrue(output.contains("Tester"));
        assertTrue(output.contains("Score"));
        assertTrue(output.contains("EASY"));
    }

    @Test
    public void testClearLeaderboardRemovesAllEntries() {
        leaderboard.addScore("Player", 100, 2, 60, Game.Difficulty.MEDIUM);
        leaderboard.clearLeaderboard();
        assertEquals(0, leaderboard.size());
    }

    @Test
    public void testSizeMatchesNumberOfEntries() {
        leaderboard.addScore("X", 50, 1, 20, Game.Difficulty.EASY);
        assertEquals(1, leaderboard.size());
    }
    
}
