package com.model;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;;

public class DataLoader extends DataConstants{
	
	public static ArrayList<Player> getPlayers() {
		ArrayList<Player> Players = new ArrayList<Player>();
		
		try {
			FileReader reader = new FileReader(USER_FILE_NAME);
			JSONArray peopleJSON = (JSONArray)new JSONParser().parse(reader);
			
			for(int i=0; i < peopleJSON.size(); i++) {
				JSONObject personJSON = (JSONObject)peopleJSON.get(i);
				UUID id = UUID.fromString((String)personJSON.get(USER_ID));
				String displayName = (String)personJSON.get(USER_NAME);
                int hintsUsed = ((Long)personJSON.get(USER_HINTS_USED)).intValue();
                ArrayList<String> inventory = ((ArrayList<String>)personJSON.get(USER_INVENTORY));
                ArrayList<String> storedHints = ((ArrayList<String>)personJSON.get(USER_STORED_HINTS));
				int strikes = ((Long)personJSON.get(USER_STRIKES)).intValue();
                int currentScore = ((Long)personJSON.get(USER_CURRENT_SCORE)).intValue();
                Progress progress = new Progress(hintsUsed, inventory, storedHints, strikes, currentScore);
				
				Players.add(new Player(id, displayName, progress));
			}
			
			return Players;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Players;
	}

	public static void main(String[] args){
		ArrayList<Player> players = DataLoader.getPlayers();

		for(Player player : players){
			System.out.println(player);
		}
	}
}