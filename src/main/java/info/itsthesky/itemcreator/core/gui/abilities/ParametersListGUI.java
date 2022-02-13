package info.itsthesky.itemcreator.core.gui.abilities;

import com.cryptomorin.xseries.XMaterial;
import dev.dbassett.skullcreator.SkullCreator;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.api.abilities.Ability;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import info.itsthesky.itemcreator.utils.ChatWaiter;
import info.itsthesky.itemcreator.utils.Pagination;
import info.itsthesky.itemcreator.utils.Utils;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ParametersListGUI extends FastInv {

	private static final HashMap<UUID, Integer> playersPages = new HashMap<>();
	private static int getPlayerPage(Player player) {
		return playersPages.getOrDefault(player.getUniqueId(), 0);
	}

	public ParametersListGUI(final Ability ability, final CustomItem item, final Player player) {
		super(9 * 6, LangLoader.get().format("gui.title.parameters"));
		final Pagination<Ability.AbilityParameter> parameters = new Pagination<>(22, item.getAbilityParameters(ability));
		setItems(getBorders(), new ItemBuilder(XMaterial.GREEN_STAINED_GLASS_PANE.parseItem())
				.name(Utils.colored("&1"))
				.build());

		setItem(0, new ItemBuilder(XMaterial.ARROW.parseMaterial())
				.name(LangLoader.get().format("gui.items.back")).build(), ev -> {
			new AbilityListGUI(item, player).open(player);
		});

		setItem(49, SkullCreator.itemWithBase64(new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
				.name(LangLoader.get().format("gui.items.page_info.name"))
				.lore(LangLoader.get().formatsList("gui.items.page_info.lore",
						getPlayerPage(player) + 1,
						parameters.totalPages(),
						parameters.getPage(getPlayerPage(player)).size(),
						ItemCreator.getInstance().getRegisteredProperties().size()))
				.build(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdlZDY2ZjVhNzAyMDlkODIxMTY3ZDE1NmZkYmMwY2EzYmYxMWFkNTRlZDVkODZlNzVjMjY1ZjdlNTAyOWVjMSJ9fX0="));

		setItem(45, SkullCreator.itemWithBase64(new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
				.name(LangLoader.get().format("gui.items.previous"))
				.build(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWFlNzg0NTFiZjI2Y2Y0OWZkNWY1NGNkOGYyYjM3Y2QyNWM5MmU1Y2E3NjI5OGIzNjM0Y2I1NDFlOWFkODkifX19"), ev -> {
			if (getPlayerPage(player) <= 0)
				player.sendMessage(LangLoader.get().format("messages.first_page"));
			else {
				playersPages.put(player.getUniqueId(), getPlayerPage(player) - 1);
				new ParametersListGUI(ability, item, player).open(player);
			}
		});

		setItem(53, SkullCreator.itemWithBase64(new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem())
				.name(LangLoader.get().format("gui.items.next"))
				.build(), "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE3ZjM2NjZkM2NlZGZhZTU3Nzc4Yzc4MjMwZDQ4MGM3MTlmZDVmNjVmZmEyYWQzMjU1Mzg1ZTQzM2I4NmUifX19"), ev -> {
			if (getPlayerPage(player) + 1 >= parameters.totalPages())
				player.sendMessage(LangLoader.get().format("messages.last_page"));
			else {
				playersPages.put(player.getUniqueId(), getPlayerPage(player) + 1);
				new ParametersListGUI(ability, item, player).open(player);
			}
		});

		int i = -1;
		final List<Integer> slots =
				Arrays.asList(19, 20, 21, 22, 23, 24, 25,
						28, 29, 30, 31, 32, 33, 34,
						37, 38, 39, 40, 41, 42, 43);
		for (Ability.AbilityParameter parameter : parameters.getPage(getPlayerPage(player))) {
			i++;
			setItem(slots.get(i), parameter.getParameter().getRepresentation().asItem(), ev -> {
				player.closeInventory();
				player.sendMessage(LangLoader.get().format("messages.enter_parameter_value"));
				new ChatWaiter(player, event -> {
					final String rawValue = event.getMessage();
					final Object value = parameter.getParameter().parse(rawValue, player);
					if (value != null) {
						item.setAbilityParameter(ability, parameter, value);
						item.saveAbilities();
						player.sendMessage(LangLoader.get().format("messages.parameter_changed"));
					}
					new ParametersListGUI(ability, item, player).open(player);
				}, true, true);
			});
		}
	}
}
