package info.itsthesky.itemcreator.core.properties.base;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.api.properties.simple.SimpleMetaStateProperty;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

public class UnbreakProperty extends SimpleMetaStateProperty {

	@Override
	public String getId() {
		return "unbreakable";
	}

	@Override
	public XMaterial getMaterial() {
		return XMaterial.ANVIL;
	}

	@Override
	public @Nullable Boolean fromBukkit(ItemStack stack) {
		return stack.getItemMeta().isUnbreakable();
	}

	@Override
	public ItemMeta convert(ItemMeta original, Boolean value) {
		original.setUnbreakable(value);
		return original;
	}
}
