package info.itsthesky.itemcreator.api.properties.base;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BlockProperty<T> extends ItemProperty<T> implements Listener {

	public BlockProperty(ItemCreator instance) {
		instance.getServer().getPluginManager().registerEvents(this, instance);
	}

	@Override
	public List<String> isCompatible(CustomItem item) {
		final List<String> errors = new ArrayList<>();
		if (!item.hasPropertySet("ic_tag"))
			errors.add(LangLoader.get().format("property.property_require",
					LangLoader.get().format("property.ic_tag.name")));
		return errors;
	}

	public abstract void editBlock(Block block, T value);

	@Override
	public ItemStack apply(ItemStack item, T value) {
		return item;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent e) {
		if (e.getItemInHand() == null || e.getItemInHand().getType().equals(Material.AIR))
			return;
		final CustomItem item = ItemCreator.getInstance().getApi().convert(e.getItemInHand());
		if (item == null || !item.hasPropertySet(getId()))
			return;
		editBlock(e.getBlock(), item.getPropertyValue(this));
	}
}
