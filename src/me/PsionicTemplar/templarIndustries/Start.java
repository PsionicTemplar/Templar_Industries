package me.PsionicTemplar.templarIndustries;

import java.util.HashMap;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.IndustrialWorkbench;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Test;
import me.PsionicTemplar.templarIndustries.Recipes.TemplarIndustriesRecipes.TestRecipes;



public class Start extends JavaPlugin{
	
	private static Start plugin; 
	
	private static HashMap<String, TemplarBlock> blocks;
	
	public static Plugin getPlugin(){
		JavaPlugin plugin = Start.plugin;
		return plugin;
	}
	
	public static Start getStart(){
		return plugin;
	}
	
	public void onEnable(){
		plugin = this;
		blocks = new HashMap<String, TemplarBlock>();
		
		try{
			this.getCommand("test").setExecutor(new CommandExtender());
			
			blocks.put("Test Block", new Test("Test Block"));
			blocks.put("Industrial Workbench", new IndustrialWorkbench("Industrial Workbench"));
			loadEvents();
			
			TestRecipes.load();
		}catch(Exception ex){
			System.out.println("[Templar Industries] There was an error with the startup. Please report this:");
			System.out.println("[Templar Industries] --------------------------------------------------------");
			ex.printStackTrace();
			System.out.println("[Templar Industries] --------------------------------------------------------");
			this.setEnabled(false);
		}

		plugin = this;
	}
	
	private void loadEvents(){
		for(String t : blocks.keySet()){
			getServer().getPluginManager().registerEvents((Listener) blocks.get(t), this);
		}
	}
	
	public void onDisable(){
		
		try{
			
		}catch(Exception ex){
			System.out.println("[Templar Industries] There was an error with the shutdown. Please report this:");
			System.out.println("[Templar Industries] --------------------------------------------------------");
			ex.printStackTrace();
			System.out.println("[Templar Industries] --------------------------------------------------------");
		}
	}
	
	public static TemplarBlock getBlock(String name){
		try{
			return blocks.get(name);
		}catch(Exception ex){
			return null;
		}
	}

}
