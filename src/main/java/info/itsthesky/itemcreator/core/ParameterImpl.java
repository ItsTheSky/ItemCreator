package info.itsthesky.itemcreator.core;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.api.abilities.RawAbilityParameter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class ParameterImpl<T> implements RawAbilityParameter<T> {

	private final String id;
	private final XMaterial material;
	private final T def;
	private final BiFunction<String, Player, T> parser;

	public ParameterImpl(String id, XMaterial material, T def,
						 BiFunction<String, Player, T> parser) {
		this.id = id;
		this.material = material;
		this.def = def;
		this.parser = parser;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public XMaterial getMaterial() {
		return material;
	}

	@Override
	public T getDefaultValue() {
		return def;
	}

	@Override
	public T parse(@NotNull String input, @Nullable Player player) {
		return parser.apply(input, player);
	}
}
