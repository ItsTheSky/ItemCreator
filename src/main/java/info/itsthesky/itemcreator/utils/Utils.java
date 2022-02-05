package info.itsthesky.itemcreator.utils;

import org.bukkit.ChatColor;

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

}
