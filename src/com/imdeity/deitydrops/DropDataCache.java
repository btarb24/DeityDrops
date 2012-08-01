package com.imdeity.deitydrops;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.plugin.Plugin;

public class DropDataCache 
{
	private HashMap<Integer, DropChance> _map = new HashMap<Integer, DropChance>();
	private boolean _logResults = true;
	private Plugin _plugin;
	
	public DropDataCache(boolean logResults, Plugin plugin)
	{
		_logResults = logResults;
		_plugin =  plugin;
	}
	
	//store or update a drop chance
	public void put(int id, int brokenBlock, int startingLevel, int matureLevel, double startingPercent, double maturePercent, int resultBlock)
	{
		//see if this block id is already in the hash so we can update it
		DropChance chance = _map.get(brokenBlock);
		
		//did it exist? if not make a new chance obj
		if (chance == null)
			chance = new DropChance(_plugin, _logResults);
		
		//put the data in the chance obj (it will figure out if it's dupe or not
		chance.put(id, startingLevel, matureLevel, startingPercent, maturePercent, resultBlock);
		
		//ok now add the chance to this hash
		_map.put(brokenBlock,  chance);
	}
	
	//return the list of drops
	public ArrayList<Integer> get(int brokenBlock, int currentLevel, String playerName)
	{
		//get the data from the map
		DropChance chance = _map.get(brokenBlock);
		
		//null means the block wasn't in the hash.. so they get no rewards 
		if (chance == null)
			return null;
		
		//block was found.. now have it check to see if they were lucky enough to get a drop result
		return chance.getResult(currentLevel, playerName);
	}
}
