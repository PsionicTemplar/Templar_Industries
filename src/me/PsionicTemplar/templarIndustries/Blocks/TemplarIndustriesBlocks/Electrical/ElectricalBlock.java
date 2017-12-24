package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Electrical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;

import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;

public abstract class ElectricalBlock extends TemplarBlock{
	

	protected HashMap<Location, Double> inputs = new HashMap<Location, Double>();
	protected HashMap<Location, Double> maxinputs = new HashMap<Location, Double>();
	protected HashMap<Location, List<Location>> recieved = new HashMap<Location, List<Location>>();

	public ElectricalBlock(String name, int inventorySize) {
		super(name, inventorySize);
		this.isElectrical = true;
	}
	
	public double getInputs(Location l){
		return inputs.get(l);
	}
	
	public ElectricalBlock setInput(Location l, double amount){
		if(amount > maxinputs.get(l)){
			this.inputs.put(l, maxinputs.get(l));
			return this;
		}
		this.inputs.put(l, amount);
		return this;
	}
	
	public double getMaxInput(Location l){
		return maxinputs.get(l);
	}
	
	public List<Location> getRecieved(Location l){
		return recieved.get(l);
	}
	
	public ElectricalBlock addRecieved(Location l, Location rec){
		this.recieved.get(l).add(rec);
		return this;
	}
	
	public ElectricalBlock resetRecieved(Location l){
		this.recieved.put(l, new ArrayList<Location>());
		return this;
	}
}
