package info.itsthesky.itemcreator.core.gui;

import com.cryptomorin.xseries.XMaterial;
import dev.dbassett.skullcreator.SkullCreator;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.api.properties.base.ItemProperty;
import info.itsthesky.itemcreator.api.properties.base.MultipleItemProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import info.itsthesky.itemcreator.utils.Pagination;
import info.itsthesky.itemcreator.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;

import java.util.*;

public class EditorGUI extends FastInv {

	private static final HashMap<UUID, Integer> playersPages = new HashMap<>();

	public EditorGUI(final CustomItem item, final boolean openedFromGUI, final Player player) {
		super(9 * 6, LangLoader.get().format("gui.title.editor", item.getId()));
		final Pagination<ItemProperty> properties = new Pagination<>(22, ItemCreator.getInstance().getRegisteredProperties().values());
		setItems(getBorders(), new ItemBuilder(XMaterial.GREEN_STAINED_GLASS_PANE.parseItem())
				.name(Utils.colored("&1"))
				.build());

		setItem(4, new ItemBuilder(item.asItem())
				.addLore(LangLoader.get().formatsList("gui.items.give_info", item.getId()))
				.build(), ev -> ev.getWhoClicked().getInventory().addItem(item.asItem()));
		setItem(0, new ItemBuilder(XMaterial.ARROW.parseMaterial())
				.name(LangLoader.get().format("gui.items.back")).build(), ev -> {
			if (openedFromGUI) {
				new ItemListGUI(ItemCreator.getInstance().getApi().loadAllItems())
						.open((Player) ev.getWhoClicked());
			} else {
				ev.getWhoClicked().closeInventory();
			}
		});

		setItem(49, SkullCreator.itemWithBase64(new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
						.name(LangLoader.get().format("gui.items.page_info.name"))
						.lore(LangLoader.get().formatsList("gui.items.page_info.lore",
								getPlayerPage(player) + 1,
								properties.totalPages(),
								properties.getPage(getPlayerPage(player)).size(),
								ItemCreator.getInstance().getRegisteredProperties().size()))
				.build(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0="));

		final ItemProperty enabledProperty = item.getProperties()
				.stream()
				.filter(prop -> prop.getId().equals("enabled"))
				.findAny()
				.orElse(null);
		setItem(8, enabledProperty.asMenuItem(), ev -> enabledProperty.onEditorClick(ev, item));

		setItem(45, SkullCreator.itemWithBase64(new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
				.name(LangLoader.get().format("gui.items.previous"))
				.build(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWFlNzg0NTFiZjI2Y2Y0OWZkNWY1NGNkOGYyYjM3Y2QyNWM5MmU1Y2E3NjI5OGIzNjM0Y2I1NDFlOWFkODkifX19"), ev -> {
			if (getPlayerPage(player) <= 0)
				player.sendMessage(LangLoader.get().format("messages.first_page"));
			else {
				playersPages.put(player.getUniqueId(), getPlayerPage(player) - 1);
				new EditorGUI(item, openedFromGUI, player).open(player);
			}
		});

		setItem(53, SkullCreator.itemWithBase64(new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
				.name(LangLoader.get().format("gui.items.next"))
				.build(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE3ZjM2NjZkM2NlZGZhZTU3Nzc4Yzc4MjMwZDQ4MGM3MTlmZDVmNjVmZmEyYWQzMjU1Mzg1ZTQzM2I4NmUifX19"), ev -> {
			if (getPlayerPage(player) + 1 >= properties.totalPages())
				player.sendMessage(LangLoader.get().format("messages.last_page"));
			else {
				playersPages.put(player.getUniqueId(), getPlayerPage(player) + 1);
				new EditorGUI(item, openedFromGUI, player).open(player);
			}
		});

		int i = -1;
		final List<Integer> slots =
				Arrays.asList(19, 20, 21, 22, 23, 24, 25,
						      28, 29, 30, 31, 32, 33, 34,
						      37, 38, 39, 40, 41, 42, 43);
		for (ItemProperty property : properties.getPage(getPlayerPage(player))) {
			// Skip enabled since we put this property somewhere else
			if (property.getId().equals("enabled"))
				continue;
			i++;
			final Object value = item.getPropertyValue(property);
			final List<String> lores = new ArrayList<>();
			final List errors = property.isCompatible(item);
			final ItemBuilder builder = new ItemBuilder(property.asMenuItem())
					.flags(ItemFlag.HIDE_ENCHANTS);

			if (errors.isEmpty()) {
				if (property.isSimple()) {
					lores.addAll(
							LangLoader.get().formatsList("property.general_lore",
									(value instanceof Boolean
											? Utils.colored((((Boolean) value)
											? LangLoader.get().format("true")
											: LangLoader.get().format("false")))
											: (value == null ? "None" : property.asString(value))))
					);
					if (property.allowClearing())
						lores.addAll(Arrays.asList("",
								LangLoader.get().format("property.clear_info")));
				} else {
					final MultipleItemProperty mitem = (MultipleItemProperty) property;
					for (String line : LangLoader.get().formatsList("property.general_multiple_lore")) {
						line = line.replace("%1", mitem.getActionLore());
						if (line.contains("%2"))
							for (Object s : mitem.formatGUI(item.getPropertyValue(mitem)))
								lores.add(Utils.colored(s.toString()));
						else
							lores.add(Utils.colored(line));
					}
				}
				if (value instanceof Boolean && ((Boolean) value))
					builder.enchant(Enchantment.LOOT_BONUS_BLOCKS);
			} else {
				lores.add("");
				lores.addAll(errors);
				lores.add("");
				builder.type(XMaterial.BARRIER.parseMaterial());
			}
			builder.addLore(lores);
			setItem(slots.get(i), builder.build(), ev -> {
				if (errors.isEmpty()) {
					if (ev.getClick().equals(ClickType.DROP)) {
						if (!property.allowClearing())
							ev.getWhoClicked().sendMessage(LangLoader.get().format("messages.clearing_not_allowed"));
						else {
							property.setInternalValue(item, property.getDefaultValue());
							item.setPropertyValue(property, property.getDefaultValue());
							ev.getWhoClicked().sendMessage(LangLoader.get().format("messages.success"));
							new EditorGUI(item, openedFromGUI, player).open((Player) ev.getWhoClicked());
						}
					} else
						property.onEditorClick(ev, item);
				} else {
					ev.getWhoClicked().sendMessage(LangLoader.get().format("messages.property_require_info"));
					for (Object error : errors.stream().map(Object::toString).toList())
						ev.getWhoClicked().sendMessage(LangLoader.get().format("messages.property_require_format",
								Utils.colored(error.toString())));
				}
			});
		}
	}

	private static int getPlayerPage(Player player) {
		return playersPages.getOrDefault(player.getUniqueId(), 0);
	}
}
