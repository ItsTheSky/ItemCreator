package info.itsthesky.itemcreator.core.properties.events;

import com.cryptomorin.xseries.XMaterial;
import de.leonhard.storage.Config;
import de.tr7zw.changeme.nbtapi.NBTItem;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.api.properties.multiple.MultipleNBTProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import info.itsthesky.itemcreator.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandsProperty extends MultipleNBTProperty<CommandsProperty.CommandData> implements Listener {

	public CommandsProperty(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@Override
	public String getId() {
		return "commands";
	}

	@Override
	public boolean allowClearing() {
		return true;
	}

	@Override
	public List<CommandData> getDefaultValue() {
		return new ArrayList<>();
	}

	@Override
	public XMaterial getMaterial() {
		return XMaterial.COMMAND_BLOCK;
	}

	@Override
	public @Nullable CommandData parseSingle(String input, Player player) {
		try {
			return CommandData.serialize(input.replaceFirst(" ", "²"));
		} catch (Exception ex) {
			if (player != null)
				player.sendMessage(LangLoader.get().format("messages.wrong_command"));
			return null;
		}
	}

	@Override
	public String getActionLore() {
		return "Left click to add, right click to remove";
	}

	@Override
	public List<String> formatGUI(List<CommandData> values) {
		return values
				.stream()
				.map(cmd -> Utils.colored("  &6→ &6" + cmd.getCommand() + "&e when &6" + Utils.beauty(cmd.getAction())))
				.collect(Collectors.toList());
	}

	@Override
	public String getParsingKey() {
		return "commands";
	}

	public boolean saveMultiple(CustomItem item, List<CommandData> values, Player player) {
		final Config config = ItemCreator.getInstance().getApi().getItemConfig(item);
		config.set(getId(), values.stream().map(CommandData::toString).collect(Collectors.toList()));
		return true;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onItemClick(PlayerInteractEvent e) {
		/* if (!(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
			return; */
		if (e.getItem() == null || e.getItem().getType().equals(Material.AIR))
			return;
		final Player player = e.getPlayer();
		final NBTItem nbtItem = new NBTItem(e.getItem());
		if (!nbtItem.hasKey(getId()))
			return;
		try {
			final List<CommandData> commands = Stream
					.of(nbtItem.getString(getId()).split("\\|"))
					.map(CommandData::serialize)
					.collect(Collectors.toList());
			for (CommandData cmd : commands)
				if (cmd.getAction().equals(e.getAction()))
					player.performCommand(cmd.getCommand());
		} catch (Exception ignored) {

		}
	}

	@Override
	public NBTItem convert(NBTItem original, List<CommandData> value) {
		original.setString(getId(), value
				.stream()
				.map(CommandData::toString)
				.collect(Collectors.joining("|")));
		return original;
	}

	public static class CommandData {

		private final String command;
		private final Action action;

		public CommandData(String command, Action action) {
			this.command = command;
			this.action = action;
		}

		public static @NotNull CommandData serialize(String input) {
			final String[] values = input.split("²");
			final Action action = Action.valueOf(values[0].toUpperCase(Locale.ROOT).replace(" ", "_"));
			final String command = values[1];
			return new CommandData(command, action);
		}

		public static @NotNull String deserialize(CommandData input) {
			return input.getAction().name() + "²" + input.getCommand();
		}

		public String getCommand() {
			return command;
		}

		public Action getAction() {
			return action;
		}

		@Override
		public String toString() {
			return deserialize(this);
		}
	}

}
