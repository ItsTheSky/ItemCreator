package info.itsthesky.itemcreator.core.properties.base;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.dbassett.skullcreator.SkullCreator;
import info.itsthesky.itemcreator.api.properties.base.ItemProperty;
import info.itsthesky.itemcreator.api.properties.simple.SimpleMetaProperty;
import info.itsthesky.itemcreator.api.properties.simple.SimpleNBTProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.gui.EditorGUI;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import info.itsthesky.itemcreator.utils.ChatWaiter;
import info.itsthesky.itemcreator.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Base64Property extends ItemProperty<String> {

	@Override
	public String getId() {
		return "base64";
	}

	@Override
	public List<String> isCompatible(CustomItem item) {
		final List<String> errors = new ArrayList<>();
		if (!item.getPropertyValue(XMaterial.class, "material").name().equals("PLAYER_HEAD"))
			errors.add(LangLoader.get().format("property.material_require", "Player Head"));
		return errors;
	}

	@Override
	public @Nullable String fromBukkit(ItemStack stack) {
		if (!stack.getType().equals("PLAYER_SKULL"))
			return null;
		final SkullMeta meta = ((SkullMeta) stack.getItemMeta());
		final Field profileField;
		try {
			profileField = meta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			return (String) profileField.get(meta);
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public String getDefaultValue() {
		return null;
	}

	@Override
	public ItemStack apply(ItemStack item, String value) {
		return value.isEmpty() ? item : SkullCreator.itemWithBase64(item, value);
	}

	@Override
	public boolean allowClearing() {
		return true;
	}

	@Override
	public void onEditorClick(InventoryClickEvent e, CustomItem item) {
		final Player player = (Player) e.getWhoClicked();
		player.closeInventory();
		player.sendMessage(LangLoader.get().format("messages.base64"));
		new ChatWaiter(ev -> ev.getPlayer().equals(player), ev -> {
			final String rawValue = ev.getMessage();
			item.setPropertyValue(this, convert(rawValue, player));
			save(item, rawValue, player);
			player.sendMessage(LangLoader.get().format("messages.success"));
			new EditorGUI(item, true, player).open(player);
		}, true, true);
	}

	@Override
	public String asString(String value) {
		if (value.length() > 20)
			value = value.substring(0, 18) + "...";
		return value;
	}

	@Override
	public XMaterial getMaterial() {
		return XMaterial.CREEPER_HEAD;
	}

	@Override
	public @Nullable String convert(String input, Player player) {
		return input;
	}
}
