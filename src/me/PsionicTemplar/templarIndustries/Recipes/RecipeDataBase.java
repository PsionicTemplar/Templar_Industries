package me.PsionicTemplar.templarIndustries.Recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import me.PsionicTemplar.templarIndustries.Util.ItemStackCopy;

public class RecipeDataBase {

	public static HashMap<RecipeType, List<RecipeObject>> recipes = new HashMap<RecipeType, List<RecipeObject>>();
	
	/**
	 * Loads a recipe into memory.
	 * 
	 * @author Nicholas Braniff
	 * @param ro
	 */

	public static void loadRecipe(RecipeObject ro) {
		//Create a new list of recipes
		List<RecipeObject> r = new ArrayList<RecipeObject>();
		if (recipes.containsKey(ro.getType())) {
			//If the recipetype exists in the map, replace the newly create list with the value of the key.
			r = recipes.get(ro.getType());
		}
		//Add the recipe object to the list
		r.add(ro);
		//Put the list into the map with the key
		recipes.put(ro.getType(), r);
	}
	
	/**
	 * Traverses the loaded recipes with a result and returns the recipe.
	 * 
	 * @author Nicholas Braniff
	 * @param result
	 * @return
	 */

	
	//TODO This might need to return a list? 
	public static RecipeObject getRecipeObjectFromResult(ItemStack result) {
		for (RecipeType rt : recipes.keySet()) {
			for (RecipeObject ro : recipes.get(rt)) {
				if (ro.getResult().equals(ItemStackCopy.getItemStackCopy(result, ro.getResult().getAmount()))) {
					return ro;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns a matching recipe. If there is no recipe that matches, it returns null.
	 * Note: this is for shaped recipes only!
	 * 
	 * @author Nicholas Braniff
	 * @param ro
	 * @return
	 */

	public static RecipeObject getMatchingShapedRecipe(RecipeObject ro) {
		ItemStack[] items = ro.getItems();
		//Pull all recipes from the specific recipe type
		for (RecipeObject r : recipes.get(ro.getType())) {
			//Ignore if shapless
			if (r.isShapeless()) {
				continue;
			}
			int counter = 0;
			//Loop through all the items and see if each item from the given recipe matches the corresponding slot in "r"
			//If it fails, break out of the look and return null.
			//If all 9 iterations are okay, return r.
			for (ItemStack i : r.getItems()) {
				try {
					if(items[counter] == null && i == null){
						counter++;
						if (counter == 9) {
							return r;
						}
					}else{
						if (i.getAmount() > items[counter].getAmount()) {
							break;
						}
						if (!i.equals(ItemStackCopy.getItemStackCopy(items[counter], i.getAmount()))) {
							break;
						}
						counter++;
						if (counter == 9) {
							return r;
						}
					}
					
				} catch (NullPointerException ex) {
					break;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns a matching recipe. If there is no recipe that matches, it returns null.
	 * Note: this is for shapless recipes only!
	 * 
	 * @author Nicholas Braniff
	 * @param ro
	 * @return
	 */

	public static RecipeObject getMatchingShapelessRecipe(RecipeObject ro) {
		//Pull all recipes from a specific recipe type
		for (RecipeObject r : recipes.get(ro.getType())) {
			//Ignore if not shapeless
			if (!r.isShapeless()) {
				continue;
			}
			int howManyNull = 0;
			List<ItemStack> itemsRO = new ArrayList<ItemStack>();
			int howManyNullR = 0;
			List<ItemStack> itemsR = new ArrayList<ItemStack>();
			//Calculate how many null spaces are in the given recipe.
			//If a non null item is found, add it to the list.
			for (ItemStack i : ro.getItems()) {
				if (i == null) {
					howManyNull++;
				} else {
					itemsRO.add(ItemStackCopy.getItemStackCopy(i, i.getAmount()));
				}
			}
			//Calculate how many null spaces are in the for loop's recipe.
			//If a non null item is found, add it to the list.
			for (ItemStack i : r.getItems()) {
				if (i == null) {
					howManyNullR++;
				} else {
					itemsR.add(ItemStackCopy.getItemStackCopy(i, i.getAmount()));
				}
			}
			//If the number of null spaces in both counters don't match, continue.
			if (howManyNull != howManyNullR) {
				continue;
			}
			boolean itemFound = false;
			//Loop through all the items in the first list.
			//Inside that loop, loop through the second list.
			//See if any items match. If not, break.
			//If items do match, factor in amounts.
			//If the amount is greater, subtract the amounts
			//If less
			for (ItemStack i : itemsR) {
				itemFound = false;
				for (ItemStack ii : itemsRO) {
					if (i.equals(ItemStackCopy.getItemStackCopy(ii, i.getAmount()))) {
						if (i.getAmount() < ii.getAmount()) {
							ii.setAmount(ii.getAmount() - i.getAmount());
							itemFound = true;
							break;
						} else if (i.getAmount() == ii.getAmount()) {
							itemsRO.remove(ii);
							itemFound = true;
							break;
						} else {
							i.setAmount(i.getAmount() - ii.getAmount());
							itemsRO.remove(ii);
						}
					}
				}
				if (!itemFound) {
					break;
				}
			}
			if (!itemFound) {
				continue;
			}
			return r;
		}
		return null;
	}

}
