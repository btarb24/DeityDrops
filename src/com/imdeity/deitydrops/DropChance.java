package com.imdeity.deitydrops;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class DropChance 
{
	ArrayList<DropChanceEntity> _chances = new ArrayList<DropChanceEntity>();
	private boolean _logResults = true;
	Plugin _plugin;
	
	public DropChance(Plugin plugin, boolean logResults)
	{
		_logResults = logResults;
		_plugin = plugin;
	}
	
	//adds or updates a chance entry
	public void put (int id, int startingLevel, int matureLevel, double startingPercent, double maturePercent, int resultBlock)
	{
		//check if this entity is already existing. update if so.
		if (_chances.size() > 0)
			for(DropChanceEntity entity : _chances)
				if (entity.id == id)
				{
					entity.startingLevel = startingLevel;
					entity.matureLevel = matureLevel;
					entity.startingPercent = startingPercent;
					entity.maturePercent = maturePercent;
					entity.resultBlock = resultBlock;
					return;
				}
		
		//didn't exist.. let's add it
		DropChanceEntity entity = new DropChanceEntity();

		entity.id = id;
		entity.startingLevel = startingLevel;
		entity.matureLevel = matureLevel;
		entity.startingPercent = startingPercent;
		entity.maturePercent = maturePercent;
		entity.resultBlock = resultBlock;
		
		_chances.add(entity);		
	}
	
	//see if the user is lucky enough to get a bonus result block.  empty list if not
	public ArrayList<Integer> getResult(int currentLevel, String playerName)
	{
		ArrayList<Integer> resultList = new ArrayList<Integer>();
		//iterate over all of the Chances we have.
		for (DropChanceEntity entity : _chances)
		{
			int result = getResult(entity, currentLevel);
			if (result > -1) //add it if there's a valid result block
			{
				resultList.add(result);
				
				//log the result if logging is enabled
				if (_logResults)
				{
					//make a runnable task
					String sql = "INSERT INTO `drop_results` (`chance_id`, `player_name`, `result_block_id`) VALUES (?, ?, ?);";
					AsyncSqlCommand task = new AsyncSqlCommand(sql, entity.id, playerName, result);
					//fire the task
					Bukkit.getScheduler().scheduleAsyncDelayedTask(_plugin, task);
				}
			}
		}
		
		//return list.. either empty or populated
		return resultList;
	}
	
	//
	private int getResult(DropChanceEntity entity, int currentLevel)
	{
		//are they high enough?
		if (currentLevel < entity.startingLevel)
			return -1;
		
		double percentToUse = entity.maturePercent;
		//scale the percent since they didn't reach the full maturity yet
		if (currentLevel < entity.matureLevel)
		{
			//get the amount we need to scale the percent by (based on levels)
			int progress = currentLevel - entity.startingLevel;
			double amountToScale = progress / (double)(entity.matureLevel - entity.startingLevel);
			
			//we only want to scale the percent that's above the base (starting) percent.
			double percentDif = entity.maturePercent - entity.startingPercent;
			
			//compute it
			double scaledPercent =  amountToScale * percentDif;
			
			//and add the base percent back on so we have the final percent to use
			percentToUse = scaledPercent + entity.startingPercent;
		}

		double random = new Random().nextDouble(); //get a random double that's 0 to 1
		
		
		
		//check if the random is within our allowable percent
		if (random <= percentToUse)
			return entity.resultBlock;
		else
			return -1;
	}

	//simple struct for caching individual drop chances (in case there are multiple chances for a broken block)
	private class DropChanceEntity
	{
		public int id;
		public int startingLevel;
		public int matureLevel;
		public double startingPercent;
		public double maturePercent;
		public int resultBlock;
	}
	
}
