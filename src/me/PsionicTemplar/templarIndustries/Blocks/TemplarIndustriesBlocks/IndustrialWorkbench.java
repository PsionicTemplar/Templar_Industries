package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.PsionicTemplar.templarIndustries.ChatStrings;
import me.PsionicTemplar.templarIndustries.Blocks.TemplarBlock;
import me.PsionicTemplar.templarIndustries.Recipes.RecipeDataBase;
import me.PsionicTemplar.templarIndustries.Recipes.RecipeObject;
import me.PsionicTemplar.templarIndustries.Recipes.RecipeType;
import me.PsionicTemplar.templarIndustries.Util.ItemStackCopy;
import net.md_5.bungee.api.ChatColor;

public class IndustrialWorkbench extends TemplarBlock {

	private List<Integer> storage = Arrays.asList(45, 46, 47, 48, 49, 50, 51, 52, 53);
	private List<Integer> workbench = Arrays.asList(10, 11, 12, 19, 20, 21, 28, 29, 30);
	private int craftButton = 23;
	private int output = 25;

	/**
	 * Constructor for Industrial Workbench. 
	 * 
	 * @author Nicholas Braniff
	 * @param name
	 */
	
	public IndustrialWorkbench(String name) {
		super(name, 54);
	}
	
	@Override
	public ItemStack getItemStack() {
		ItemStack i = new ItemStack(Material.CRAFTING_TABLE);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8I&7n&6dustrial Workben&7c&8h"));
		im.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Templar Industries Core Block"));
		i.setItemMeta(im);
		return i;
	}

	@Override
	protected void closeInventory(InventoryCloseEvent e, Player p) {
		HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		Inventory i = e.getInventory();
		for (int temp = 0; temp < this.inventorySize; temp++) {
			//Continue if the spot isn't one of the spots needed to be saved.
			if (!storage.contains(temp) && !workbench.contains(temp) && temp != output) {
				continue;
			}
			//Continue if the spot doesn't contain an item.
			if (i.getItem(temp) == null) {
				continue;
			}
			//Continue if the spot doesn't contain an item.
			if (i.getItem(temp).getType() == Material.AIR) {
				continue;
			}
			//Put the item in the Hashmap
			items.put(temp, i.getItem(temp));
		}
		//Set the item map for the inventory/player so it can be saved. See called method for more info.
		this.loadedBlocks.get(this.locations.get(this.inGui.get(p.getUniqueId()))).setItemMap(items);
	}

