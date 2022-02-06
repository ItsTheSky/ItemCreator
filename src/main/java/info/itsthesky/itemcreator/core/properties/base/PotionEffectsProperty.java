package info.itsthesky.itemcreator.core.properties.base;

import com.cryptomorin.xseries.XMaterial;
import de.leonhard.storage.Config;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.api.properties.multiple.MultipleMetaProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import info.itsthesky.itemcreator.utils.Potion;
import info.itsthesky.itemcreator.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PotionEffectsProperty extends MultipleMetaProperty<Potion> {
	@Override
	public String getId() {
		return "potion_effects";
	}

	@Override
	public boolean allowClearing() {
		return true;
	}

	@Override
	public List<Potion> getDefaultValue() {
		return new ArrayList<>();
	}

	@Override
	public List<String> isCompatible(CustomItem item) {
		final List<String> errors = new ArrayList<>();
		if (!item.getPropertyValue(XMaterial.class, "material").name().contains("POTION"))
			errors.add(LangLoader.get().format("property.material_require", "Any Potion"));
		return errors;
	}

	@Override
	public XMaterial getMaterial() {
		return XMaterial.BLAZE_ROD;
	}

	public boolean save(CustomItem item, List<Potion> values, Player player) {
		final Config config = ItemCreator.getInstance().getApi().getItemConfig(item);
		config.set(getId(), values
				.stream()
				.map(potion -> potion.getType().getName() + " " +
						potion.getDuration() + " " + potion.getAmplifier())
				.collect(Collectors.toList()));
		return true;
	}

	@Override
	public @Nullable Potion parseSingle(String input, Player player) {
		try {
			final String[] values = input.split(" ");
			final PotionEffectType type = PotionEffectType.getByName(values[0].replace("_", " "));
			if (type == null)
				throw new UnsupportedOperationException();
			final int duration = Integer.parseInt(values[1]);
			final int amplifier = Integer.parseInt(values[2]);
			return new Potion(type, duration, amplifier);
		} catch (Exception ex) {
			if (player != null)
				player.sendMessage(LangLoader.get().format("messages.wrong_potion_effect"));
			return null;
		}
	}

	@Override
	public String getActionLore() {
		return "Left click to add, right click to remove.";
	}

	@Override
	public List<String> formatGUI(List<Potion> values) {
		return values
				.stream()
				.map(str -> Utils.colored("  &6→ &eEffect '&c"+str.getType().getName()+"&e'"
						+ ", Duration: &c" + str.getDuration() + "&e, Amplifier: &c" + str.getAmplifier()))
				.collect(Collectors.toList());
	}

	@Override
	public String getParsingKey() {
		return "potion_effects";
	}

	@Override
	public ItemMeta convert(ItemMeta original, List<Potion> value) {
		final PotionMeta meta = (PotionMeta) original;
		for (Potion potion : value)
			meta.addCustomEffect(new PotionEffect(potion.getType(), potion.getDuration(), potion.getAmplifier()), true);
		return meta;
	}
}
