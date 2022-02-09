package info.itsthesky.itemcreator.core.properties.flags;

import com.cryptomorin.xseries.XMaterial;
import fr.mrmicky.fastinv.ItemBuilder;
import info.itsthesky.itemcreator.api.properties.simple.SimpleStateProperty;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class HideUnbreakableProperty extends SimpleStateProperty {
	@Override
	public String getId() {
		return "hide_unbreakable";
	}

	@Override
	public ItemStack apply(ItemStack item, Boolean value) {
		return value ? new ItemBuilder(item).flags(ItemFlag.HIDE_UNBREAKABLE).build() : item;
	}

	@Override
	public @Nullable Boolean fromBukkit(ItemStack stack) {
		return stack.getItemMeta().getItemFlags().contains(ItemFlag.HIDE_UNBREAKABLE);
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
		return XMaterial.IRON_INGOT;
	}
}
