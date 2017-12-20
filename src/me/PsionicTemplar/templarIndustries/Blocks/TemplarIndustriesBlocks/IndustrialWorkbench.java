package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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

	@Override
	protected void inventoryClick(InventoryClickEvent e, Player p) {
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
					e.getWhoClicked().sendMessage(ChatStrings.getError() + "That is not a recipe");
					return;
				} else {
					for (ItemStack i : matched.getItems()) {
						for (int count = 0; count < 9; count++) {
							try {
								if (!i.equals(ItemStackCopy.getItemStackCopy(
										e.getClickedInventory().getItem(workbench.get(count)), i.getAmount()))){
									continue;
								}
									if (i.getAmount() < e.getClickedInventory().getItem(workbench.get(count))
											.getAmount()) {
										e.getClickedInventory().getItem(workbench.get(count)).setAmount(
												e.getClickedInventory().getItem(workbench.get(count)).getAmount()
														- i.getAmount());
									} else if (i.getAmount() == e.getClickedInventory().getItem(workbench.get(count))
											.getAmount()) {
										e.getClickedInventory().getItem(workbench.get(count)).setAmount(0);
										break;
									} else {
										i.setAmount(i.getAmount()
												- e.getClickedInventory().getItem(workbench.get(count)).getAmount());
										e.getClickedInventory().getItem(workbench.get(count)).setAmount(0);
									}
							} catch (Exception ex) {
								continue;
							}
						}
					}
				}
			} else {
				counter = 0;
				for (ItemStack i : matched.getItems()) {
					e.getClickedInventory().getItem(workbench.get(counter)).setAmount(
							e.getClickedInventory().getItem(workbench.get(counter)).getAmount() - i.getAmount());
					counter++;
				}
			}
			e.getClickedInventory().setItem(output, matched.getResult());

		}
	}

}
