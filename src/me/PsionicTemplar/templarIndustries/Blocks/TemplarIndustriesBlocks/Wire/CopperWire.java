package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Wire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.PsionicTemplar.templarIndustries.Start;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.TemplarCoalGenerator;
import net.md_5.bungee.api.ChatColor;

public class CopperWire extends TemplarBlock{
	
	private int voltage = 0;
	
	public CopperWire(String name) {
		super(name, 0);
		this.isWire = true;
	}

	@Override
	public ItemStack getItemStack() {
		ItemStack i = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "Copper Wire");
		im.setLore(Arrays.asList(ChatColor.GRAY + "Conducts 10 Tw/t", ChatColor.DARK_GRAY + "Templar Industries Wire"));
		i.setItemMeta(im);
		return i;
	}

	@Override
	protected void closeInventory(InventoryCloseEvent e, Player p) {}

	@Override
	protected void openGui(Location l, Player p) {}

	@Override
	protected void inventoryClick(InventoryClickEvent e, Player p) {}

	@Override
	public void saveInventory(InventoryCloseEvent e, Player p) {}

	@Override
	public void onBlockPlace(BlockPlaceEvent e) {
		
		Location l = e.getBlock().getLocation();
		List<Location> complete = new ArrayList<Location>();
		
		Location genLocation = getGeneratorLocation(complete, l);
		if (genLocation == null) {
			return;
		} else {
			TemplarCoalGenerator tcg = (TemplarCoalGenerator) Start.getBlock("Templar Coal Generator");
			tcg.createTree(genLocation);
		}
		
	}
	
	private Location getGeneratorLocation(List<Location> complete, Location l){
		Location newL = null;
		List<Location> possible = new ArrayList<Location>();
		possible.add(new Location(l.getWorld(), l.getX()+1, l.getY(), l.getZ()));
		possible.add(new Location(l.getWorld(), l.getX()-1, l.getY(), l.getZ()));
		possible.add(new Location(l.getWorld(), l.getX(), l.getY()+1, l.getZ()));
		possible.add(new Location(l.getWorld(), l.getX(), l.getY()-1, l.getZ()));
		possible.add(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()+1));
		possible.add(new Location(l.getWorld(), l.getX(), l.getY(), l.getZ()-1));
		
		for(Location ll : possible){
			if(Start.getBlock("Templar Coal Generator").getLocations().containsKey(ll)){
				return ll;
			}else if(Start.getBlock("Copper Wire").getLocations().containsKey(ll)){
				if(complete.contains(ll)){
					continue;
				}
				complete.add(l);
				newL = getGeneratorLocation(complete, ll);
				complete.remove(l);
				if(newL != null){
					break;
				}
			}
		}
		complete.add(l);
		return newL;
	}

	@Override
	public void onBlockBreak(BlockBreakEvent e) {}
	
	public void addVoltage(int amount){
		this.voltage += amount;
		if(this.voltage > 10){
			this.voltage = 10;
		}
	}
	
	public void takeVoltage(int amount){
		this.voltage -= amount;
		if(this.voltage < 0){
			this.voltage = 0;
		}
	}


}

