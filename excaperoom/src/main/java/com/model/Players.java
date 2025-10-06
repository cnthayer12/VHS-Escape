package com.model;

import java.util.ArrayList;

public class Players {
	private static Players players;
	private ArrayList<Player> playerList;
	
	private Players() {
		playerList = DataLoader.getPlayers();
	}
	
	public static Players getInstance() {
		if(players == null) {
			players = new Players();
		}
		
		return players;
	}

	public boolean havePlayer(String displayName) {
		for(Player player : playerList) {
			if(player.getDisplayName()().equals(displayName)) {
				return true;
			}
		}
		
		return false;
	}
	
	public User getUser(String userName) {
		for(User user : userList) {
			if(user.getUserName().equals(userName)) {
				return user;
			}
		}
		
		return null;
	}
	
	public ArrayList<User> getUsers() {
		return userList;
	}
	
	public boolean addUser(String userName, String firstName, String lastName, int age, String phoneNumber) {
		if(haveUser(userName))return false;
		
		userList.add(new User(userName, firstName, lastName, age, phoneNumber));
		return true;
	}
	
	public void saveUsers() {
		DataWriter.saveUsers();
	}
}
    
}
