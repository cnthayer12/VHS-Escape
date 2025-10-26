package com.model;

public class Driver {
    static EscapeGameFacade facade = EscapeGameFacade.getInstance();

    public static void main(String[] args) {
        facade.loadProgress();
        System.out.println("Welcome to VHS Escape!");
        System.out.println("Creating a new account for Leni Rivers (display name: lrivers) with the password password1234");
        facade.createPlayer("lrivers", "password1234");
        System.out.println("Creating a new accoutn for Leni Rivers (display name: lerivers) with the password password1234");
        facade.createPlayer("lerivers", "password1234");
    }
}
