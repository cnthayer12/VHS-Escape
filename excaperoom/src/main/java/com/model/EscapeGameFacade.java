package com.model;

import java.util.ArrayList;

public class EscapeGameFacade {
    private Game game;
    private PuzzlesManager PuzzlesManager;
    private Players players;
    private static EscapeGameFacade instance;
    private int strikes;
    
    private EscapeGameFacade() {
        this.game = Game.getInstance();
        this.PuzzlesManager = PuzzlesManager.getInstance();
        this.players = Players.getInstance();
        this.strikes = 0;
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
    
    
    public void loadProgress() {
        players.loadProgress();
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
        PuzzlesManager.startPuzzle();
    }

    public void completePuzzle() {
        game.completePuzzle();
    }

    public void skipPuzzle() {
        PuzzlesManager.skipCurrentPuzzle();
    }

    public boolean nextPuzzle() {
        return game.nextPuzzle();
    }

    public boolean submitAnswer(String answer) {
        return PuzzlesManager.submitAnswer(answer);
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
        return PuzzlesManager.getAvailableHints();
    }

    public Hint revealHint() {
        return PuzzlesManager.revealHint();
    }

    public int getHintsUsed() {
        return PuzzlesManager.getHintsUsed();
    }

    public int getStrikes() {
        return strikes;
    }

    public void addStrike() {
        strikes++;
    }

    public void resetStrikes() {
        strikes = 0;
    }

    public void updateScore(int points) {
        game.setScore(game.getScore() + points);
    }

    public int getScore() {
        return game.getScore();
    }

    public Puzzle getCurrentPuzzle() {
        return PuzzlesManager.getCurrentPuzzle();
    }

    public ArrayList<Puzzle> getAllPuzzles() {
        return PuzzlesManager.getPuzzles();
    }

    public int getTotalPuzzles() {
        return PuzzlesManager.getTotalPuzzlesCount();
    }

    public int getCurrentPuzzleIndex() {
        return PuzzlesManager.getCurrentPuzzleIndex();
    }

    public void saveProgress() {
        players.saveProgress();
    }

}