package me.PsionicTemplar.templarIndustries.Blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.PsionicTemplar.templarIndustries.ConfigObject;
import me.PsionicTemplar.templarIndustries.Start;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Generator.TemplarGenerator;
import me.PsionicTemplar.templarIndustries.Util.ItemStackCopy;

public abstract class TemplarBlock implements Listener {
	protected int id;
	protected List<Integer> openIdSlots = new ArrayList<Integer>();
	protected HashMap<Location, Integer> locations = new HashMap<Location, Integer>();
	protected HashMap<Integer, TemplarBlockObject> loadedBlocks = new HashMap<Integer, TemplarBlockObject>();
	protected HashMap<UUID, Location> inGui = new HashMap<UUID, Location>();
	protected ConfigObject co;
	protected String name;
	protected int inventorySize;
	
	protected boolean isWire;
	protected boolean isGenerator;
	protected boolean isElectrical;
	
	/**
	 * Constructor for a Templar Block. It takes in the name of a block and the inventory size you'd like
	 * for the type of block. It will then create a document with a Config Object add add the defaults to it.
	 * 
	 * Put 0 for unclickable.
	 * @param name
	 * @param inventorySize
	 * @author Nicholas Braniff
	 */
	
	public TemplarBlock(String name, int inventorySize) {
		//TODO Please add the ability to make it unclickable. See up above.
		
		//Setting the variables
		this.name = name;
		this.inventorySize = inventorySize;
		this.isWire = false;
		this.isGenerator = false;
		this.isElectrical = false;
		//Creating a new Map in order to store the information about the block.
		HashMap<String, Object> defaults = new HashMap<String, Object>();
		defaults.put("id", 0);
		defaults.put("open", new ArrayList<Integer>());
		String path = Start.getPlugin().getDataFolder() + "/Block_Data";
		String finalName = name + ".yml";
		//Creating a new FileConfiguration for the data to be stored on the hard drive.
		//See ConfigObject docmuentation for more information on this process.
		this.co = new ConfigObject(path, finalName, defaults);
		init();
	}
	
	/**
	 * Used on startup. Traces the FileConfiguration and loads needed information about the blocks into ram.
	 * 
	 * @author Nicholas Braniff
	 */

	protected void init() {
		//Retrieve the configuration from the ConfigObject.
		FileConfiguration config = co.getConfig();
		//Get the next available id
		this.id = config.getInt("id");
		//Start at ID 0 and go until the end
		for (int i = 0; i < this.id; i++) {
			if (!config.contains(i + "")) {
				//Continue if no information exists with the id
				continue;
			}
			//Get location information for the block
			Location l = new Location(Bukkit.getWorld(config.getString(i + ".world")), config.getInt(i + ".x"),
					config.getInt(i + ".y"), config.getInt(i + ".z"));
			//Load the owner
			UUID owner = UUID.fromString(config.getString(i + ".owner"));
			//Load anyone trusted.
			List<String> trusted = config.getStringList(i + ".trusted");
			List<UUID> finalTrusted = new ArrayList<UUID>();
			//Loading all those who are trusted into their UUID forms
			for (String s : trusted) {
				finalTrusted.add(UUID.fromString(s));
			}
			//Load in the currently used slots for the items
			List<Integer> itemSlots = config.getIntegerList(i + ".itemSlots");
			HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
			for (int t : itemSlots) {
				//Loop through and assign each used slot an item
				items.put(t, config.getItemStack(i + ".items." + t));
			}
			//Put all that information into a hashmap which can call the block from the block id.
			loadedBlocks.put(i, new TemplarBlockObject(this, i, l, owner, finalTrusted, items));
			//Put the location in a hashmap with the id so a block can be called using a location by getting the id.
			locations.put(l, i);
			if(this instanceof TemplarGenerator) {
				TemplarGenerator tg = (TemplarGenerator) this;
				tg.setPower(config.getLong(i + ".power"));
				tg.setTicks(config.getLong(i + ".ticks"), this.loadedBlocks.get(i).getLocation(), tg);
			}
		}
		onInit();
	}
	
	public abstract void onInit();
	
	/**
	 * Easy to use method for the blocks to increase the latest id by one and then write that to the fileconfiguration
	 * 
	 * @author Nicholas Braniff
	 */

	protected void increaseId() {
		this.id = this.id + 1;
		FileConfiguration config = co.getConfig();
		config.set("id", this.id);
		co.setConfigWrite(config);
	}
	
	/**
	 * Removing an Id from the list of open Id's
	 * 
	 * @author Nicholas Braniff
	 */

