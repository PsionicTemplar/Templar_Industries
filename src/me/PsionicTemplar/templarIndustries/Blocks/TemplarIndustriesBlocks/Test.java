package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;

public class Test extends TemplarBlock{

	public Test(String name) {
		super(name, 9);
	}

	@Override
	protected ItemStack getItemStack() {
		ItemStack i = new ItemStack(Material.STONE);
		return i;
	}

	@Override
	protected void openGui(Location l, Player p) {
		Inventory i = Bukkit.createInventory(null, 9, this.name);
		for(int slot : this.loadedBlocks.get(this.locations.get(l)).getItemMap().keySet()){
			i.setItem(slot, this.loadedBlocks.get(this.locations.get(l)).getItemMap().get(slot));
		}
		p.openInventory(i);
	}

	@Override
	protected void closeInventory(Inventory i, Player p) {
		HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		for(int temp = 0; temp < this.inventorySize; temp++){
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

}
