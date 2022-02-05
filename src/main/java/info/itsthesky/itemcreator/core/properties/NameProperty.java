package info.itsthesky.itemcreator.core.properties;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.api.properties.simple.SimpleMetaProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.gui.EditorGUI;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import info.itsthesky.itemcreator.utils.ChatWaiter;
import info.itsthesky.itemcreator.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

public class NameProperty extends SimpleMetaProperty<String> {

	@Override
	public String getId() {
		return "name";
	}

	@Override
	public String getDefaultValue() {
		return null;
	}

	@Override
	public boolean allowClearing() {
		return true;
	}

	@Override
	public void onEditorClick(InventoryClickEvent e, CustomItem item) {
		final Player player = (Player) e.getWhoClicked();
		player.closeInventory();
		player.sendMessage(LangLoader.get().format("messages.display_name"));
		new ChatWaiter(ev -> ev.getPlayer().equals(player), ev -> {
			final String rawValue = ev.getMessage();
			item.setPropertyValue(this, convert(rawValue, player));
			save(item, rawValue, player);
			player.sendMessage(LangLoader.get().format("messages.success"));
			new EditorGUI(item, true).open(player);
		}, true, true);
	}

	@Override
	public XMaterial getMaterial() {
		return XMaterial.NAME_TAG;
	}

	@Override
	public @Nullable String convert(String input, Player player) {
		return Utils.colored(input);
	}

	@Override
	public ItemMeta convert(ItemMeta original, String value) {
		original.setDisplayName(value);
		return original;
	}
}
