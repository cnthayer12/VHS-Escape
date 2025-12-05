package com.model;

import java.util.ArrayList;

import com.model.Leaderboard.LeaderboardEntry;

public class EscapeGameFacade {
    private Game game;
    private PuzzlesManager puzzlesManager;
    private Players players;
    private static EscapeGameFacade instance;
    private String item = "";
    
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
        game.endGame(true);
        game.exitMain();
    }

    public long getRemainingTime() {
        return game.getRemainingTime();
    }

    public long getElapsedTime() {
        return game.getElapsedTime();
    }
    
    public void startPuzzle() {
        puzzlesManager.startCurrentPuzzle();
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

    public int createPlayer(String username, String pass) {
        int returnVal = players.createAccount(username, pass);
        saveProgress();
        return returnVal;
    }

    public boolean login(String username, String pass) {
        players.login(username, pass);
        saveProgress();
        return false;
    }

    public Player getCurrentPlayer() {
        return Players.getCurrentPlayer();
    }

    public void logout() {
        players.logout();
        saveProgress();
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
        game.setScore(points);
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

    public ArrayList<LeaderboardEntry> getLeaderboard() {
         return Leaderboard.getInstance().getAllEntries();
    }
    public void addItem(Item item) {
        players.addItem(item);
    }

    public void setCurrentItem(String item) {
        this.item = item;
    }

    public String getCurrentItem() {
        return item;
    }

    public void generateCompletionCertificate() {
        players.generateCertificate(game.getDifficulty(), game.getScore());
    }

    public void checkProgress() {
        players.checkProgress();
    }

    public void giveItem(Item item) {
        players.addItem(item);
    }

}