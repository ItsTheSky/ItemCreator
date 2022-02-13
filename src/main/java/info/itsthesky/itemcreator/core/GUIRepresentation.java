package info.itsthesky.itemcreator.core;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import info.itsthesky.itemcreator.utils.Utils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class GUIRepresentation {

	private final XMaterial material;
	private final String name;
	private final String[] lore;
	private final LangLoader loader = ItemCreator.getInstance().getLangLoader();

	public GUIRepresentation(XMaterial material, String name, String[] lore) {
		this.material = material;
		this.name = name;
		this.lore = lore;
	}

	public @NotNull ItemStack asItem() {
		final ItemStack item = material.parseItem();
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.colored(name));
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		return item;
	}
}
