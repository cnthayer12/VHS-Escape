package com.model;

import java.util.ArrayList;

public class EscapeGameFacade {
    private Game game;
    private PuzzlesManager puzzlesManager;
    private Players players;
    private static EscapeGameFacade instance;
    
    private EscapeGameFacade() {
        this.game = Game.getInstance();
        this.puzzlesManager = PuzzlesManager.getInstance();
        this.players = Players.getInstance();
    }
    
    public static EscapeGameFacade getInstance() {
        if (instance == null) {
            instance = new EscapeGameFacade();
        }
        return instance;
    }
    
    public void startGame(Player player, Game.Difficulty difficulty) {
        game.initializeGame(player, difficulty);
    }
    
    public boolean loadProgress() {
        return players.loadProgress();
    }

    public void pauseGame() {
        game.pause();
    }

    public void resumeGame() {
        game.resume();
    }       

    public void endGame() {
        game.exitMain();
    }
    
    public void startPuzzle() {
        puzzlesManager.startPuzzle();
    }

    public void completePuzzle() {
        game.completePuzzle();
    }

    public void skipPuzzle() {
        puzzlesManager.skipCurrentPuzzle();
    }

    public boolean nextPuzzle() {
        return game.nextPuzzle();
    }

    public boolean submitAnswer(String answer) {
        return puzzlesManager.submitAnswer(answer);
    }

    public double getProgressPercent() {
        return game.progressPercent();
    }

    public void createPlayer(String username, String pass) {
        players.createAccount(username, pass);
    }

    public void displayStory() {
        game.displayStory();
    }

    public ArrayList<Hint> getAvailableHints() {
        return puzzlesManager.getAvailableHints();
    }

    public Hint revealHint() {
        return puzzlesManager.revealHint();
    }

    public int getHintsUsed() {
        return puzzlesManager.getHintsUsed();
    }

    public int getStrikes() {
        return players.getStrikes();
    }

    public void addStrike() {
        players.addStrike();
    }

    public void resetStrikes() {
        players.resetStrikes();
    }

    public void updateScore(int points) {
        game.updateScore(points);
    }

    public int getScore() {
        return game.getScore();
    }

    public Puzzle getCurrentPuzzle() {
        return puzzlesManager.getCurrentPuzzle();
    }

    public ArrayList<Puzzle> getAllPuzzles() {
        return puzzlesManager.getPuzzles();
    }

    public int getTotalPuzzles() {
        return puzzlesManager.getTotalPuzzlesCount();
    }

    public int getCurrentPuzzleIndex() {
        return game.getCurrentPuzzleIndex();
    }

    public void saveProgress() {
        players.saveProgress();
    }
}