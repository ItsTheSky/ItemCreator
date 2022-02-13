package info.itsthesky.itemcreator.api.abilities;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.api.ISnowflake;
import info.itsthesky.itemcreator.core.GUIRepresentation;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface RawAbilityParameter<T> extends ISnowflake {

	XMaterial getMaterial();

	default GUIRepresentation getRepresentation() {
		return new GUIRepresentation(getMaterial(),
				"abilities.parameters." + getId() + "name",
				"abilities.parameters." + getId() + "lore");
	}

	T getDefaultValue();

	T parse(@NotNull String input, @Nullable Player player);

}
