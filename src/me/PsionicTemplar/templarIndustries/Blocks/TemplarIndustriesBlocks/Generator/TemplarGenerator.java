package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.PsionicTemplar.templarIndustries.Start;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Electrical.ElectricalBlock;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Wire.Wire;
import me.PsionicTemplar.templarIndustries.Util.TemplarTree;
import me.PsionicTemplar.templarIndustries.Util.TemplarTree.Node;

public abstract class TemplarGenerator extends TemplarBlock implements Listener{

	protected HashMap<Location, TemplarTree<Location>> trees = new HashMap<Location, TemplarTree<Location>>();
	protected HashMap<Location, Double> outputs = new HashMap<Location, Double>();
	protected HashMap<Location, Double> maxVoltage = new HashMap<Location, Double>();

	public TemplarGenerator(String name, int inventorySize) {
		super(name, inventorySize);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent ex){
		if(ex.getAction() == Action.LEFT_CLICK_AIR){
			for(Location l : trees.keySet()){
				sendEnergy(l);
			}
			return;
		}
		Node<Location> node = null;
		for(Location l : trees.keySet()){
			node = trees.get(l).findNode(ex.getClickedBlock().getLocation());
			if(node != null){
				break;
			}
		}
		if(node == null){
			System.out.println("No block found");
			return;
		}
		
		boolean isWire = node.getBlockType().isWire();
		Wire w = null;
		boolean isElectrical = node.getBlockType().isElectrical();
		ElectricalBlock e = null;
		boolean isGenerator = node.getBlockType().isGenerator();
		TemplarGenerator t = null;
		if (isWire) {
			w = (Wire) node.getBlockType();
			ex.getPlayer().sendMessage("Input: " + w.getInput(ex.getClickedBlock().getLocation()));
			ex.getPlayer().sendMessage("Output: " + w.getOutput(ex.getClickedBlock().getLocation()));
		}
		if (isElectrical) {
			e = (ElectricalBlock) node.getBlockType();
			ex.getPlayer().sendMessage("Input: " + e.getInputs(ex.getClickedBlock().getLocation()));
		}
		if (isGenerator) {
			t = (TemplarGenerator) node.getBlockType();
			ex.getPlayer().sendMessage("Output: " + t.getMachineOutput(ex.getClickedBlock().getLocation()));
		}
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
		int depth = n.getDepth() + 1;
		for (Location ll : possible) {
			int tempD = 1000;
			if(complete.contains(ll)){
				tempD = first.findNode(ll).getDepth();
			}
			if (complete.contains(ll) && (tempD > depth)) {
				Node<Location> node = first.findNode(ll);
				node.getParent().removeChild(node);
				System.out.println("Removed Child at " + ll);
				complete.remove(ll);
				for (Location lll : complete) {
					Node<Location> nn = first.findNode(lll);
					if (!nn.getParent().equals(n)) {
						continue;
					}
					n.removeChild(nn);
					System.out.println("Removed Child at " + lll);
					complete.remove(nn);
				}
			} else {
				for (TemplarBlock t : Start.getBlocks()) {
					if (t.getLocations().containsKey(ll)) {
						n.addChild(ll, t);
						System.out.println("Added Child at " + ll);
						complete.add(ll);
						break;
					}
				}
			}
		}
		boolean t = false;
		if(n.getChildren().isEmpty()){
			System.out.println("EMPTY");
		}
		for (Node<Location> node : n.getChildren()) {
			if (n.getBlockType().isElectrical() || (n.getBlockType().isGenerator() && !first.getNodeInstance().equals(n))) {
				continue;
			}
			boolean tt = traverseTree(node.getData(), complete, node, first);
			if(tt){
				t = true;
			}
			if (!tt) {
				n.removeChild(node);
			}
		}
		if (n.getBlockType().isElectrical()) {
			System.out.println("true");
			return true;
		}
		complete.add(l);
		System.out.println(Boolean.valueOf(t));
		return t;
	}

	public void sendEnergy(Location l) {
		TemplarTree<Location> tree = trees.get(l);
//		resetEnergyR(tree.getNodeInstance());
		sendEnergyR(tree.getNodeInstance(), 0, false);
	}

	private void resetEnergyR(Node<Location> node) {
		boolean isWire = node.getBlockType().isWire();
		Wire w = null;
		boolean isElectrical = node.getBlockType().isElectrical();
		ElectricalBlock e = null;
		if (isWire) {
			w = (Wire) node.getBlockType();
			w.setInput(node.getData(), 0.0);
		}
		if (isElectrical) {
			e = (ElectricalBlock) node.getBlockType();
			e.setInput(node.getData(), 0.0);
			e.resetRecieved(node.getData());
		}
		for (Node<Location> n : node.getChildren()) {
			resetEnergyR(n);
		}
	}

	private void sendEnergyR(Node<Location> node, double newInput, boolean combined) {
		boolean isWire = node.getBlockType().isWire();
		Wire w = null;
		boolean isElectrical = node.getBlockType().isElectrical();
		ElectricalBlock e = null;
		boolean isGenerator = node.getBlockType().isGenerator();
		TemplarGenerator t = null;
		int children = node.getChildren().size();
		double output = 0.0;
		if (isWire) {
			w = (Wire) node.getBlockType();
			if (w.getOutput(node.getData()) == 0) {
				if (combined) {
					w.setInput(node.getData(), newInput);
				} else {
					w.setInput(node.getData(), w.getInput(node.getData()) + newInput);
				}
				w.setOutput(node.getData());
				return;
			} else {
				if (w.getInput(node.getData()) != 0) {
					if (combined) {
						w.setInput(node.getData(), newInput);
					} else {
						w.setInput(node.getData(), w.getInput(node.getData()) + newInput);
					}
					combined = true;
				} else {
					if (combined) {
						w.setInput(node.getData(), newInput);
					} else {
						w.setInput(node.getData(), w.getInput(node.getData()) + newInput);
					}
				}
				w.setOutput(node.getData());
				output = w.getOutput(node.getData());
			}
		}
		if (isElectrical) {
			e = (ElectricalBlock) node.getBlockType();
			if (combined && e.getRecieved(node.getData()).contains(node.getParent().getData())) {
				e.setInput(node.getData(), newInput);
			} else if (combined){
				e.setInput(node.getData(), e.getInputs(node.getData()) + newInput);
				e.addRecieved(node.getData(), node.getParent().getData());
			}
			else {
				e.setInput(node.getData(), e.getInputs(node.getData()) + newInput);
			}
		}
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
			sendEnergyR(n, outputPerChild, combined);
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
