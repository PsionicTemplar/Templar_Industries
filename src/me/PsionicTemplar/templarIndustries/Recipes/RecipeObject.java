package me.PsionicTemplar.templarIndustries.Recipes;

import org.bukkit.inventory.ItemStack;

public class RecipeObject {

	private RecipeType type;
	private ItemStack[] items;
	private ItemStack result;
	private boolean shapeless;
	
	public RecipeObject(RecipeType type, ItemStack[] items, ItemStack result, boolean isShapeless) {
		this.type = type;
		this.items = items;
		this.result = result;
		this.shapeless = isShapeless;
	}

	public RecipeType getType() {
		return type;
	}

	public ItemStack[] getItems() {
		return items;
	}
	
	public RecipeObject setItems(ItemStack[] items){
		this.items = items;
		return this;
	}

	public ItemStack getResult() {
		return result;
	}
	
	public RecipeObject setResult(ItemStack result){
		this.result = result;
		return this;
	}
	
	public ItemStack getSlot(int slot){
		return items[slot];
	}
	
	public boolean isShapeless(){
		return this.shapeless;
	}
	
	public RecipeObject setShapeless(boolean shapeless){
		this.shapeless = shapeless;
		return this;
	}
}
