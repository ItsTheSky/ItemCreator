package info.itsthesky.itemcreator.core.properties;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.api.properties.simple.SimpleMetaProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.gui.EditorGUI;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import info.itsthesky.itemcreator.utils.ChatWaiter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EntityTypeProperty extends SimpleMetaProperty<EntityType> {
	@Override
	public String getId() {
		return "egg_type";
	}

	@Override
	public boolean allowClearing() {
		return true;
	}

	@Override
	public List<String> isCompatible(CustomItem item) {
		final List<String> errors = new ArrayList<>();
		if (!item.getPropertyValue(XMaterial.class, "material").name().contains("SPAWN_EGG"))
			errors.add(LangLoader.get().format("property.material_require", "Any Spawn Egg"));
		return errors;
	}

	@Override
	public EntityType getDefaultValue() {
		return null;
	}

	@Override
	public void onEditorClick(InventoryClickEvent e, CustomItem item) {
		final Player player = (Player) e.getWhoClicked();
		player.closeInventory();
		player.sendMessage(LangLoader.get().format("messages.entity_type"));
		new ChatWaiter(ev -> ev.getPlayer().equals(player), ev -> {
			final EntityType type = convert(ev.getMessage(), player);
			if (type != null) {
				item.setPropertyValue(this, type);
				save(item, ev.getMessage(), player);
				player.sendMessage(LangLoader.get().format("messages.success"));
			}
			new EditorGUI(item, true, player).open(player);
		}, true, true);
	}

	@Override
	public XMaterial getMaterial() {
		return XMaterial.EGG;
	}

	@Override
	public @Nullable EntityType convert(String input, Player player) {
		try {
			return EntityType.valueOf(input.toUpperCase(Locale.ROOT).replace(" ", "_"));
		} catch (Exception ex) {
			if (player != null)
				player.sendMessage(LangLoader.get().format("messages.wrong_entity_type"));
			return null;
		}
	}

	@Override
	public ItemMeta convert(ItemMeta original, EntityType value) {
		((SpawnEggMeta) original).setSpawnedType(value);
		return original;
	}
}
