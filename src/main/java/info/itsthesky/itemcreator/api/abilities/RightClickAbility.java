package info.itsthesky.itemcreator.api.abilities;

import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.core.CustomItem;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class RightClickAbility extends Ability<PlayerInteractEvent> {

	// Check https://spigotmc.org/threads/204644/
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEvent(@NotNull PlayerInteractEvent event) {
		onGenericEvent(event);
	}

	@Override
	public @Nullable CustomItem getItem(PlayerInteractEvent event) {
		final ItemStack stack = event.getItem();
		if (stack == null || stack.getType().equals(Material.AIR))
			return null;
		return ItemCreator.getInstance().getApi().convert(stack);
	}

	@Override
	public boolean validate(PlayerInteractEvent event) {
		return event.getAction().equals(Action.RIGHT_CLICK_AIR)
				|| event.getAction().equals(Action.RIGHT_CLICK_BLOCK);
	}

}
