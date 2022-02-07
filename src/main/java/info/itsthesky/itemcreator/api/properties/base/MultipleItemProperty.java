package info.itsthesky.itemcreator.api.properties.base;

import de.leonhard.storage.Config;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.gui.EditorGUI;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import info.itsthesky.itemcreator.utils.ChatWaiter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MultipleItemProperty<T> extends ItemProperty<List<T>> {

	public abstract @Nullable T parseSingle(String input, Player player);

	public abstract String getActionLore();

	public abstract List<String> formatGUI(List<T> values);

	public abstract String getParsingKey();

	@Override
	public List<T> getValue(@NotNull Config itemFile) {
		return itemFile.get(getId(), new ArrayList<>())
				.stream()
				.map(obj -> parseSingle(obj.toString(), null)).collect(Collectors.toList());
	}

	@Override
	public void onEditorClick(InventoryClickEvent e, CustomItem item) {
		final List<T> current = new ArrayList<>(item.getDefaultProperty(this, new ArrayList<>()));
		final Player player = (Player) e.getWhoClicked();
		if (e.getClick().equals(ClickType.RIGHT)) {
			if (current.isEmpty()) {
				player.sendMessage(LangLoader.get().format("messages.no_more_value"));
				new EditorGUI(item, true, player).open(player);
				return;
			}
			current.remove(current.size() - 1);
			item.setPropertyValue(this, current);
			saveMultiple(item, current, player);
			player.sendMessage(LangLoader.get().format("messages.success"));
			new EditorGUI(item, true, player).open(player);
			return;
		}
		player.closeInventory();
		player.sendMessage(LangLoader.get().format("messages.parsing_" + getParsingKey()));
		new ChatWaiter(ev -> ev.getPlayer().equals(player),
				ev -> {
					final T parsed = parseSingle(ev.getMessage(), ev.getPlayer());
					if (parsed != null) {
						current.add(parsed);
						item.setPropertyValue(this, current);
						saveMultiple(item, current, ev.getPlayer());
						ev.getPlayer().sendMessage(LangLoader.get().format("messages.success"));
					}
					new EditorGUI(item, true, player).open(ev.getPlayer());
				}, true, true);
	}

	public boolean saveMultiple(CustomItem item, List<T> values, Player player) {
		final Config config = ItemCreator.getInstance().getApi().getItemConfig(item);
		final @Nullable List<T> parsed = values;
		config.set(getId(), parsed);
		return true;
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	@Override
	public boolean save(CustomItem item, String rawValue, Player player) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<T> convert(String input, Player player) {
		throw new UnsupportedOperationException();
	}
}
