package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Wire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.PsionicTemplar.templarIndustries.Start;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Generator.TemplarGenerator;

public abstract class Wire extends TemplarBlock {

	protected HashMap<Location, Double> voltages = new HashMap<Location, Double>();
	protected HashMap<Location, Double> loss = new HashMap<Location, Double>();
	protected HashMap<Location, Double> maxVoltage = new HashMap<Location, Double>();

	public Wire(String name, int inventorySize) {
		super(name, inventorySize);
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
	
	@Override
	public void onBlockPlace(BlockPlaceEvent e){
		Location l = e.getBlock().getLocation();
		this.voltages.put(l, 0.0);
		List<Location> complete = new ArrayList<Location>();
		
		Location genLocation = getGeneratorLocation(complete, l);
		TemplarGenerator tcg = null;
		if (genLocation == null) {
			return;
		} else {
			for(TemplarBlock tb : Start.getBlocks()){
				if(!tb.getLocations().containsKey(genLocation)){
					continue;
				}
				tcg = (TemplarGenerator) tb;
			}
			tcg.createTree(genLocation);
		}
	}

	@Override
	public void onBlockBreak(BlockBreakEvent e) {
		this.voltages.remove(e.getBlock().getLocation());
	}

	public Wire addVoltage(Location l, double amount) {
		Double voltage = voltages.get(l);
		voltage += amount;
		if (voltage > this.maxVoltage.get(l)) {
			voltage = this.maxVoltage.get(l);
		}
		voltages.put(l, voltage);
		return this;
	}

	public Wire takeVoltage(Location l, double amount) {
		Double voltage = voltages.get(l);
		voltage += amount;
		if (voltage < 0) {
			voltage = 0.0;
		}
		voltages.put(l, voltage);
		return this;
	}
	
	public double getCurrentVoltage(Location l){
		double v = (this.voltages.get(l) - this.loss.get(l));
		if(v < 0){
			v = 0;
		}
		return v;
	}
}
