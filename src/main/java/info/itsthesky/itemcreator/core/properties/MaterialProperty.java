package info.itsthesky.itemcreator.core.properties;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.api.properties.base.ItemProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.gui.EditorGUI;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import info.itsthesky.itemcreator.utils.ChatWaiter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class MaterialProperty extends ItemProperty<XMaterial> {

	@Override
	public String getId() {
		return "material";
	}

	@Override
	public ItemStack apply(ItemStack item, XMaterial value) {
		item.setType(value.parseMaterial());
		return item;
	}

	@Override
	public XMaterial getDefaultValue() {
		return XMaterial.GRASS_BLOCK;
	}

	@Override
	public void onEditorClick(InventoryClickEvent e, CustomItem item) {
		final Player player = (Player) e.getWhoClicked();
		player.closeInventory();
		player.sendMessage(LangLoader.get().format("messages.parsing_material"));
		new ChatWaiter(ev -> ev.getPlayer().equals(player), ev -> {
			final String rawValue = ev.getMessage();
			final XMaterial material = convert(rawValue, player);
			if (material != null && !material.parseMaterial().isAir()) {
				item.setPropertyValue(this, material);
				save(item, rawValue, player);
				player.sendMessage(LangLoader.get().format("messages.success"));
			}
			new EditorGUI(item, true).open(player);
		}, true, true);
	}

	@Override
	public XMaterial getMaterial() {
		return XMaterial.GRASS_BLOCK;
	}

	@Override
	public boolean allowClearing() {
		return false;
	}

	@Override
	public @Nullable XMaterial convert(String input, Player player) {
		final XMaterial material = XMaterial
				.matchXMaterial(input.toUpperCase(Locale.ROOT).replace(" ", "_"))
				.orElse(null);
		if (material == null && player != null)
			player.sendMessage(LangLoader.get().format("messages.wrong_material"));
		return material;
	}
}
