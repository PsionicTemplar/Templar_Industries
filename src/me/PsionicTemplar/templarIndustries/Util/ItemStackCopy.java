package me.PsionicTemplar.templarIndustries.Util;

import java.util.List;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackCopy {
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
	
	public static ItemStack getItemStackCopy(ItemStack copyThis, int amount, short damage, Map<Enchantment, Integer> enchants) {
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
}
