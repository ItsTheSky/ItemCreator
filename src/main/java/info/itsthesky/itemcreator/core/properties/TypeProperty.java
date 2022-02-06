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

import java.util.ArrayList;
import java.util.List;

public class TypeProperty extends SimpleMetaProperty<String> {
	@Override
	public String getId() {
		return "custom_type";
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
		player.sendMessage(LangLoader.get().format("messages.custom_type"));
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
		return XMaterial.LEAD;
	}

	@Override
	public @Nullable String convert(String input, Player player) {
		return Utils.colored("&8" + input);
	}

	@Override
	public ItemMeta convert(ItemMeta original, String value) {
		final List<String> lore = original.getLore() != null ?
				original.getLore() : new ArrayList<>();
		lore.add(0, "");
		lore.add(0, value);
		original.setLore(lore);
		return original;
	}
}
