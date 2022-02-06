package info.itsthesky.itemcreator.core.properties.base;

import com.cryptomorin.xseries.XMaterial;
import de.leonhard.storage.Config;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.api.properties.simple.SimpleMetaProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.gui.EditorGUI;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import info.itsthesky.itemcreator.utils.ChatWaiter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PotionColorProperty extends SimpleMetaProperty<Color> {
	private static final Pattern rgb = Pattern.compile("(.+),( )?(.+),( )?(.+)");

	@Override
	public String getId() {
		return "potion_color";
	}

	@Override
	public String asString(Color value) {
		return value.getRed() + ", " + value.getGreen() + ", " + value.getBlue();
	}

	@Override
	public List<String> isCompatible(CustomItem item) {
		final List<String> errors = new ArrayList<>();
		if (!item.getPropertyValue(XMaterial.class, "material").name().contains("POTION"))
			errors.add(LangLoader.get().format("property.material_require", "Any Potion"));
		return errors;
	}

	@Override
	public boolean allowClearing() {
		return true;
	}

	@Override
	public Color getDefaultValue() {
		return null;
	}

	@Override
	public void onEditorClick(InventoryClickEvent e, CustomItem item) {
		final Player player = (Player) e.getWhoClicked();
		player.closeInventory();
		player.sendMessage(LangLoader.get().formats("messages.enter_color"));
		new ChatWaiter(player, ev -> {
			final Color color = convert(ev.getMessage(), player);
			if (color != null) {
				setInternalValue(item, color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
				//save(item, color.getRed() + ", " + color.getGreen() + ", " + color.getBlue(), player);
				item.setPropertyValue(this, color);
				player.sendMessage(LangLoader.get().formats("messages.success"));
			}
			new EditorGUI(item, true).open(player);
		}, true, true);
	}

	@Override
	public XMaterial getMaterial() {
		return XMaterial.GLOWSTONE_DUST;
	}

	private Color parse(String input) {
		final Matcher matcher = rgb.matcher(input);
		if (!matcher.matches()) {
			try {
				return (Color) Color.class.getDeclaredField(input.toUpperCase(Locale.ROOT).replace(" ", "_")).get(null);
			} catch (Exception ex) {
				return null;
			}
		} else {
			try {
				final String[] values = input.replace(" ", "").split(",");
				final int red = Integer.parseInt(values[0]);
				final int green = Integer.parseInt(values[1]);
				final int blue = Integer.parseInt(values[2]);
				return Color.fromRGB(red, green, blue);
			} catch (Exception ex) {
				return null;
			}
		}
	}

	@Override
	public @Nullable Color convert(String input, Player player) {
		final Color color = parse(input);
		if (color == null && player != null)
			player.sendMessage(LangLoader.get().format("messages.wrong_color"));
		return color;
	}

	@Override
	public ItemMeta convert(ItemMeta original, Color value) {
		final PotionMeta meta = (PotionMeta) original;
		meta.setColor(value);
		return meta;
	}
}
