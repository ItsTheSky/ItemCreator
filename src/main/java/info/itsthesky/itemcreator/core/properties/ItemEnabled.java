package info.itsthesky.itemcreator.core.properties;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.api.properties.simple.SimpleStateProperty;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.gui.EditorGUI;
import info.itsthesky.itemcreator.core.gui.ItemListGUI;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ItemEnabled extends SimpleStateProperty {
	@Override
	public String getId() {
		return "enabled";
	}

	@Override
	public void onEditorClick(InventoryClickEvent e, CustomItem item) {
		final Boolean current = item.getPropertyValue(this);
		item.setPropertyValue(this, Boolean.FALSE.equals(current));
		save(item, ""+ Boolean.FALSE.equals(current), (Player) e.getWhoClicked());
		e.getWhoClicked().sendMessage(LangLoader.get().format("messages.success"));
		if (Boolean.TRUE.equals(current))
			new ItemListGUI(ItemCreator.getInstance().getApi().loadAllItems(), (Player) e.getWhoClicked()).open(((Player) e.getWhoClicked()));
		else
			new EditorGUI(item, true, ((Player) e.getWhoClicked())).open((Player) e.getWhoClicked());
	}

	@Override
	public ItemStack apply(ItemStack item, Boolean value) {
		return item;
	}

	@Override
	public boolean allowClearing() {
		return false;
	}

	@Override
	public Boolean getDefaultValue() {
		return true;
	}

	@Override
	public XMaterial getMaterial() {
		return XMaterial.REDSTONE;
	}
}
