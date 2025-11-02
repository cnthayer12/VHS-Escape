package com.model;

import org.junit.Test;
import org.junit.*;
import java.util.ArrayList;

import static org.junit.Assert.*;


public class TestGame {

    private Game game;
    private Player testPlayer;

    @Before
    public void setUp() {
        game = Game.getInstance();
        testPlayer = new Player("TestUser", new ArrayList<>(), "password123");
    }

    @After
    public void tearDown() {
        game.deleteGame();
    }

    // ---------------- Singleton Tests ----------------

    @Test
    public void testSingletonReturnsSameInstance() {
        assertEquals(Game.getInstance(), Game.getInstance());
    }

    @Test
    public void testSingletonIsNotNull() {
        assertNotNull(Game.getInstance());
    }

    @Test
    public void testDeleteGameResetsSingleton() {
        Game instance1 = Game.getInstance();
        instance1.deleteGame();
        Game instance2 = Game.getInstance();
        assertNotSame(instance1, instance2);
    }

    // ---------------- Constructor/Initialization ----------------

    @Test
    public void testInitialDefaults() {
        assertEquals(1000, game.getScore());
        assertFalse(game.isPaused());
        assertFalse(game.isOver());
        assertNotNull(game.getPuzzles());
        assertEquals(0, game.getCompletedCount());
        assertNotNull(game.getStory());
        assertTrue(game.getStory().contains("VHS"));
        assertNotNull(game.getGameID());
    }

    // ---------------- Difficulty Enum ----------------

    @Test
    public void testDifficultyEnumValues() {
        assertEquals(1800, Game.Difficulty.EASY.getTimeLimit());
        assertEquals(1200, Game.Difficulty.MEDIUM.getTimeLimit());
        assertEquals(900, Game.Difficulty.HARD.getTimeLimit());
    }

    @Test
    public void testDifficultyEnumToString() {
        for (Game.Difficulty d : Game.Difficulty.values()) {
            assertNotNull(d.toString());
        }
    }

    // ---------------- Initialize Game ----------------

    @Test
    public void testInitializeGameBasic() {
        game.initializeGame(testPlayer, Game.Difficulty.MEDIUM);
        assertEquals(testPlayer, game.getCurrentPlayer());
        assertEquals(Game.Difficulty.MEDIUM, game.getDifficulty());
        assertEquals(1000, game.getScore());
        assertFalse(game.isPaused());
        assertFalse(game.isOver());
        assertNotNull(game.getStartTime());
        assertEquals(0, game.getCompletedCount());
    }

    @Test
public void testInitializeGameResetsPreviousValues() {
    game.setScore(10);
    game.initializeGame(testPlayer, Game.Difficulty.EASY);
    assertEquals(1000, game.getScore());
    assertEquals(0, game.getCompletedCount());
}


    @Test
    public void testInitializeGameWithNullPlayer() {
        game.initializeGame(null, Game.Difficulty.HARD);
        assertNull(game.getCurrentPlayer());
    }

    // ---------------- Pause/Resume Logic ----------------

