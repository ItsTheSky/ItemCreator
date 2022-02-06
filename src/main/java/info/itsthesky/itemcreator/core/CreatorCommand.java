package info.itsthesky.itemcreator.core;

import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.core.gui.ItemListGUI;
import info.itsthesky.itemcreator.utils.Utils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CreatorCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (args.length == 0) {
			sendHelp(sender);
			return true;
		}
		final String sub = args[0];

		switch (sub) {
			case "get" -> {
				if (!(sender instanceof Player)) {
					sender.sendMessage(Utils.colored("&cThis command cannot be executed in the console."));
					return false;
				}
				if (args.length == 1) {
					sender.sendMessage(Utils.colored("&cYou must provide the item's ID!"));
					return false;
				}
				final String id = args[1];
				final @Nullable CustomItem item = ItemCreator.getInstance().getApi().getItemFromId(id);
				if (item == null) {
					sender.sendMessage(Utils.colored("&cThere's no custom item with that id. Use &4/ic list&c to see them all."));
					return false;
				}
				sender.sendMessage(Utils.colored("&aGive complete!"));
				((Player) sender).getInventory().addItem(item.asItem());
				return true;
			}
			case "menu" -> {
				if (!(sender instanceof Player)) {
					sender.sendMessage(Utils.colored("&cThis command cannot be executed in the console."));
					return false;
				}
				new ItemListGUI(ItemCreator.getInstance().getApi().loadAllItems()).open((Player) sender);
				return true;
			}
			case "generate_default" -> {
				sender.sendMessage(Utils.colored("&6Generating ..."));
				ItemCreator.getInstance().saveResource("items/cactus_helmet.yml", true);
				ItemCreator.getInstance().saveResource("items/copper_ingot.yml", true);
				ItemCreator.getInstance().saveResource("items/killer.yml", true);
				ItemCreator.getInstance().saveResource("items/golem_spawner.yml", true);
				sender.sendMessage(Utils.colored("&aSuccess! Item generated in &2plugins/ItemCreator/items/&a !"));
				return true;
			}
			case "list" -> {
				if (args.length == 1) {
					sender.sendMessage(Utils.colored("&cEnumeration needed: &6enchantments&c, &6potion_effects"));
					return false;
				}
				switch (args[1]) {
					case "enchantments" -> {
						sender.sendMessage(Utils.colored("&6List of enchantments &e("+ Enchantment.values().length +")&6:"));
						for (Enchantment enchantment : Enchantment.values())
							sender.sendMessage(Utils.colored("  &6→ &e" +
									WordUtils.capitalize(enchantment.getKey().getKey().replace('_', ' ').toLowerCase(Locale.ENGLISH))
									+ " &7(ID: "+enchantment.getName()+")"));
						return true;
					}
					case "potion_effects" -> {
						sender.sendMessage(Utils.colored("&6List of Potion Effects &e("+ PotionEffectType.values().length +")&6:"));
						for (PotionEffectType enchantment : PotionEffectType.values())
							sender.sendMessage(Utils.colored("  &6→ &e" +
									WordUtils.capitalize(enchantment.getName().replace('_', ' ').toLowerCase(Locale.ENGLISH))
									+ " &7(ID: "+enchantment.getId()+")"));
						return true;
					}
					case "entities" -> {
						sender.sendMessage(Utils.colored("&6List of Entities &e("+ EntityType.values().length +")&6:"));
						for (EntityType enchantment : EntityType.values())
							sender.sendMessage(Utils.colored("  &6→ &e" +
									WordUtils.capitalize(enchantment.getName().replace('_', ' ').toLowerCase(Locale.ENGLISH))
									+ " &7(ID: "+enchantment.name()+")"));
						return true;
					}
				}
			}
		}
		sendHelp(sender);
		return true;
	}

	public void sendHelp(CommandSender sender) {
		sender.sendMessage(Utils.colored("&6&lItemCreator &ev" + ItemCreator.getInstance().getDescription().getVersion()
		+ "&e made by &6ItsTheSky#1234:"));
		sender.sendMessage(
				Utils.colored("&1"),
				Utils.colored("&6/ic help &7- &eShow this help page"),
				Utils.colored("&6/ic get <item id> &7- &eGet the specified custom item"),
				Utils.colored("&6/ic menu &7- &eOpen the main ItemCreator menu"),
				Utils.colored("&6/ic list (enchantments|potion_effects|entities) &7- &eList possible values of enumeration"),
				Utils.colored("&6/ic generate_default &7- &eGenerate the default items ItemCreator provide to see everything it can do."),
				Utils.colored("&1")
		);
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		final List<String> completions = new ArrayList<>();
		if (args.length == 1)
			completions.addAll(Arrays.asList("help", "get", "menu", "list", "generate_default"));
		else {
			if (args.length == 2) {
				switch (args[0]) {
					case "get":
						completions.addAll(ItemCreator
								.getInstance()
								.getApi()
								.loadAllItems()
								.stream()
								.map(CustomItem::getId)
								.toList());
						break;
					case "list":
						completions.addAll(Arrays.asList("enchantments", "potion_effects", "entities"));
						break;
				}
			}
		}
		return completions;
	}

}
