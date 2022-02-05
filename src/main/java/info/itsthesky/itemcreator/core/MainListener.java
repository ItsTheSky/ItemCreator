package info.itsthesky.itemcreator.core;

import de.tr7zw.changeme.nbtapi.NBTItem;
import info.itsthesky.itemcreator.ItemCreator;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MainListener implements Listener {

	private final ItemCreator instance;
	public MainListener(ItemCreator instance) {
		this.instance = instance;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClick(@NotNull InventoryClickEvent e) {
		final InventoryType type = e.getWhoClicked().getOpenInventory().getType();
		final @Nullable CustomItem item = instance.getApi().convert(e.getCurrentItem());
		if (item == null)
			return;
		if (e.getWhoClicked().getGameMode().equals(GameMode.CREATIVE) && ItemCreator.getConfiguration().getBoolean("creative_bypass_restriction"))
			return;
		final NBTItem nbtItem = new NBTItem(e.getCurrentItem());
		if (type == InventoryType.WORKBENCH && nbtItem.getBoolean("ItemCreator.state.cant_craft"))
			e.setCancelled(true);
		if (type == InventoryType.ENCHANTING && nbtItem.getBoolean("ItemCreator.state.cant_enchant"))
			e.setCancelled(true);
		if (e.getSlotType().equals(InventoryType.SlotType.CRAFTING) && nbtItem.getBoolean("ItemCreator.state.cant_craft"))
			e.setCancelled(true);
	}

}
