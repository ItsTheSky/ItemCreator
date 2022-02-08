package info.itsthesky.itemcreator.core;

import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.api.properties.base.ItemProperty;
import info.itsthesky.itemcreator.api.properties.base.MultipleItemProperty;
import info.itsthesky.itemcreator.core.gui.EditorGUI;
import info.itsthesky.itemcreator.core.gui.ItemListGUI;
import info.itsthesky.itemcreator.utils.Utils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CreatorCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (args.length == 0) {
			sendHelp(sender);
			return true;
		}
		final String sub = args[0];

		switch (sub) {
			case "get":
				if (!(sender instanceof Player)) {
					sender.sendMessage(Utils.colored("&cThis command cannot be executed in the console."));
					return false;
				}
				if (args.length == 1) {
					sender.sendMessage(Utils.colored("&cYou must provide the item's ID!"));
					return false;
				}
				Player player = args.length == 2 ? ((Player) sender).getPlayer() : Bukkit
						.getOnlinePlayers()
						.stream()
						.filter(pl -> pl.getName().equals(args[2]))
						.findAny()
						.orElse(null);
				if (player == null)
					player = ((Player) sender).getPlayer();
				String id = args[1];
				@Nullable CustomItem item = ItemCreator.getInstance().getApi().getItemFromId(id);
				if (item == null) {
					sender.sendMessage(Utils.colored("&cThere's no custom item with that id. Use &4/ic list&c to see them all."));
					return false;
				}
				sender.sendMessage(Utils.colored("&aGive complete!"));
				player.getInventory().addItem(item.asItem());
				return true;
			case "menu":
				if (!(sender instanceof Player)) {
					sender.sendMessage(Utils.colored("&cThis command cannot be executed in the console."));
					return false;
				}
				new ItemListGUI(ItemCreator.getInstance().getApi().loadAllItems(), (Player) sender).open((Player) sender);
				return true;
			case "generate_default":
				sender.sendMessage(Utils.colored("&6Generating ..."));
				ItemCreator.getInstance().saveResource("items/cactus_helmet.yml", true);
				ItemCreator.getInstance().saveResource("items/copper_ingot.yml", true);
				ItemCreator.getInstance().saveResource("items/killer.yml", true);
				ItemCreator.getInstance().saveResource("items/golem_spawner.yml", true);
				sender.sendMessage(Utils.colored("&aSuccess! Item generated in &2plugins/ItemCreator/items/&a !"));
				return true;
			case "convert":
				if (!(sender instanceof Player)) {
					sender.sendMessage(Utils.colored("&cThis command cannot be executed in the console."));
					return false;
				}
				player = (Player) sender;
				if (args.length == 1) {
					sender.sendMessage(Utils.colored("&cYou must specify the new custom item's ID."));
					return false;
				}
				id = args[1];
				if (ItemCreator.getInstance().getApi().exits(id)) {
					sender.sendMessage(Utils.colored("&cA Custom Item with that ID already exist."));
					return false;
				}
				if (player.getInventory().getItemInMainHand() == null ||
						player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
					player.sendMessage(Utils.colored("&cThis item cannot be converted."));
					return false;
				}
				final ItemStack stack = player.getInventory().getItemInMainHand().clone();
				item = new CustomItem(id);
				player.sendMessage(Utils.colored("&eLoading properties ..."));
				try {
					int i = 1;
					int max = ItemCreator.getInstance().getRegisteredProperties().values().size();
					for (ItemProperty property : ItemCreator.getInstance().getRegisteredProperties().values()) {
						item.registerProperty(property);
						final Object value = property.supportSerialization(stack)
								? property.fromBukkit(stack)
								: property.getDefaultValue();
						if (value != null) {
							if (property instanceof MultipleItemProperty)
								((MultipleItemProperty) property).saveMultiple(item, (List) value, player);
							else
								property.save(item, value.toString(), player);
							item.setPropertyValue(property, value);
						}
						player.sendMessage(Utils.colored(" &c→ &5[&d"+i+"&7/"+max+"&d] &6Changing &e" + property.getId() + "&6, support: &e" + property.supportSerialization(stack)
								+ "&6, value: &e" + value));
						i++;
					}
					player.sendMessage(Utils.colored("&aFinished!"));
					new EditorGUI(item, true, player).open(player);
				} catch (Exception ex) {
					player.sendMessage(Utils.colored("&4An internal error occured while parsing your item:"),
							Utils.colored("  &c" + ex.getMessage()));
					ex.printStackTrace();
					return false;
				}
				return true;
			case "list":
				if (args.length == 1) {
					sender.sendMessage(Utils.colored("&cEnumeration needed: &6enchantments&c, &6potion_effects"));
					return false;
				}
				switch (args[1]) {
					case "enchantments":
						sender.sendMessage(Utils.colored("&6List of enchantments &e("+ Enchantment.values().length +")&6:"));
						for (Enchantment enchantment : Enchantment.values())
							sender.sendMessage(Utils.colored("  &6→ &e" +
									WordUtils.capitalize(enchantment.getKey().getKey().replace('_', ' ').toLowerCase(Locale.ENGLISH))
									+ " &7(ID: "+enchantment.getName()+")"));
						return true;
					case "potion_effects":
						sender.sendMessage(Utils.colored("&6List of Potion Effects &e("+ PotionEffectType.values().length +")&6:"));
						for (PotionEffectType enchantment : PotionEffectType.values())
							sender.sendMessage(Utils.colored("  &6→ &e" +
									WordUtils.capitalize(enchantment.getName().replace('_', ' ').toLowerCase(Locale.ENGLISH))
									+ " &7(ID: "+enchantment.getId()+")"));
						return true;
					case "entities":
						sender.sendMessage(Utils.colored("&6List of Entities &e("+ EntityType.values().length +")&6:"));
						for (EntityType enchantment : EntityType.values())
							sender.sendMessage(Utils.colored("  &6→ &e" +
									WordUtils.capitalize(enchantment.getName().replace('_', ' ').toLowerCase(Locale.ENGLISH))
									+ " &7(ID: "+enchantment.name()+")"));
						return true;
					case "attributes":
						sender.sendMessage(Utils.colored("&6List of Attributes &e("+ Attribute.values().length +")&6:"));
						for (Attribute attribute : Attribute.values())
							sender.sendMessage(Utils.colored("  &6→ &e" +
									Utils.beauty(attribute)
									+ " &7(ID: "+attribute.name()+")"));
						return true;
					case "operations":
						sender.sendMessage(Utils.colored("&6List of Operations &e("+ AttributeModifier.Operation.values().length +")&6:"));
						for (AttributeModifier.Operation attribute : AttributeModifier.Operation.values())
							sender.sendMessage(Utils.colored("  &6→ &e" +
									Utils.beauty(attribute)
									+ " &7(ID: "+attribute.name()+")"));
						return true;
					case "actions":
						sender.sendMessage(Utils.colored("&6List of Actions &e("+ Action.values().length +")&6:"));
						for (Action attribute : Action.values())
							sender.sendMessage(Utils.colored("  &6→ &e" +
									Utils.beauty(attribute)
									+ " &7(ID: "+attribute.name()+")"));
						return true;
					case "slots":
						sender.sendMessage(Utils.colored("&6List of Equipment Slots &e("+ EquipmentSlot.values().length +")&6:"));
						for (EquipmentSlot attribute : EquipmentSlot.values())
							sender.sendMessage(Utils.colored("  &6→ &e" +
									Utils.beauty(attribute)
									+ " &7(ID: "+attribute.name()+")"));
						return true;
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
				Utils.colored("&6/ic list (enchantments|actions|potion_effects|entities|operations|attributes|slots) &7- &eList possible values of enumeration"),
				Utils.colored("&6/ic convert <item id> &7- &cBETA &eConvert the item you're holding into an ItemCreator item."),
				Utils.colored("&6/ic generate_default &7- &eGenerate the default items ItemCreator provide to see everything it can do."),
				Utils.colored("&1")
		);
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		final List<String> completions = new ArrayList<>();
		if (args.length == 1)
			completions.addAll(Arrays.asList("help", "get", "menu", "list", "generate_default", "convert"));
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
								.collect(Collectors.toList()));
						break;
					case "list":
						completions.addAll(Arrays.asList("enchantments", "actions", "potion_effects", "entities", "slots", "attributes", "operations"));
						break;
				}
			} else if (args.length == 3) {
				switch (args[0]) {
					case "get":
						completions.addAll(Bukkit.getOnlinePlayers()
								.stream()
								.map(HumanEntity::getName)
								.collect(Collectors.toList()));
						break;
				}
			}
		}
		return completions;
	}

}
