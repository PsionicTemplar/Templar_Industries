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
import me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Generator.TemplarGenerator;
import net.md_5.bungee.api.ChatColor;

public class CopperWire extends Wire{
	
	public CopperWire(String name) {
		super(name, 0, 10.0);
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
}

