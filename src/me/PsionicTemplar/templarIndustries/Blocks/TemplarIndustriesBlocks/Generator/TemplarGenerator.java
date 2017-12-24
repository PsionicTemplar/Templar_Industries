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

public abstract class TemplarGenerator extends TemplarBlock {

	protected HashMap<Location, TemplarTree<Location>> trees = new HashMap<Location, TemplarTree<Location>>();
	protected HashMap<Location, Double> outputs = new HashMap<Location, Double>();
	protected HashMap<Location, Double> maxVoltage = new HashMap<Location, Double>();

	public TemplarGenerator(String name, int inventorySize) {
		super(name, inventorySize);
	}

	public void createTree(Location l) {
		List<Location> complete = new ArrayList<Location>();
		complete.add(l);
		TemplarTree<Location> t = new TemplarTree<Location>(l, this);
		traverseTree(l, complete, t.getNodeInstance(), t);
		trees.put(l, t);
	}

	private boolean traverseTree(Location l, List<Location> complete, Node<Location> n, TemplarTree<Location> first) {
		List<Location> possible = new ArrayList<Location>();
		possible.add(new Location(l.getWorld(), l.getX() + 1, l.getY(), l.getZ()));
		possible.add(new Location(l.getWorld(), l.getX() - 1, l.getY(), l.getZ()));
		possible.add(new Location(l.getWorld(), l.getX(), l.getY() + 1, l.getZ()));
		possible.add(new Location(l.getWorld(), l.getX(), l.getY() - 1, l.getZ()));
		possible.add(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ() + 1));
		possible.add(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ() - 1));
		int depth = n.getDepth();
		for (Location ll : possible) {
			if (complete.contains(ll) && (first.findNode(ll).getDepth() > depth)) {
				Node<Location> node = first.findNode(ll);
				node.getParent().removeChild(node);
				complete.remove(ll);
				for (Location lll : complete) {
					Node<Location> nn = first.findNode(lll);
					if (!nn.getParent().equals(n)) {
						continue;
					}
					n.removeChild(nn);
					complete.remove(nn);
				}
			} else {
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
		boolean t = false;
		for (Node<Location> node : n.getChildren()) {
			if (node.getBlockType().isElectrical() || node.getBlockType().isGenerator()) {
				continue;
			}
			t = traverseTree(node.getData(), complete, node, first);
			if (!t) {
				n.removeChild(node);
			}
		}
		if (n.getChildren().isEmpty() && n.getBlockType().isElectrical()) {
			return true;
		}
		complete.add(l);
		return t;
	}

	public void sendEnergy(Location l) {
		TemplarTree<Location> tree = trees.get(l);
		resetEnergyR(tree.getNodeInstance());
		sendEnergyR(tree.getNodeInstance(), 0);
	}

	private void resetEnergyR(Node<Location> node) {
		boolean isWire = node.getBlockType().isWire();
		Wire w = null;
		// boolean isElectrical = node.getBlockType().isElectrical();
		// ElectricalBlock e = null;
		if (isWire) {
			w = (Wire) node.getBlockType();
			w.setInput(node.getData(), 0.0);
		}
		// if(isElectrical){
		// e = (ElectricalBlock) node.getBlockType();
		// <Set input to 0>
		// }
		for (Node<Location> n : node.getChildren()) {
			resetEnergyR(n);
		}
	}

	private void sendEnergyR(Node<Location> node, double newInput) {
		boolean isWire = node.getBlockType().isWire();
		Wire w = null;
		// boolean isElectrical = node.getBlockType().isElectrical();
		// ElectricalBlock e = null;
		boolean isGenerator = node.getBlockType().isGenerator();
		TemplarGenerator t = null;
		int children = node.getChildren().size();
		double output = 0.0;
		if (isWire) {
			w = (Wire) node.getBlockType();
			if (w.getOutput(node.getData()) == 0) {
				return;
			} else {
				w.setInput(node.getData(), w.getInput(node.getData()) + newInput);
				w.setOutput(node.getData());
				output = w.getOutput(node.getData());
			}
		}
		// if(isElectrical){
		// e = (ElectricalBlock) node.getBlockType();
		// <Set input>
		// }
		if (isGenerator) {
			t = (TemplarGenerator) node.getBlockType();
			if (t.getMachineOutput(node.getData()) == 0) {
				return;
			} else {
				output = t.getMachineOutput(node.getData());
			}
		}
		double outputPerChild = output;
		if (children != 0) {
			outputPerChild = output / children;
		}
		for (Node<Location> n : node.getChildren()) {
			sendEnergyR(n, outputPerChild);
		}
	}

	public boolean hasElectricMachineInTree(Location l) {
		TemplarTree<Location> tree = trees.get(l);
		return findElectricMachine(tree.getNodeInstance());
	}

	private boolean findElectricMachine(Node<Location> node) {
		if (node.getBlockType().isElectrical()) {
			return true;
		} else {
			for (Node<Location> n : node.getChildren()) {
				if (findElectricMachine(n)) {
					return true;
				}
			}
			return false;
		}
	}

	public double getMachineOutput(Location l) {
		return outputs.get(l);
	}

	public TemplarGenerator setMachineOutput(Location l, double amount) {
		if (amount > this.maxVoltage.get(l)) {
			this.outputs.put(l, this.maxVoltage.get(l));
			return this;
		}
		this.outputs.put(l, amount);
		return this;
	}

	public double getMaxOutput(Location l) {
		return this.maxVoltage.get(l);
	}

}
