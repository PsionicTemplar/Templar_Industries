package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Wire;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;
import net.md_5.bungee.api.ChatColor;

public class CopperWire extends TemplarBlock{

	public CopperWire(String name) {
		super(name, 0);
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
	protected void closeInventory(InventoryCloseEvent e, Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void openGui(Location l, Player p) {}

	@Override
	protected void inventoryClick(InventoryClickEvent e, Player p) {}

	@Override
	public void saveInventory(InventoryCloseEvent e, Player p) {}

	@Override
	public void onBlockPlace(BlockPlaceEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBlockBreak(BlockBreakEvent e) {}

}
