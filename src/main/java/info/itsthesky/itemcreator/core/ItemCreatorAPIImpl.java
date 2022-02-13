package info.itsthesky.itemcreator.core;

import de.leonhard.storage.Config;
import de.tr7zw.changeme.nbtapi.NBTItem;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.api.ItemCreatorAPI;
import info.itsthesky.itemcreator.api.abilities.Ability;
import info.itsthesky.itemcreator.api.abilities.RawAbilityParameter;
import info.itsthesky.itemcreator.api.properties.base.ItemProperty;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ItemCreatorAPIImpl implements ItemCreatorAPI {

	private final ItemCreator instance;
	public ItemCreatorAPIImpl(ItemCreator instance) {
		this.instance = instance;
	}

	@Override
	public @NotNull CustomItem getItemFromId(String id, boolean force) {
		if (!exists(id) && !force)
			return null;
		final boolean disabled = new File(instance.getDataFolder(), "items/-" + id + ".yml").exists();
		final CustomItem item = new CustomItem(id);
		final Config config = getItemConfig(item);
		for (Ability ability : ItemCreator.getInstance().getRegisteredAbilities().values()) {
			if (!config.getOrDefault("abilities." + ability.getId() + ".present", false))
				continue;
			final List<Ability.AbilityParameter> parameters = new ArrayList<>();
			for (String parameterID : config.singleLayerKeySet("abilities." + ability.getId() + ".parameters")) {
				final RawAbilityParameter parameter = ItemCreator.getInstance().getParameterFromId(parameterID);
				parameters.add(new Ability.AbilityParameter<>(
						config.getOrSetDefault("abilities." + ability.getId() + ".parameters." + parameterID, parameter.getDefaultValue()),
						parameter));
			}
			item.registerAbility(ability, parameters.toArray(new Ability.AbilityParameter[0]));
			//item.setAbilityParameters(ability, parameters.toArray(new Ability.AbilityParameter[0]));
		}
		for (ItemProperty property : ItemCreator.getInstance().getRegisteredProperties().values()) {
			if (!property.isSavable())
				continue;
			item.registerProperty(property);
			if (config.contains(property.getId())) {
				final Object value;
				if (property.isSimple())
					value = property.convert(config.getString(property.getId()), null);
				else
					value = property.getValue(config);
				item.setPropertyValue(property, value);
			} else {
				if (property.getDefaultValue() != null) {
					config.set(property.getId(), property.getDefaultValue());
					final Object value;
					if (property.isSimple())
						value = property.convert(config.get(property.getId()).toString(), null);
					else
						value = property.getValue(config);
					item.setPropertyValue(property, value);
				}
			}
		}
		return item;
	}

	@Override
	public @Nullable CustomItem convert(ItemStack item) {
		if (item == null || item.getType().equals(Material.AIR))
			return null;
		final NBTItem nbtItem = new NBTItem(item);
		if (!nbtItem.hasKey("ItemCreator.ID"))
			return null;
		return getItemFromId(nbtItem.getString("ItemCreator.ID"));
	}

	@Override
	public void deleteItem(CustomItem item) {
		getItemFile(item).delete();
	}

	@Override
	public boolean exists(String id) {
		return new File(instance.getDataFolder(), "items/" + id + ".yml").exists()
				|| new File(instance.getDataFolder(), "items/-" + id + ".yml").exists();
	}

	private int prop = 1;
	@Override
	public void registerProperty(ItemProperty property) {
		prop++;
		instance.getRegisteredProperties().put(prop, property);
	}

	private int abil = 1;
	@Override
	public void registerAbility(Ability ability) {
		abil++;
		instance.getRegisteredAbilities().put(abil, ability);
	}

	private int params = 1;
	@Override
	public void registerParameter(RawAbilityParameter parameter) {
		instance.getRegisteredParameters().put(params, parameter);
		params++;
	}

	@Override
	public @NotNull File getItemFile(CustomItem item) {
		return new File(instance.getDataFolder(), "items/" + item.getId() + ".yml");
	}

	@Override
	public @NotNull File getItemsFolder() {
		final File folder = new File(instance.getDataFolder(), "items");
		if (!folder.exists())
			folder.mkdirs();
		return folder;
	}
}
