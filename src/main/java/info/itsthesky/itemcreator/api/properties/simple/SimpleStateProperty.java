package info.itsthesky.itemcreator.api.properties.simple;

import info.itsthesky.itemcreator.api.properties.base.ItemProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.gui.EditorGUI;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import info.itsthesky.itemcreator.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.Nullable;

public abstract class SimpleStateProperty extends ItemProperty<Boolean> {

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

	@Override
	public void onEditorClick(InventoryClickEvent e, CustomItem item) {
		final Boolean current = item.getPropertyValue(this);
		item.setPropertyValue(this, Boolean.FALSE.equals(current));
		save(item, ""+ Boolean.FALSE.equals(current), (Player) e.getWhoClicked());
		new EditorGUI(item, true, (Player) e.getWhoClicked()).open((Player) e.getWhoClicked());
		e.getWhoClicked().sendMessage(LangLoader.get().format("messages.success"));
	}
}
