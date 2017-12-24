package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Electrical;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class TestElectric extends ElectricalBlock{

	public TestElectric(String name) {
		super(name, 9);
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent e) {
		// TODO Auto-generated method stub
		
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
