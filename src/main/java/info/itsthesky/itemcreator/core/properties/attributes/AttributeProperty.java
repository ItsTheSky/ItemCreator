package info.itsthesky.itemcreator.core.properties.attributes;

import com.cryptomorin.xseries.XMaterial;
import de.leonhard.storage.Config;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.api.properties.multiple.MultipleMetaProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import info.itsthesky.itemcreator.utils.Utils;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public class AttributeProperty extends MultipleMetaProperty<AttributeData> {

	@Override
	public String getId() {
		return "attributes";
	}

	@Override
	public boolean allowClearing() {
		return true;
	}

	@Override
	public List<AttributeData> getDefaultValue() {
		return new ArrayList<>();
	}

	@Override
	public XMaterial getMaterial() {
		return XMaterial.COMPARATOR;
	}

	@Override
	public @Nullable AttributeData parseSingle(String input, Player player) {
		final String[] values = input.split(":");

		try {
			final Attribute attribute = Attribute.valueOf(safe(values[0]));
			final AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(safe(values[1]));
			final double value = Double.parseDouble(values[2]);
			final EquipmentSlot slot = values.length == 3 ? null : EquipmentSlot.valueOf(safe(values[3]));

			return new AttributeData(attribute, operation, value, slot);
		} catch (Exception ex) {
			if (player != null)
				player.sendMessage(LangLoader.get().format("messages.wrong_attribute"));
			return null;
		}
	}

	private String safe(String input) {
		return input.replace(" ", "_").toUpperCase(Locale.ROOT);
	}

	@Override
	public String getActionLore() {
		return "Left click to add, right click to remove";
	}

	@Override
	public List<String> formatGUI(List<AttributeData> values) {
		return values.stream()
				.map(data -> Utils.colored("  &6→ &6" + Utils.beauty(data.getAttribute())
						+ "&6: &e" + data.getAmount()
						+ "&6, &e" + Utils.beauty(data.getOperation())
						+ "&6, &e" + (data.getSlot() == null ? "None" : Utils.beauty(data.getSlot()))))
				.collect(Collectors.toList());
	}

	@Override
	public String getParsingKey() {
		return "attributes";
	}

	@Override
	public boolean saveMultiple(CustomItem item, List<AttributeData> values, Player player) {
		final Config config = ItemCreator.getInstance().getApi().getItemConfig(item);
		final @Nullable List<AttributeData> parsed = values;
		config.set(getId(), parsed.stream().map(AttributeData::toString).collect(Collectors.toList()));
		return true;
	}

	@Override
	public ItemMeta convert(ItemMeta original, List<AttributeData> values) {
		for (AttributeData data : values)
			original.addAttributeModifier(data.getAttribute(), new AttributeModifier(
					UUID.randomUUID(),
					data.getAttribute().getKey().getKey(),
					data.getAmount(),
					data.getOperation(),
					data.getSlot()
			));
		return original;
	}
}
