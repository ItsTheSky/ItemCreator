package info.itsthesky.itemcreator;

import de.leonhard.storage.Config;
import fr.mrmicky.fastinv.FastInvManager;
import info.itsthesky.itemcreator.api.ItemCreatorAPI;
import info.itsthesky.itemcreator.api.properties.base.ItemProperty;
import info.itsthesky.itemcreator.core.CreatorCommand;
import info.itsthesky.itemcreator.core.ItemCreatorAPIImpl;
import info.itsthesky.itemcreator.core.MainListener;
import info.itsthesky.itemcreator.core.langs.LangLoader;
import info.itsthesky.itemcreator.core.properties.*;
import info.itsthesky.itemcreator.core.properties.base.*;
import info.itsthesky.itemcreator.core.properties.events.CantCraftProperty;
import info.itsthesky.itemcreator.core.properties.events.CantEnchantProperty;
import info.itsthesky.itemcreator.core.properties.flags.HideEffectsProperty;
import info.itsthesky.itemcreator.core.properties.flags.HideEnchantProperty;
import info.itsthesky.itemcreator.core.properties.flags.HideUnbreakableProperty;
import info.itsthesky.itemcreator.core.properties.spawners.SpawnerSpawnCountProperty;
import info.itsthesky.itemcreator.core.properties.spawners.SpawnerTypeProperty;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;

public final class ItemCreator extends JavaPlugin {

	private static ItemCreator instance;
	private static Config config;
	private HashMap<Integer, ItemProperty> registeredProperties;
	private ItemCreatorAPI api;
	private LangLoader langLoader;

	@Override
	public void onEnable() {
		getLogger().info("Loading ItemCreator v" + getDescription().getVersion() + " by ItsTheSky");

		final File configFile = new File(getDataFolder(), "config.yml");
		if (!configFile.exists())
			saveResource("config.yml", true);
		config = new Config(configFile);

		FastInvManager.register(this);

		instance = this;
		api = new ItemCreatorAPIImpl(this);
		langLoader = new LangLoader(this, config.getOrSetDefault("default_language", "en_US"));

		getLogger().info("Registering Properties ...");
		registeredProperties = new HashMap<>();
		api.registerProperty(new ItemEnabled());
		api.registerProperty(new MaterialProperty());
		api.registerProperty(new NameProperty());
		api.registerProperty(new LoreProperty());
		api.registerProperty(new ICTagProperty());
		api.registerProperty(new TypeProperty());
		api.registerProperty(new PotionColorProperty());
		api.registerProperty(new ArmorColorProperty());
		api.registerProperty(new EnchantmentProperty());
		api.registerProperty(new RarityProperty());
		api.registerProperty(new Base64Property());
		api.registerProperty(new CMDProperty());
		if (getServer().getVersion().contains("1.8"))
			api.registerProperty(new EntityTypeProperty());
		api.registerProperty(new OwnerProperty());
		api.registerProperty(new SpawnerTypeProperty(this));
		api.registerProperty(new SpawnerSpawnCountProperty(this));
		api.registerProperty(new PotionEffectsProperty());

		api.registerProperty(new CantCraftProperty());
		api.registerProperty(new CantEnchantProperty());
		api.registerProperty(new UnbreakProperty());
		api.registerProperty(new HideEnchantProperty());
		api.registerProperty(new HideUnbreakableProperty());
		api.registerProperty(new HideEffectsProperty());

		getLogger().info("Registering listeners ...");
		getServer().getPluginManager().registerEvents(new MainListener(this), this);

		getCommand("itemcreator").setExecutor(new CreatorCommand());
		getCommand("itemcreator").setTabCompleter(new CreatorCommand());

		getLogger().info("ItemCreator as been loaded!");
	}

	public static ItemCreator getInstance() {
		if (instance == null)
			throw new RuntimeException(new IllegalAccessException("ItemCreator is not running."));
		return instance;
	}

	public ItemCreatorAPI getApi() {
		return api;
	}

	public HashMap<Integer, ItemProperty> getRegisteredProperties() {
		return registeredProperties;
	}

	@NotNull
	public static Config getConfiguration() {
		return config;
	}

	@Override
	public void onDisable() {
		instance = null;
	}

	public LangLoader getLangLoader() {
		return langLoader;
	}
}
