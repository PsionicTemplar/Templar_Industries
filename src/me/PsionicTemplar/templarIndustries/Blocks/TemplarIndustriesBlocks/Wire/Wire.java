package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Wire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;

import me.PsionicTemplar.templarIndustries.Start;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;

public abstract class Wire extends TemplarBlock {

	protected HashMap<Location, Double> voltages = new HashMap<Location, Double>();
	protected double maxVoltage;

	public Wire(String name, int inventorySize, double maxVoltage) {
		super(name, inventorySize);
		this.maxVoltage = maxVoltage;
		this.isWire = true;
	}

	protected Location getGeneratorLocation(List<Location> complete, Location l) {
		Location newL = null;
		List<Location> possible = new ArrayList<Location>();
		possible.add(new Location(l.getWorld(), l.getX() + 1, l.getY(), l.getZ()));
		possible.add(new Location(l.getWorld(), l.getX() - 1, l.getY(), l.getZ()));
		possible.add(new Location(l.getWorld(), l.getX(), l.getY() + 1, l.getZ()));
		possible.add(new Location(l.getWorld(), l.getX(), l.getY() - 1, l.getZ()));
		possible.add(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ() + 1));
		possible.add(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ() - 1));

		for (Location ll : possible) {
			for (TemplarBlock tb : Start.getBlocks()) {
				if (!tb.getLocations().containsKey(ll)) {
					continue;
				}
				if (tb.isGenerator()) {
					return ll;
				} else if (tb.isWire()) {
					if (complete.contains(ll)) {
						continue;
					}
					complete.add(l);
					newL = getGeneratorLocation(complete, ll);
					complete.remove(l);
					if (newL != null) {
						break;
					}
				}
			}
		}
		complete.add(l);
		return newL;
	}

	public void addVoltage(Location l, double amount) {
		Double voltage = voltages.get(l);
		voltage += amount;
		if (voltage > this.maxVoltage) {
			voltage = this.maxVoltage;
		}
		voltages.put(l, voltage);
	}

	public void takeVoltage(Location l, double amount) {
		Double voltage = voltages.get(l);
		voltage += amount;
		if (voltage < 0) {
			voltage = 0.0;
		}
		voltages.put(l, voltage);
	}
}
