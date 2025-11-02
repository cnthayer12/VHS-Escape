package com.model;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import java.util.ArrayList;

public class TestGame {
    
    private Game game;
    private Player testPlayer;
    
    @Before
    public void setUp() {
        game = Game.getInstance();
        ArrayList<Progress> progressList = new ArrayList<>();
        testPlayer = new Player("TestUser", progressList, "password123");
    }
    
    @After
    public void tearDown() {
        // Clean up singleton instance after each test
        game.deleteGame();
    }
    
    // Singleton Tests
    
    @Test
    public void testGetInstanceReturnsSameInstance() {
        Game instance1 = Game.getInstance();
        Game instance2 = Game.getInstance();
        assertEquals(instance1, instance2);
    }
    
    @Test
    public void testGetInstanceReturnsNonNull() {
        assertNotNull(Game.getInstance());
    }
    
    // Constructor/Initialization Tests
    
    @Test
    public void testGameInitializesWithDefaultScore() {
        assertEquals(1000, game.getScore());
    }
    
    @Test
    public void testGameInitializesAsNotPaused() {
        assertFalse(game.isPaused());
    }
    
    @Test
    public void testGameInitializesAsNotOver() {
        assertFalse(game.isOver());
    }
    
    @Test
    public void testGameInitializesWithEmptyPuzzlesList() {
        assertNotNull(game.getPuzzles());
    }
    
    @Test
    public void testGameInitializesWithZeroCompletedCount() {
        assertEquals(0, game.getCompletedCount());
    }
    
    @Test
    public void testGameInitializesWithDefaultStory() {
        assertNotNull(game.getStory());
        assertTrue(game.getStory().contains("VHS Escape Room"));
    }
    
    @Test
    public void testGameInitializesWithGameID() {
        assertNotNull(game.getGameID());
    }
    
    // Difficulty Enum Tests
    
    @Test
    public void testDifficultyEasyTimeLimit() {
        assertEquals(1800, Game.Difficulty.EASY.getTimeLimit());
    }
    
    @Test
    public void testDifficultyMediumTimeLimit() {
        assertEquals(1200, Game.Difficulty.MEDIUM.getTimeLimit());
    }
    
    @Test
    public void testDifficultyHardTimeLimit() {
        assertEquals(900, Game.Difficulty.HARD.getTimeLimit());
    }
    
    // initializeGame Tests
    
