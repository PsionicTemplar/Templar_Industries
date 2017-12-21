package me.PsionicTemplar.templarIndustries;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.IndustrialWorkbench;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Generator.TemplarCoalGenerator;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Wire.CopperWire;
import me.PsionicTemplar.templarIndustries.Recipes.TemplarIndustriesRecipes.TestRecipes;

public class Start extends JavaPlugin {

	private static Start plugin;

	private static HashMap<String, TemplarBlock> blocks;

	public static Plugin getPlugin() {
		JavaPlugin plugin = Start.plugin;
		return plugin;
	}

	public static Start getStart() {
		return plugin;
	}

	public void onEnable() {
		plugin = this;
		blocks = new HashMap<String, TemplarBlock>();

		try {
			this.getCommand("test").setExecutor(new CommandExtender());

			blocks.put("Industrial Workbench", new IndustrialWorkbench("Industrial Workbench"));
			blocks.put("Copper Wire", new CopperWire("Copper Wire"));
			blocks.put("Templar Coal Generator", new TemplarCoalGenerator("Templar Coal Generator"));
			loadEvents();

			TestRecipes.load();
		} catch (Exception ex) {
			System.out.println("[Templar Industries] There was an error with the startup. Please report this:");
			System.out.println("[Templar Industries] --------------------------------------------------------");
			ex.printStackTrace();
			System.out.println("[Templar Industries] --------------------------------------------------------");
			this.setEnabled(false);
		}

		plugin = this;
	}

	private void loadEvents() {
		for (String t : blocks.keySet()) {
			getServer().getPluginManager().registerEvents((Listener) blocks.get(t), this);
		}
	}

	public void onDisable() {

		try {
			for (Player p : Bukkit.getOnlinePlayers()) {
				for (TemplarBlock tb : blocks.values()) {
					tb.saveInventory(new InventoryCloseEvent(p.getOpenInventory()), p);
				}
				p.closeInventory();
			}
		} catch (Exception ex) {
			System.out.println("[Templar Industries] There was an error with the shutdown. Please report this:");
			System.out.println("[Templar Industries] --------------------------------------------------------");
			ex.printStackTrace();
			System.out.println("[Templar Industries] --------------------------------------------------------");
		}
	}

	public static TemplarBlock getBlock(String name) {
		try {
			return blocks.get(name);
		} catch (Exception ex) {
			return null;
		}
	}
	
	public static Collection<TemplarBlock> getBlocks(){
		return blocks.values();
	}

}
