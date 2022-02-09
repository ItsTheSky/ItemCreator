package info.itsthesky.itemcreator.core.properties.flags;

import com.cryptomorin.xseries.XMaterial;
import fr.mrmicky.fastinv.ItemBuilder;
import info.itsthesky.itemcreator.api.properties.simple.SimpleStateProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HideAttributesProperty extends SimpleStateProperty {

	@Override
	public @Nullable Boolean fromBukkit(ItemStack stack) {
		return stack.getItemMeta().getItemFlags().contains(ItemFlag.HIDE_ATTRIBUTES);
	}

	@Override
	public String getId() {
		return "hide_attributes";
	}

	@Override
	public ItemStack apply(ItemStack item, Boolean value) {
		return value ? new ItemBuilder(item).flags(ItemFlag.HIDE_ATTRIBUTES).build() : item;
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
		return XMaterial.REPEATER;
	}
}
