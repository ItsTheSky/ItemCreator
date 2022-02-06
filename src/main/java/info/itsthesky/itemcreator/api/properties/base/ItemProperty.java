package info.itsthesky.itemcreator.api.properties.base;

import com.cryptomorin.xseries.XMaterial;
import de.leonhard.storage.Config;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.api.ISnowflake;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.GUIRepresentation;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class ItemProperty<T> implements ISnowflake {

	public abstract ItemStack apply(ItemStack item, T value);

	public @Nullable T fromBukkit(ItemStack stack) {
		return null;
	};

	public boolean supportSerialization(ItemStack item) {
		return fromBukkit(item) != null;
	}

	public String asString(T value) {
		return value.toString();
	}

	public boolean isSimple() {
		return true;
	}

	public abstract boolean allowClearing();

	public ItemStack asMenuItem() {
		return asRepresentation().asItem();
	}

	public List<String> isCompatible(CustomItem item) {
		return new ArrayList<>();
	}

	public GUIRepresentation asRepresentation() {
		return new GUIRepresentation(getMaterial(),
				"property." + getId() + ".name",
				"property." + getId() + ".lore");
	}

	public abstract T getDefaultValue();

	public T getValue(@NotNull Config itemFile) {
		return convert(itemFile.getString(getId()), null);
	}

	public abstract void onEditorClick(InventoryClickEvent e,
									   CustomItem item);

	public abstract XMaterial getMaterial();

	public abstract @Nullable T convert(String input, Player player);

	public void setInternalValue(CustomItem item, Object value) {
		final Config config = ItemCreator.getInstance().getApi().getItemConfig(item);
		config.set(getId(), value);
	}

	public boolean save(CustomItem item, String rawValue, Player player) {
		final Config config = ItemCreator.getInstance().getApi().getItemConfig(item);
		final @Nullable T parsed = convert(rawValue, player);
		if (parsed != null)
			config.set(getId(), parsed);
		else
			return false;
		return true;
	}

}
