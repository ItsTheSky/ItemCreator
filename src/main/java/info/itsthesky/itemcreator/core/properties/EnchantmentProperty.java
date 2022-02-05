package info.itsthesky.itemcreator.core.properties;

import com.cryptomorin.xseries.XMaterial;
import de.leonhard.storage.Config;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.api.properties.base.MultipleItemProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import info.itsthesky.itemcreator.utils.Enchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EnchantmentProperty extends MultipleItemProperty<Enchant> {
	@Override
	public String getId() {
		return "enchantments";
	}

	@Override
	public ItemStack apply(ItemStack item, List<Enchant> value) {
		for (Enchant enchant : value)
			item.addUnsafeEnchantment(enchant.getEnchantment(), enchant.getLevel());
		return item;
	}

	@Override
	public boolean allowClearing() {
		return true;
	}

	@Override
	public List<Enchant> getDefaultValue() {
		return new ArrayList<>();
	}

	@Override
	public XMaterial getMaterial() {
		return XMaterial.ENCHANTED_BOOK;
	}

	@Override
	public @Nullable Enchant parseSingle(String input, Player player) {
		final String[] values = input.split(":");
		final Enchantment enchantment;
		final int level;
		try {
			enchantment = (Enchantment) Enchantment.class.getDeclaredField(values[0].toUpperCase(Locale.ROOT).replace(" ", "_")).get(null);
			level = Integer.parseInt(values[1]);
		} catch (Exception ex) {
			if (player != null)
				player.sendMessage(LangLoader.get().format("messages.wrong_enchant"));
			return null;
		}
		return new Enchant(enchantment, level);
	}

	@Override
	public String getActionLore() {
		return "Left click to add enchant, right click to remove last one.";
	}

	@Override
	public boolean save(CustomItem item, List<Enchant> values, Player player) {
		final Config config = ItemCreator.getInstance().getApi().getItemConfig(item);
		config.set(getId(), values
				.stream()
				.map(str -> str.getEnchantment().getName() + ":" + str.getLevel())
				.collect(Collectors.toList()));
		return true;
	}

	@Override
	public List<String> formatGUI(List<Enchant> values) {
		return values
				.stream()
				.map(v -> "  &6→ &e" + v.toString())
				.collect(Collectors.toList());
	}
}
