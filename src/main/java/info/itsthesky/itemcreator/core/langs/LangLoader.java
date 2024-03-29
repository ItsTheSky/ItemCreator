package info.itsthesky.itemcreator.core.langs;

import de.leonhard.storage.Json;
import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.utils.Utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LangLoader {

	public static LangLoader get() {
		return ItemCreator.getInstance().getLangLoader();
	}

	private final Json lang;
	private final ItemCreator creator;
	public LangLoader(ItemCreator creator, String code) {
		File langFile = new File(creator.getDataFolder(), "langs/" + code + ".json");
		if (!langFile.exists()) {
			langFile = new File(creator.getDataFolder(), "langs/en_US.json");
			if (!langFile.exists())
				ItemCreator.getInstance().saveResource("langs/en_US.json", true);
		}
		this.creator = creator;
		this.lang = new Json(langFile);
		checkLangUpdate();
	}

	public void checkLangUpdate() {
		final String current = creator.getDescription().getVersion();
		final String langFile = lang.getOrSetDefault("lang_version", "2.1.0");
		if (!current.equalsIgnoreCase(langFile)) {
			creator.getLogger().warning("Your current language file wasn't update, some keys could be missing.");
			creator.getLogger().warning("Delete the current 'en_US.json' file to let the new one generate by himself.");
			creator.getLogger().warning("You can also set the 'lang_version' to the current ItemCreator version, in order to bypass these warnings.");
		}
	}

	public String format(String key, Object... replacement) {
		String raw = format(key);
		int i = 1;
		for (Object s : replacement) {
			raw = raw.replace("%" + i, s.toString());
			i++;
		}
		return raw;
	}

	public String format(String key) {
		if (!lang.contains(key))
			creator.getLogger().severe("Unknown language key: " + key);
		return Utils.colored(lang.getOrSetDefault(key, "Unknown Language Key: " + key));
	}

	public List<String> formatsList(String key, Object... replacement) {
		return Stream.of(formats(key))
				.map(str -> {
					int i = 1;
					for (Object s : replacement) {
						str = str.replace("%" + i, s.toString());
						i++;
					}
					return str;
				}).collect(Collectors.toList());
	}

	public List<String> formatsList(String key) {
		return List.of(formats(key));
	}

	public String[] formats(String key) {
		if (!lang.contains(key))
			creator.getLogger().severe("Unknown language key: " + key);
		return Arrays
				.stream(lang.getOrSetDefault(key, new String[] {"Unknown Language Key: " + key}))
				.map(Utils::colored)
				.toArray(String[]::new);
	}

}
