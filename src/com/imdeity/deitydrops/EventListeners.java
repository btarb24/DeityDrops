package com.imdeity.deitydrops;

import java.sql.SQLDataException;
import java.util.ArrayList;

import com.gmail.nossr50.util.BlockChecks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import com.imdeity.deityapi.DeityAPI;
import com.imdeity.deityapi.api.DeityListener;
import com.imdeity.deityapi.records.DatabaseResults;

public class EventListeners extends DeityListener 
{
	//the mcmmo data for all players that have signed online
	private PlayerDataCache _playerCache;
	private DropDataCache _dropCache;
	
	public EventListeners(PlayerDataCache playerCache, DropDataCache dropCache)
	{
		_playerCache = playerCache;
		_dropCache = dropCache;
	}
	
	@EventHandler
	public void OnPlayerJoin(PlayerJoinEvent event)
	{
		//get the player's name
		String playerName = event.getPlayer().getName();
		
		//get the skill levels for this user
		String sql = "SELECT * FROM mcmmo_skills JOIN mcmmo_users ON mcmmo_users.id = mcmmo_skills.user_id WHERE mcmmo_users.user = ?";
	    DatabaseResults query = DeityAPI.getAPI().getDataAPI().getMySQL().readEnhanced(sql, playerName);
	    
	    McmmoData data = new McmmoData();
        if (query != null && query.hasRows()) {
        	
        	//get each skill level and set it into the data object
            try { data.setMining((int)query.getLong(0, "mining"));                
            } catch (SQLDataException e) {  }//likely not worth outputting. just leave value at 0;

            try { data.setWoodcutting((int)query.getLong(0, "woodcutting"));                
            } catch (SQLDataException e) {  }//likely not worth outputting. just leave value at 0;
            
            try { data.setExcavation((int)query.getLong(0, "excavation"));                
            } catch (SQLDataException e) {  }//likely not worth outputting. just leave value at 0;
            
            try { data.setHerbalism((int)query.getLong(0, "herbalism"));                
            } catch (SQLDataException e) {  }//likely not worth outputting. just leave value at 0;

            /* not currently used.. might as well comment out for now
             * 
            try { data.setTaming(query.getInteger(0, "taming"));                
            } catch (SQLDataException e) {  }//likely not worth outputting. just leave value at 0;
            
            try { data.setRepair(query.getInteger(0, "repair"));                
            } catch (SQLDataException e) {  }//likely not worth outputting. just leave value at 0;

            try { data.setUnarmed(query.getInteger(0, "unarmed"));                
            } catch (SQLDataException e) {  }//likely not worth outputting. just leave value at 0;

            try { data.setArchery(query.getInteger(0, "archery"));                
            } catch (SQLDataException e) {  }//likely not worth outputting. just leave value at 0;

            try { data.setSwords(query.getInteger(0, "swords"));                
            } catch (SQLDataException e) {  }//likely not worth outputting. just leave value at 0;

            try { data.setAxes(query.getInteger(0, "axes"));                
            } catch (SQLDataException e) {  }//likely not worth outputting. just leave value at 0;

            try { data.setAcrobatics(query.getInteger(0, "acrobatics"));                
            } catch (SQLDataException e) {  }//likely not worth outputting. just leave value at 0;

            try { data.setFishing(query.getInteger(0, "fishing"));                
            } catch (SQLDataException e) {  }//likely not worth outputting. just leave value at 0;
            */

            //push it into the hashmap
            _playerCache.put(playerName,  data);
        }
	}
	
	@EventHandler
	public void OnBlockBreak(BlockBreakEvent event)
	{
		String playerName = event.getPlayer().getName();
		
		//find out what Skill it uses and get the player's current level for that skill		
		int skillLevel = 0;
		
		if (BlockChecks.canBeSuperBroken(event.getBlock())) {//MINING
			skillLevel = _playerCache.get(playerName).getMining();
		}
		else if (BlockChecks.canBeGigaDrillBroken(event.getBlock())) {//EXCAVATION
			skillLevel = _playerCache.get(playerName).getExcavation();
		}
		else if (BlockChecks.isLog(event.getBlock())) {//WOODCUTTING
			skillLevel = _playerCache.get(playerName).getWoodcutting();
		}
		else if (BlockChecks.canBeGreenTerra(event.getBlock())) { //HERBALISM
			skillLevel = _playerCache.get(playerName).getHerbalism();
		}
		
		//get a list of the drops rewarded
		ArrayList<Integer> results = _dropCache.get(event.getBlock().getTypeId(), skillLevel, playerName);
		
		//exit if there were no results
		if (results == null || results.isEmpty())
			return;
		
		//drop the items where the block was broken
		for (Integer result : results)
			event.getPlayer().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(result));
	}

}
