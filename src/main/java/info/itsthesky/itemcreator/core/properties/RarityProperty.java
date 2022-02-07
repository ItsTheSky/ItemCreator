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

public class RarityProperty extends SimpleMetaProperty<String> {
	@Override
	public String getId() {
		return "rarity";
	}

	@Override
	public boolean allowClearing() {
		return true;
	}

	@Override
	public String getDefaultValue() {
		return null;
	}

	@Override
	public void onEditorClick(InventoryClickEvent e, CustomItem item) {
		final Player player = (Player) e.getWhoClicked();
		player.closeInventory();
		player.sendMessage(LangLoader.get().format("messages.rarity"));
		new ChatWaiter(ev -> ev.getPlayer().equals(player), ev -> {
			final String rawValue = ev.getMessage();
			item.setPropertyValue(this, convert(rawValue, player));
			save(item, rawValue, player);
			player.sendMessage(LangLoader.get().format("messages.success"));
			new EditorGUI(item, true, player).open(player);
		}, true, true);
	}

	@Override
	public XMaterial getMaterial() {
		return XMaterial.ENDER_EYE;
	}

	@Override
	public @Nullable String convert(String input, Player player) {
		return Utils.colored(input);
	}

	@Override
	public ItemMeta convert(ItemMeta original, String value) {
		final List<String> lores =
				original.getLore() == null ? new ArrayList<>() : original.getLore();
		if (!lores.isEmpty() && !lores.get(lores.size() - 1).equals(""))
			lores.add("");
		lores.add(Utils.colored(value));
		original.setLore(lores);
		return original;
	}
}
