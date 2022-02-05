package info.itsthesky.itemcreator.api.properties.multiple;

import info.itsthesky.itemcreator.api.properties.base.ItemProperty;
import info.itsthesky.itemcreator.api.properties.base.MultipleItemProperty;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class MultipleMetaProperty<T> extends MultipleItemProperty<T> {

	@Override
	public ItemStack apply(ItemStack item, List<T> values) {
		final ItemMeta meta = item.getItemMeta();
		item.setItemMeta(convert(meta, values));
		return item;
	}

	public abstract ItemMeta convert(ItemMeta original, List<T> value);
}
