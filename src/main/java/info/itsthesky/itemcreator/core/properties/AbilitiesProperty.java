package info.itsthesky.itemcreator.core.properties;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.api.properties.base.MenuProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.gui.abilities.AbilityListGUI;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AbilitiesProperty extends MenuProperty {

	@Override
	public XMaterial getMaterial() {
		return XMaterial.FIRE_CHARGE;
	}

	@Override
	public List<String> isCompatible(CustomItem item) {
		if (!item.hasPropertySet("ic_tag"))
			return Collections.singletonList(LangLoader.get().format("property.property_require",
					LangLoader.get().format("property.ic_tag.name")));
		return new ArrayList<>();
	}

	@Override
	public void onClick(Player player, CustomItem item) {
		new AbilityListGUI(item, player).open(player);
	}

	@Override
	public String getId() {
		return "abilities";
	}
}
