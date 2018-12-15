package me.PsionicTemplar.templarIndustries.Recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import me.PsionicTemplar.templarIndustries.Util.ItemStackCopy;

public class RecipeDataBase {

	public static HashMap<RecipeType, List<RecipeObject>> recipes = new HashMap<RecipeType, List<RecipeObject>>();
	
	/**
	 * Loads a recipe into memory. If recipe already exists in the system, return false and kill the plugin.
	 * 
	 * @author Nicholas Braniff
	 * @param ro
	 * @return Boolean stating whether the recipe was loaded or not.
	 */

	public static boolean loadRecipe(RecipeObject ro) {
		if(getMatchingShapedRecipe(ro) != null || getMatchingShapelessRecipe(ro) != null) {
			return false;
		}
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
		return true;
	}
	
	/**
	 * Traverses the loaded recipes with a result and returns the recipe.
	 * 
	 * @author Nicholas Braniff
	 * @param result
	 * @return RecipeObject. If null, no recipe was found or more than one recipe was found.
	 */

	public static RecipeObject getRecipeObjectFromResult(ItemStack result) {
		List<RecipeObject> returnList = new ArrayList<RecipeObject>();
		for (RecipeType rt : recipes.keySet()) {
			for (RecipeObject ro : recipes.get(rt)) {
				if (ro.getResult().equals(ItemStackCopy.getItemStackCopy(result, ro.getResult().getAmount()))) {
					returnList.add(ro);
				}
			}
		}
		if(returnList.isEmpty() || returnList.size() > 1) {
			return null;
		}
		return returnList.get(0);
	}
	
	/**
	 * Returns a matching recipe. If there is no recipe that matches, it returns null.
	 * Note: this is for shaped recipes only!
	 * 
	 * @author Nicholas Braniff
	 * @param ro
	 * @return RecipeObject. If null, no recipe was found or more than one recipe was found.
	 */

	public static RecipeObject getMatchingShapedRecipe(RecipeObject ro) {
		ItemStack[] items = ro.getItems();
		List<RecipeObject> returnList = new ArrayList<RecipeObject>();
		//Check to see if the recipe arraylist is null
		if(recipes.get(ro.getType()) == null) {
			return null;
		}
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
							returnList.add(r);
						}
					}
					
				} catch (NullPointerException ex) {
					break;
				}
			}
		}
		if(returnList.isEmpty() || returnList.size() > 1) {
			return null;
		}
		return returnList.get(0);
	}
	
	/**
	 * Returns a matching recipe. If there is no recipe that matches, it returns null.
	 * Note: this is for shapless recipes only!
	 * 
	 * @author Nicholas Braniff
	 * @param ro
	 * @return RecipeObject. If null, no recipe was found or more than one recipe was found.
	 */

	public static RecipeObject getMatchingShapelessRecipe(RecipeObject ro) {
		List<RecipeObject> returnList = new ArrayList<RecipeObject>();
		//Check to see if the recipe arraylist is null
		if(recipes.get(ro.getType()) == null) {
			return null;
		}
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
			//See if any items match. If not, move on.
			//If items do match, factor in amounts.
			//If the amount is greater, subtract the amounts and set the item as found. Move on.
			//If equal, remove it from the list, set the item as found, and move on.
			//If less, we still need to search so we remove the item from the list, but keep going.
			//    Additionlly, we set the amount on the first item so we arn't looking for more than 
			//    we've already found.
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
			//If the final item is not found, we move onto the next recipeobject in the recipe database
			//Return the recipe if it fits
			returnList.add(r);
		}
		if(returnList.isEmpty() || returnList.size() > 1) {
			return null;
		} else {
			return returnList.get(0);
		}
	}

}
