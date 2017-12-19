package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

public class IndustrialWorkbench extends TemplarBlock {

	private List<Integer> storage = Arrays.asList(53, 52, 51, 50, 49, 48, 47, 46, 45);
	private List<Integer> workbench = Arrays.asList(10, 11, 12, 19, 20, 21, 28, 29, 30);
	private int craftButton = 23;
	private int output = 25;

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
		HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		Inventory i = e.getInventory();
		for(int temp = 0; temp < this.inventorySize; temp++){
			if(!storage.contains(temp) && !workbench.contains(temp) && temp != output){
				continue;
			}
			if(i.getItem(temp) == null){
				continue;
			}
			if(i.getItem(temp).getType() == Material.AIR){
				continue;
			}
			items.put(temp, i.getItem(temp));
		}
		this.loadedBlocks.get(this.locations.get(this.inGui.get(p.getUniqueId()))).setItemMap(items);
	}

	@Override
	protected void openGui(Location l, Player p) {
		Inventory inv = Bukkit.createInventory(null, this.inventorySize, this.name);
		for (int slot : this.loadedBlocks.get(this.locations.get(l)).getItemMap().keySet()) {
			inv.setItem(slot, this.loadedBlocks.get(this.locations.get(l)).getItemMap().get(slot));
		}
		ItemStack none = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		ItemMeta im = none.getItemMeta();
		im.setDisplayName("");
		none.setItemMeta(im);
		for(int i = 0; i < this.inventorySize; i++){
			if(storage.contains(i) || workbench.contains(i) || i == output || i == craftButton){
				continue;
			}
			inv.setItem(i, none);
		}
		p.openInventory(inv);
	}

	@Override
	protected void inventoryClick(InventoryClickEvent e, Player p) {
	}

}
