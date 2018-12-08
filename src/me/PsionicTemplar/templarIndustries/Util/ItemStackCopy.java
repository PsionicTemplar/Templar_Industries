package me.PsionicTemplar.templarIndustries.Util;

import java.util.List;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackCopy {

	/**
	 * Copy's an ItemStack and sets the amount to 1
	 * 
	 * @author Nicholas Braniff
	 * @param copyThis
	 * @return
	 */

	public static ItemStack getItemStackCopy(ItemStack copyThis) {
		ItemStack i = new ItemStack(copyThis.getType());
		i.setItemMeta(copyThis.getItemMeta());
		if (!copyThis.getEnchantments().isEmpty()) {
			for (Enchantment e : copyThis.getEnchantments().keySet()) {
				i.addUnsafeEnchantment(e, copyThis.getEnchantments().get(e));
			}
		}
		return i;
	}

	/**
	 * Copy's an ItemStack with a specified amount
	 * 
	 * @author Nicholas Braniff
	 * @param copyThis
	 * @param amount
	 * @return
	 */

	public static ItemStack getItemStackCopy(ItemStack copyThis, int amount) {
		ItemStack i = new ItemStack(copyThis.getType(), amount);

		i.setItemMeta(copyThis.getItemMeta());
		if (!copyThis.getEnchantments().isEmpty()) {
			for (Enchantment e : copyThis.getEnchantments().keySet()) {
				i.addUnsafeEnchantment(e, copyThis.getEnchantments().get(e));
			}
		}
		return i;
	}

	/**
	 * Copy's an ItemStack and allows you to add both a name, lore, and amount
	 * 
	 * @author Nicholas Braniff
	 * @param copyThis
	 * @param amount
	 * @param name
	 * @param lore
	 * @return
	 */

	public static ItemStack getItemStackCopy(ItemStack copyThis, int amount, String name, List<String> lore) {
		ItemStack i = new ItemStack(copyThis.getType(), amount);

		i.setItemMeta(copyThis.getItemMeta());
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(name);
		im.setLore(lore);
		i.setItemMeta(im);

		if (!copyThis.getEnchantments().isEmpty()) {
			for (Enchantment e : copyThis.getEnchantments().keySet()) {
				i.addUnsafeEnchantment(e, copyThis.getEnchantments().get(e));
			}
		}
		return i;
	}

	/**
	 * Copy's an ItemStack. Specify the amount, damage, and enchantments. Useful for
	 * tools.
	 * 
	 * @author Nicholas Braniff
	 * @param copyThis
	 * @param amount
	 * @param damage
	 * @param enchants
	 * @return
	 */

	public static ItemStack getItemStackCopy(ItemStack copyThis, int amount, short damage,
			Map<Enchantment, Integer> enchants) {
		ItemStack i = new ItemStack(copyThis.getType(), amount);

		i.setItemMeta(copyThis.getItemMeta());
		Damageable dam = (Damageable) i.getItemMeta();
		dam.setDamage(damage);
		i.setItemMeta((ItemMeta) dam);
		if (enchants != null && !enchants.isEmpty()) {
			for (Enchantment e : enchants.keySet()) {
				i.addUnsafeEnchantment(e, enchants.get(e));
			}
		}
		return i;
	}

	/**
	 * Copy's an ItemStack. Specify the amount, damage, enchantments, name, and
	 * lore. Useful for custom tools.
	 * 
	 * @author Nicholas Braniff
	 * @param copyThis
	 * @param amount
	 * @param damage
	 * @param enchants
	 * @param name
	 * @param lore
	 * @return
	 */

	public static ItemStack getItemStackCopy(ItemStack copyThis, int amount, short damage,
			Map<Enchantment, Integer> enchants, String name, List<String> lore) {
		ItemStack i = new ItemStack(copyThis.getType(), amount);

		i.setItemMeta(copyThis.getItemMeta());
		Damageable dam = (Damageable) i.getItemMeta();
		dam.setDamage(damage);
		i.setItemMeta((ItemMeta) dam);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(name);
		im.setLore(lore);
		i.setItemMeta(im);
		if (enchants != null && !enchants.isEmpty()) {
			for (Enchantment e : enchants.keySet()) {
				i.addUnsafeEnchantment(e, enchants.get(e));
			}
		}
		return i;
	}
}