	protected void removeIdFromOpen() {
		try {
			this.openIdSlots.remove(0);
			FileConfiguration config = co.getConfig();
			config.set("open", this.openIdSlots);
			co.setConfigWrite(config);
		} catch (Exception ex) {
			return;
		}
	}
	
	/**
	 * Adding an Id to the list of reusable Id's
	 * 
	 * @author Nicholas Braniff
	 * @param id
	 */
	
	protected void addIdToOpen(int id){
		this.openIdSlots.add(id);
		FileConfiguration config = co.getConfig();
		config.set("open", this.openIdSlots);
		co.setConfigWrite(config);
	}
	
	/**
	 * Event for placing a block. Essentially it assigns the block an Id and adds it to the fileconfiguration to get saved.
	 * 
	 * @author Nicholas Braniff
	 * @param e
	 */

	@EventHandler
	public void addBlock(BlockPlaceEvent e) {
		//See if the item is the correct item or not
		if (!e.getItemInHand().equals(ItemStackCopy.getItemStackCopy(getItemStack(), e.getItemInHand().getAmount()))) {
			return;
		}
		FileConfiguration config = co.getConfig();
		Location l = e.getBlock().getLocation();
		UUID owner = e.getPlayer().getUniqueId();
		int tempId = 0;
		if (!this.openIdSlots.isEmpty()) {
			//Grab the first available Id
			tempId = this.openIdSlots.get(0);
			removeIdFromOpen();
		} else {
			//If no Id's are available, use the current Id and then increase it.
			tempId = this.id;
			increaseId();
		}
		//Put the information about the block into the config
		config.set(tempId + ".world", l.getWorld().getName());
		config.set(tempId + ".x", l.getBlockX());
		config.set(tempId + ".y", l.getBlockY());
		config.set(tempId + ".z", l.getBlockZ());
		config.set(tempId + ".owner", owner.toString());
		config.set(tempId + ".trusted", new ArrayList<String>());
		config.set(tempId + ".itemSlots", new ArrayList<Integer>());
		
		if(this instanceof TemplarGenerator) {
			config.set(tempId + ".ticks", 0L);
			config.set(tempId + ".power", 0L);
		}

		//Write the config to the harddrive and add the location and the block to the maps for later use.
		co.setConfigWrite(config);
		locations.put(l, tempId);
		loadedBlocks.put(tempId, new TemplarBlockObject(this, tempId, l, owner, new ArrayList<UUID>(),
				new HashMap<Integer, ItemStack>()));
		
		//Call the abstract method in case any class extending this class needs to add more to the block place event.
		onBlockPlace(e);
	}
	
	/**
	 * Abstract method used for extended classes in case they need to do anything else on the block place event.
	 * 
	 * @author Nicholas Braniff
	 * @param e
	 */
	
	public abstract void onBlockPlace(BlockPlaceEvent e);
	
	/**
	 * Remove Block Event.
	 * When a block is broken, it will remove it from the the config and the hashmaps. The used Id will be added to the open Id list.
	 * 
	 * @author Nicholas Braniff
	 * @param e
	 */

	@EventHandler
	public void removeBlock(BlockBreakEvent e) {
		//Verify the broken block's location is an actual location of one of the blocks.
		Location l = e.getBlock().getLocation();
		if (!locations.containsKey(l)) {
			return;
		}
		//Add Id to the openId list
		int tempId = locations.get(l);
		addIdToOpen(tempId);
		//Removing the block information from the config and writing it to the harddrive
		FileConfiguration config = co.getConfig();
		config.set(tempId + "", null);
		co.setConfigWrite(config);
		//Cancelling the event and then manually setting the block to air
		e.setCancelled(true);
		new BukkitRunnable() {

			@Override
			public void run() {
				l.getWorld().getBlockAt(l).setType(Material.AIR);
			}

		}.runTaskLater(Start.getPlugin(), 0);
		
		//Setting the location for the item drop; Dropping the correct item so it can be reused; removing the block from memory;

		new BukkitRunnable() {

			@Override
			public void run() {
				Location loc = new Location(l.getWorld(), l.getBlockX() + 0.5, l.getBlockY(), l.getBlockZ() + 0.5);
				loc.getWorld().dropItemNaturally(loc, getItemStack());
				for (int i : loadedBlocks.get(tempId).getItemMap().keySet()) {
					loc.getWorld().dropItemNaturally(loc, loadedBlocks.get(tempId).getItemMap().get(i));
				}
				loadedBlocks.remove(tempId);
				locations.remove(l);
			}

		}.runTaskLater(Start.getPlugin(), 5);
		//Calling the block break abstract method in case a class wants to add something to this event
		onBlockBreak(e);
	}
	
