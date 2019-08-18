package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Generator;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Electrical.ElectricalBlock;

public abstract class TemplarGenerator extends TemplarBlock implements Listener {

	protected HashMap<Location, Double> maxVoltage = new HashMap<Location, Double>();
	protected HashMap<Location, ElectricalBlock> users = new HashMap<Location, ElectricalBlock>();

	public TemplarGenerator(String name, int inventorySize, Location blockLocation) {
		super(name, inventorySize);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent ex) {
		
	}
	
	public void addUser(Location l, ElectricalBlock e) {
		users.put(l, e);
	}
	
	public void removeUser(Location l) {
		if(users.containsKey(l)) {
			users.remove(l);
		}
	}
	
	public HashMap<Location, ElectricalBlock> getUsers(){
		return this.users;
	}

	public double getMaxOutput(Location l) {
		return this.maxVoltage.get(l);
	}
	
	@Override
	public void onInit() {
		
	}

}
