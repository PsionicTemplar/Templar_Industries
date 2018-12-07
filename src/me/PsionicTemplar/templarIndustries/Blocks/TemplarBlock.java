package me.PsionicTemplar.templarIndustries.Blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
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
		}
	}
	
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
	
	protected void addIdToOpen(int i){
		this.openIdSlots.add(i);
		FileConfiguration config = co.getConfig();
		config.set("open", this.openIdSlots);
		co.setConfigWrite(config);
	}

	@EventHandler
	public void addBlock(BlockPlaceEvent e) {
		if (!e.getItemInHand().equals(ItemStackCopy.getItemStackCopy(getBlockItem(), e.getItemInHand().getAmount()))) {
			return;
		}
		FileConfiguration config = co.getConfig();
		Location l = e.getBlock().getLocation();
		UUID owner = e.getPlayer().getUniqueId();
		int tempId = 0;
		if (!this.openIdSlots.isEmpty()) {
			tempId = this.openIdSlots.get(0);
			removeIdFromOpen();
		} else {
			tempId = this.id;
			increaseId();
		}
		config.set(tempId + ".world", l.getWorld().getName());
		config.set(tempId + ".x", l.getBlockX());
		config.set(tempId + ".y", l.getBlockY());
		config.set(tempId + ".z", l.getBlockZ());
		config.set(tempId + ".owner", owner.toString());
		config.set(tempId + ".trusted", new ArrayList<String>());
		config.set(tempId + ".itemSlots", new ArrayList<Integer>());

		co.setConfigWrite(config);
		locations.put(l, tempId);
		loadedBlocks.put(tempId, new TemplarBlockObject(this, tempId, l, owner, new ArrayList<UUID>(),
				new HashMap<Integer, ItemStack>()));
		onBlockPlace(e);
	}
	
	public abstract void onBlockPlace(BlockPlaceEvent e);

	@EventHandler
	public void removeBlock(BlockBreakEvent e) {
		Location l = e.getBlock().getLocation();
		if (!locations.containsKey(l)) {
			return;
		}
		int tempId = locations.get(l);
		addIdToOpen(tempId);
		FileConfiguration config = co.getConfig();
		config.set(tempId + "", null);
		co.setConfigWrite(config);
		e.setCancelled(true);
		new BukkitRunnable() {

			@Override
			public void run() {
				l.getWorld().getBlockAt(l).setType(Material.AIR);
			}

		}.runTaskLater(Start.getPlugin(), 0);

		new BukkitRunnable() {

			@Override
			public void run() {
				Location loc = new Location(l.getWorld(), l.getBlockX() + 0.5, l.getBlockY(), l.getBlockZ() + 0.5);
				loc.getWorld().dropItemNaturally(loc, getBlockItem());
				for (int i : loadedBlocks.get(tempId).getItemMap().keySet()) {
					loc.getWorld().dropItemNaturally(loc, loadedBlocks.get(tempId).getItemMap().get(i));
				}
				loadedBlocks.remove(tempId);
				locations.remove(l);
			}

		}.runTaskLater(Start.getPlugin(), 5);
		onBlockBreak(e);
	}
	
	public abstract void onBlockBreak(BlockBreakEvent e);

	public abstract ItemStack getItemStack();

	
	//Look at this bit of code and see if the utility will help reduce it
	public ItemStack getBlockItem() {
		ItemStack copyThis = getItemStack();
		ItemStack i = new ItemStack(copyThis.getType(), 1);
		i.setItemMeta(copyThis.getItemMeta());
		if (!copyThis.getEnchantments().isEmpty()) {
			for (Enchantment e : copyThis.getEnchantments().keySet()) {
				i.addUnsafeEnchantment(e, copyThis.getEnchantments().get(e));
			}
		}
		return i;
	}

	@EventHandler
	public void onBlockInteract(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getPlayer().isSneaking() || this.isWire) {
			return;
		}
		if (!this.locations.containsKey(e.getClickedBlock().getLocation())) {
			return;
		}
		e.setCancelled(true);
		openGui(e.getClickedBlock().getLocation(), e.getPlayer());
		inGui.put(e.getPlayer().getUniqueId(), e.getClickedBlock().getLocation());
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (!inGui.containsKey(e.getPlayer().getUniqueId())) {
			return;
		}
		closeInventory(e, Bukkit.getPlayer(e.getPlayer().getUniqueId()));
		inGui.remove(e.getPlayer().getUniqueId());
	}

	protected abstract void closeInventory(InventoryCloseEvent e, Player p);

	protected abstract void openGui(Location l, Player p);
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		if(!this.inGui.containsKey(e.getWhoClicked().getUniqueId())){
			return;
		}
		if(e.getClickedInventory() == null){
			return;
		}
		inventoryClick(e, Bukkit.getPlayer(e.getWhoClicked().getUniqueId()));
	}
	
	protected abstract void inventoryClick(InventoryClickEvent e, Player p);
	public abstract void saveInventory(InventoryCloseEvent e, Player p);

	public boolean isWire() {
		return isWire;
	}

	public boolean isGenerator() {
		return isGenerator;
	}
	
	public boolean isElectrical(){
		return isElectrical;
	}

	public HashMap<Location, Integer> getLocations() {
		return locations;
	}
}
