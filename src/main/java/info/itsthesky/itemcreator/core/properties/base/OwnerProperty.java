package info.itsthesky.itemcreator.core.properties.base;

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
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OwnerProperty extends SimpleMetaProperty<String> {
	@Override
	public String getId() {
		return "skull_owner";
	}

	@Override
	public boolean allowClearing() {
		return true;
	}

	@Override
	public List<String> isCompatible(CustomItem item) {
		final List<String> errors = new ArrayList<>();
		if (!item.getPropertyValue(XMaterial.class, "material").name().equals("PLAYER_HEAD"))
			errors.add(LangLoader.get().format("property.material_require", "Player Head"));
		return errors;
	}

	@Override
	public String getDefaultValue() {
		return null;
	}

	@Override
	public void onEditorClick(InventoryClickEvent e, CustomItem item) {
		final Player player = (Player) e.getWhoClicked();
		player.closeInventory();
		player.sendMessage(LangLoader.get().format("messages.skull_owner"));
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
		return XMaterial.CHEST_MINECART;
	}

	@Override
	public @Nullable String convert(String input, Player player) {
		return input;
	}

	@Override
	public ItemMeta convert(ItemMeta original, String value) {
		((SkullMeta) original).setOwner(value);
		return original;
	}
}
