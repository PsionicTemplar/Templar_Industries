package me.PsionicTemplar.templarIndustries;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExtender implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command base, String alias, String[] args) {
		Player p = null;
		if ((sender instanceof Player)) {
			p = (Player) sender;
		}
		switch(alias.toString()){
		case "test":
			p.sendMessage("Yay!");
			return true;
		}
		return false;
	}

}
