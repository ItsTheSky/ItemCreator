package info.itsthesky.itemcreator.core.properties.flags;

import com.cryptomorin.xseries.XMaterial;
import fr.mrmicky.fastinv.ItemBuilder;
import info.itsthesky.itemcreator.api.properties.simple.SimpleStateProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class HideEffectsProperty extends SimpleStateProperty {

	@Override
	public List<String> isCompatible(CustomItem item) {
		final List<String> errors = new ArrayList<>();
		if (!item.getPropertyValue(XMaterial.class, "material").name().contains("POTION"))
			errors.add(LangLoader.get().format("property.material_require", "Any Potion"));
		return errors;
	}

	@Override
	public String getId() {
		return "hide_effects";
	}

	@Override
	public ItemStack apply(ItemStack item, Boolean value) {
		return value ? new ItemBuilder(item).flags(ItemFlag.HIDE_POTION_EFFECTS).build() : item;
	}

	@Override
	public boolean allowClearing() {
		return false;
	}

	@Override
	public Boolean getDefaultValue() {
		return false;
	}

	@Override
	public XMaterial getMaterial() {
		return XMaterial.BLAZE_POWDER;
	}
}
