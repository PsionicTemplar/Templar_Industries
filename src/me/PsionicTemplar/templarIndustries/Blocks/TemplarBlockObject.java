package me.PsionicTemplar.templarIndustries.Blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public class TemplarBlockObject {
	private int id;
	private Location location;
	private UUID owner;
	private List<UUID> trusted;
	private HashMap<Integer, ItemStack> items;
	private TemplarBlock tb;
	
	public TemplarBlockObject(TemplarBlock tb, int id, Location location, UUID owner, List<UUID> trusted, HashMap<Integer, ItemStack> items) {
		this.tb = tb;
		this.id = id;
		this.location = location;
		this.owner = owner;
		this.trusted = trusted;
		this.items = items;
	}

	public int getId(){
		return this.id;
	}
	
	public UUID getOwner() {
		return owner;
	}

	public TemplarBlockObject setOwner(UUID owner) {
		this.owner = owner;
		FileConfiguration config = tb.co.getConfig();
		config.set(this.id + ".owner", owner);
		tb.co.setConfigWrite(config);
		return this;
	}

	public Location getLocation() {
		return location;
	}

	public List<UUID> getTrusted() {
		return trusted;
	}
	
	public TemplarBlockObject addTrusted(UUID uuid){
		this.trusted.add(uuid);
		FileConfiguration config = tb.co.getConfig();
		config.set(this.id + ".trusted", trusted);
		tb.co.setConfigWrite(config);
		return this;
	}
	
	public TemplarBlockObject removeTrusted(UUID uuid){
		this.trusted.remove(uuid);
		FileConfiguration config = tb.co.getConfig();
		config.set(this.id + ".trusted", trusted);
		tb.co.setConfigWrite(config);
		return this;
	}
	
	public TemplarBlockObject resetTrusted(){
		this.trusted = new ArrayList<UUID>();
		FileConfiguration config = tb.co.getConfig();
		config.set(this.id + ".trusted", trusted);
		tb.co.setConfigWrite(config);
		return this;
	}
	
	public HashMap<Integer, ItemStack> getItemMap(){
		return this.items;
	}
	
	public TemplarBlockObject setItemMap(HashMap<Integer, ItemStack> items){
		this.items = items;
		List<Integer> itemSlots = new ArrayList<Integer>(items.keySet());
		FileConfiguration config = tb.co.getConfig();
		config.set(this.id + ".itemSlots", itemSlots);
		config.set(this.id + ".items", null);
		for(int i : itemSlots){
			config.set(this.id + ".items." + i, items.get(i));
		}
		tb.co.setConfigWrite(config);
		return this;
	}
}
