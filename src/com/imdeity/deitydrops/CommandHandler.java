package com.imdeity.deitydrops;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class CommandHandler implements CommandExecutor {

	private DropDataCache _dropCache;
	
	public CommandHandler(DropDataCache dropCache)
	{
		_dropCache = dropCache;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) 
	{
		//ignore commands that do not begin with nether
		if (!cmd.getName().equalsIgnoreCase("deitydrops"))
			return true; //wrong command.. howd it get here??
		
		//ensure they are allowed to admin this plugin
		if (! sender.hasPermission("Deity.deitydrops.admin"))
		{
			sender.sendMessage("You do not have permission");
			return true;
		}
		
		if (args.length == 0)
		{
			//argument fail. output usage
			outputUsage(sender);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("refreshcache"))
		{
			_dropCache.Update();
			sender.sendMessage("Cache successfully updated.");
		}
		
		return true;
	}

	//the usage for /nether admin
	private void outputUsage(CommandSender sender)
	{
		List<String> messages = new ArrayList<String>();
		messages.add("DEITY DROPS ADMIN USAGE");
		messages.add("/dd refreshCache  //loads fresh values from the database");
		sender.sendMessage(messages.toArray(new String[messages.size()]));
	}
}
