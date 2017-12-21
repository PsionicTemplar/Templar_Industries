package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Generator;

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

import net.md_5.bungee.api.ChatColor;

public class TemplarCoalGenerator extends TemplarGenerator {

	public TemplarCoalGenerator(String name) {
		super(name, 9);
		this.isGenerator = true;
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent e) {
		this.createTree(e.getBlock().getLocation());
	}

	

	@Override
	public void onBlockBreak(BlockBreakEvent e) {
		//TODO send a pulse through to stop electric flow from this generator through the tree


		this.trees.remove(e.getBlock().getLocation());
	}

	@Override
	public ItemStack getItemStack() {
		ItemStack i = new ItemStack(Material.FURNACE, 1);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&0C&8o&7a&fl Genera&7t&8o&0r"));
		im.setLore(Arrays.asList(ChatColor.GRAY + "Outputs 5 TW/t",ChatColor.DARK_GRAY + "Templar Industries Generator"));
		i.setItemMeta(im);
		return i;
	}

	@Override
	protected void closeInventory(InventoryCloseEvent e, Player p) {
		
	}

	@Override
	protected void openGui(Location l, Player p) {
		
	}

	@Override
	protected void inventoryClick(InventoryClickEvent e, Player p) {
		
	}

	@Override
	public void saveInventory(InventoryCloseEvent e, Player p) {
		
	}

}
