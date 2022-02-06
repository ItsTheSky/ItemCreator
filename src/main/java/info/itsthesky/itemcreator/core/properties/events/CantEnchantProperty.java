package info.itsthesky.itemcreator.core.properties.events;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.api.properties.simple.SimpleNBTStateProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.langs.LangLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CantEnchantProperty extends SimpleNBTStateProperty {
	@Override
	public String getId() {
		return "cant_enchant";
	}

	@Override
	public XMaterial getMaterial() {
		return XMaterial.ENCHANTING_TABLE;
	}

	@Override
	public List<String> isCompatible(CustomItem item) {
		if (!item.hasPropertySet("ic_tag"))
			return Collections.singletonList(LangLoader.get().format("property.property_require",
					LangLoader.get().format("property.ic_tag.name")));
		return new ArrayList<>();
	}
}
