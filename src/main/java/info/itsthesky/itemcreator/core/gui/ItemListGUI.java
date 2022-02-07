package info.itsthesky.itemcreator.core.gui;

import com.cryptomorin.xseries.XMaterial;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import info.itsthesky.itemcreator.utils.ChatWaiter;
import info.itsthesky.itemcreator.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class ItemListGUI extends FastInv {
	public ItemListGUI(final List<CustomItem> items) {
		super(9*6, LangLoader.get().format("gui.title.list", items.size()));
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
				if (ItemCreator.getInstance().getApi().exits(id))
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

		int i = 19;
		for (CustomItem item : items) {
			setItem(i, new ItemBuilder(item.asItem())
					.addLore(item.isEnabled() ?
							LangLoader.get().formatsList("gui.items.general_lore")
							: new ArrayList<>())
					.build(), ev -> {
				if (item.isEnabled()) {
					switch (ev.getClick()) {
						case LEFT, SHIFT_LEFT -> new EditorGUI(item, true, ((Player) ev.getWhoClicked())).open((Player) ev.getWhoClicked());
						case RIGHT, SHIFT_RIGHT -> ev.getWhoClicked().getInventory().addItem(item.asItem());
						case MIDDLE -> {
							item.toggleEnabled();
							new ItemListGUI(ItemCreator.getInstance().getApi().loadAllItems()).open((Player) ev.getWhoClicked());
						}
						case CONTROL_DROP -> {
							ItemCreator.getInstance().getApi().deleteItem(item);
							new ItemListGUI(ItemCreator.getInstance().getApi().loadAllItems()).open((Player) ev.getWhoClicked());
						}
					}
				} else {
					if (ev.getClick().equals(ClickType.MIDDLE)) {
						item.toggleEnabled();
						new ItemListGUI(ItemCreator.getInstance().getApi().loadAllItems()).open((Player) ev.getWhoClicked());
					}
				}
			});
			i++;
		}
	}

	@Override
	protected void onClick(InventoryClickEvent event) {
		event.setCancelled(true);
	}
}
