package com.model;

public class Driver {
    private EscapeGameFacade facade;
    private Players players;
    
    public Driver() {
        this.facade = EscapeGameFacade.getInstance();
        this.players = Players.getInstance();
    }
    
}