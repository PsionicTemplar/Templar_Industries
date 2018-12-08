package me.PsionicTemplar.templarIndustries.Recipes;

import org.bukkit.inventory.ItemStack;

public class RecipeObject {

	private RecipeType type;
	private ItemStack[] items;
	private ItemStack result;
	private boolean shapeless;
	
	/**
	 * Constructor for a Recipe.
	 * 
	 * @author Nicholas Braniff
	 * @param type
	 * @param items
	 * @param result
	 * @param isShapeless
	 */
	
	public RecipeObject(RecipeType type, ItemStack[] items, ItemStack result, boolean isShapeless) {
		this.type = type;
		this.items = items;
		this.result = result;
		this.shapeless = isShapeless;
	}
	
	/**
	 * Get the type of recipe.
	 * 
	 * @author Nicholas Braniff
	 * @return
	 */

	public RecipeType getType() {
		return type;
	}

	/**
	 * Return's the items used in the recipe.
	 * 
	 * @author Nicholas Braniff
	 * @return
	 */
	
	public ItemStack[] getItems() {
		return items;
	}
	
	/**
	 * Set's the items used in the recipe.
	 * 
	 * @author Nicholas Braniff
	 * @param items
	 * @return
	 */
	
	public RecipeObject setItems(ItemStack[] items){
		this.items = items;
		return this;
	}
	
	/**
	 * Returns the result of the recipe.
	 * 
	 * @author Nicholas Braniff
	 * @return
	 */

	public ItemStack getResult() {
		return result;
	}
	
	/**
	 * Sets the result of the recipe
	 * 
	 * @author Nicholas Braniff
	 * @param result
	 * @return
	 */
	
	public RecipeObject setResult(ItemStack result){
		this.result = result;
		return this;
	}
	
	/**
	 * Gets the item from a specific slot in the recipe.
	 * Goes from 0-8.
	 * 
	 * @author Nicholas Braniff
	 * @param slot
	 * @return
	 */
	
	public ItemStack getSlot(int slot){
		return items[slot];
	}
	
	/**
	 * Returns if recipe is a shapless recipe
	 * 
	 * @author Nicholas Braniff
	 * @return
	 */
	
	public boolean isShapeless(){
		return this.shapeless;
	}
	
	/**
	 * Set whether or not the recipe is shapless
	 * 
	 * @author Nicholas Braniff
	 * @param shapeless
	 * @return
	 */
	
	public RecipeObject setShapeless(boolean shapeless){
		this.shapeless = shapeless;
		return this;
	}
}
