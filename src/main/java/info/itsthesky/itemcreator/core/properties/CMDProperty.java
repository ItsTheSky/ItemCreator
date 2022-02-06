package info.itsthesky.itemcreator.core.properties;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.api.properties.simple.SimpleMetaProperty;
import info.itsthesky.itemcreator.api.properties.simple.SimpleNBTProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.gui.EditorGUI;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import info.itsthesky.itemcreator.utils.ChatWaiter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

public class CMDProperty extends SimpleMetaProperty<Integer> {
	@Override
	public String getId() {
		return "custom_model_data";
	}

	@Override
	public @Nullable Integer fromBukkit(ItemStack stack) {
		return stack.getItemMeta().hasCustomModelData() ? stack.getItemMeta().getCustomModelData() : null;
	}

	@Override
	public boolean allowClearing() {
		return true;
	}

	@Override
	public Integer getDefaultValue() {
		return null;
	}

	@Override
	public void onEditorClick(InventoryClickEvent e, CustomItem item) {
		final Player player = (Player) e.getWhoClicked();
		player.closeInventory();
		player.sendMessage(LangLoader.get().format("messages.parsing_custom_model_data"));
		new ChatWaiter(ev -> ev.getPlayer().equals(player), ev -> {
			final Integer value = convert(ev.getMessage(), ev.getPlayer());
			if (value != null) {
				item.setPropertyValue(this, value);
				save(item, ev.getMessage(), player);
				player.sendMessage(LangLoader.get().format("messages.success"));
			}
			new EditorGUI(item, true).open(player);
		}, true, true);
	}

	@Override
	public XMaterial getMaterial() {
		return XMaterial.PAINTING;
	}

	@Override
	public @Nullable Integer convert(String input, Player player) {
		try {
			return Integer.parseInt(input);
		} catch (Exception ex) {
			if (player != null)
				player.sendMessage(LangLoader.get().format("messages.wrong_number"));
			return null;
		}
	}

	@Override
	public ItemMeta convert(ItemMeta original, Integer value) {
		original.setCustomModelData(value);
		return original;
	}
}
