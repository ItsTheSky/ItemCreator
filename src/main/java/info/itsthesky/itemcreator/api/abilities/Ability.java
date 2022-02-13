package info.itsthesky.itemcreator.api.abilities;

import com.cryptomorin.xseries.XMaterial;
import de.leonhard.storage.Config;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.api.ISnowflake;
import info.itsthesky.itemcreator.api.cooldown.CooldownManager;
import info.itsthesky.itemcreator.core.CustomItem;
import info.itsthesky.itemcreator.core.GUIRepresentation;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public abstract class Ability<E extends Event> implements Listener, ISnowflake {

	public void onGenericEvent(@NotNull E event) {
		if (!validate(event))
			return;
		final Player player = getPlayer(event);
		final CustomItem item = getItem(event);
		if (item == null)
			return;
		if (ItemCreator.getCooldownManager().isOnCooldown(player.getUniqueId(), "ability_" + getId())) {
			player.sendMessage(LangLoader.get().format("messages.ability_on_cooldown", ItemCreator.getCooldownManager().getUserCooldown(player.getUniqueId(), "ability_" + getId()) / 20));
			return;
		}
		ItemCreator.getCooldownManager().setUserCooldown(player.getUniqueId(), "ability_" + getId(), TimeUnit.SECONDS, getCooldown(item));
		launch(event, item, Stream.of(item.getAbilities().get(getId()))
				.map(para -> new AbilityParameter(para.getParameter().parse(para.getValue().toString(), null), para.getParameter())).toArray(AbilityParameter[]::new));
	};

	public abstract @Nullable CustomItem getItem(E event);

	public abstract @NotNull Player getPlayer(E event);

	public abstract boolean validate(E event);

	public abstract XMaterial getMaterial();

	public abstract @NotNull RawAbilityParameter<?>[] getParameters();

	public abstract void launch(@NotNull E event,
								@Nullable CustomItem item,
								@NotNull AbilityParameter<?>[] parameters);

	public boolean hasCooldown() {
		return Arrays.stream(getParameters()).anyMatch(para -> para.getId().equals(DefaultParameters.ABILITY_COOLDOWN.getId()));
	}

	public @NotNull Integer getCooldown(final CustomItem item) {
		if (!hasCooldown())
			return 0;
		return DefaultParameters.ABILITY_COOLDOWN.parse(
				Stream.of(item.getAbilityParameters(this))
						.filter(para -> para.getParameter().getId().equals(DefaultParameters.ABILITY_COOLDOWN.getId()))
						.map(AbilityParameter::getValue)
						.findAny()
						.orElse("0")
						.toString(), null).intValue();
	}

	public GUIRepresentation getRepresentation() {
		return new GUIRepresentation(getMaterial(), LangLoader.get().format("abilities." + getId() + ".name"), LangLoader.get().formats("abilities." + getId() + ".lore"));
	}

	public <T> AbilityParameter<T> getParameterValue(RawAbilityParameter<T> raw, CustomItem item) {
		final Config config = ItemCreator.getInstance().getApi().getItemConfig(item);
		final T value = config.getOrSetDefault("abilities." + getId() + "." + raw.getId(), raw.getDefaultValue());
		return new AbilityParameter<>(value, raw);
	}

	public static class AbilityParameter<T> {

		private final T value;
		private final RawAbilityParameter<T> parameter;

		public AbilityParameter(T value, RawAbilityParameter<T> parameter) {
			this.value = value;
			this.parameter = parameter;
		}

		public T getValue() {
			return value;
		}

		public RawAbilityParameter<T> getParameter() {
			return parameter;
		}
	}
}
