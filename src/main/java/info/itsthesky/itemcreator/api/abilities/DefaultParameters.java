package info.itsthesky.itemcreator.api.abilities;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.core.ParameterImpl;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.function.BiFunction;

/**
 * List of default ability parameters
 */
public final class DefaultParameters {

	private static final BiFunction<String, Player, Integer> INTEGER_PARSER = (input, player) -> {
		try {
			return Integer.parseInt(input);
		} catch (Exception ex) {
			player.sendMessage(LangLoader.get().format("messages.wrong_number"));
			return null;
		}
	};

	public static final RawAbilityParameter<Integer> ABILITY_FORCE =
			new ParameterImpl<>("ability_force", XMaterial.GOLDEN_SWORD, 2, INTEGER_PARSER);
	public static final RawAbilityParameter<Integer> ABILITY_COOLDOWN =
			new ParameterImpl<>("ability_cooldown", XMaterial.CLOCK, 0, INTEGER_PARSER);

}
