package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;

public class Test extends TemplarBlock{

	public Test(String name) {
		super(name);
	}

	@Override
	protected ItemStack getItemStack() {
		ItemStack i = new ItemStack(Material.STONE);
		return i;
	}

	@Override
	protected void openGui() {
		
		
	}

}
