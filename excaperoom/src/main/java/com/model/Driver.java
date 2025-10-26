package com.model;

public class Driver {
<<<<<<< HEAD
    private EscapeGameFacade facade;
    private Players players;
    
    public Driver() {
        this.facade = EscapeGameFacade.getInstance();
        this.players = Players.getInstance();
    }
    
}
=======
    static EscapeGameFacade facade = EscapeGameFacade.getInstance();

    public static void main(String[] args) {
        facade.loadProgress();
        System.out.println("Welcome to VHS Escape!");
        System.out.println("Creating a new account for Leni Rivers (display name: lrivers) with the password password1234, lrivers is an account that already exists for her brother though!");
        facade.createPlayer("lrivers", "password1234");
        System.out.println("Creating a new account for Leni Rivers (display name: lerivers) with the password password1234, since lrivers was taken");
        facade.createPlayer("lerivers", "password1234");
    }
}
>>>>>>> 076b231e707aef9a0b8231428577666211946b29
