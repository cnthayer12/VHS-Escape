package com.model;

import java.util.ArrayList;
import java.util.UUID;

public class Players {

    private static Players instance = null;
    private static ArrayList<Player> players;
    private static Player currentPlayer = null;

    private Players() {
        players = new ArrayList<>();
    }

    public static Players getInstance() {
        if (instance == null) {
            instance = new Players();
        }
        return instance;
    }

    public static ArrayList<Player> getPlayers() {
        return players;
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setPlayers(ArrayList<Player> players) {
        Players.players = players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Player getPlayer(UUID id) {
        for (Player player : players) {
            if (player.getId().equals(id)) {
                return player;
            }
        }
        return null;
    }

    public boolean removePlayer(UUID id) {
        Player player = getPlayer(id);
        if (player != null) {
            players.remove(player);
            return true;
        }
        return false;
    }

    public void login(String display) {
        if(currentPlayer != null)
        {
            System.out.println("Could not log in, already logged in.");
            return;
        }
        for(int i = 0; i < players.size(); i++){
            Player search = players.get(i);
            if(search.getDisplayName().equals(display)){
                currentPlayer = search;
                System.out.println("Successfully logged in!");
                return;
            }
        }
        System.out.println("Could not log in, user does not exist.");
    }

    public void logout() {
        if(currentPlayer != null) {
            currentPlayer = null;
            System.out.println("Successfully logged out.");
        } else {
            System.out.println("Could not log out, no player is current logged in.");
        }
    }

    public void createAccount(String displayName) {
        if(currentPlayer != null) {
            System.out.println("Could not create account, a user is already logged in.");
            return;
        }
        for(Player player : players) {
            if(player.getDisplayName().equals(displayName)) {
                System.out.println("Could not create account, one with this name already exists.");
                return;
            }
        }
        Progress progressInstance = new Progress();
        ArrayList<Progress> progress = new ArrayList<Progress>();
        progress.add(progressInstance);
        Player newPlayer = new Player(displayName, progress);
        players.add(newPlayer);
        System.out.println("Account created successfully! Logging in now.");
        Players.getInstance().login(displayName);
    }
}
