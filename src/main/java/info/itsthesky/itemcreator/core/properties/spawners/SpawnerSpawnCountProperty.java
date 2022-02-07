package info.itsthesky.itemcreator.core.properties.spawners;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.gui.EditorGUI;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import info.itsthesky.itemcreator.utils.ChatWaiter;
import org.apache.commons.lang.WordUtils;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SpawnerSpawnCountProperty extends SpawnerProperty<Integer> {
	public SpawnerSpawnCountProperty(ItemCreator instance) {
		super(instance);
	}

	@Override
	public void update(CreatureSpawner spawner, Integer value) {
		spawner.setSpawnCount(value);
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
	public String getId() {
		return "spawner_spawn_count";
	}

	@Override
	public boolean allowClearing() {
		return true;
	}

	@Override
	public Integer getDefaultValue() {
		return 4;
	}

	@Override
	public void onEditorClick(InventoryClickEvent e, CustomItem item) {
		final Player player = (Player) e.getWhoClicked();
		player.closeInventory();
		player.sendMessage(LangLoader.get().format("messages.parsing_number"));
		new ChatWaiter(ev -> ev.getPlayer().equals(player), ev -> {
			final Integer type = convert(ev.getMessage(), player);
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
		return XMaterial.RAIL;
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
}
