package info.itsthesky.itemcreator.core.properties.flags;

import com.cryptomorin.xseries.XMaterial;
import fr.mrmicky.fastinv.ItemBuilder;
import info.itsthesky.itemcreator.api.properties.simple.SimpleStateProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class HideEnchantProperty extends SimpleStateProperty {
	@Override
	public String getId() {
		return "hide_enchants";
	}

	@Override
	public ItemStack apply(ItemStack item, Boolean value) {
		return value ? new ItemBuilder(item).flags(ItemFlag.HIDE_ENCHANTS).build() : item;
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
		return XMaterial.PAPER;
	}
}