	@Override
	protected void openGui(Location l, Player p) {
		//Create the inventory instance
		Inventory inv = Bukkit.createInventory(null, this.inventorySize, this.name);
		//Set the saved items in the Item Map into the inventory.
		for (int slot : this.loadedBlocks.get(this.locations.get(l)).getItemMap().keySet()) {
			inv.setItem(slot, this.loadedBlocks.get(this.locations.get(l)).getItemMap().get(slot));
		}
		//Set items for all the ambient items
		ItemStack none = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
		ItemMeta im = none.getItemMeta();
		im.setDisplayName("");
		none.setItemMeta(im);
		ItemStack craft = new ItemStack(Material.CRAFTING_TABLE);
		im = craft.getItemMeta();
		im.setDisplayName(ChatColor.GREEN + "Click Me to Craft");
		im.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Shift Left Click to Craft a Stack"));
		craft.setItemMeta(im);
		//Set all the blank spots to the blank item
		for (int i = 0; i < this.inventorySize; i++) {
			if (storage.contains(i) || workbench.contains(i) || i == output || i == craftButton) {
				continue;
			}
			inv.setItem(i, none);
		}
		//Set the crafting button
		inv.setItem(craftButton, craft);
		//Open the inventory
		p.openInventory(inv);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void inventoryClick(InventoryClickEvent e, Player p) {
		int howManyCrafted = 0;
		//Only if the inventory is a player inventory.
		if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
			if (!e.isShiftClick()) {
				return;
			}
			// Check to see if shift click and then if it is, put the item in a
			// slot that isn't the output or a spot that has an ambient item in it.
			e.setCancelled(true);
			Inventory inv = e.getWhoClicked().getOpenInventory().getTopInventory();
			//First, look at the workbench section
			for (int slot : workbench) {
				//Continue if the workbench slot is null
				if (inv.getItem(slot) == null) {
					continue;
				}
				//Continue if the item in the workbench slot is maxed out
				if (inv.getItem(slot).getAmount() == inv.getItem(slot).getMaxStackSize()) {
					continue;
				}
				//Continue if the item in the workbench does not equal the current item.
				if (!inv.getItem(slot)
						.equals(ItemStackCopy.getItemStackCopy(e.getCurrentItem(), inv.getItem(slot).getAmount()))) {
					continue;
				}
				//Calculate the total amount of items between the workbench
				int total = inv.getItem(slot).getAmount() + e.getCurrentItem().getAmount();
				int left = 0;
				//If the total is more than the max stack size of the item in the workbench, figure out what would be left
				if (total > inv.getItem(slot).getMaxStackSize()) {
					left = total - inv.getItem(slot).getMaxStackSize();
					total = inv.getItem(slot).getMaxStackSize();
				}
				//Give the item in the workbench the "total" amount
				inv.getItem(slot).setAmount(total);
				//Give the current item the amount of what's left
				e.getCurrentItem().setAmount(left);
				//If nothing is left, return.
				if (left == 0) {
					return;
				}
			}
			//Next, look at the storage section.
			for (int slot : storage) {
				//Continue if the storage slot is null
				if (inv.getItem(slot) == null) {
					continue;
				}
				//Continue if the storage slot is maxed out
				if (inv.getItem(slot).getAmount() == inv.getItem(slot).getMaxStackSize()) {
					continue;
				}
				//Continue if the storage slot does not equal the current item
				if (!inv.getItem(slot)
						.equals(ItemStackCopy.getItemStackCopy(e.getCurrentItem(), inv.getItem(slot).getAmount()))) {
					continue;
				}
				//Calculate the total amount of items between the storage
				int total = inv.getItem(slot).getAmount() + e.getCurrentItem().getAmount();
				int left = 0;
				//If the total is more than the max stack size of the item in the storage, figure out what would be left
				if (total > inv.getItem(slot).getMaxStackSize()) {
					left = total - inv.getItem(slot).getMaxStackSize();
					total = inv.getItem(slot).getMaxStackSize();
				}
				//Give the item in the storage the "total" amount
				inv.getItem(slot).setAmount(total);
				//Give the current item the amount of what's left
				e.getCurrentItem().setAmount(left);
				//If nothing is left, return.
				if (left == 0) {
					return;
				}
			}
			//Next, back to workbench to fill in null spacing
			for (int slot : workbench) {
				//If the space isn't null, continue
				if (inv.getItem(slot) != null) {
					continue;
				}
				//Set the space in the workbench to the current item. Set the current item amount to 0 to get rid of it.
				inv.setItem(slot, ItemStackCopy.getItemStackCopy(e.getCurrentItem(), e.getCurrentItem().getAmount()));
				e.getCurrentItem().setAmount(0);
				return;
			}
			//Finally, back to storage to fill in null spacing
			for (int slot : storage) {
				//If the space isn't null, continue
				if (inv.getItem(slot) != null) {
					continue;
				}
				//Set the space in the storage to the current item. Set the current item amount to 0 to get rid of it.
				inv.setItem(slot, ItemStackCopy.getItemStackCopy(e.getCurrentItem(), e.getCurrentItem().getAmount()));
				e.getCurrentItem().setAmount(0);
				return;
			}
			return;
		}
		//Now onto if the inventory is the workbench inventory.
		//Don't do anything if the player clicks in the storage/workbench spaces
		if (storage.contains(e.getSlot()) || workbench.contains(e.getSlot())) {
			return;
		}
		e.setCancelled(true);
		//Check to see if the craftbutton slot is the clicked slot.
		if (craftButton == e.getSlot()) {
			int counter = 0;
			ItemStack[] items = new ItemStack[9];
			for (int slot : workbench) {
				//Start to populate the item array with the items in the workbench slots
				if (e.getClickedInventory().getItem(slot) == null) {
					items[counter] = null;
				} else if (e.getClickedInventory().getItem(slot).getType() == Material.AIR) {
					items[counter] = null;
				} else {
					items[counter] = ItemStackCopy.getItemStackCopy(e.getClickedInventory().getItem(slot),
							e.getClickedInventory().getItem(slot).getAmount());
				}
				counter++;
			}
			//create the RecipeObject with the current items
			RecipeObject ro = new RecipeObject(RecipeType.INDUSTRIAL_WORKBENCH, items, null, false);
			//Match it with a shaped recipe
			RecipeObject matched = RecipeDataBase.getMatchingShapedRecipe(ro);
			if (matched == null) {
				//If it doesn't match, match it with a shapeless recipe
				matched = RecipeDataBase.getMatchingShapelessRecipe(ro);
				if (matched == null) {
					//If neither match, return
					e.getWhoClicked().sendMessage(ChatStrings.getError() + "That is not a recipe!");
					return;
				} else {
					try {
						//Check if the output matches the matched recipe's output
						if (!matched.getResult().equals(ItemStackCopy.getItemStackCopy(
								e.getClickedInventory().getItem(output), matched.getResult().getAmount()))) {
							e.getWhoClicked()
									.sendMessage(ChatStrings.getError() + "Please empty the output slot first...");
							return;
						}
						//Check to see if the combined amount between the current output and the recipe's output will go over the
						//output max stack size
						int resultAmount = e.getClickedInventory().getItem(output).getAmount()
								+ matched.getResult().getAmount();
						if (resultAmount > e.getClickedInventory().getItem(output).getMaxStackSize()) {
							e.getWhoClicked()
									.sendMessage(ChatStrings.getError() + "Please empty the output slot first...");
							return;
						}
					} catch (NullPointerException ex) {
					}
					int howManyCraftableTimes = 1;
					//If the crafting button is shift clicked
					if (e.isShiftClick()) {
						//If the output slot is empty
						if (e.getClickedInventory().getItem(output) == null) {
							//Set the total amount of craftable times
							howManyCraftableTimes = (int) (matched.getResult().getMaxStackSize()
									/ matched.getResult().getAmount());
						} else {
							//Set the total amount of craftable times according to how many items are already in the output slot
							howManyCraftableTimes = (int) ((e.getClickedInventory().getItem(output).getMaxStackSize()
									- e.getClickedInventory().getItem(output).getAmount()
											/ matched.getResult().getAmount()));
						}
					}
					//Loop through and construct a copy array of the items in the workbench
					for (int temp = 0; temp < howManyCraftableTimes; temp++) {
						ItemStack[] itemsInWorkbench = new ItemStack[9];
						int tt = 0;
						for (int in : workbench) {
							if (e.getClickedInventory().getItem(in) == null) {
								itemsInWorkbench[tt] = null;
							} else {
								itemsInWorkbench[tt] = ItemStackCopy.getItemStackCopy(
										e.getClickedInventory().getItem(in),
										e.getClickedInventory().getItem(in).getAmount());
							}

							tt++;
						}
						boolean doLoop = true;
						//Loop through the items in the workbench and start to remove the used items
						for (ItemStack falseI : matched.getItems()) {
							if (falseI == null) {
								continue;
							}
							ItemStack i = ItemStackCopy.getItemStackCopy(falseI, falseI.getAmount());
							//Loop through each slot in the workbench
							for (int count = 0; count < 9; count++) {
								//If the item is null, continue
								if (e.getClickedInventory().getItem(workbench.get(count)) == null) {
									continue;
								}
								//If the items don't equal, continue
								if (!i.equals(ItemStackCopy.getItemStackCopy(
										e.getClickedInventory().getItem(workbench.get(count)), i.getAmount()))) {
									continue;
								}
								//If the items in the recipe are less than the ones in the workbench, set the workbench items
								//to their amount minus the recipe item amount
								//Set the item to null and break
								if (i.getAmount() < e.getClickedInventory().getItem(workbench.get(count)).getAmount()) {
									e.getClickedInventory().getItem(workbench.get(count))
											.setAmount(e.getClickedInventory().getItem(workbench.get(count)).getAmount()
													- i.getAmount());
									i = null;
									break;
									//If the amounts are equal, set the workbench item to 0, i to null, and break.
								} else if (i.getAmount() == e.getClickedInventory().getItem(workbench.get(count))
										.getAmount()) {
									e.getClickedInventory().getItem(workbench.get(count)).setAmount(0);
									i = null;
									break;
									//If the recipe amount is greater than the workbench, set the recipe amount to its amount 
									//minus the workbench and set the workbench item's amount to 0
								} else {
									i.setAmount(i.getAmount()
											- e.getClickedInventory().getItem(workbench.get(count)).getAmount());
									e.getClickedInventory().getItem(workbench.get(count)).setAmount(0);
								}
							}
							//If the item still isn't null, break
							if (i != null) {
								doLoop = false;
								break;
							}
						}
						//If the item wasn't null, put the items back in the workbench like they were.
						if (!doLoop) {
							tt = 0;
							for (ItemStack i : itemsInWorkbench) {
								e.getClickedInventory().setItem(workbench.get(tt), i);
								tt++;
							}
							howManyCrafted = 0;
							break;
						}
						howManyCrafted++;
					}
				}
			} else {
				//Shaped check
				try {
					//Check if the output items match
					if (!matched.getResult().equals(ItemStackCopy.getItemStackCopy(
							e.getClickedInventory().getItem(output), matched.getResult().getAmount()))) {
						e.getWhoClicked().sendMessage(ChatStrings.getError() + "Please empty the output slot first...");
						return;
					}
					int resultAmount = e.getClickedInventory().getItem(output).getAmount()
							+ matched.getResult().getAmount();
					//Check to make sure the sum of both amounts don't go over the max stack size
					if (resultAmount > e.getClickedInventory().getItem(output).getMaxStackSize()) {
						e.getWhoClicked().sendMessage(ChatStrings.getError() + "Please empty the output slot first...");
						return;
					}
				} catch (NullPointerException ex) {
				}
				int howManyCraftableTimes = 1;
				//Get the amount of times an item is craftable
				if (e.isShiftClick()) {
					if (e.getClickedInventory().getItem(output) == null) {
						howManyCraftableTimes = (int) (matched.getResult().getMaxStackSize()
								/ matched.getResult().getAmount());
					} else {
						howManyCraftableTimes = (int) ((e.getClickedInventory().getItem(output).getMaxStackSize()
								- e.getClickedInventory().getItem(output).getAmount()
										/ matched.getResult().getAmount()));
					}
				}
				//Loop through as many times as craftable
				for (int t = 0; t < howManyCraftableTimes; t++) {
					ItemStack[] itemsInWorkbench = new ItemStack[9];
					int tt = 0;
					//Loop through to make a copy of the items in the workbench
					for (int in : workbench) {
						if (e.getClickedInventory().getItem(in) == null) {
							itemsInWorkbench[tt] = null;
						} else {
							itemsInWorkbench[tt] = ItemStackCopy.getItemStackCopy(
									e.getClickedInventory().getItem(in),
									e.getClickedInventory().getItem(in).getAmount());
						}
						tt++;
					}
					boolean doLoop = true;
					counter = 0;
					//Loop through and remove the items from the recipe from the workbench
					for (ItemStack i : matched.getItems()) {
						//If the item in the recipe is null or the workbench is null, continue
						if (i == null && e.getClickedInventory().getItem(workbench.get(counter)) == null) {
							counter++;
							continue;
						}
						try {
							//Subtract the amounts of the recipe and the workbench
							e.getClickedInventory().getItem(workbench.get(counter))
									.setAmount(e.getClickedInventory().getItem(workbench.get(counter)).getAmount()
											- i.getAmount());
						} catch (NullPointerException ex) {
							//break loop if there is a problem
							doLoop = false;
							break;
						}
						counter++;
					}
					if (!doLoop) {
						//reset the workbench if there was a problem
						tt = 0;
						for (ItemStack i : itemsInWorkbench) {
							e.getClickedInventory().setItem(workbench.get(tt), i);
							tt++;
						}
						howManyCrafted = 0;
						break;
					}
					howManyCrafted++;
				}

			}
			try {
				//set the amount of the output to its current amount plus the amount from how many crafted
				e.getClickedInventory().setItem(output,
						ItemStackCopy.getItemStackCopy(matched.getResult(),
								(matched.getResult().getAmount() * howManyCrafted)
										+ e.getClickedInventory().getItem(output).getAmount()));
			} catch (NullPointerException ex) {
				//If the output is null, just set it to how many crafted
				e.getClickedInventory().setItem(output, ItemStackCopy.getItemStackCopy(matched.getResult(),
						(matched.getResult().getAmount() * howManyCrafted)));
			}
		}
		//If the slot clicked is the output slot
		if (e.getSlot() == output) {
			//If the cursor doesn't have anything in it, put the output item into the cursor
			if (e.getCursor() == null) {
				e.setCursor(ItemStackCopy.getItemStackCopy(e.getCurrentItem(), e.getCurrentItem().getAmount()));
				e.getCurrentItem().setAmount(0);
				return;
			}
			//If the cursor doesn't have anything in it, put the output item into the cursor
			if (e.getCursor().getType() == Material.AIR) {
				e.setCursor(ItemStackCopy.getItemStackCopy(e.getCurrentItem(), e.getCurrentItem().getAmount()));
				e.getCurrentItem().setAmount(0);
				return;
			}
			//If the cursor doesn't match the output item, return
			if (!e.getCursor().equals(ItemStackCopy.getItemStackCopy(e.getCurrentItem(), e.getCursor().getAmount()))) {
				return;
			}
			//if the cursor matches the output item, do math to figure out how much can go into the cursor
			//set the cursor to the final amount (It may be the max stack size) and set the output to its final amount (it may be 0)
			int finalAmount = e.getCursor().getAmount() + e.getCurrentItem().getAmount();
			if (finalAmount > e.getCursor().getMaxStackSize()) {
				int sub = e.getCursor().getMaxStackSize() - e.getCursor().getAmount();
				e.getCurrentItem().setAmount(e.getCurrentItem().getAmount() - sub);
				e.getCursor().setAmount(e.getCursor().getMaxStackSize());
			} else {
				e.getCurrentItem().setAmount(0);
				e.getCursor().setAmount(finalAmount);
			}
		}
	}

	@Override
	public void saveInventory(InventoryCloseEvent e, Player p) {
		//Make sure the player is in the gui
		if (!this.inGui.containsKey(p.getUniqueId())) {
			return;
		}
		HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		Inventory i = e.getInventory();
		//Loop through to put the items into the item map from the industrial workbench inventory
		for (int temp = 0; temp < this.inventorySize; temp++) {
			//Make sure it is a storage/workbench/output slot
			if (!storage.contains(temp) && !workbench.contains(temp) && temp != output) {
				continue;
			}
			//Make sure the item isn't null
			if (i.getItem(temp) == null) {
				continue;
			}
			//Make sure the item isn't null
			if (i.getItem(temp).getType() == Material.AIR) {
				continue;
			}
			//Put the item in the item map
			items.put(temp, i.getItem(temp));
		}
		//Set the item map and write it to memory. See the method for more details.
		this.loadedBlocks.get(this.locations.get(this.inGui.get(p.getUniqueId()))).setItemMap(items);
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent e) {}

	@Override
	public void onBlockBreak(BlockBreakEvent e) {}

	@Override
	public void onInit() {
		// TODO Auto-generated method stub
		
	}

}
