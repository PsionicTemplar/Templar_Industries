package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Generator;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;

public abstract class TemplarGenerator extends TemplarBlock implements Listener {

	protected HashMap<Location, Double> maxVoltage = new HashMap<Location, Double>();

	public TemplarGenerator(String name, int inventorySize) {
		super(name, inventorySize);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent ex) {
	}

	public double getMaxOutput(Location l) {
		return this.maxVoltage.get(l);
	}

}
