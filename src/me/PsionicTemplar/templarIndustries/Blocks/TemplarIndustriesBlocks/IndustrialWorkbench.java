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

	public IndustrialWorkbench(String name) {
		super(name, 54);
	}

	@Override
	public ItemStack getItemStack() {
		ItemStack i = new ItemStack(Material.WORKBENCH);
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
			if (!storage.contains(temp) && !workbench.contains(temp) && temp != output) {
				continue;
			}
			if (i.getItem(temp) == null) {
				continue;
			}
			if (i.getItem(temp).getType() == Material.AIR) {
				continue;
			}
			items.put(temp, i.getItem(temp));
		}
		this.loadedBlocks.get(this.locations.get(this.inGui.get(p.getUniqueId()))).setItemMap(items);
	}

	@Override
	protected void openGui(Location l, Player p) {
		Inventory inv = Bukkit.createInventory(null, this.inventorySize, this.name);
		for (int slot : this.loadedBlocks.get(this.locations.get(l)).getItemMap().keySet()) {
			inv.setItem(slot, this.loadedBlocks.get(this.locations.get(l)).getItemMap().get(slot));
		}
		ItemStack none = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		ItemMeta im = none.getItemMeta();
		im.setDisplayName("");
		none.setItemMeta(im);
		ItemStack craft = new ItemStack(Material.WORKBENCH);
		im = craft.getItemMeta();
		im.setDisplayName(ChatColor.GREEN + "Click Me to Craft");
		im.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Shift Left Click to Craft a Stack"));
		craft.setItemMeta(im);
		for (int i = 0; i < this.inventorySize; i++) {
			if (storage.contains(i) || workbench.contains(i) || i == output || i == craftButton) {
				continue;
			}
			inv.setItem(i, none);
		}
		inv.setItem(craftButton, craft);
		p.openInventory(inv);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void inventoryClick(InventoryClickEvent e, Player p) {
		int howManyCrafted = 0;
		if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
			if (!e.isShiftClick()) {
				return;
			}
			// Check to see if shift click and then if it is, put the item in a
			// slot that isn't the output
			e.setCancelled(true);
			Inventory inv = e.getWhoClicked().getOpenInventory().getTopInventory();
			for (int slot : workbench) {
				if (inv.getItem(slot) == null) {
					continue;
				}
				if (inv.getItem(slot).getAmount() == inv.getItem(slot).getMaxStackSize()) {
					continue;
				}
				if (!inv.getItem(slot)
						.equals(ItemStackCopy.getItemStackCopy(e.getCurrentItem(), inv.getItem(slot).getAmount()))) {
					continue;
				}
				int total = inv.getItem(slot).getAmount() + e.getCurrentItem().getAmount();
				int left = 0;
				if (total > inv.getItem(slot).getMaxStackSize()) {
					left = total - inv.getItem(slot).getMaxStackSize();
					total = inv.getItem(slot).getMaxStackSize();
				}
				inv.getItem(slot).setAmount(total);
				e.getCurrentItem().setAmount(left);
				if (left == 0) {
					return;
				}
			}
			for (int slot : storage) {
				if (inv.getItem(slot) == null) {
					continue;
				}
				if (inv.getItem(slot).getAmount() == inv.getItem(slot).getMaxStackSize()) {
					continue;
				}
				if (!inv.getItem(slot)
						.equals(ItemStackCopy.getItemStackCopy(e.getCurrentItem(), inv.getItem(slot).getAmount()))) {
					continue;
				}
				int total = inv.getItem(slot).getAmount() + e.getCurrentItem().getAmount();
				int left = 0;
				if (total > inv.getItem(slot).getMaxStackSize()) {
					left = total - inv.getItem(slot).getMaxStackSize();
					total = inv.getItem(slot).getMaxStackSize();
				}
				inv.getItem(slot).setAmount(total);
				e.getCurrentItem().setAmount(left);
				if (left == 0) {
					return;
				}
			}
			for (int slot : workbench) {
				if (inv.getItem(slot) != null) {
					continue;
				}
				inv.setItem(slot, ItemStackCopy.getItemStackCopy(e.getCurrentItem(), e.getCurrentItem().getAmount()));
				e.getCurrentItem().setAmount(0);
				return;
			}
			for (int slot : storage) {
				if (inv.getItem(slot) != null) {
					continue;
				}
				inv.setItem(slot, ItemStackCopy.getItemStackCopy(e.getCurrentItem(), e.getCurrentItem().getAmount()));
				e.getCurrentItem().setAmount(0);
				return;
			}
			return;
		}
		if (storage.contains(e.getSlot()) || workbench.contains(e.getSlot())) {
			return;
		}
		e.setCancelled(true);
		if (craftButton == e.getSlot()) {
			int counter = 0;
			ItemStack[] items = new ItemStack[9];
			for (int slot : workbench) {
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
			RecipeObject ro = new RecipeObject(RecipeType.INDUSTRIAL_WORKBENCH, items, null, false);
			RecipeObject matched = RecipeDataBase.getMatchingShapedRecipe(ro);
			if (matched == null) {
				matched = RecipeDataBase.getMatchingShapelessRecipe(ro);
				if (matched == null) {
					e.getWhoClicked().sendMessage(ChatStrings.getError() + "That is not a recipe!");
					return;
				} else {
					try {
						if (!matched.getResult().equals(ItemStackCopy.getItemStackCopy(
								e.getClickedInventory().getItem(output), matched.getResult().getAmount()))) {
							e.getWhoClicked()
									.sendMessage(ChatStrings.getError() + "Please empty the output slot first...");
							return;
						}
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
						for (ItemStack falseI : matched.getItems()) {
							if (falseI == null) {
								continue;
							}
							ItemStack i = ItemStackCopy.getItemStackCopy(falseI, falseI.getAmount());
							for (int count = 0; count < 9; count++) {
								if (e.getClickedInventory().getItem(workbench.get(count)) == null) {
									continue;
								}
								if (!i.equals(ItemStackCopy.getItemStackCopy(
										e.getClickedInventory().getItem(workbench.get(count)), i.getAmount()))) {
									continue;
								}
								if (i.getAmount() < e.getClickedInventory().getItem(workbench.get(count)).getAmount()) {
									e.getClickedInventory().getItem(workbench.get(count))
											.setAmount(e.getClickedInventory().getItem(workbench.get(count)).getAmount()
													- i.getAmount());
									i = null;
									break;
								} else if (i.getAmount() == e.getClickedInventory().getItem(workbench.get(count))
										.getAmount()) {
									e.getClickedInventory().getItem(workbench.get(count)).setAmount(0);
									i = null;
									break;
								} else {
									i.setAmount(i.getAmount()
											- e.getClickedInventory().getItem(workbench.get(count)).getAmount());
									e.getClickedInventory().getItem(workbench.get(count)).setAmount(0);
								}
							}
							if (i != null) {
								doLoop = false;
								break;
							}
						}
						if (!doLoop) {
							tt = 0;
							for (ItemStack i : itemsInWorkbench) {
								e.getClickedInventory().setItem(workbench.get(tt), i);
								tt++;
							}
							break;
						}
						howManyCrafted++;
					}
				}
			} else {
				try {
					if (!matched.getResult().equals(ItemStackCopy.getItemStackCopy(
							e.getClickedInventory().getItem(output), matched.getResult().getAmount()))) {
						e.getWhoClicked().sendMessage(ChatStrings.getError() + "Please empty the output slot first...");
						return;
					}
					int resultAmount = e.getClickedInventory().getItem(output).getAmount()
							+ matched.getResult().getAmount();
					if (resultAmount > e.getClickedInventory().getItem(output).getMaxStackSize()) {
						e.getWhoClicked().sendMessage(ChatStrings.getError() + "Please empty the output slot first...");
						return;
					}
				} catch (NullPointerException ex) {
				}
				int howManyCraftableTimes = 1;
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
				for (int t = 0; t < howManyCraftableTimes; t++) {
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
					counter = 0;
					for (ItemStack i : matched.getItems()) {
						if (i == null && e.getClickedInventory().getItem(workbench.get(counter)) == null) {
							counter++;
							continue;
						}
						try {
							e.getClickedInventory().getItem(workbench.get(counter))
									.setAmount(e.getClickedInventory().getItem(workbench.get(counter)).getAmount()
											- i.getAmount());
						} catch (NullPointerException ex) {
							doLoop = false;
							break;
						}
						counter++;
					}
					if (!doLoop) {
						tt = 0;
						for (ItemStack i : itemsInWorkbench) {
							e.getClickedInventory().setItem(workbench.get(tt), i);
							tt++;
						}
						break;
					}
					howManyCrafted++;
				}

			}
			try {
				e.getClickedInventory().setItem(output,
						ItemStackCopy.getItemStackCopy(matched.getResult(),
								(matched.getResult().getAmount() * howManyCrafted)
										+ e.getClickedInventory().getItem(output).getAmount()));
			} catch (NullPointerException ex) {
				e.getClickedInventory().setItem(output, ItemStackCopy.getItemStackCopy(matched.getResult(),
						(matched.getResult().getAmount() * howManyCrafted)));
			}
		}
		if (e.getSlot() == output) {
			if (e.getCursor() == null) {
				e.setCursor(ItemStackCopy.getItemStackCopy(e.getCurrentItem(), e.getCurrentItem().getAmount()));
				e.getCurrentItem().setAmount(0);
				return;
			}
			if (e.getCursor().getType() == Material.AIR) {
				e.setCursor(ItemStackCopy.getItemStackCopy(e.getCurrentItem(), e.getCurrentItem().getAmount()));
				e.getCurrentItem().setAmount(0);
				return;
			}
			if (!e.getCursor().equals(ItemStackCopy.getItemStackCopy(e.getCurrentItem(), e.getCursor().getAmount()))) {
				return;
			}
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
		if (!this.inGui.containsKey(p.getUniqueId())) {
			return;
		}
		HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		Inventory i = e.getInventory();
		for (int temp = 0; temp < this.inventorySize; temp++) {
			if (!storage.contains(temp) && !workbench.contains(temp) && temp != output) {
				continue;
			}
			if (i.getItem(temp) == null) {
				continue;
			}
			if (i.getItem(temp).getType() == Material.AIR) {
				continue;
			}
			items.put(temp, i.getItem(temp));
		}
		this.loadedBlocks.get(this.locations.get(this.inGui.get(p.getUniqueId()))).setItemMap(items);
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent e) {}

	@Override
	public void onBlockBreak(BlockBreakEvent e) {}

}
