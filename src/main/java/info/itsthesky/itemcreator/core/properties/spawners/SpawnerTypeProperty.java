package info.itsthesky.itemcreator.core.properties.spawners;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.api.properties.base.BlockProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.gui.EditorGUI;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import info.itsthesky.itemcreator.utils.ChatWaiter;
import org.apache.commons.lang.WordUtils;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SpawnerTypeProperty extends SpawnerProperty<EntityType> {
	public SpawnerTypeProperty(ItemCreator instance) {
		super(instance);
	}

	@Override
	public void update(CreatureSpawner spawner, EntityType value) {
		spawner.setSpawnedType(value);
	}

	@Override
	public List<String> isCompatible(CustomItem item) {
		final List<String> errors = new ArrayList<>();
		if (!item.getPropertyValue(XMaterial.class, "material").equals(XMaterial.SPAWNER))
			errors.add(LangLoader.get().format("property.material_require", "Mob Spawner"));
		errors.addAll(super.isCompatible(item));
		return errors;
	}

	@Override
	public String asString(EntityType value) {
		return WordUtils.capitalize(value.name().toLowerCase().replace("_", " "));
	}

	@Override
	public String getId() {
		return "spawner_type";
	}

	@Override
	public boolean allowClearing() {
		return false;
	}

	@Override
	public EntityType getDefaultValue() {
		return EntityType.PIG;
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
			new EditorGUI(item, true).open(player);
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
}
