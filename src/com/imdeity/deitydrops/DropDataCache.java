package com.imdeity.deitydrops;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.plugin.Plugin;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.records.DatabaseResults;

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
	

	public void Update()
	{
		//get a list of all of the drop chance records in the db
		String sql = "SELECT * FROM drop_chances;";
	    DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql);
	    
	    //make sure it's not empty
        if (query != null && query.hasRows()) 
        {
        	//iterate over all of the records and put them in cache
        	for(int i = 0; i < query.rowCount(); i++)
        	{
        		int id, brokenBlock, startingLevel, matureLevel, resultBlock;
        		double startingPercent, maturePercent;
        		
                try 
                { 
                	//read in the values from the db record
                	id = query.getInteger(i, "id");
                	brokenBlock = query.getInteger(i, "broken_block");
                	startingLevel = query.getInteger(i, "starting_level");
                	matureLevel = query.getInteger(i, "mature_level");
                	startingPercent = query.getDouble(i, "starting_percent");
                	maturePercent = query.getDouble(i, "mature_percent");
                	resultBlock = query.getInteger(i, "result_block");
                	
                	//put this record in the cache
                	this.put(id, brokenBlock, startingLevel, matureLevel, startingPercent, maturePercent, resultBlock);
                	
                } catch (SQLDataException e) {  }//likely not worth outputting. just leave value at 0;
        	}
        }
	}
}
