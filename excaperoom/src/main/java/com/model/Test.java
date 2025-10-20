package com.model;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args){
        Players players = Players.getInstance();
        ArrayList<Player> playerList = DataLoader.getPlayers();
        players.setPlayers(playerList);
        System.out.println("Login scenario, success:");
        players.login("Lukin");

        System.out.println("\nLogin scenario, unsuccessful (already logged in):");
        players.login("Bob");

        System.out.println("\nLogout scenario, successful:");
        players.logout();

        System.out.println("\nLogin scenario, unsuccessful (account doesn't exist):");
        players.login("Bob");

        System.out.println("\nLogout scenario, unsuccessful (already logged out):");
        players.logout();
        
        System.out.println("\nCreate account scenario, unsuccessful (account exists already):");
        players.createAccount("Lukin");

        System.out.println("\nCreate account scenario, successful:");
        players.createAccount("Bob");

        System.out.println("\nCreate account scenario, unsuccessful (already logged in):");
        players.createAccount("Phil");

        System.out.println("\nLogout scenario, successful:");
        players.logout();

    }
}