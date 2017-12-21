package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;

import me.PsionicTemplar.templarIndustries.Start;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;
import me.PsionicTemplar.templarIndustries.Util.TemplarTree;
import me.PsionicTemplar.templarIndustries.Util.TemplarTree.Node;

public abstract class TemplarGenerator extends TemplarBlock{

	protected HashMap<Location, TemplarTree<Location>> trees = new HashMap<Location, TemplarTree<Location>>();
	
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
			traverseTree(node.getData(), complete, node);
		}
		complete.add(l);
	}

}
