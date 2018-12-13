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
	
	/**
	 * Constructor that takes in:
	 * Templar Block
	 * Id
	 * Location
	 * Owner's UUID
	 * List of trusted UUID
	 * and the map of items in the block currently.
	 * 
	 * @author Nicholas Braniff
	 * @param tb
	 * @param id
	 * @param location
	 * @param owner
	 * @param trusted
	 * @param items
	 */
	
	public TemplarBlockObject(TemplarBlock tb, int id, Location location, UUID owner, List<UUID> trusted, HashMap<Integer, ItemStack> items) {
		this.tb = tb;
		this.id = id;
		this.location = location;
		this.owner = owner;
		this.trusted = trusted;
		this.items = items;
	}
	
	/**
	 * Return's the Id of the block.
	 * 
	 * @author Nicholas Braniff
	 * @return
	 */

	public int getId(){
		return this.id;
	}
	
	/**
	 * Return's the owner's UUID of the block.
	 * 
	 * @author Nicholas Braniff
	 * @return
	 */
	
	public UUID getOwner() {
		return owner;
	}
	
	/**
	 * Set the owner of the block.
	 * 
	 * @author Nicholas Braniff
	 * @param owner
	 * @return
	 */

	public TemplarBlockObject setOwner(UUID owner) {
		this.owner = owner;
		FileConfiguration config = tb.co.getConfig();
		config.set(this.id + ".owner", owner);
		tb.co.setConfigWrite(config);
		return this;
	}
	
	/**
	 * Return the location of the block.
	 * 
	 * @author Nicholas Braniff
	 * @return
	 */

	public Location getLocation() {
		return location;
	}
	
	/**
	 * Return the list of trusted UUID to the block.
	 * 
	 * @author Nicholas Braniff
	 * @return
	 */

	public List<UUID> getTrusted() {
		return trusted;
	}
	
	/**
	 * Adds a user's UUID to the trusted list so they can access the block.
	 * 
	 * @author Nicholas Braniff
	 * @param uuid
	 * @return
	 */
	
	public TemplarBlockObject addTrusted(UUID uuid){
		this.trusted.add(uuid);
		FileConfiguration config = tb.co.getConfig();
		config.set(this.id + ".trusted", trusted);
		tb.co.setConfigWrite(config);
		return this;
	}
	
	/**
	 * Remove a user's UUID to the trusted list so they can't access the block.
	 * 
	 * @author Nicholas Braniff
	 * @param uuid
	 * @return
	 */
	
	public TemplarBlockObject removeTrusted(UUID uuid){
		this.trusted.remove(uuid);
		FileConfiguration config = tb.co.getConfig();
		config.set(this.id + ".trusted", trusted);
		tb.co.setConfigWrite(config);
		return this;
	}
	
	/**
	 * Remove all from the trusted list so only the owner can access it.
	 * 
	 * @author Nicholas Braniff
	 * @return
	 */
	
	public TemplarBlockObject resetTrusted(){
		this.trusted = new ArrayList<UUID>();
		FileConfiguration config = tb.co.getConfig();
		config.set(this.id + ".trusted", trusted);
		tb.co.setConfigWrite(config);
		return this;
	}
	
	/**
	 * Get the current items in the block.
	 * 
	 * @author Nicholas Braniff
	 * @return
	 */
	
	public HashMap<Integer, ItemStack> getItemMap(){
		return this.items;
	}
	
	/**
	 * Set the current items in the block.
	 * 
	 * @author Nicholas Braniff
	 * @param items
	 * @return
	 */
	
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
