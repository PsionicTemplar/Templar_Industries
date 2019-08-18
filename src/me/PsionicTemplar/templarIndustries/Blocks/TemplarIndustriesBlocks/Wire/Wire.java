package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Wire;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;

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

	@Override
	public void onBlockPlace(BlockPlaceEvent e) {

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
