package me.PsionicTemplar.templarIndustries.Recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import me.PsionicTemplar.templarIndustries.Util.ItemStackCopy;

public class RecipeDataBase {

	public static HashMap<RecipeType, List<RecipeObject>> recipes = new HashMap<RecipeType, List<RecipeObject>>();

	public static void loadRecipe(RecipeObject ro) {
		List<RecipeObject> r = new ArrayList<RecipeObject>();
		if (recipes.containsKey(ro.getType())) {
			r = recipes.get(ro.getType());
		}
		r.add(ro);
		recipes.put(ro.getType(), r);
	}

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

	public static RecipeObject getMatchingShapedRecipe(RecipeObject ro) {
		ItemStack[] items = ro.getItems();
		for (RecipeObject r : recipes.get(ro.getType())) {
			if (r.isShapeless()) {
				continue;
			}
			// for (int i = 0; i < 9; i++) {
			// items[i] = ItemStackCopy.getItemStackCopy(items[i],
			// r.getSlot(i).getAmount());
			// }
			// ro.setItems(items).setResult(r.getResult());
			int counter = 0;
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

	public static RecipeObject getMatchingShapelessRecipe(RecipeObject ro) {
		for (RecipeObject r : recipes.get(ro.getType())) {
			if (!r.isShapeless()) {
				continue;
			}
			int howManyNull = 0;
			List<ItemStack> itemsRO = new ArrayList<ItemStack>();
			int howManyNullR = 0;
			List<ItemStack> itemsR = new ArrayList<ItemStack>();
			if (howManyNull != howManyNullR) {
				continue;
			}
			for (ItemStack i : ro.getItems()) {
				if (i == null) {
					howManyNull++;
				} else {
					itemsRO.add(ItemStackCopy.getItemStackCopy(i, i.getAmount()));
				}
			}
			for (ItemStack i : r.getItems()) {
				if (i == null) {
					howManyNullR++;
				} else {
					itemsR.add(ItemStackCopy.getItemStackCopy(i, i.getAmount()));
				}
			}
			boolean itemFound = false;
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
