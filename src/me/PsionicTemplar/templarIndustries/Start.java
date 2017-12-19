package me.PsionicTemplar.templarIndustries;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class Start extends JavaPlugin{
	
	private static Start plugin; 
	
	public static Plugin getPlugin(){
		JavaPlugin plugin = Start.plugin;
		return plugin;
	}
	
	public void onEnable(){
		System.out.println("[Templar Industries] Starting Up!");
		
		try{
			
		}catch(Exception ex){
			System.out.println("[Templar Industries] There was an error with the startup. Please report this:");
			System.out.println("[Templar Industries] --------------------------------------------------------");
			ex.printStackTrace();
			System.out.println("[Templar Industries] --------------------------------------------------------");
			this.setEnabled(false);
		}
	}
	
	public void onDisable(){
		System.out.println("[Templar Industries] Shutting Down!");
		
		try{
			
		}catch(Exception ex){
			System.out.println("[Templar Industries] There was an error with the shutdown. Please report this:");
			System.out.println("[Templar Industries] --------------------------------------------------------");
			ex.printStackTrace();
			System.out.println("[Templar Industries] --------------------------------------------------------");
		}
	}

}