    @Test
    public void testPauseAndResumeToggle() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        game.pause();
        assertTrue(game.isPaused());
        game.resume();
        assertFalse(game.isPaused());
    }

    @Test
    public void testPauseWhenAlreadyPausedDoesNotCrash() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        game.pause();
        game.pause();
        assertTrue(game.isPaused());
    }

    @Test
    public void testResumeWhenNotPausedDoesNotCrash() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        game.resume();
        assertFalse(game.isPaused());
    }

    // ---------------- Time Handling ----------------

    @Test
    public void testElapsedTimeWithoutInitialization() {
        assertEquals(0, game.getElapsedTime());
    }

    @Test
    public void testElapsedTimeAfterStart() throws InterruptedException {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        long first = game.getElapsedTime();
        Thread.sleep(50);
        long second = game.getElapsedTime();
        assertTrue(second >= first);
    }

    @Test
    public void testRemainingTimePositiveForAllDifficulties() {
        for (Game.Difficulty d : Game.Difficulty.values()) {
            game.deleteGame();
            game = Game.getInstance();
            game.initializeGame(testPlayer, d);
            long remaining = game.getRemainingTime();
            assertTrue("Remaining time negative for " + d, remaining >= 0);
        }
    }

    @Test
    public void testIsTimeUpInitiallyFalse() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        assertFalse(game.isTimeUp());
    }

    // ---------------- Score Calculation ----------------

    @Test
    public void testCalculateScoreReturnsNonNegative() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        game.setScore(-100);
        assertTrue(game.calculateScore() >= 0);
    }

    @Test
    public void testCalculateScoreUpdatesGameScore() {
        game.initializeGame(testPlayer, Game.Difficulty.MEDIUM);
        int score = game.calculateScore();
        assertEquals(score, game.getScore());
    }

    @Test
    public void testCalculateScoreWithNoProgressList() {
        game.initializeGame(testPlayer, Game.Difficulty.HARD);
        assertEquals(1000, game.calculateScore());
    }

    // ---------------- Progress and Completion ----------------

    @Test
    public void testProgressPercentWithEmptyList() {
        game.setPuzzles(new ArrayList<>());
        assertEquals(0.0, game.progressPercent(), 0.01);
    }

    @Test
    public void testProgressPercentZeroCompleted() {
        ArrayList<Puzzle> puzzles = new ArrayList<>();
        puzzles.add(new Trivia());
        puzzles.add(new Trivia());
        game.setPuzzles(puzzles);
        assertEquals(0.0, game.progressPercent(), 0.01);
    }

    // ---------------- Setter & Getter Edge Cases ----------------

    @Test
    public void testSetCurrentPlayerToNull() {
        game.setCurrentPlayer(null);
        assertNull(game.getCurrentPlayer());
    }

    @Test
    public void testSetDifficultyThenGet() {
        game.setDifficulty(Game.Difficulty.HARD);
        assertEquals(Game.Difficulty.HARD, game.getDifficulty());
    }

    @Test
    public void testSetNegativeScore() {
        game.setScore(-200);
        assertEquals(-200, game.getScore());
    }

    @Test
    public void testSetStoryNull() {
        game.setStory(null);
        assertNull(game.getStory());
    }

    @Test
    public void testSetPuzzlesNullDoesNotCrash() {
        game.setPuzzles(null);
        assertNull(game.getPuzzles());
    }


    // ---------------- deleteGame Behavior ----------------

    @Test
    public void testDeleteGamePreservesNoState() {
        game.initializeGame(testPlayer, Game.Difficulty.MEDIUM);
        game.deleteGame();
        Game newGame = Game.getInstance();
        assertNotSame(game, newGame);
        assertNull(newGame.getCurrentPlayer());
        assertEquals(1000, newGame.getScore());
    }

    // ---------------- Integration and State Mutation ----------------

    @Test
    public void testPauseResumeDoesNotAdvanceTimeSignificantly() throws InterruptedException {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        Thread.sleep(50);
        long beforePause = game.getElapsedTime();
        game.pause();
        Thread.sleep(100);
        game.resume();
        long afterResume = game.getElapsedTime();
        assertTrue(afterResume - beforePause < 200);
    }

    @Test
    public void testDifferentDifficultyTimeComparisons() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        long easyTime = game.getRemainingTime();
        game.deleteGame();
        game = Game.getInstance();
        game.initializeGame(testPlayer, Game.Difficulty.HARD);
        long hardTime = game.getRemainingTime();
        assertTrue(easyTime > hardTime);
    }

    // ---------------- Potential Bug Probes ----------------

    @Test
    public void testInitializeGameTwiceOverwritesPlayerAndDifficulty() {
        Player p1 = new Player("A", new ArrayList<>(), "x");
        Player p2 = new Player("B", new ArrayList<>(), "y");
        game.initializeGame(p1, Game.Difficulty.EASY);
        game.initializeGame(p2, Game.Difficulty.HARD);
        assertEquals(p2, game.getCurrentPlayer());
        assertEquals(Game.Difficulty.HARD, game.getDifficulty());
    }

    @Test
    public void testGameToStringContainsImportantInfo() {
        String s = game.toString();
        assertNotNull(s);
        assertTrue(s.contains("Game") || s.contains("Score") || s.contains("VHS"));
    }

    @Test
    public void testChangingScoreDoesNotCrashProgressPercent() {
        game.setScore(500);
        assertNotNull(game.progressPercent());
    }

    @Test
    public void testGameCanHandleNullPlayerAndStorySimultaneously() {
        game.setCurrentPlayer(null);
        game.setStory(null);
        assertNull(game.getCurrentPlayer());
        assertNull(game.getStory());
    }
}

