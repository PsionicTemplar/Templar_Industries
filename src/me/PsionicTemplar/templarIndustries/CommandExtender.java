package me.PsionicTemplar.templarIndustries;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExtender implements CommandExecutor {

	/**
	 * Switch case for each command. See the code for additional comments.
	 * 
	 * @author Nicholas Braniff
	 */
	
	@Override
	public boolean onCommand(CommandSender sender, Command base, String alias, String[] args) {
		//Set the player if applicable
		Player p = null;
		if ((sender instanceof Player)) {
			p = (Player) sender;
		}
		switch(alias.toString()){
		//Does what I want this command to do. Used in testing.
		case "test":
			p.getInventory().addItem(Start.getBlock("Industrial workbench").getItemStack());
			return true;
		}
		return false;
	}

}
