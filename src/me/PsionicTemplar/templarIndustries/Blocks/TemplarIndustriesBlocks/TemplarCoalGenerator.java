package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import me.PsionicTemplar.templarIndustries.Start;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;
import me.PsionicTemplar.templarIndustries.Util.TemplarTree;
import me.PsionicTemplar.templarIndustries.Util.TemplarTree.Node;

public class TemplarCoalGenerator extends TemplarBlock {

	private HashMap<Location, TemplarTree<Location>> trees = new HashMap<Location, TemplarTree<Location>>();

	public TemplarCoalGenerator(String name) {
		super(name, 9);
		this.isGenerator = true;
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent e) {
		// TODO Auto-generated method stub

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

	@Override
	public void onBlockBreak(BlockBreakEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public ItemStack getItemStack() {
		// TODO Auto-generated method stub
		return new ItemStack(Material.STONE);
	}

	@Override
	protected void closeInventory(InventoryCloseEvent e, Player p) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void openGui(Location l, Player p) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void inventoryClick(InventoryClickEvent e, Player p) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveInventory(InventoryCloseEvent e, Player p) {
		// TODO Auto-generated method stub

	}

}
