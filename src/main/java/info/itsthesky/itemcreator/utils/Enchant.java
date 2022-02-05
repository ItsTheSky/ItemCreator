package info.itsthesky.itemcreator.utils;

import org.bukkit.enchantments.Enchantment;

public class Enchant {

	private final Enchantment enchantment;
	private final int level;

	public Enchant(Enchantment enchantment, int level) {
		this.enchantment = enchantment;
		this.level = level;
	}

	public Enchantment getEnchantment() {
		return enchantment;
	}

	public int getLevel() {
		return level;
	}

	@Override
	public String toString() {
		return enchantment.getKey().getKey() + " (level "+level+")";
	}
}
