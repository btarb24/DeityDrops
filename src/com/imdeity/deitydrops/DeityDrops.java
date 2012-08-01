package com.imdeity.deitydrops;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityPlugin;

public class DeityDrops extends DeityPlugin {

	//the mcmmo data for all players that have signed online
	private PlayerDataCache _playerCache;
	private DropDataCache _dropCache;
	
	@Override
	protected void initCmds() {
		getCommand("DeityDrops").setExecutor(new CommandHandler(_dropCache));
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
		
		_dropCache.Update();
	}

	@Override
	protected void initInternalDatamembers() { 	}

	@Override
	protected void initListeners() {
		this.registerListener(new EventListeners(_playerCache, _dropCache));
	}

	@Override
	protected void initPlugin() {  	}

	@Override
	protected void initLanguage() {    }

	@Override
	protected void initTasks() {	}
}
