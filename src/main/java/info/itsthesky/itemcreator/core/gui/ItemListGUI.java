package info.itsthesky.itemcreator.core.gui;

import com.cryptomorin.xseries.XMaterial;
import dev.dbassett.skullcreator.SkullCreator;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import info.itsthesky.itemcreator.utils.ChatWaiter;
import info.itsthesky.itemcreator.utils.Pagination;
import info.itsthesky.itemcreator.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ItemListGUI extends FastInv {

	private static final HashMap<UUID, Integer> playersPages = new HashMap<>();

	public ItemListGUI(final List<CustomItem> items, Player opener) {
		super(9*6, LangLoader.get().format("gui.title.list", items.size()));
		final Pagination<CustomItem> pagination = new Pagination<>(22, items);
		setItems(getBorders(), new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem())
				.name(Utils.colored("&1"))
				.build());

		setItem(8, new ItemBuilder(XMaterial.SLIME_BALL.parseItem())
				.name(LangLoader.get().format("gui.items.create.name"))
				.lore(LangLoader.get().formatsList("gui.items.create.lore"))
				.build(), ev -> {
			final Player player = (Player) ev.getWhoClicked();
			player.closeInventory();
			player.sendMessage(LangLoader.get().format("messages.enter_item_id"));
			new ChatWaiter(player, e -> {
				final String id = e.getMessage();
				if (ItemCreator.getInstance().getApi().exists(id))
					player.sendMessage(LangLoader.get().format("messages.item_already_exist"));
				else {
					final CustomItem item = ItemCreator.getInstance().getApi().getItemFromId(id, true);
					player.sendMessage(LangLoader.get().format("messages.item_created"));
					new EditorGUI(item, true, player).open(player);
					return;
				}
				open(player);
			}, true, true);
		});

		setItem(4, new ItemBuilder(XMaterial.PAPER.parseMaterial())
				.name(LangLoader.get().format("gui.items.list_info.name"))
				.lore(LangLoader.get().formatsList("gui.items.list_info.lore"))
				.build());

		if (pagination.exists(0)) {
			setItem(45, SkullCreator.itemWithBase64(new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
					.name(LangLoader.get().format("gui.items.previous"))
					.build(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWFlNzg0NTFiZjI2Y2Y0OWZkNWY1NGNkOGYyYjM3Y2QyNWM5MmU1Y2E3NjI5OGIzNjM0Y2I1NDFlOWFkODkifX19"), ev -> {
				if (getPlayerPage(opener) <= 0)
					opener.sendMessage(LangLoader.get().format("messages.first_page"));
				else {
					playersPages.put(opener.getUniqueId(), getPlayerPage(opener) - 1);
					new ItemListGUI(items, opener).open(opener);
				}
			});

			setItem(49, SkullCreator.itemWithBase64(new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
					.name(LangLoader.get().format("gui.items.page_info.name"))
					.lore(LangLoader.get().formatsList("gui.items.page_info.lore",
							getPlayerPage(opener) + 1,
							pagination.totalPages(),
							pagination.getPage(getPlayerPage(opener)).size(),
							items.size()))
					.build(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0="));


			setItem(53, SkullCreator.itemWithBase64(new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
					.name(LangLoader.get().format("gui.items.next"))
					.build(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE3ZjM2NjZkM2NlZGZhZTU3Nzc4Yzc4MjMwZDQ4MGM3MTlmZDVmNjVmZmEyYWQzMjU1Mzg1ZTQzM2I4NmUifX19"), ev -> {
				if (getPlayerPage(opener) + 1 >= pagination.totalPages())
					opener.sendMessage(LangLoader.get().format("messages.last_page"));
				else {
					playersPages.put(opener.getUniqueId(), getPlayerPage(opener) + 1);
					new ItemListGUI(items, opener).open(opener);
				}
			});

			int i = 19;
			for (CustomItem item : items) {
				setItem(i, new ItemBuilder(item.asItem())
						.addLore(item.isEnabled() ?
								LangLoader.get().formatsList("gui.items.general_lore")
								: new ArrayList<>())
						.build(), ev -> {
					if (item.isEnabled()) {
						switch (ev.getClick()) {
							case LEFT:
							case SHIFT_LEFT:
								new EditorGUI(item, true, ((Player) ev.getWhoClicked())).open((Player) ev.getWhoClicked());
								break;
							case RIGHT:
							case SHIFT_RIGHT:
								ev.getWhoClicked().getInventory().addItem(item.asItem());
								break;
							case MIDDLE:
								item.toggleEnabled();
								new ItemListGUI(ItemCreator.getInstance().getApi().loadAllItems(), opener).open((Player) ev.getWhoClicked());
								break;
							case CONTROL_DROP:
								ItemCreator.getInstance().getApi().deleteItem(item);
								new ItemListGUI(ItemCreator.getInstance().getApi().loadAllItems(), opener).open((Player) ev.getWhoClicked());
								break;
						}
					} else {
						if (ev.getClick().equals(ClickType.MIDDLE)) {
							item.toggleEnabled();
							new ItemListGUI(ItemCreator.getInstance().getApi().loadAllItems(), opener).open((Player) ev.getWhoClicked());
						}
					}
				});
				i++;
			}
		} else {
			setItem(31, new ItemBuilder(XMaterial.BARRIER.parseItem())
					.name(LangLoader.get().format("gui.items.no_items.name"))
					.lore(LangLoader.get().formatsList("gui.items.no_items.lore"))
					.build());
		}
	}

	@Override
	protected void onClick(InventoryClickEvent event) {
		event.setCancelled(true);
	}

	private static int getPlayerPage(Player player) {
		return playersPages.getOrDefault(player.getUniqueId(), 0);
	}
}
