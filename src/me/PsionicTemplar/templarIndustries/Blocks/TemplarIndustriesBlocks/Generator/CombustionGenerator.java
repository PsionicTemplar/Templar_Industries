package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Generator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.PsionicTemplar.templarIndustries.Util.ItemStackCopy;
import net.md_5.bungee.api.ChatColor;

public class CombustionGenerator extends TemplarGenerator {

	public CombustionGenerator(String name, int inventorySize) {
		super(name, inventorySize);
	}

	@Override
	public void guiFinish(Player p, Inventory inv, int input, int charge, int power, int fire) {
		ItemStack pow = new ItemStack(Material.REDSTONE);
		ItemMeta im = pow.getItemMeta();
		im.setDisplayName(ChatColor.DARK_RED + "Power Level");
		im.setLore(Arrays.asList(ChatColor.RED + "" + this.powerLevel + " Tw"));
		pow.setItemMeta(im);
		ItemStack fir;
		if (isOn) {
			fir = new ItemStack(Material.BLAZE_ROD);
			im = fir.getItemMeta();
			im.setDisplayName(ChatColor.GOLD + "Power Generation: " + ChatColor.GREEN + "Active");
			im.setLore(Arrays.asList(
					ChatColor.YELLOW + "" + this.fuelTicksLeft + ChatColor.GOLD + " ticks left until depletion"));
			fir.setItemMeta(im);
		} else {
			fir = new ItemStack(Material.STICK);
			im = fir.getItemMeta();
			im.setDisplayName(ChatColor.GOLD + "Power Generation: " + ChatColor.DARK_RED + "Not Active");
			fir.setItemMeta(im);
		}

		inv.setItem(power, pow);
		inv.setItem(fire, fir);
		p.openInventory(inv);
	}

	@Override
	public void onInit() {
		this.acceptedFuel = new HashMap<ItemStack, Long>();
		this.acceptedCharges = new HashMap<ItemStack, Long>();
		this.acceptedFuel.put(new ItemStack(Material.COAL, 1), 1600L);
		this.acceptedFuel.put(new ItemStack(Material.COAL_BLOCK, 1), 16000L);
		this.acceptedFuel.put(new ItemStack(Material.CHARCOAL, 1), 1600L);
		this.acceptedFuel.put(new ItemStack(Material.BLAZE_ROD, 1), 2400L);
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBlockBreak(BlockBreakEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public ItemStack getItemStack() {
		ItemStack i = new ItemStack(Material.FURNACE);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8Co&7mb&fus&7ti&8on &8G&7en&fera&7to&8r"));
		im.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Templar Industries Electrical Block"));
		i.setItemMeta(im);
		return i;
	}

	@Override
	public boolean tick(Location l) {
		boolean validFuel = false;
		Inventory inv = updateOpenInventory(l, 0);
		ItemStack in;
		ItemStack ch;
		if(this.fuelTicksLeft != 0L) {
			this.isOn = true;
		}
		if (inv == null) {
			in = this.loadedBlocks.get(this.locations.get(l)).getItemMap().get(this.input);
			if (in != null) {
				in = ItemStackCopy.getItemStackCopy(in, in.getAmount());
			}

			ch = this.loadedBlocks.get(this.locations.get(l)).getItemMap().get(this.charge);
			if (ch != null) {
				ch = ItemStackCopy.getItemStackCopy(ch, ch.getAmount());
			}
		}else {
			in = inv.getItem(this.input);
			if (in != null) {
				in = ItemStackCopy.getItemStackCopy(in, in.getAmount());
			}

			ch = inv.getItem(this.charge);
			if (ch != null) {
				ch = ItemStackCopy.getItemStackCopy(ch, ch.getAmount());
			}
		}
		if (this.fuelTicksLeft == 0L) {
			if (in == null || in.getType() == Material.AIR) {
			} else {
				for (ItemStack i : acceptedFuel.keySet()) {
					if (ItemStackCopy.getItemStackCopy(i, in.getAmount()).equals(in)) {
						validFuel = true;
						break;
					}
				}
			}
			if (!validFuel) {
				this.isOn = false;
				return false;
			} else {
				this.isOn = true;
				long ticks = this.acceptedFuel.get(ItemStackCopy.getItemStackCopy(in));
				this.fuelTicksLeft = ticks;
				inv = updateOpenInventory(l, 1);
				if (inv == null) {
					in.setAmount(in.getAmount() - 1);
					this.saveInventoryRemote(in, ch, l);
				}
			}
		} else {
			this.fuelTicksLeft -= 10L;
			this.powerLevel += 10L;
			co.getConfig().set(this.locations.get(l) + ".power", this.powerLevel);
			co.getConfig().set(this.locations.get(l) + ".ticks", this.fuelTicksLeft);
			co.setConfigWrite(co.getConfig());
			if (this.fuelTicksLeft == 0L) {
				this.isOn = false;
			}
			updateOpenInventory(l, 0);
		}
		return true;
	}

	private Inventory updateOpenInventory(Location l, int amount) {
		for (UUID uuid : this.inGui.keySet()) {
			if (this.inGui.get(uuid).equals(l)) {
				Inventory inv = Bukkit.getPlayer(uuid).getOpenInventory().getTopInventory();
				ItemStack pow = new ItemStack(Material.REDSTONE);
				ItemMeta im = pow.getItemMeta();
				im.setDisplayName(ChatColor.DARK_RED + "Power Level");
				im.setLore(Arrays.asList(ChatColor.RED + "" + this.powerLevel + " Tw"));
				pow.setItemMeta(im);
				ItemStack fir;
				if (isOn) {
					fir = new ItemStack(Material.BLAZE_ROD);
					im = fir.getItemMeta();
					im.setDisplayName(ChatColor.GOLD + "Power Generation: " + ChatColor.GREEN + "Active");
					im.setLore(Arrays.asList(ChatColor.YELLOW + "" + this.fuelTicksLeft + ChatColor.GOLD
							+ " ticks left until depletion"));
					fir.setItemMeta(im);
				} else {
					fir = new ItemStack(Material.STICK);
					im = fir.getItemMeta();
					im.setDisplayName(ChatColor.GOLD + "Power Generation: " + ChatColor.DARK_RED + "Not Active");
					fir.setItemMeta(im);
				}
				if (inv.getItem(this.input) != null) {
					inv.setItem(this.input, ItemStackCopy.getItemStackCopy(inv.getItem(this.input),
							inv.getItem(this.input).getAmount() - amount));
				}
				inv.setItem(this.power, pow);
				inv.setItem(this.fire, fir);
				Bukkit.getPlayer(uuid).updateInventory();
				return inv;
			}
		}
		return null;
	}

}
