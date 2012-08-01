package com.imdeity.deitydrops;

import java.sql.SQLDataException;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityPlugin;
import com.imdeity.deityapi.records.DatabaseResults;

public class DeityDrops extends DeityPlugin {

	//the mcmmo data for all players that have signed online
	private PlayerDataCache _playerCache;
	private DropDataCache _dropCache;
	
	@Override
	protected void initCmds() {
		//TODO: commands to get some stats
	}

	@Override
	protected void initConfig() {
		//whether we persist a record for each drop to the database or not
		this.config.addDefaultConfigValue("LogDropsToDatabase", true);
	}

	@Override
	protected void initDatabase() {
		DeityAPI.getAPI().getDataAPI().getMySQL().write("CREATE TABLE IF NOT EXISTS `drop_chances` ("+
				"`id` INT NOT NULL AUTO_INCREMENT ,"+
				"`broken_block` INT NOT NULL ,"+
				"`starting_level` INT NULL DEFAULT 1 COMMENT 'level that you begin to receive this drop chance' ,"+
				"`mature_level` INT NULL DEFAULT 100 COMMENT 'level where you reach its full drop percent' ,"+
				"`starting_percent` DOUBLE NULL DEFAULT 0.00001 ," +
				"`mature_percent` DOUBLE NULL DEFAULT 0.001 ," +
				"`result_block` INT NOT NULL ," +
				"`last_modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'when this record was last modified (useful to track results if you change this Chances properties)' ," +
				"PRIMARY KEY (`id`) ," +
				"UNIQUE INDEX `idDropChance_UNIQUE` (`id` ASC) );");
		
		DeityAPI.getAPI().getDataAPI().getMySQL().write("CREATE TABLE IF NOT EXISTS `drop_results` ("+
				"`id` INT NOT NULL AUTO_INCREMENT ,"+
				"`chance_id` INT NOT NULL COMMENT 'the id of the chance record that spawned this drop' ,"+
				"`player_name` VARCHAR(45) NOT NULL ,"+
				"`date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'the date of the item drop' ," +
				"`result_block_id` INT NOT NULL ," +
				"PRIMARY KEY (`id`) );");
		
		boolean logResults = this.getConfig().getBoolean("LogDropsToDatabase");
		_playerCache = new PlayerDataCache();
		_dropCache = new DropDataCache(logResults, this);
		
		populateDropCache();
	}

	@Override
	protected void initInternalDatamembers() { 	}

	@Override
	protected void initListeners() {
		this.registerListener(new EventListeners(_playerCache, _dropCache));
	}

	@Override
	protected void initPlugin() {
		
	}

	@Override
	protected void initLanguage() {    }

	@Override
	protected void initTasks() {	}
	
	private void populateDropCache()
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
                	_dropCache.put(id, brokenBlock, startingLevel, matureLevel, startingPercent, maturePercent, resultBlock);
                	
                } catch (SQLDataException e) {  }//likely not worth outputting. just leave value at 0;
        	}
        }
	}
}
