package info.itsthesky.itemcreator.api.properties.base;

import info.itsthesky.itemcreator.core.CustomItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public abstract class MenuProperty extends ItemProperty<Object> {

	@Override
	public ItemStack apply(ItemStack item, Object value) {
		return item;
	}

	@Override
	public boolean allowClearing() {
		return false;
	}

	@Override
	public Object getDefaultValue() {
		return null;
	}

	@Override
	public boolean isSavable() {
		return false;
	}

	public abstract void onClick(Player player, CustomItem item);

	@Override
	public void onEditorClick(InventoryClickEvent e, CustomItem item) {
		onClick((Player) e.getWhoClicked(), item);
	}

	@Override
	public @Nullable Void convert(String input, Player player) {
		return null;
	}
}
