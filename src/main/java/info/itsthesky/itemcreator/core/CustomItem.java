package info.itsthesky.itemcreator.core;

import com.cryptomorin.xseries.XMaterial;
import de.leonhard.storage.Config;
import de.tr7zw.changeme.nbtapi.NBTItem;
import fr.mrmicky.fastinv.ItemBuilder;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.api.ISnowflake;
import info.itsthesky.itemcreator.api.abilities.Ability;
import info.itsthesky.itemcreator.api.abilities.RawAbilityParameter;
import info.itsthesky.itemcreator.api.properties.base.ItemProperty;
import info.itsthesky.itemcreator.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represent a whole custom item from {@link info.itsthesky.itemcreator.ItemCreator}.
 * <br> Hold every handler and specific information a custom item can actually have.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class CustomItem implements ISnowflake {

	private final String id;
	private final List<ItemProperty> properties;
	private final HashMap<String, Object> propertiesValue;
	private final HashMap<String, Ability.AbilityParameter[]> abilities;

	public CustomItem(final String id) {
		this.properties = new ArrayList<>();
		this.propertiesValue = new HashMap<>();
		this.abilities = new HashMap<>();
		this.id = id;
	}

	public void registerProperty(ItemProperty property) {
		properties.removeIf(prop -> prop.getId().equals(property.getId()));
		properties.add(property);
	}

	public void registerAbility(Ability ability) {
		if (hasAbility(ability))
			return;
		final List<Ability.AbilityParameter> parameters = new ArrayList<>();
		for (RawAbilityParameter raw : ability.getParameters())
			parameters.add(new Ability.AbilityParameter<>(raw.getDefaultValue(), raw));
		abilities.put(ability.getId(), parameters.toArray(new Ability.AbilityParameter[0]));
		saveAbilities();
	}

	public void registerAbility(Ability ability, Ability.AbilityParameter[] parameters) {
		if (hasAbility(ability))
			return;
		/* final List<Ability.AbilityParameter> parameters = new ArrayList<>();
		for (RawAbilityParameter raw : ability.getParameters())
			parameters.add(new Ability.AbilityParameter<>(raw.getDefaultValue(), raw)); */
		abilities.put(ability.getId(), parameters);
		saveAbilities();
	}

	public void setAbilityParameters(Ability ability, Ability.AbilityParameter[] parameters) {
		abilities.put(ability.getId(), parameters);
		saveAbilities();
	}

	public void saveAbilities() {
		final Config config = ItemCreator.getInstance().getApi().getItemConfig(this);
		abilities.forEach((id, parameters) -> {
			config.set("abilities." + id + ".present", true);
			for (Ability.AbilityParameter parameter : parameters)
				config.set("abilities." + id + ".parameters." + parameter.getParameter().getId(), parameter.getValue());
		});
	}

	public <T> void setPropertyValue(ItemProperty property, T value) {
		propertiesValue.put(property.getId(), value);
	}

	public <T> @Nullable T getDefaultProperty(ItemProperty property, T def) {
		return getPropertyValue(property) == null
				? def : getPropertyValue(property);
	}

	public <T> @Nullable T getPropertyValue(ItemProperty property) {
		return (T) propertiesValue.getOrDefault(property.getId(), null);
	}

	@SuppressWarnings("unchecked")
	public <T> @Nullable T getPropertyValue(Class<T> clazz, String propertyID) {
		final ItemProperty<T> property = (ItemProperty<T>) properties
				.stream()
				.filter(prop -> prop.getId().equals(propertyID))
				.findAny()
				.orElse(null);
		if (property == null)
			return null;
		return (T) propertiesValue.getOrDefault(property.getId(), null);
	}

	@Deprecated
	public boolean save() {
		return save(null);
	}

	private boolean save(Player player) {
		final List<Boolean> validates = new ArrayList<>();
		for (ItemProperty<Object> property : properties) {
			/**
			 * Custom parsing for {@link info.itsthesky.itemcreator.api.properties.base.MultipleItemProperty}
			 */
			if (property.isSimple())
				validates.add(property.save(this, propertiesValue.get(property.getId()).toString(), player));
		}
		return validates.stream().anyMatch(b -> !b);
	}

	@Override
	public String getId() {
		return id;
	}

	public ItemStack asItem() {
		if (getPropertyValue(Boolean.class, "enabled")) {
			ItemStack base = XMaterial.GRASS_BLOCK.parseItem();
			for (ItemProperty<Object> property : properties) {
				if (!property.isSavable())
					continue;
				final Object obj = propertiesValue.get(property.getId());
				if (obj != null && property.isCompatible(this).isEmpty()) {
					base = property.apply(base, obj).clone();
				}
			}
			final NBTItem nbtItem = new NBTItem(base);
			if (getPropertyValue(Boolean.class, "ic_tag"))
				nbtItem.setString("ItemCreator.ID", getId());
			return nbtItem.getItem();
		} else {
			return new ItemBuilder(XMaterial.GRAY_DYE.parseItem())
					.name(Utils.colored("&7Item Disabled"))
					.lore(Utils.colored("&8" + getId() + " &8is currently disabled."),
							Utils.colored("&8Use the &7Middle Click&8 to enable it again."))
					.build();
		}
	}

	public HashMap<String, Ability.AbilityParameter[]> getAbilities() {
		return abilities;
	}

	public List<ItemProperty> getProperties() {
		return properties;
	}

	public boolean hasPropertySet(String propertyID) {
		final ItemProperty property = getProperties()
				.stream()
				.filter(prop -> prop.getId().equals(propertyID))
				.findAny()
				.orElse(null);
		if (property == null || getPropertyValue(property) == null)
			return false;
		return !propertiesValue.getOrDefault(propertyID, property.getDefaultValue()).equals(property.getDefaultValue());
	}

	public void toggleEnabled() {
		final ItemProperty enabledProperty = getProperties()
				.stream()
				.filter(prop -> prop.getId().equals("enabled"))
				.findAny()
				.orElse(null);
		final boolean value = !isEnabled();
		setPropertyValue(enabledProperty, value);
		enabledProperty.save(this, ""+value, null);
		//ItemCreator.getInstance().getApi().getItemConfig(this).set("enabled", value);
	}

	public boolean isEnabled() {
		return getPropertyValue(Boolean.class, "enabled");
	}

	public List<Ability> getFormattedAbilities() {
		return abilities.keySet()
				.stream()
				.map(id -> ItemCreator.getInstance()
						.getRegisteredAbilities()
						.values()
						.stream()
						.filter(ab -> ab.getId().equals(id)).findAny().orElse(null))
				.collect(Collectors.toList());
	}

	public boolean hasAbility(Ability ability) {
		return abilities.containsKey(ability.getId());
	}

	public Ability.AbilityParameter[] getAbilityParameters(Ability ability) {
		return abilities.getOrDefault(ability.getId(), new Ability.AbilityParameter[0]);
	}

	public void setAbilityParameter(Ability ability, Ability.AbilityParameter parameter, Object value) {
		final List<Ability.AbilityParameter> current = new ArrayList<>(List.of(getAbilityParameters(ability)));
		current.removeIf(par -> par.getParameter().getId().equals(parameter.getParameter().getId()));
		current.add(new Ability.AbilityParameter<>(value, parameter.getParameter()));
		abilities.put(ability.getId(), current.toArray(new Ability.AbilityParameter[0]));
	}
}
