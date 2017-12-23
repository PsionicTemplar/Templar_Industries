package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;

import me.PsionicTemplar.templarIndustries.Start;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Wire.Wire;
import me.PsionicTemplar.templarIndustries.Util.TemplarTree;
import me.PsionicTemplar.templarIndustries.Util.TemplarTree.Node;

public abstract class TemplarGenerator extends TemplarBlock{

	protected HashMap<Location, TemplarTree<Location>> trees = new HashMap<Location, TemplarTree<Location>>();
	protected HashMap<Location, Double> outputs = new HashMap<Location, Double>();
	
	public TemplarGenerator(String name, int inventorySize) {
		super(name, inventorySize);
	}
	
	public void createTree(Location l) {
		List<Location> complete = new ArrayList<Location>();
		complete.add(l);
		TemplarTree<Location> t = new TemplarTree<Location>(l, this);
		traverseTree(l, complete, t.getNodeInstance());
		trees.put(l, t);
	}

	private void traverseTree(Location l, List<Location> complete, Node<Location> n) {
		List<Location> possible = new ArrayList<Location>();
		possible.add(new Location(l.getWorld(), l.getX() + 1, l.getY(), l.getZ()));
		possible.add(new Location(l.getWorld(), l.getX() - 1, l.getY(), l.getZ()));
		possible.add(new Location(l.getWorld(), l.getX(), l.getY() + 1, l.getZ()));
		possible.add(new Location(l.getWorld(), l.getX(), l.getY() - 1, l.getZ()));
		possible.add(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ() + 1));
		possible.add(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ() - 1));
		for (Location ll : possible) {
			if (complete.contains(ll)) {
				continue;
			}
			for (TemplarBlock t : Start.getBlocks()) {
				if (t.getLocations().containsKey(ll)) {
					n.addChild(ll, t);
					complete.add(ll);
					break;
				}
			}
		}
		for (Node<Location> node : n.getChildren()) {
			if(node.getBlockType().isElectrical() || node.getBlockType().isGenerator()) {
				continue;
			}
			traverseTree(node.getData(), complete, node);
		}
		complete.add(l);
		sendEnergy(l);
	}
	
	public void sendEnergy(Location l) {
		TemplarTree<Location> tree = trees.get(l);
		for(Node<Location> n : tree.getNodeInstance().getChildren()) {
			sendEnergyR(n, new ArrayList<Location>());
		}
	}
	
	private void sendEnergyR(Node<Location> node, List<Location> complete) {
		
	}
	
	public boolean hasElectricMachineInTree(Location l) {
		TemplarTree<Location> tree = trees.get(l);
		return findElectricMachine(tree.getNodeInstance());
	}
	
	private boolean findElectricMachine(Node<Location> node) {
		if(node.getBlockType().isElectrical()) {
			return true;
		}else {
			for(Node<Location> n : node.getChildren()) {
				if(findElectricMachine(n)) {
					return true;
				}
			}
			return false;
		}
	}
	
	public double getMachineOutput(Location l) {
		return outputs.get(l);
	}

}
