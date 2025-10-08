package com.model;

import java.util.ArrayList;
/**
 * EscapeGameFacade - STUB version
 */
public class EscapeGameFacade {
    //Instance variables from UML
    private Game game;
    private PuzzlesManager puzzleman;
    private Players players;
    private static EscapeGameFacade instance;
    
    // Constructor - null for now
    public EscapeGameFacade() {
        this.game = Game.getInstance();
        this.puzzleman = PuzzlesManager.getInstance();
        this.players = Players.getInstance();
    }
    
    /**
     * Starts the game
     * @return true if game started successfully
     */
    public boolean startGame() {
        System.out.println("STUB startGame() called");
        return true; 
    }
    
    /**
     * Gets the singleton instance 
     * @return the singleton instance
     */
    public static EscapeGameFacade getInstance() {
        System.out.println("STUB getInstance() called");
        if (instance == null) {
            instance = new EscapeGameFacade();
        }
        return instance;
    }
    
    /**
     * Saves current progress 
     * @return true if save was successful
     */
    public boolean saveProgress() {
        System.out.println("STUB EscapeGameFacade.saveProgress() called");
        //return DataWriter.saveAll(players.getPlayers(), new ArrayList<Progress>());
        return true;
    }
    

    public boolean loadProgress() {
        System.out.println("STUB loadProgress() called");
        ArrayList<Player> loadedPlayers = DataLoader.getPlayers();
        return loadedPlayers != null;
    }

    public void pauseGame() {
        System.out.println("STUB pauseGame() called");
        if (game != null) {
            game.pause();
        }
    }

    public void resumeGame() {
        System.out.println("STUB resumeGame() called");
        if (game != null) {
            game.resume();
        }
    }       

    public void endGame() {
        System.out.println("STUB endGame() called");
        if (game != null) {
            game.exitMain();
        }
    }
    
    public boolean startPuzzle() {
        System.out.println("STUB EscapeGameFacade.startPuzzle() called");
        Puzzle current = puzzleman.getCurrentPuzzle();
        if (current != null) {
            current.startPuzzle();
            return true;
        }
        return false;
    }   
    
    public boolean completePuzzle() {
        System.out.println("STUB EscapeGameFacade.completePuzzle() called");
        Puzzle current = puzzleman.getCurrentPuzzle();
        if (current != null) {
            return current.completePuzzle();
        }
        return false;
    }

    public void skipPuzzle() {
        System.out.println("STUB EscapeGameFacade.skipPuzzle() called");
        Puzzle current = puzzleman.getCurrentPuzzle();
        if (current != null) {
            current.skip();
        }
    }

    public boolean nextPuzzle() {
        System.out.println("[STUB] EscapeGameFacade.nextPuzzle() called");
        return game != null && game.nextPuzzle();
    }

    /**
     * Submit answer for current puzzle - STUB
     * @param answer the player's answer
     * @return true if answer is correct
     */
    public boolean submitAnswer(String answer) {
        System.out.println("STUB EscapeGameFacade.submitAnswer() called: " + answer);
        return true;
    }

    /**
     * Set game difficulty - STUB
     * @param difficulty the difficulty level
     */
    //public void setDifficulty(Difficulty difficulty) {
        //System.out.println("STUB EscapeGameFacade.setDifficulty() called: " + difficulty);
    //}

    /**
     * Get current game progress percentage - STUB
     * @return progress as percentage (0.0 to 1.0)
     */
    public double getProgressPercent() {
        System.out.println("STUB EscapeGameFacade.getProgressPercent() called");
        return game != null ? game.progressPercent() : 0.0;
    }

    public boolean createPlayer() {
        //System.out.println("STUB EscapeGameFacade.createPlayer() called: " + username);
        //Player newPlayer = new Player(username);
        //players.setCurrentPlayers(newPlayer);
        return true;
    }

    public void displayStory() {
        System.out.println("STUB EscapeGameFacade.displayStory() called");
        if (game != null) {
        game.displayStory();
        }
    }

    /**
     * Get available hints for current puzzle - STUB
     * @return list of available hints
     */
    public ArrayList<Hint> getAvailableHints() {
        System.out.println("STUB EscapeGameFacade.getAvailableHints() called");
        Puzzle current = puzzleman.getCurrentPuzzle();
        if (current != null) {
            return current.getHints();
        }
        return new ArrayList<>();
    }

    /**
     * Get number of hints used - STUB
     * @return number of hints used
     */
    public int getHintsUsed() {
        System.out.println("STUB EscapeGameFacade.getHintsUsed() called");
        return 0;
    }

    /**
     * Reveal a hidden item - STUB
     * @param hiddenItem the hidden item to reveal
     * @return the revealed item
     */
    public Item revealHiddenItem(HiddenItem hiddenItem) {
        System.out.println("STUB EscapeGameFacade.revealHiddenItem() called");
        if (hiddenItem != null) {
            return hiddenItem.revealItem();
        }
        return null;
    }

    /**
     * Get number of strikes - STUB
     * @return number of strikes (wrong answers)
     */
    public int getStrikes() {
        System.out.println("STUB EscapeGameFacade.getStrikes() called");
        return 0;
    }

    /**
     * Add a strike (wrong answer penalty) - STUB
     */
    public void addStrike() {
        System.out.println("STUB EscapeGameFacade.addStrike() called");
    }

    /**
     * Update score - STUB
     * @param points points to add
     */
    public void updateScore(int points) {
        System.out.println("[STUB] EscapeGameFacade.updateScore() called: " + points);
        if (game != null) {
            game.setScore(game.getScore() + points);
        }
    }

    public boolean getInstance(String string) {
        throw new UnsupportedOperationException("Unimplemented method 'getInstance'");
    }
}