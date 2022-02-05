package info.itsthesky.itemcreator.core.properties;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.api.properties.multiple.MultipleMetaProperty;
import info.itsthesky.itemcreator.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LoreProperty extends MultipleMetaProperty<String> {

	@Override
	public String getId() {
		return "lore";
	}

	@Override
	public List<String> getDefaultValue() {
		return new ArrayList<>();
	}

	@Override
	public boolean allowClearing() {
		return true;
	}

	@Override
	public XMaterial getMaterial() {
		return XMaterial.BOOK;
	}

	@Override
	public @Nullable String parseSingle(String input, Player player) {
		return Utils.colored(input);
	}

	@Override
	public String getActionLore() {
		return "Left click for adding, right click for removing.";
	}

	@Override
	public List<String> formatGUI(List<String> values) {
		return values
				.stream()
				.map(str -> Utils.colored("  &6→ &r" + str))
				.collect(Collectors.toList());
	}

	@Override
	public ItemMeta convert(ItemMeta original, List<String> value) {
		original.setLore(value);
		return original;
	}

	@Override
	public String getParsingKey() {
		return "lore";
	}
}
