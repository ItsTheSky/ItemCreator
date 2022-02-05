package info.itsthesky.itemcreator.core;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
import info.itsthesky.itemcreator.api.ISnowflake;
import info.itsthesky.itemcreator.api.properties.base.ItemProperty;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represent a whole custom item from {@link info.itsthesky.itemcreator.ItemCreator}.
 * <br> Hold every handler and specific information a custom item can actually have.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class CustomItem implements ISnowflake {

	private final String id;
	private final List<ItemProperty> properties;
	private final HashMap<String, Object> propertiesValue;

	public CustomItem(final String id) {
		this.properties = new ArrayList<>();
		this.propertiesValue = new HashMap<>();
		this.id = id;
	}

	public void registerProperty(ItemProperty property) {
		properties.removeIf(prop -> prop.getId().equals(property.getId()));
		properties.add(property);
	}

	public <T> void setPropertyValue(ItemProperty property, T value) {
		propertiesValue.put(property.getId(), value);
	}

	public <T> @Nullable T getDefaultProperty(ItemProperty property, T def) {
		return getPropertyValue(property) == null
				? def : getPropertyValue(property);
	}

	public <T> @Nullable T getPropertyValue(ItemProperty property) {
		return (T) propertiesValue.getOrDefault(property.getId(), null);
	}

	@SuppressWarnings("unchecked")
	public <T> @Nullable T getPropertyValue(Class<T> clazz, String propertyID) {
		final ItemProperty<T> property = (ItemProperty<T>) properties
				.stream()
				.filter(prop -> prop.getId().equals(propertyID))
				.findAny()
				.orElse(null);
		if (property == null)
			return null;
		return (T) propertiesValue.getOrDefault(property.getId(), null);
	}

	@Deprecated
	public boolean save() {
		return save(null);
	}

	private boolean save(Player player) {
		final List<Boolean> validates = new ArrayList<>();
		for (ItemProperty<Object> property : properties) {
			/**
			 * Custom parsing for {@link info.itsthesky.itemcreator.api.properties.base.MultipleItemProperty}
			 */
			if (property.isSimple())
				validates.add(property.save(this, propertiesValue.get(property.getId()).toString(), player));
		}
		return validates.stream().anyMatch(b -> !b);
	}

	@Override
	public String getId() {
		return id;
	}

	public ItemStack asItem() {
		ItemStack base = XMaterial.GRASS_BLOCK.parseItem();
		for (ItemProperty<Object> property : properties) {
			final Object obj = propertiesValue.get(property.getId());
			if (obj != null && property.isCompatible(this).isEmpty()) {
				base = property.apply(base, obj).clone();
			}
		}
		final NBTItem nbtItem = new NBTItem(base);
		if (getPropertyValue(Boolean.class, "ic_tag"))
			nbtItem.setString("ItemCreator.ID", getId());
		return nbtItem.getItem();
	}

	public List<ItemProperty> getProperties() {
		return properties;
	}

	public boolean hasPropertySet(String propertyID) {
		final ItemProperty property = getProperties()
				.stream()
				.filter(prop -> prop.getId().equals(propertyID))
				.findAny()
				.orElse(null);
		if (property == null || getPropertyValue(property) == null)
			return false;
		return !propertiesValue.getOrDefault(propertyID, property.getDefaultValue()).equals(property.getDefaultValue());
	}
}
