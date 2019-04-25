package be.waslet.dp.main;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffect;

public class DeathPenaltiesConfig
{

	private DeathPenalties plugin;
	private FileConfiguration translationsConfig;
	
	public DeathPenaltiesConfig (DeathPenalties plugin)
	{
		this.plugin = plugin;
		plugin.saveDefaultConfig();
		File directory = plugin.getDataFolder();
		if (!directory.exists()) directory.mkdirs();
		File file = new File(directory, "translations.yml");
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		translationsConfig = new YamlConfiguration();
		try
		{
			translationsConfig.load(file);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (InvalidConfigurationException e)
		{
			e.printStackTrace();
		}
	}

	public String[] getTranslations ()
	{
		return null;
	}
	
	public boolean getPotionEffectsInSeconds ()
	{
		return plugin.getConfig().getBoolean(DeathPenaltiesOption.EFFECT_FORMAT_SECONDS.getConfigPath());
	}
	
	public boolean getPotionEffectsTrueLevel ()
	{
		return plugin.getConfig().getBoolean(DeathPenaltiesOption.EFFECT_FORMAT_LEVELS.getConfigPath());
	}
	
	/**
	 * Set a world penalty value in the config and save it
	 * @param worldName The world name the value has to be changed
	 * @param option The option enum value that should be changed
	 * @param value The value as a string to write
	 */
	public void setWorldValue (String worldName, DeathPenaltiesOption option, Object value)
	{
		plugin.getConfig().set(worldName + "." + option.getConfigPath(), value);
		plugin.saveConfig();
	}

	public void setWorldEffectsValue (String worldName, String[] value)
	{
		plugin.getConfig().set(worldName + "." + DeathPenaltiesOption.EFFECTS.getConfigPath(), value);
		plugin.saveConfig();
	}
	
	/**
	 * Load in plugin memory death penalties values of a world from config
	 * @param worldName Name of the world values to load
	 * @param durationInSeconds Effects formatting
	 * @param trueLevel Effects formatting
	 */
	public void loadWorldConfiguration (String worldName, DeathPenaltiesWorld defaultValues, boolean durationInSeconds, boolean trueLevel)
	{
		// check if value if is config and save it or default if not found
		boolean enabled = (plugin.getConfig().contains(worldName + "." + DeathPenaltiesOption.ENABLED.getConfigPath())) ? plugin.getConfig().getBoolean(worldName + "." + DeathPenaltiesOption.ENABLED.getConfigPath(), false) : defaultValues.isEnabled();
		double respawnHealthPercentage = (plugin.getConfig().contains(worldName + "." + DeathPenaltiesOption.HEALTH_PERCENTAGE.getConfigPath())) ? plugin.getConfig().getDouble(worldName + "." + DeathPenaltiesOption.HEALTH_PERCENTAGE.getConfigPath()) : 0.0;
		double respawnFoodPercentage = (plugin.getConfig().contains(worldName + "." + DeathPenaltiesOption.FOOD_PERCENTAGE.getConfigPath())) ? plugin.getConfig().getDouble(worldName + "." + DeathPenaltiesOption.FOOD_PERCENTAGE.getConfigPath()) : 0.0;
		double respawnMoneyLostPercentage = (plugin.getConfig().contains(worldName + "." + DeathPenaltiesOption.MONEY_LOST_PERCENTAGE.getConfigPath())) ? plugin.getConfig().getDouble(worldName + "." + DeathPenaltiesOption.MONEY_LOST_PERCENTAGE.getConfigPath()) : 0.0;
		double respawnItemsLostPercentage = (plugin.getConfig().contains(worldName + "." + DeathPenaltiesOption.ITEMS_LOST_PERCENTAGE.getConfigPath())) ? plugin.getConfig().getDouble(worldName + "." + DeathPenaltiesOption.ITEMS_LOST_PERCENTAGE.getConfigPath()) : 0.0;
		// check config and check if has percentage value set (must ignore default and set 0 then to avoid using default flat)
		double respawnHealthFlat = (respawnHealthPercentage <= 0) ? (plugin.getConfig().contains(worldName + "." + DeathPenaltiesOption.HEALTH_FLAT.getConfigPath())) ? plugin.getConfig().getDouble(worldName + "." + DeathPenaltiesOption.HEALTH_FLAT.getConfigPath()) : defaultValues.getRespawnHealthFlat() : 0.0;
		int respawnFoodFlat = (respawnFoodPercentage <= 0) ? (plugin.getConfig().contains(worldName + "." + DeathPenaltiesOption.FOOD_FLAT.getConfigPath())) ? plugin.getConfig().getInt(worldName + "." + DeathPenaltiesOption.FOOD_FLAT.getConfigPath()) : defaultValues.getRespawnFoodFlat() : 0;
		double respawnMoneyLostFlat = (respawnMoneyLostPercentage <= 0) ? (plugin.getConfig().contains(worldName + "." + DeathPenaltiesOption.MONEY_LOST_FLAT.getConfigPath())) ? plugin.getConfig().getDouble(worldName + "." + DeathPenaltiesOption.MONEY_LOST_FLAT.getConfigPath()) : defaultValues.getRespawnMoneyLostFlat() : 0.0;
		int respawnItemsLostFlat = (respawnItemsLostPercentage <= 0) ? (plugin.getConfig().contains(worldName + "." + DeathPenaltiesOption.ITEMS_LOST_FLAT.getConfigPath())) ? plugin.getConfig().getInt(worldName + "." + DeathPenaltiesOption.ITEMS_LOST_FLAT.getConfigPath()) : defaultValues.getRespawnItemsLostFlat() : 0;
		respawnHealthPercentage = (respawnHealthFlat <= 0 && respawnHealthPercentage <= 0) ? defaultValues.getRespawnHealthPercentage() : respawnHealthPercentage;
		respawnFoodPercentage = (respawnFoodFlat <= 0 && respawnFoodPercentage <= 0) ? defaultValues.getRespawnFoodPercentage() : respawnFoodPercentage;
		respawnMoneyLostPercentage = (respawnMoneyLostFlat <= 0 && respawnMoneyLostPercentage <= 0) ? defaultValues.getRespawnMoneyLostPercentage() : respawnMoneyLostPercentage;
		respawnItemsLostPercentage = (respawnItemsLostFlat <= 0 && respawnItemsLostPercentage <= 0) ? defaultValues.getRespawnItemsLostPercentage() : respawnItemsLostPercentage;
		List<String> potionEffects = plugin.getConfig().getStringList(worldName + "." + DeathPenaltiesOption.EFFECTS.getConfigPath());
		PotionEffect[] respawnEffects = defaultValues.getRespawnEffects();
		if (plugin.getConfig().contains(worldName + "." + DeathPenaltiesOption.EFFECTS.getConfigPath()))
		{
			try
			{
				respawnEffects = plugin.getParser().getParsedPotionsEffects(potionEffects.toArray(new String[potionEffects.size()]), durationInSeconds, trueLevel);
			}
			catch (DeathPenaltiesParserException exc)
			{
				plugin.getServer().getLogger().log(Level.INFO, "[" + plugin.getName() + "] " + exc.getMessage());
			}
		}
		plugin.setDeathPenaltiesWorld(worldName, new DeathPenaltiesWorld(enabled, respawnHealthFlat, respawnFoodFlat, respawnMoneyLostFlat, respawnItemsLostFlat, respawnHealthPercentage, respawnFoodPercentage, respawnMoneyLostPercentage, respawnItemsLostPercentage, respawnEffects));
	}

	/**
	 * Load in plugin memory death penalties default values from config
	 * @param durationInSeconds Effects formatting
	 * @param trueLevel Effects formatting
	 */
	public DeathPenaltiesWorld loadDefaultValues (boolean durationInSeconds, boolean trueLevel)
	{
		// check for each value if default values are set in config otherwise set non used values
		boolean enabled = plugin.getConfig().getBoolean("default_values." + DeathPenaltiesOption.ENABLED.getConfigPath(), false);
		double respawnHealthPercentage = plugin.getConfig().getDouble("default_values." + DeathPenaltiesOption.HEALTH_PERCENTAGE.getConfigPath(), 0.0);
		double respawnFoodPercentage = plugin.getConfig().getDouble("default_values." + DeathPenaltiesOption.FOOD_PERCENTAGE.getConfigPath(), 0.0);
		double respawnMoneyLostPercentage = plugin.getConfig().getDouble("default_values." + DeathPenaltiesOption.MONEY_LOST_PERCENTAGE.getConfigPath(), 0.0);
		double respawnItemsLostPercentage = plugin.getConfig().getDouble("default_values." + DeathPenaltiesOption.ITEMS_LOST_PERCENTAGE.getConfigPath(), 0.0);
		double respawnHealthFlat = (respawnHealthPercentage <= 0) ? (plugin.getConfig().contains("default_values." + DeathPenaltiesOption.HEALTH_FLAT.getConfigPath())) ? plugin.getConfig().getDouble("default_values." + DeathPenaltiesOption.HEALTH_FLAT.getConfigPath()) : 0.0 : 0.0;
		int respawnFoodFlat = (respawnFoodPercentage <= 0) ? (plugin.getConfig().contains("default_values." + DeathPenaltiesOption.FOOD_FLAT.getConfigPath())) ? plugin.getConfig().getInt("default_values." + DeathPenaltiesOption.FOOD_FLAT.getConfigPath()) : 0 : 0;
		double respawnMoneyLostFlat = (respawnMoneyLostPercentage <= 0) ? (plugin.getConfig().contains("default_values." + DeathPenaltiesOption.MONEY_LOST_FLAT.getConfigPath())) ? plugin.getConfig().getDouble("default_values." + DeathPenaltiesOption.MONEY_LOST_FLAT.getConfigPath()) : 0.0 : 0.0;
		int respawnItemsLostFlat = (respawnItemsLostPercentage <= 0) ? (plugin.getConfig().contains("default_values." + DeathPenaltiesOption.ITEMS_LOST_FLAT.getConfigPath())) ? plugin.getConfig().getInt("default_values." + DeathPenaltiesOption.ITEMS_LOST_FLAT.getConfigPath()) : 0 : 0;
		List<String> potionEffects = plugin.getConfig().getStringList("default_values." + DeathPenaltiesOption.EFFECTS.getConfigPath());
		PotionEffect[] respawnEffects = new PotionEffect[0];
		try
		{
			respawnEffects = plugin.getParser().getParsedPotionsEffects(potionEffects.toArray(new String[potionEffects.size()]), durationInSeconds, trueLevel);
		}
		catch (DeathPenaltiesParserException exc)
		{
			plugin.getServer().getLogger().log(Level.INFO, "[" + plugin.getName() + "] " + exc.getMessage());
		}
		DeathPenaltiesWorld defaultValues = new DeathPenaltiesWorld(enabled, respawnHealthFlat, respawnFoodFlat, respawnMoneyLostFlat, respawnItemsLostFlat, respawnHealthPercentage, respawnFoodPercentage, respawnMoneyLostPercentage, respawnItemsLostPercentage, respawnEffects);
		plugin.setDeathPenaltiesWorld("default_values", defaultValues);
		return defaultValues;
	}

}
