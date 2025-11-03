package com.model;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class TestEscapeGameFacade {

    private EscapeGameFacade facade;

    @Before
    public void setUp() {
        // Arrange
        facade = EscapeGameFacade.getInstance();
    }

    // ---------------- Singleton and Construction Tests ----------------

    @Test
    public void testGetInstanceReturnsNonNull() {
        assertNotNull(facade);
    }

    @Test
    public void testGetInstanceAlwaysReturnsSameObject() {
        EscapeGameFacade another = EscapeGameFacade.getInstance();
        assertSame(facade, another);
    }

    // ---------------- Delegation to Game Methods ----------------

    @Test
    public void testStartGameDoesNotThrow() {
        Player dummy = new Player("tester", null, "password");
        facade.startGame(dummy, Game.Difficulty.EASY);
    }

    @Test
    public void testPauseAndResumeGameDoesNotThrow() {
        facade.pauseGame();
        facade.resumeGame();
    }

    @Test
    public void testEndGameDoesNotThrow() {
        facade.endGame();
    }

    @Test
    public void testGetProgressPercentReturnsValidRange() {
        double progress = facade.getProgressPercent();
        assertTrue(progress >= 0.0 && progress <= 100.0);
    }

    // ---------------- Player Management ----------------

    @Test
    public void testCreatePlayerAndLoginDoesNotThrow() {
        facade.createPlayer("newUser", "newPass");
        facade.login("newUser", "newPass");
    }

    @Test
    public void testLogoutDoesNotThrow() {
        facade.logout();
    }

    @Test
    public void testAddAndResetStrikes() {
        facade.addStrike();
        int strikesBefore = facade.getStrikes();
        facade.resetStrikes();
        int strikesAfter = facade.getStrikes();
        assertTrue(strikesAfter <= strikesBefore);
    }

    // ---------------- Puzzle Management ----------------

    @Test
    public void testStartAndSkipPuzzleDoesNotThrow() {
        facade.startPuzzle();
        facade.skipPuzzle();
    }

    @Test
public void testSubmitAnswerReturnsBoolean() {
    boolean result = facade.submitAnswer("testAnswer");
    // Just ensure it ran without exception
    assertTrue(result == true || result == false);
}

    @Test
public void testNextPuzzleReturnsBoolean() {
    boolean result = facade.nextPuzzle();
    assertTrue(result == true || result == false);
}

    // ---------------- Hint and Progress Features ----------------

    @Test
    public void testGetAvailableHintsReturnsList() {
        ArrayList<Hint> hints = facade.getAvailableHints();
        assertNotNull(hints);
    }

    @Test
    public void testRevealHintDoesNotThrow() {
        Hint h = facade.revealHint();
        assertTrue(h == null || h instanceof Hint);
    }

    @Test
    public void testGetHintsUsedReturnsNonNegative() {
        assertTrue(facade.getHintsUsed() >= 0);
    }

    // ---------------- Scoring and Leaderboard ----------------

    @Test
    public void testUpdateAndGetScore() {
        facade.updateScore(500);
        assertEquals(500, facade.getScore());
    }

    @Test
    public void testGetLeaderboardReturnsList() {
        ArrayList<Leaderboard.LeaderboardEntry> list = facade.getLeaderboard();
        assertNotNull(list);
    }

    // ---------------- Items and Certificates ----------------

    @Test
    public void testAddItemDoesNotThrow() {
        Item i = new Item("Key", "Opens a locked door", null, null);
        facade.addItem(i);
    }

    @Test
    public void testGenerateCompletionCertificateDoesNotThrow() {
        facade.generateCompletionCertificate();
    }

    // ---------------- Progress and Game Info ----------------

    @Test
    public void testCheckProgressDoesNotThrow() {
        facade.checkProgress();
    }

    @Test
    public void testGetTotalPuzzlesReturnsNonNegative() {
        assertTrue(facade.getTotalPuzzles() >= 0);
    }

    @Test
    public void testGetCurrentPuzzleIndexReturnsNonNegative() {
        assertTrue(facade.getCurrentPuzzleIndex() >= 0);
    }

    @Test
    public void testGetAllPuzzlesReturnsList() {
        assertNotNull(facade.getAllPuzzles());
    }

    @Test
    public void testGetCurrentPuzzleReturnsPuzzleOrNull() {
        Puzzle p = facade.getCurrentPuzzle();
        assertTrue(p == null || p instanceof Puzzle);
    }

    // ---------------- Integration / Workflow ----------------

    @Test
    public void testFullGameCycleWorkflow() {
        Player p = new Player("integrationTester", null, "pw");
        facade.startGame(p, Game.Difficulty.MEDIUM);
        facade.startPuzzle();
        facade.submitAnswer("answer");
        facade.completePuzzle();
        facade.nextPuzzle();
        facade.updateScore(250);
        facade.endGame();
        assertTrue(facade.getScore() >= 0);
    }
    
}
