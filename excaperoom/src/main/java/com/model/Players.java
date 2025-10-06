package com.model;

import java.util.ArrayList;
import java.util.UUID;

public class Players {

    private static Players instance = null;

    private ArrayList<Player> players;

    private Players() {
        players = new ArrayList<>();
    }

    public static Players getInstance() {
        if (instance == null) {
            instance = new Players();
        }
        return instance;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
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
}
