package info.itsthesky.itemcreator.api.abilities;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.api.ISnowflake;
import info.itsthesky.itemcreator.core.GUIRepresentation;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface RawAbilityParameter<T> extends ISnowflake {

	XMaterial getMaterial();

	default GUIRepresentation getRepresentation() {
		return new GUIRepresentation(getMaterial(),
				LangLoader.get().format("abilities.parameters." + getId() + "name"),
				LangLoader.get().formats("abilities.parameters." + getId() + "lore"));
	}

	T getDefaultValue();

	T parse(@NotNull String input, @Nullable Player player);

}
