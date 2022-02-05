package info.itsthesky.itemcreator.core;

import de.leonhard.storage.Config;
import de.tr7zw.changeme.nbtapi.NBTItem;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.api.ItemCreatorAPI;
import info.itsthesky.itemcreator.api.properties.base.ItemProperty;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class ItemCreatorAPIImpl implements ItemCreatorAPI {

	private final ItemCreator instance;
	public ItemCreatorAPIImpl(ItemCreator instance) {
		this.instance = instance;
	}

	@Override
	public @NotNull CustomItem getItemFromId(String id, boolean force) {
		if (!exits(id) && !force)
			return null;
		final CustomItem item = new CustomItem(id);
		final Config config = getItemConfig(item);
		for (ItemProperty property : ItemCreator.getInstance().getRegisteredProperties().values()) {
			item.registerProperty(property);
			if (config.contains(property.getId())) {
				final Object value;
				if (property.isSimple())
					value = property.convert(config.getString(property.getId()), null);
				else
					value = property.getValue(config);
				item.setPropertyValue(property, value);
			} else {
				if (property.getDefaultValue() != null) {
					config.set(property.getId(), property.getDefaultValue());
					final Object value;
					if (property.isSimple())
						value = property.convert(config.get(property.getId()).toString(), null);
					else
						value = property.getValue(config);
					item.setPropertyValue(property, value);
				}
			}
		}
		return item;
	}

	@Override
	public @Nullable CustomItem convert(ItemStack item) {
		if (item == null || item.getType().equals(Material.AIR))
			return null;
		final NBTItem nbtItem = new NBTItem(item);
		if (!nbtItem.hasKey("ItemCreator.ID"))
			return null;
		return getItemFromId(nbtItem.getString("ItemCreator.ID"));
	}

	@Override
	public void deleteItem(CustomItem item) {
		getItemFile(item).delete();
	}

	@Override
	public boolean exits(String id) {
		return new File(instance.getDataFolder(), "items/" + id + ".yml").exists();
	}

	private int prop = 1;
	@Override
	public void registerProperty(ItemProperty property) {
		prop++;
		instance.getRegisteredProperties().put(prop, property);
	}

	@Override
	public @NotNull File getItemFile(CustomItem item) {
		return new File(instance.getDataFolder(), "items/" + item.getId() + ".yml");
	}

	@Override
	public @NotNull File getItemsFolder() {
		final File folder = new File(instance.getDataFolder(), "items");
		if (!folder.exists())
			folder.mkdirs();
		return folder;
	}
}
