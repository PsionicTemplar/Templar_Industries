package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Wire;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.PsionicTemplar.templarIndustries.Start;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Generator.TemplarGenerator;

public abstract class Wire extends TemplarBlock {

	/**
	 * Constructor for a generic wire
	 * 
	 * @author Nicholas Braniff
	 * @param name
	 * @param inventorySize
	 */
	public Wire(String name, int inventorySize) {
		super(name, inventorySize);
		this.isWire = true;
	}

	protected Location getGeneratorLocation(List<Location> complete, Location l, List<Location> generators) {
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
					if(!generators.contains(ll)) {
						generators.add(ll);
					}
					break;
				} else if (tb.isWire()) {
					if (complete.contains(ll)) {
						continue;
					}
					complete.add(l);
					newL = getGeneratorLocation(complete, ll, generators);
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

	@Override
	public void onBlockPlace(BlockPlaceEvent e) {
		Location l = e.getBlock().getLocation();
		List<Location> complete = new ArrayList<Location>();
		List<Location> generators = new ArrayList<Location>();

		getGeneratorLocation(complete, l, generators);
		TemplarGenerator tcg = null;
		if (generators.isEmpty()) {
			return;
		} else {
			for (TemplarBlock tb : Start.getBlocks()) {
				for(Location genloc : generators) {
					if (!tb.getLocations().containsKey(genloc)) {
						continue;
					}
					tcg = (TemplarGenerator) tb;
					tcg.createTree(genloc);
				}
			}
			
		}
	}

	@Override
	public void onBlockBreak(BlockBreakEvent e) {
	}

//	public double getCurrentVoltage(Location l) {
//		double v = (this.inputs.get(l) - this.loss.get(l));
//		if (v < 0) {
//			v = 0;
//		}
//		return v;
//	}

//	public double getInput(Location l) {
//		return inputs.get(l);
//	}

//	public Wire setInput(Location l, double amount) {
//		if(amount > this.maxVoltage.get(l)){
//			inputs.put(l, this.maxVoltage.get(l));
//			return this;
//		}
//		inputs.put(l, amount);
//		return this;
//	}

//	public double getOutput(Location l) {
//		return this.outputs.get(l);
//	}

//	public Wire setOutput(Location l) {
//		this.outputs.put(l, getCurrentVoltage(l));
//		return this;
//	}
}
