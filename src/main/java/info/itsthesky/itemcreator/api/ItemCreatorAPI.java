package info.itsthesky.itemcreator.api;

import de.leonhard.storage.Config;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.api.abilities.Ability;
import info.itsthesky.itemcreator.api.abilities.RawAbilityParameter;
import info.itsthesky.itemcreator.api.properties.base.ItemProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import info.itsthesky.itemcreator.api.properties.simple.*;
import info.itsthesky.itemcreator.api.properties.multiple.*;

/**
 * The main ItemCreator instance of the API.
 * <br> Get items, register properties and more using {@link ItemCreator#getApi()}.
 */
public interface ItemCreatorAPI {

	/**
	 * Get a {@link CustomItem} from its internal unique ID.
	 * <br> If the item doesn't exist, this will return null.
	 * @param id The custom item's id
	 * @return The custom item linked to that id, else null
	 */
	default @Nullable CustomItem getItemFromId(String id) {
		return getItemFromId(id, false);
	}

	/**
	 * Get a {@link CustomItem} from its internal unique ID.
	 * <br> If the item doesn't exist, and 'force' is true, this method will create the item.
	 * @param id    The custom item's item
	 * @param force Either the item should be created if it doesn't exist
	 */
	@NotNull CustomItem getItemFromId(String id, boolean force);

	/**
	 * Create a new {@link CustomItem} with the following id.
	 * @param id The custom item id
	 * @return The never-null created item
	 */
	default @NotNull CustomItem createItem(String id) {
		return getItemFromId(id, true);
	}

	/**
	 * Convert a bukkit {@link ItemStack} into a {@link CustomItem}
	 * @param item The item stack
	 * @return
	 */
	@Nullable CustomItem convert(ItemStack item);

	/**
	 * Delete (forever) a specific custom item.
	 * @param item The custom item to delete
	 */
	void deleteItem(CustomItem item);

	/**
	 * Check if a specific custom item with that id exist or not.
	 * @param id The custom item's id
	 * @return True if it exists, else false
	 */
	boolean exists(String id);

	/**
	 * Register a new {@link ItemProperty} into {@link ItemCreator}.
	 * <br> You can be helped with the following sub-classes:
	 * <ul>
	 *     <li>{@link SimpleMetaProperty}</li>
	 *     <li>{@link SimpleMetaStateProperty}</li>
	 *     <li>{@link SimpleNBTProperty}</li>
	 *     <li>{@link SimpleNBTStateProperty}</li>
	 *     <li>{@link SimpleStateProperty}</li>
	 *     <li>{@link MultipleMetaProperty}</li>
	 *     <li>{@link MultipleNBTProperty}</li>
	 * </ul>
	 * @param property
	 */
	void registerProperty(ItemProperty property);

	void registerAbility(Ability ability);

	void registerParameter(RawAbilityParameter parameter);

	@NotNull File getItemFile(CustomItem item);

	@NotNull File getItemsFolder();

	default @NotNull Config getItemConfig(CustomItem item) {
		return new Config(getItemFile(item));
	}

	default @NotNull List<CustomItem> loadAllItems() {
		final List<CustomItem> items = new ArrayList<>();
		for (File file : getItemsFolder().listFiles())
			items.add(getItemFromId(file.getName().split("\\.")[0]));
		return items;
	}

}
