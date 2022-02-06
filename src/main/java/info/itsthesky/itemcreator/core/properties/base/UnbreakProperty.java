package info.itsthesky.itemcreator.core.properties.base;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.api.properties.simple.SimpleMetaStateProperty;
import org.bukkit.inventory.meta.ItemMeta;

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
	public ItemMeta convert(ItemMeta original, Boolean value) {
		original.setUnbreakable(value);
		return original;
	}
}
