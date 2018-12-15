package me.PsionicTemplar.templarIndustries;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.IndustrialWorkbench;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Electrical.TestElectric;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Generator.TemplarCoalGenerator;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Wire.CopperWire;
import me.PsionicTemplar.templarIndustries.Recipes.RecipeDataBase;
import me.PsionicTemplar.templarIndustries.Recipes.RecipeObject;
import me.PsionicTemplar.templarIndustries.Recipes.TemplarIndustriesRecipes.TestRecipes;

public class Start extends JavaPlugin {

	private static Start plugin;

	private static HashMap<String, TemplarBlock> blocks;

	/**
	 * Return Static instance of plugin
	 * 
	 * @author Nicholas Braniff
	 * @return
	 */
	
	public static Plugin getPlugin() {
		JavaPlugin plugin = Start.plugin;
		return plugin;
	}
	
	/**
	 * Return Static instance of class
	 * 
	 * @author Nicholas Braniff
	 * @return
	 */
	
	public static Start getStart() {
		return plugin;
	}
	
	/**
	 * Run on enable
	 * 
	 * @author Nicholas Braniff
	 */

	public void onEnable() {
		plugin = this;
		blocks = new HashMap<String, TemplarBlock>();

		try {
			//Create Commands
			this.getCommand("test").setExecutor(new CommandExtender());

			//Load premade blocks into memory
			blocks.put("Industrial Workbench".toUpperCase().replaceAll(" ", "_"), new IndustrialWorkbench("Industrial Workbench"));
			blocks.put("Copper Wire".toUpperCase().replaceAll(" ", "_"), new CopperWire("Copper Wire"));
			blocks.put("Templar Coal Generator".toUpperCase().replaceAll(" ", "_"), new TemplarCoalGenerator("Templar Coal Generator"));
			blocks.put("Test".toUpperCase().replaceAll(" ", "_"), new TestElectric("Test"));
			
			//Load events
			loadEvents();

			//Load recipes
			loadRecipes();
		} catch (Exception ex) {
			//Crash Handler 
			System.out.println("[Templar Industries] There was an error with the startup. Please report this:");
			System.out.println("[Templar Industries] --------------------------------------------------------");
			ex.printStackTrace();
			System.out.println("[Templar Industries] --------------------------------------------------------");
			this.setEnabled(false);
		}

		plugin = this;
	}
	
	/**
	 * Loop through all the blocks and load their events
	 * 
	 * @author Nicholas Braniff
	 */

	private void loadEvents() {
		for (String t : blocks.keySet()) {
			getServer().getPluginManager().registerEvents((Listener) blocks.get(t), this);
		}
	}
	
	/**
	 * Base Recipe Loader
	 * 
	 * @author Nicholas Braniff
	 */
	
	private void loadRecipes() {
		TestRecipes.load();
	}
	
	/**
	 * Recipe Loader for other recipes. Fails if there is a duplicate recipe.
	 * 
	 * @author Nicholas Braniff
	 * @param ro
	 */
	
	public void addRecipe(RecipeObject ro) {
		if(!RecipeDataBase.loadRecipe(ro)) {
			System.out.println("[Templar Industries] This recipe overlaps another existing recipe.");
			int counter = 1;
			for(ItemStack i : ro.getItems()) {
				System.out.println("[Templar Industries] Slot " + counter + ": " + i.getType().name());
				counter++;
			}
			System.out.println("[Templar Industries] Result: " + ro.getResult().getType().name());
			this.setEnabled(false);
		}
	}
	
	/**
	 * Run on disable
	 * 
	 * @author Nicholas Braniff
	 */

	public void onDisable() {

		try {
			//Look through and save/close each online player's inventories if they are in a block
			for (Player p : Bukkit.getOnlinePlayers()) {
				for (TemplarBlock tb : blocks.values()) {
					tb.saveInventory(new InventoryCloseEvent(p.getOpenInventory()), p);
				}
				p.closeInventory();
			}
		} catch (Exception ex) {
			//Crash handler
			System.out.println("[Templar Industries] There was an error with the shutdown. Please report this:");
			System.out.println("[Templar Industries] --------------------------------------------------------");
			ex.printStackTrace();
			System.out.println("[Templar Industries] --------------------------------------------------------");
		}
	}
	
	/**
	 * Return the block instance of the name you give it. Each word should have a space.
	 * For Example: Copper Wire
	 * Don't put: CopperWire
	 * Acceptable: Copper_Wire or COPPER_WIRE
	 * 
	 * @author Nicholas Braniff
	 * @param name
	 * @return
	 */

	//TODO Make enum?
	public static TemplarBlock getBlock(String name) {
		name = name.toUpperCase().replaceAll(" ", "_");
		try {
			return blocks.get(name);
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * Return all loaded blocks
	 * 
	 * @author Nicholas Braniff
	 * @return
	 */
	
	public static Collection<TemplarBlock> getBlocks(){
		return blocks.values();
	}

}
