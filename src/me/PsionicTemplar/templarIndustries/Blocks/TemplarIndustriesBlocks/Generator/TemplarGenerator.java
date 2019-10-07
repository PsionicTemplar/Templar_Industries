package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Generator;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.PsionicTemplar.templarIndustries.Start;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Electrical.ElectricalBlock;
import me.PsionicTemplar.templarIndustries.Util.ItemStackCopy;

public abstract class TemplarGenerator extends TemplarBlock implements Listener {
	protected int input = 19;
	protected int charge = 25;
	protected int power = 13;
	protected int fire = 31;

	protected long fuelTicksLeft;
	protected long powerLevel;
	protected boolean isOn = false;

	protected HashMap<Location, Double> maxVoltage = new HashMap<Location, Double>();
	protected HashMap<Location, ElectricalBlock> users = new HashMap<Location, ElectricalBlock>();

	protected HashMap<ItemStack, Long> acceptedFuel;
	protected HashMap<ItemStack, Long> acceptedCharges;

	public TemplarGenerator(String name, int inventorySize) {
		super(name, inventorySize);
	}

	@Override
	protected void closeInventory(InventoryCloseEvent e, Player p) {
		HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		Inventory i = e.getInventory();
		for (int temp = 0; temp < this.inventorySize; temp++) {
			// Continue if the spot isn't one of the spots needed to be saved.
			if (temp != input && temp != charge) {
				continue;
			}
			// Continue if the spot doesn't contain an item.
			if (i.getItem(temp) == null) {
				continue;
			}
			// Continue if the spot doesn't contain an item.
			if (i.getItem(temp).getType() == Material.AIR) {
				continue;
			}
			// Put the item in the Hashmap
			items.put(temp, i.getItem(temp));
		}
		// Set the item map for the inventory/player so it can be saved. See called
		// method for more info.
		this.loadedBlocks.get(this.locations.get(this.inGui.get(p.getUniqueId()))).setItemMap(items);
	}

	@Override
	public void saveInventory(InventoryCloseEvent e, Player p) {
		// Make sure the player is in the gui
		if (!this.inGui.containsKey(p.getUniqueId())) {
			return;
		}
		HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		Inventory i = e.getInventory();
		// Loop through to put the items into the item map from the industrial workbench
		// inventory
		for (int temp = 0; temp < this.inventorySize; temp++) {
			// Make sure it is a storage/workbench/output slot
			if (temp != input && temp != charge) {
				continue;
			}
			// Make sure the item isn't null
			if (i.getItem(temp) == null) {
				continue;
			}
			// Make sure the item isn't null
			if (i.getItem(temp).getType() == Material.AIR) {
				continue;
			}
			// Put the item in the item map
			items.put(temp, i.getItem(temp));
		}
		// Set the item map and write it to memory. See the method for more details.
		this.loadedBlocks.get(this.locations.get(this.inGui.get(p.getUniqueId()))).setItemMap(items);
	}

	public void saveInventoryRemote(ItemStack in, ItemStack ch, Location l) {
		HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		items.put(input, in);
		items.put(charge, ch);
		// Set the item map and write it to memory. See the method for more details.
		this.loadedBlocks.get(locations.get(l)).setItemMap(items);
	}

	@Override
	protected void openGui(Location l, Player p) {
		// Create the inventory instance
		Inventory inv = Bukkit.createInventory(null, this.inventorySize, this.name);
		// Set the saved items in the Item Map into the inventory.
		for (int slot : this.loadedBlocks.get(this.locations.get(l)).getItemMap().keySet()) {
			inv.setItem(slot, this.loadedBlocks.get(this.locations.get(l)).getItemMap().get(slot));
		}
		// Set items for all the ambient items
		ItemStack none = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
		ItemMeta im = none.getItemMeta();
		im.setDisplayName("");
		none.setItemMeta(im);
		// Set all the blank spots to the blank item
		for (int i = 0; i < this.inventorySize; i++) {
			if (i == input || i == charge || i == power || i == fire) {
				continue;
			}
			inv.setItem(i, none);
		}
		// Open the inventory
		guiFinish(p, inv, input, charge, power, fire);
	}

	public abstract void guiFinish(Player p, Inventory inv, int input, int charge, int power, int fire);

	@Override
	protected void inventoryClick(InventoryClickEvent e, Player player) {
		if (!inGui.containsKey(player.getUniqueId())) {
			return;
		}
		if (e.getClickedInventory() == null) {
			return;
		}
		if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
			return;
		}
		if (e.getSlot() != input && e.getSlot() != charge) {
			e.setCancelled(true);
		} else {
			if (e.getCursor() == null || e.getCursor().getType() == Material.AIR) {
				return;
			}
			if (e.getSlot() == input) {
				e.setCancelled(true);
				for (ItemStack i : acceptedFuel.keySet()) {
					if (ItemStackCopy.getItemStackCopy(i, e.getCursor().getAmount()).equals(e.getCursor())) {
						e.setCancelled(false);
						Location l = this.inGui.get(player.getUniqueId());
						TemplarGenerator tg = this;
						new BukkitRunnable() {
							@Override
							public void run() {
								GeneratorTicker.addToActive(l, tg);
								GeneratorTicker.tickGenerators();
							}

						}.runTaskLater(Start.getPlugin(), 5);
					}
				}
			} else {
				e.setCancelled(true);
				for (ItemStack i : acceptedCharges.keySet()) {
					ItemStackCopy.getItemStackCopy(i);
					// TODO add code for batteries and what not
				}
			}
		}
	}

	public boolean tickGenerator(Location l) {
		return tick(l);
	}

	public abstract boolean tick(Location l);

	public void addUser(Location l, ElectricalBlock e) {
		users.put(l, e);
	}

	public void removeUser(Location l) {
		if (users.containsKey(l)) {
			users.remove(l);
		}
	}

	public HashMap<Location, ElectricalBlock> getUsers() {
		return this.users;
	}

	public double getMaxOutput(Location l) {
		return this.maxVoltage.get(l);
	}
	
	public void setPower(long powerLevel) {
		this.powerLevel = powerLevel;
	}

	public void setTicks(long ticks, Location l, TemplarGenerator tg) {
		this.fuelTicksLeft = ticks;
		GeneratorTicker.addToActive(l, tg);
		GeneratorTicker.tickGenerators();
	}
}
