package info.itsthesky.itemcreator.api.properties.multiple;

import de.tr7zw.changeme.nbtapi.NBTItem;
import info.itsthesky.itemcreator.api.properties.base.ItemProperty;
import info.itsthesky.itemcreator.api.properties.base.MultipleItemProperty;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class MultipleNBTProperty<T> extends MultipleItemProperty<T> {

	@Override
	public ItemStack apply(ItemStack item, List<T> values) {
		return convert(new NBTItem(item), values).getItem();
	}

	public abstract NBTItem convert(NBTItem original, List<T> value);
}
