package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;
import net.md_5.bungee.api.ChatColor;

public class IndustrialWorkbench extends TemplarBlock{

	public IndustrialWorkbench(String name) {
		super(name, 54);
	}

	@Override
	public ItemStack getItemStack() {
		ItemStack i = new ItemStack(Material.WORKBENCH);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8I&7n&6dustrial Workben&7c&8h"));
		im.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Templar Industries Core Block"));
		i.setItemMeta(im);
		return i;
	}

	@Override
	protected void closeInventory(InventoryCloseEvent e, Player p) {
	}

	@Override
	protected void openGui(Location l, Player p) {
		Inventory inv = Bukkit.createInventory(null, this.inventorySize, this.name);
		for(int slot : this.loadedBlocks.get(this.locations.get(l)).getItemMap().keySet()){
			inv.setItem(slot, this.loadedBlocks.get(this.locations.get(l)).getItemMap().get(slot));
		}
		p.openInventory(inv);
	}

	@Override
	protected void inventoryClick(InventoryClickEvent e, Player p) {
	}

}
