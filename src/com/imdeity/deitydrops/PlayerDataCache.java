package com.imdeity.deitydrops;

import java.util.HashMap;

public class PlayerDataCache
{
	private HashMap<String, McmmoData> _map = new HashMap<String, McmmoData>();
	
	//store or update a player's mcmmo data
	public void put(String player, McmmoData data)
	{
		_map.put(player,  data);
	}
	
	//retrieve the mcmmo data for a player
	public McmmoData get(String player)
	{
		//get the data from the map
		McmmoData data = _map.get(player);
		
		//null means the player wasn't in the hash.. no clue how this would happen. just return all 0s.
		if (data == null)
			data = new McmmoData();
		
		return data;
	}
}
