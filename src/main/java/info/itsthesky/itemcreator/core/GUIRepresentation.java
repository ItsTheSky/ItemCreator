package info.itsthesky.itemcreator.core;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class GUIRepresentation {

	private final XMaterial material;
	private final String nameKey;
	private final String descKey;
	private final LangLoader loader = ItemCreator.getInstance().getLangLoader();

	public GUIRepresentation(XMaterial material, String nameKey, String descKey) {
		this.material = material;
		this.nameKey = nameKey;
		this.descKey = descKey;
	}

	public @NotNull ItemStack asItem() {
		final ItemStack item = material.parseItem();
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(loader.format(nameKey));
		meta.setLore(loader.formatsList(descKey));
		item.setItemMeta(meta);
		return item;
	}
}
