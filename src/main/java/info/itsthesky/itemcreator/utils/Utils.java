package info.itsthesky.itemcreator.utils;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;

import java.util.function.Supplier;

public final class Utils {

	public static String colored(String input) {
		return ChatColor.translateAlternateColorCodes('&', input);
	}

	public static <T> T verify(Supplier<T> supplier) {
		try {
			return supplier.get();
		} catch (Exception ex) {
			return null;
		}
	}

	public static String beauty(Enum<?> enumeration) {
		return beauty(enumeration.name());
	}

	public static String beauty(String input) {
		return WordUtils.capitalize(input.replace("_", " ").toLowerCase());
	}
}
