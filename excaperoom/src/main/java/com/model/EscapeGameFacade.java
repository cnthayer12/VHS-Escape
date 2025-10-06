package com.model;

/**
 * EscapeGameFacade - STUB version
 */
public class EscapeGameFacade {
    //Instance variables from UML
    //private Game game;
    //private PuzzlesManager puzzleman;
    private Players players;
    private static EscapeGameFacade instance;
    
    // Constructor - null for now
    public EscapeGameFacade() {
        //this.game = null;
        //this.puzzleman = null;
        this.players = null;
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
        System.out.println("STUB saveProgress() called");
        return true;  
}

    public boolean getInstance(String string) {
        throw new UnsupportedOperationException("Unimplemented method 'getInstance'");
    }
}