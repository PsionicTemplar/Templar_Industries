package me.PsionicTemplar.templarIndustries.Recipes.TemplarIndustriesRecipes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.PsionicTemplar.templarIndustries.Recipes.RecipeDataBase;
import me.PsionicTemplar.templarIndustries.Recipes.RecipeObject;
import me.PsionicTemplar.templarIndustries.Recipes.RecipeType;

public class TestRecipes {
	public static void load(){
		sampleShaped();
		sampleShapless();
	}
	
	private static void sampleShaped(){
		ItemStack a = new ItemStack(Material.STONE);
		ItemStack b = new ItemStack(Material.COBBLESTONE);
		ItemStack c = new ItemStack(Material.DIRT);
		ItemStack d = new ItemStack(Material.ARROW);
		ItemStack e = new ItemStack(Material.PUMPKIN_PIE);
		ItemStack f = new ItemStack(Material.GRASS);
		ItemStack g = new ItemStack(Material.DIAMOND_ORE);
		ItemStack h = new ItemStack(Material.DIAMOND);
		ItemStack i = new ItemStack(Material.APPLE);
		ItemStack result = new ItemStack(Material.NOTE_BLOCK);
		ItemStack[] items = {a, b, c, d, e, f, g, h, i};
		RecipeObject ro = new RecipeObject(RecipeType.INDUSTRIAL_WORKBENCH, items, result, false);
		RecipeDataBase.loadRecipe(ro);
	}
	
	private static void sampleShapless(){
		ItemStack a = new ItemStack(Material.STONE);
		ItemStack b = new ItemStack(Material.COBBLESTONE);
		ItemStack c = new ItemStack(Material.DIRT);
		ItemStack d = new ItemStack(Material.ARROW);
		ItemStack e = new ItemStack(Material.PUMPKIN_PIE);
		ItemStack f = new ItemStack(Material.GRASS);
		ItemStack g = new ItemStack(Material.DIAMOND_ORE);
		ItemStack h = new ItemStack(Material.DIAMOND);
		ItemStack i = new ItemStack(Material.IRON_INGOT);
		ItemStack result = new ItemStack(Material.GOLD_INGOT);
		ItemStack[] items = {a, b, c, d, e, f, g, h, i};
		RecipeObject ro = new RecipeObject(RecipeType.INDUSTRIAL_WORKBENCH, items, result, true);
		RecipeDataBase.loadRecipe(ro);
	}
}