	/**
	 * Abstract method used for extended classes in case they need to do anything else on the block break event.
	 * 
	 * @author Nicholas Braniff
	 * @param e
	 */
	
	public abstract void onBlockBreak(BlockBreakEvent e);
	
	/**
	 * Abstract method for extended classes to define the item they'd like the block to be.
	 * 
	 * @author Nicholas Braniff
	 */

	public abstract ItemStack getItemStack();
	
	/**
	 * Right click event used to trigger the opening of the block's gui
	 * 
	 * @author Nicholas Braniff
	 * @param e
	 */

	@EventHandler
	public void onBlockInteract(PlayerInteractEvent e) {
		//Return if the person isn't right clicking the block, is crouching, or the block is a wire
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getPlayer().isSneaking() || this.isWire) {
			return;
		}
		//Verify the block's location matches an actual block's location
		if (!this.locations.containsKey(e.getClickedBlock().getLocation())) {
			return;
		}
		e.setCancelled(true);
		//Open the gui
		openGui(e.getClickedBlock().getLocation(), e.getPlayer());
		//Put the player in the gui hashmap to keep track of who is and isn't in the gui
		inGui.put(e.getPlayer().getUniqueId(), e.getClickedBlock().getLocation());
	}
	
	/**
	 * Event to remove the person from the gui hashmap once the inventory is closed. 
	 * 
	 * @author Nicholas Braniff
	 * @param e
	 */

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (!inGui.containsKey(e.getPlayer().getUniqueId())) {
			return;
		}
		closeInventory(e, Bukkit.getPlayer(e.getPlayer().getUniqueId()));
		inGui.remove(e.getPlayer().getUniqueId());
	}
	
	/**
	 * Abstract method called when inventory is closed so extended classes can manipulate it.
	 * 
	 * @author Nicholas Braniff
	 * @param e
	 * @param player
	 */

	protected abstract void closeInventory(InventoryCloseEvent e, Player player);
	
	/**
	 * Abstract method used so the extended classes can design the openable gui
	 * 
	 * @author Nicholas Braniff
	 * @param location
	 * @param player
	 */

	protected abstract void openGui(Location location, Player player);
	
	/**
	 * Inventory click event used to call the abstract method for inventory clicks.
	 * 
	 * @author Nicholas Braniff
	 * @param e
	 */
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		//Check if person is in gui
		if(!this.inGui.containsKey(e.getWhoClicked().getUniqueId())){
			return;
		}
		//Make sure the person is clicking inside of the inventory
		if(e.getClickedInventory() == null){
			return;
		}
		//Call the abstract method
		inventoryClick(e, Bukkit.getPlayer(e.getWhoClicked().getUniqueId()));
	}
	
	/**
	 * Abstract method used to let extended classes choose what happens when something is clicked in an inventory
	 * 
	 * @author Nicholas Braniff
	 * @param e
	 * @param player
	 */
	
	protected abstract void inventoryClick(InventoryClickEvent e, Player player);
	
	/**
	 * Event used to call the abstract method for saving the inventory and then removing the player from the inGui map.
	 * 
	 * @author Nicholas Braniff
	 * @param e
	 */
	
	@EventHandler
	public void inventoryCloseEvent(InventoryCloseEvent e) {
		if(!this.inGui.containsKey(e.getPlayer().getUniqueId())) {
			return;
		}
		this.saveInventory(e, Bukkit.getPlayer(e.getPlayer().getUniqueId()));
		this.inGui.remove(e.getPlayer().getUniqueId());
	}
	
	/**
	 * Abstract method for extended classes to add to what should happen when the inventory is closed.
	 * 
	 * @author Nicholas Braniff
	 * @param e
	 * @param p
	 */
	
	public abstract void saveInventory(InventoryCloseEvent e, Player p);
	
	/**
	 * Return if the block is a wire
	 * 
	 * @author Nicholas Braniff
	 * @return Boolean of whether it is a wire or not
	 */

	public boolean isWire() {
		return isWire;
	}
	
	/**
	 * Return if the block is a generator
	 * 
	 * @author Nicholas Braniff
	 * @return Boolean of whether is is a generator or not
	 */

	public boolean isGenerator() {
		return isGenerator;
	}
	
	/**
	 * Return if the block is electrical
	 * 
	 * @author Nicholas Braniff
	 * @return Boolean of whether it is electrical or not
	 */
	
	public boolean isElectrical(){
		return isElectrical;
	}
	
	/**
	 * Return the hashmap of locations
	 * 
	 * @author Nicholas Braniff
	 * @return Hashmap containing all the block locations.
	 */

	public HashMap<Location, Integer> getLocations() {
		return locations;
	}
	
}
