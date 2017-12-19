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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.PsionicTemplar.templarIndustries.ConfigObject;
import me.PsionicTemplar.templarIndustries.Start;
import me.PsionicTemplar.templarIndustries.Util.ItemStackCopy;

public abstract class TemplarBlock implements Listener {
	protected int id;
	protected List<Integer> openIdSlots;
	protected HashMap<Location, Integer> locations = new HashMap<Location, Integer>();
	protected HashMap<Integer, TemplarBlockObject> loadedBlocks = new HashMap<Integer, TemplarBlockObject>();
	protected ConfigObject co;

	public TemplarBlock(String name) {
		HashMap<String, Object> defaults = new HashMap<String, Object>();
		defaults.put("id", 0);
		defaults.put("open", new ArrayList<Integer>());
		String path = Start.getPlugin().getDataFolder() + "/Block_Data";
		String finalName = name + ".yml";
		this.co = new ConfigObject(path, finalName, defaults);
		init();
	}

	protected void init() {
		FileConfiguration config = co.getConfig();
		this.id = config.getInt("id");
		for (int i = 0; i < this.id; i++) {
			if (!config.contains(this.id + "")) {
				continue;
			}
			Location l = new Location(Bukkit.getWorld(config.getString(i + ".world")), config.getInt(i + ".x"),
					config.getInt(i + ".y"), config.getInt(i + ".z"));
			UUID owner = UUID.fromString(config.getString(i + ".owner"));
			List<String> trusted = config.getStringList(i + ".trusted");
			List<UUID> finalTrusted = new ArrayList<UUID>();
			for (String s : trusted) {
				finalTrusted.add(UUID.fromString(s));
			}
			List<Integer> itemSlots = config.getIntegerList(i + ".itemSlots");
			HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
			for (int t : itemSlots) {
				items.put(t, config.getItemStack(i + ".items." + t));
			}
			loadedBlocks.put(i, new TemplarBlockObject(this, i, l, owner, finalTrusted, items));
			locations.put(l, i);
		}
	}

	protected void increaseId() {
		this.id = this.id + 1;
		FileConfiguration config = co.getConfig();
		config.set("id", this.id);
		co.setConfigWrite(config);
	}

	protected void removeIdFromOpen(int i) {
		try {
			this.openIdSlots.remove(i);
			FileConfiguration config = co.getConfig();
			config.set("open", this.openIdSlots);
			co.setConfigWrite(config);
		} catch (Exception ex) {
			return;
		}
	}

	@EventHandler
	public void addBlock(BlockPlaceEvent e) {
		if(!e.getItemInHand().equals(ItemStackCopy.getItemStackCopy(getBlockItem(), e.getItemInHand().getAmount()))){
			return;
		}
		FileConfiguration config = co.getConfig();
		Location l = e.getBlock().getLocation();
		UUID owner = e.getPlayer().getUniqueId();
		int tempId = 0;
		if (!this.openIdSlots.isEmpty()) {
			tempId = this.openIdSlots.get(0);
			removeIdFromOpen(tempId);
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
	}

	@EventHandler
	public void removeBlock(BlockBreakEvent e) {
		Location l = e.getBlock().getLocation();
		if (!locations.containsKey(l)) {
			return;
		}
		int tempId = locations.get(l);
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
				l.getWorld().dropItemNaturally(l, getBlockItem());
				for (int i : loadedBlocks.get(tempId).getItemMap().keySet()) {
					l.getWorld().dropItemNaturally(l, loadedBlocks.get(tempId).getItemMap().get(i));
				}
				loadedBlocks.remove(tempId);
				locations.remove(l);
			}

		}.runTaskLater(Start.getPlugin(), 5);
	}

	protected abstract ItemStack getItemStack();

	public ItemStack getBlockItem() {
		ItemStack copyThis = getItemStack();
		ItemStack i = new ItemStack(copyThis.getType(), 1, copyThis.getDurability());
		i.setItemMeta(copyThis.getItemMeta());
		if (!copyThis.getEnchantments().isEmpty()) {
			for (Enchantment e : copyThis.getEnchantments().keySet()) {
				i.addUnsafeEnchantment(e, copyThis.getEnchantments().get(e));
			}
		}
		return i;
	}
}