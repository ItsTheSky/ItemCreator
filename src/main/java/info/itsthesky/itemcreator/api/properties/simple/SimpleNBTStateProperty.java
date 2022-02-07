package info.itsthesky.itemcreator.api.properties.simple;

import de.leonhard.storage.Config;
import de.tr7zw.changeme.nbtapi.NBTItem;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.api.properties.base.ItemProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.gui.EditorGUI;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import info.itsthesky.itemcreator.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.Nullable;

public abstract class SimpleNBTStateProperty extends SimpleNBTProperty<Boolean> {

	@Override
	public void onEditorClick(InventoryClickEvent e, CustomItem item) {
		final Boolean current = item.getPropertyValue(this);
		item.setPropertyValue(this, Boolean.FALSE.equals(current));
		save(item, ""+ Boolean.FALSE.equals(current), (Player) e.getWhoClicked());
		new EditorGUI(item, true, (Player) e.getWhoClicked()).open((Player) e.getWhoClicked());
		e.getWhoClicked().sendMessage(LangLoader.get().format("messages.success"));
	}

	@Override
	public boolean allowClearing() {
		return false;
	}

	@Override
	public Boolean getDefaultValue() {
		return false;
	}

	@Override
	public boolean save(CustomItem item, String rawValue, Player player) {
		final Config config = ItemCreator.getInstance().getApi().getItemConfig(item);
		if (!config.contains(getId()))
			config.set(getId(), false);
		return super.save(item, rawValue, player);
	}

	@Override
	public NBTItem convert(NBTItem original, Boolean value) {
		original.setBoolean("ItemCreator.state." + getId(), value);
		return original;
	}

	@Override
	public @Nullable Boolean convert(String input, Player player) {
		try {
			return Boolean.valueOf(input);
		} catch (Exception ex) {
			if (player != null)
				player.sendMessage(Utils.colored("&cUnknown boolean state, should be either &4true &cor &4false&c."));
			return null;
		}
	}

}
