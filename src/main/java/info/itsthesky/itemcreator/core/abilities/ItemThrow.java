package info.itsthesky.itemcreator.core.abilities;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.api.abilities.DefaultParameters;
import info.itsthesky.itemcreator.api.abilities.RawAbilityParameter;
import info.itsthesky.itemcreator.api.abilities.RightClickAbility;
import info.itsthesky.itemcreator.core.CustomItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemThrow extends RightClickAbility implements Listener {

	public ItemThrow(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public String getId() {
		return "item_throw";
	}

	@Override
	public @NotNull Player getPlayer(PlayerInteractEvent event) {
		return event.getPlayer();
	}

	@Override
	public XMaterial getMaterial() {
		return XMaterial.FISHING_ROD;
	}

	@Override
	public @NotNull RawAbilityParameter<?>[] getParameters() {
		return new RawAbilityParameter[] {
				DefaultParameters.ABILITY_COOLDOWN,
				DefaultParameters.ABILITY_FORCE
		};
	}

	@Override
	public void launch(@NotNull PlayerInteractEvent event, @Nullable CustomItem citem, @NotNull AbilityParameter<?>[] parameters) {
		if (citem == null)
			return;
		event.setCancelled(true);
		final Number force = ((Number) parameters[1].getValue()).doubleValue() / 10;
		final Player player = event.getPlayer();
		final ItemStack tool = player.getInventory().getItemInMainHand().clone();
		tool.setAmount(1);

		final ItemStack editedItem = player.getInventory().getItemInMainHand().clone();
		if (editedItem.getAmount() > 0)
			editedItem.setAmount(editedItem.getAmount() - 1);
		player.getInventory().setItemInMainHand(editedItem);

		player.getWorld().dropItem(player.getLocation().clone().add(0, 1, 0), tool, item -> {
			final Vector direction = player.getLocation().getDirection().normalize();
			item.setVelocity(new Vector(direction.getX(), direction.getY() + force.doubleValue(), direction.getZ()));
		});
	}

}
