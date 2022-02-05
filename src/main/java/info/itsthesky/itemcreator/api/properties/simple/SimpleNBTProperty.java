package info.itsthesky.itemcreator.api.properties.simple;

import de.tr7zw.changeme.nbtapi.NBTItem;
import info.itsthesky.itemcreator.api.properties.base.ItemProperty;
import org.bukkit.inventory.ItemStack;

public abstract class SimpleNBTProperty<T> extends ItemProperty<T> {

	@Override
	public ItemStack apply(ItemStack item, T value) {
		return convert(new NBTItem(item), value).getItem();
	}

	public abstract NBTItem convert(NBTItem original, T value);
}
