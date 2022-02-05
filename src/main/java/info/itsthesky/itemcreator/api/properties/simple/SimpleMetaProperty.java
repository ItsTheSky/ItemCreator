package info.itsthesky.itemcreator.api.properties.simple;

import de.leonhard.storage.Config;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.api.properties.base.ItemProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class SimpleMetaProperty<T> extends ItemProperty<T> {

	@Override
	public ItemStack apply(ItemStack item, T value) {
		final ItemMeta meta = item.getItemMeta();
		item.setItemMeta(convert(meta, (T) value));
		return item;
	}

	@Override
	public boolean save(CustomItem item, String rawValue, Player player) {
		final Config config = ItemCreator.getInstance().getApi().getItemConfig(item);
		if (!config.contains(getId()))
			config.set(getId(), false);
		return super.save(item, rawValue, player);
	}

	public abstract ItemMeta convert(ItemMeta original, T value);
}
