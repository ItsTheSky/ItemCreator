package info.itsthesky.itemcreator.core;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.core.gui.ItemListGUI;
import info.itsthesky.itemcreator.utils.Utils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
				}
			}
		}

		return true;
	}

	public void sendHelp(CommandSender sender) {

	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		return null;
	}

}