    @Test
    public void testInitializeGameSetsPlayer() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        assertEquals(testPlayer, game.getCurrentPlayer());
    }
    
    @Test
    public void testInitializeGameSetsDifficulty() {
        game.initializeGame(testPlayer, Game.Difficulty.HARD);
        assertEquals(Game.Difficulty.HARD, game.getDifficulty());
    }
    
    @Test
    public void testInitializeGameResetsScore() {
        game.setScore(500);
        game.initializeGame(testPlayer, Game.Difficulty.MEDIUM);
        assertEquals(1000, game.getScore());
    }
    
    @Test
    public void testInitializeGameSetsStartTime() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        assertNotNull(game.getStartTime());
    }
    
    @Test
    public void testInitializeGameResetsCompletedCount() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        assertEquals(0, game.getCompletedCount());
    }
    
    @Test
    public void testInitializeGameSetsNotPaused() {
        game.initializeGame(testPlayer, Game.Difficulty.MEDIUM);
        assertFalse(game.isPaused());
    }
    
    @Test
    public void testInitializeGameSetsNotOver() {
        game.initializeGame(testPlayer, Game.Difficulty.MEDIUM);
        assertFalse(game.isOver());
    }
    
    // calculateScore Tests
    
    @Test
    public void testCalculateScoreWithNoProgress() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        int score = game.calculateScore();
        assertEquals(1000, score);
    }
    
    @Test
    public void testCalculateScoreNeverNegative() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        game.setScore(-500);
        int score = game.calculateScore();
        assertTrue(score >= 0);
    }
    
    @Test
    public void testCalculateScoreUpdatesGameScore() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        game.calculateScore();
        assertEquals(game.getScore(), game.calculateScore());
    }
    
    // pause and resume Tests
    
    @Test
    public void testPauseChangesIsPausedToTrue() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        game.pause();
        assertTrue(game.isPaused());
    }
    
    @Test
    public void testPauseWhenAlreadyPaused() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        game.pause();
        game.pause();
        assertTrue(game.isPaused());
    }
    
    @Test
    public void testResumeChangesIsPausedToFalse() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        game.pause();
        game.resume();
        assertFalse(game.isPaused());
    }
    
    @Test
    public void testResumeWhenNotPaused() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        game.resume();
        assertFalse(game.isPaused());
    }
    
    @Test
    public void testPauseAndResumeMultipleTimes() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        game.pause();
        assertTrue(game.isPaused());
        game.resume();
        assertFalse(game.isPaused());
        game.pause();
        assertTrue(game.isPaused());
        game.resume();
        assertFalse(game.isPaused());
    }
    
    // getElapsedTime Tests
    
    @Test
    public void testGetElapsedTimeWithNoStartTime() {
        assertEquals(0, game.getElapsedTime());
    }
    
    @Test
    public void testGetElapsedTimeAfterInitialization() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        long elapsed = game.getElapsedTime();
        assertTrue(elapsed >= 0);
    }
    
    @Test
    public void testGetElapsedTimeIncreases() throws InterruptedException {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        long time1 = game.getElapsedTime();
        Thread.sleep(100);
        long time2 = game.getElapsedTime();
        assertTrue(time2 >= time1);
    }
    
    // getRemainingTime Tests
    
    @Test
    public void testGetRemainingTimeWithEasyDifficulty() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        long remaining = game.getRemainingTime();
        assertTrue(remaining > 0);
        assertTrue(remaining <= 1800);
    }
    
    @Test
    public void testGetRemainingTimeNeverNegative() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        long remaining = game.getRemainingTime();
        assertTrue(remaining >= 0);
    }
    
    // isTimeUp Tests
    
    @Test
    public void testIsTimeUpInitiallyFalse() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        assertFalse(game.isTimeUp());
    }
    
    // progressPercent Tests
    
    @Test
    public void testProgressPercentWithNoPuzzles() {
        assertEquals(0.0, game.progressPercent(), 0.01);
    }
    
    @Test
    public void testProgressPercentWithEmptyPuzzleList() {
        game.setPuzzles(new ArrayList<Puzzle>());
        assertEquals(0.0, game.progressPercent(), 0.01);
    }
    
    // deleteGame Tests
    
    @Test
    public void testDeleteGameResetsInstance() {
        Game instance1 = Game.getInstance();
        instance1.deleteGame();
        Game instance2 = Game.getInstance();
        assertFalse(instance1 == instance2);
    }
    
    // Setter Tests
    
    @Test
    public void testSetCurrentPlayer() {
        ArrayList<Progress> progressList = new ArrayList<>();
        Player newPlayer = new Player("NewUser", progressList, "pass456");
        game.setCurrentPlayer(newPlayer);
        assertEquals(newPlayer, game.getCurrentPlayer());
    }
    
    @Test
    public void testSetDifficulty() {
        game.setDifficulty(Game.Difficulty.HARD);
        assertEquals(Game.Difficulty.HARD, game.getDifficulty());
    }
    
    @Test
    public void testSetScore() {
        game.setScore(2500);
        assertEquals(2500, game.getScore());
    }
    
    @Test
    public void testSetStory() {
        String newStory = "A new adventure begins...";
        game.setStory(newStory);
        assertEquals(newStory, game.getStory());
    }
    
    @Test
    public void testSetPuzzles() {
        ArrayList<Puzzle> puzzles = new ArrayList<>();
        puzzles.add(new Trivia());
        puzzles.add(new Trivia());
        game.setPuzzles(puzzles);
        assertEquals(2, game.getPuzzles().size());
    }
    
    // Integration Tests
    
    @Test
    public void testCompleteGameInitializationWorkflow() {
        game.initializeGame(testPlayer, Game.Difficulty.MEDIUM);
        
        assertEquals(testPlayer, game.getCurrentPlayer());
        assertEquals(Game.Difficulty.MEDIUM, game.getDifficulty());
        assertEquals(1000, game.getScore());
        assertFalse(game.isPaused());
        assertFalse(game.isOver());
        assertEquals(0, game.getCompletedCount());
        assertNotNull(game.getStartTime());
    }
    
    @Test
    public void testPauseAndResumeAffectsElapsedTime() throws InterruptedException {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        
        Thread.sleep(100);
        long timeBeforePause = game.getElapsedTime();
        
        game.pause();
        Thread.sleep(100);
        
        game.resume();
        long timeAfterResume = game.getElapsedTime();
        
        // Time shouldn't have increased much during pause
        assertTrue(timeAfterResume - timeBeforePause < 150);
    }
    
    @Test
    public void testDifferentDifficultiesHaveDifferentTimeLimits() {
        game.initializeGame(testPlayer, Game.Difficulty.EASY);
        long easyRemaining = game.getRemainingTime();
        game.deleteGame();
        
        game = Game.getInstance();
        ArrayList<Progress> progressList = new ArrayList<>();
        Player newTestPlayer = new Player("TestUser", progressList, "password123");
        game.initializeGame(newTestPlayer, Game.Difficulty.HARD);
        long hardRemaining = game.getRemainingTime();
        
        assertTrue(easyRemaining > hardRemaining);
    }
}
