package be.waslet.dp.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DeathPenaltiesConfig
{

	private DeathPenalties plugin;
	private File mainConfigFile;
	private FileConfiguration mainConfig;
	private FileConfiguration translationsConfig;
	
	public DeathPenaltiesConfig (DeathPenalties plugin)
	{
		this.plugin = plugin;
		File directory = plugin.getDataFolder();
		if (!directory.exists()) directory.mkdirs();
		this.mainConfigFile = new File(directory, "config.yml");
		File translationsFile = new File(directory, "translations.yml");
		this.mainConfig = new YamlConfiguration();
		this.translationsConfig = new YamlConfiguration();
		if (!this.mainConfigFile.exists()) plugin.saveResource("config.yml", false);
		if (!translationsFile.exists()) plugin.saveResource("translations.yml", false);
		try
		{
			this.mainConfig.load(this.mainConfigFile);
			this.translationsConfig.load(translationsFile);
		}
		catch (IOException exc)
		{
			exc.printStackTrace();
		}
		catch (InvalidConfigurationException exc)
		{
			exc.printStackTrace();
		}
	}
	
	public String[] getTranslations ()
	{
		return null;
	}
	
	public boolean getBypassOpPermission ()
	{
		if (!this.mainConfig.contains("bypass-op-permission"))
	    {
			this.mainConfig.set("bypass-op-permission", false);
			try
			{
				this.mainConfig.save(this.mainConfigFile);
			}
			catch (IOException exc)
			{
				exc.printStackTrace();
			}
	    }
		return this.mainConfig.getBoolean("bypass-op-permission", false);
	}
	
	/**
	 * Set a world penalty value in the config and save it
	 * @param worldName The world name the value has to be changed
	 * @param option The option enum value that should be changed
	 * @param value The value as a string to write
	 */
	public void setWorldValue (String worldName, DeathPenaltiesOption option, Object value)
	{
		this.mainConfig.set(worldName + "." + option.getConfigPath(), value);
		try
		{
			this.mainConfig.save(this.mainConfigFile);
		}
		catch (IOException exc)
		{
			exc.printStackTrace();
		}
	}
	
	/**
	 * Load in plugin memory death penalties values of a world from config
	 * @param worldName Name of the world values to load
	 * @param durationInSeconds Effects formatting
	 * @param trueLevel Effects formatting
	 */
	public void loadWorldConfiguration (String worldName, DeathPenaltiesWorld defaultValues)
	{
		String worldNamePath = worldName + ".";
		// check if value if is config and save it or default if not found
		boolean enabled = (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.ENABLED.getConfigPath())) ? this.mainConfig.getBoolean(worldNamePath + DeathPenaltiesOption.ENABLED.getConfigPath(), false) : defaultValues.isEnabled();
		double respawnHealthPercentage = (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.HEALTH_PERCENTAGE.getConfigPath())) ? this.mainConfig.getDouble(worldNamePath + DeathPenaltiesOption.HEALTH_PERCENTAGE.getConfigPath()) : 0.0;
		double respawnHealthChancePercentage = (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.HEALTH_CHANCE_PERCENTAGE.getConfigPath())) ? this.mainConfig.getDouble(worldNamePath + DeathPenaltiesOption.HEALTH_CHANCE_PERCENTAGE.getConfigPath()) : defaultValues.getRespawnHealthChancePercentage();
		double respawnFoodPercentage = (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.FOOD_PERCENTAGE.getConfigPath())) ? this.mainConfig.getDouble(worldNamePath + DeathPenaltiesOption.FOOD_PERCENTAGE.getConfigPath()) : 0.0;
		double respawnFoodChancePercentage = (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.FOOD_CHANCE_PERCENTAGE.getConfigPath())) ? this.mainConfig.getDouble(worldNamePath + DeathPenaltiesOption.FOOD_CHANCE_PERCENTAGE.getConfigPath()) : defaultValues.getRespawnFoodChancePercentage();
		double deathMoneyLostPercentage = (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.MONEY_LOST_PERCENTAGE.getConfigPath())) ? this.mainConfig.getDouble(worldNamePath + DeathPenaltiesOption.MONEY_LOST_PERCENTAGE.getConfigPath()) : 0.0;
		double deathMoneyLostChancePercentage = (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.MONEY_LOST_CHANCE_PERCENTAGE.getConfigPath())) ? this.mainConfig.getDouble(worldNamePath + DeathPenaltiesOption.MONEY_LOST_CHANCE_PERCENTAGE.getConfigPath()) : defaultValues.getDeathMoneyLostChancePercentage();
		double deathItemsDroppedPercentage = (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.ITEMS_DROPPED_PERCENTAGE.getConfigPath())) ? this.mainConfig.getDouble(worldNamePath + DeathPenaltiesOption.ITEMS_DROPPED_PERCENTAGE.getConfigPath()) : 0.0;
		double deathItemsDroppedChancePercentage = (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.ITEMS_DROPPED_CHANCE_PERCENTAGE.getConfigPath())) ? this.mainConfig.getDouble(worldNamePath + DeathPenaltiesOption.ITEMS_DROPPED_CHANCE_PERCENTAGE.getConfigPath()) : defaultValues.getDeathItemsDroppedChancePercentage();
		double deathItemsDestroyedPercentage = (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.ITEMS_DESTROYED_PERCENTAGE.getConfigPath())) ? this.mainConfig.getDouble(worldNamePath + DeathPenaltiesOption.ITEMS_DESTROYED_PERCENTAGE.getConfigPath()) : 0.0;
		double deathItemsDestroyedChancePercentage = (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.ITEMS_DESTROYED_CHANCE_PERCENTAGE.getConfigPath())) ? this.mainConfig.getDouble(worldNamePath + DeathPenaltiesOption.ITEMS_DESTROYED_CHANCE_PERCENTAGE.getConfigPath()) : defaultValues.getDeathItemsDestroyedChancePercentage();
		double deathExperienceDroppedPercentage = (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.EXPERIENCE_DROPPED_PERCENTAGE.getConfigPath())) ? this.mainConfig.getDouble(worldNamePath + DeathPenaltiesOption.EXPERIENCE_DROPPED_PERCENTAGE.getConfigPath()) : 0.0;
		double deathExperienceDroppedChancePercentage = (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.EXPERIENCE_DROPPED_CHANCE_PERCENTAGE.getConfigPath())) ? this.mainConfig.getDouble(worldNamePath + DeathPenaltiesOption.EXPERIENCE_DROPPED_CHANCE_PERCENTAGE.getConfigPath()) : defaultValues.getDeathExperienceDroppedChancePercentage();
		double deathExperienceDestroyedPercentage = (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.EXPERIENCE_DESTROYED_PERCENTAGE.getConfigPath())) ? this.mainConfig.getDouble(worldNamePath + DeathPenaltiesOption.EXPERIENCE_DESTROYED_PERCENTAGE.getConfigPath()) : 0.0;
		double deathExperienceDestroyedChancePercentage = (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.EXPERIENCE_DESTROYED_CHANCE_PERCENTAGE.getConfigPath())) ? this.mainConfig.getDouble(worldNamePath + DeathPenaltiesOption.EXPERIENCE_DESTROYED_CHANCE_PERCENTAGE.getConfigPath()) : defaultValues.getDeathExperienceDestroyedChancePercentage();
		double deathLevelsDestroyedPercentage = (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.LEVELS_DESTROYED_PERCENTAGE.getConfigPath())) ? this.mainConfig.getDouble(worldNamePath + DeathPenaltiesOption.LEVELS_DESTROYED_PERCENTAGE.getConfigPath()) : 0.0;
		double deathLevelsDestroyedChancePercentage = (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.LEVELS_DESTROYED_CHANCE_PERCENTAGE.getConfigPath())) ? this.mainConfig.getDouble(worldNamePath + DeathPenaltiesOption.LEVELS_DESTROYED_CHANCE_PERCENTAGE.getConfigPath()) : defaultValues.getDeathLevelsDestroyedChancePercentage();
		// check config and check if has percentage value set (must ignore default and set 0 then to avoid using default flat)
		double respawnHealthFlat = (respawnHealthPercentage <= 0) ? (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.HEALTH_FLAT.getConfigPath())) ? this.mainConfig.getDouble(worldNamePath + DeathPenaltiesOption.HEALTH_FLAT.getConfigPath()) : defaultValues.getRespawnHealthFlat() : 0.0;
		int respawnFoodFlat = (respawnFoodPercentage <= 0) ? (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.FOOD_FLAT.getConfigPath())) ? this.mainConfig.getInt(worldNamePath + DeathPenaltiesOption.FOOD_FLAT.getConfigPath()) : defaultValues.getRespawnFoodFlat() : 0;
		double deathMoneyLostFlat = (deathMoneyLostPercentage <= 0) ? (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.MONEY_LOST_FLAT.getConfigPath())) ? this.mainConfig.getDouble(worldNamePath + DeathPenaltiesOption.MONEY_LOST_FLAT.getConfigPath()) : defaultValues.getDeathMoneyLostFlat() : 0.0;
		int deathItemsDroppedFlat = (deathItemsDroppedPercentage <= 0) ? (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.ITEMS_DROPPED_FLAT.getConfigPath())) ? this.mainConfig.getInt(worldNamePath + DeathPenaltiesOption.ITEMS_DROPPED_FLAT.getConfigPath()) : defaultValues.getDeathItemsDroppedFlat() : 0;
		int deathItemsDestroyedFlat = (deathItemsDestroyedPercentage <= 0) ? (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.ITEMS_DESTROYED_FLAT.getConfigPath())) ? this.mainConfig.getInt(worldNamePath + DeathPenaltiesOption.ITEMS_DESTROYED_FLAT.getConfigPath()) : defaultValues.getDeathItemsDestroyedFlat() : 0;
		double deathExperienceDroppedFlat = (deathExperienceDroppedPercentage <= 0) ? (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.EXPERIENCE_DROPPED_FLAT.getConfigPath())) ? this.mainConfig.getDouble(worldNamePath + DeathPenaltiesOption.EXPERIENCE_DROPPED_FLAT.getConfigPath()) : defaultValues.getDeathExperienceDroppedFlat() : 0;
		double deathExperienceDestroyedFlat = (deathExperienceDestroyedPercentage <= 0) ? (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.EXPERIENCE_DESTROYED_FLAT.getConfigPath())) ? this.mainConfig.getDouble(worldNamePath + DeathPenaltiesOption.EXPERIENCE_DESTROYED_FLAT.getConfigPath()) : defaultValues.getDeathExperienceDestroyedFlat() : 0;
		int deathLevelsDestroyedFlat = (deathLevelsDestroyedPercentage <= 0) ? (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.LEVELS_DESTROYED_FLAT.getConfigPath())) ? this.mainConfig.getInt(worldNamePath + DeathPenaltiesOption.LEVELS_DESTROYED_FLAT.getConfigPath()) : defaultValues.getDeathLevelsDestroyedFlat() : 0;
		respawnHealthPercentage = (respawnHealthFlat <= 0 && respawnHealthPercentage <= 0) ? defaultValues.getRespawnHealthPercentage() : respawnHealthPercentage;
		respawnFoodPercentage = (respawnFoodFlat <= 0 && respawnFoodPercentage <= 0) ? defaultValues.getRespawnFoodPercentage() : respawnFoodPercentage;
		deathMoneyLostPercentage = (deathMoneyLostFlat <= 0 && deathMoneyLostPercentage <= 0) ? defaultValues.getDeathMoneyLostPercentage() : deathMoneyLostPercentage;
		deathItemsDroppedPercentage = (deathItemsDroppedFlat <= 0 && deathItemsDroppedPercentage <= 0) ? defaultValues.getDeathItemsDroppedPercentage() : deathItemsDroppedPercentage;
		deathItemsDestroyedPercentage = (deathItemsDestroyedFlat <= 0 && deathItemsDestroyedPercentage <= 0) ? defaultValues.getDeathItemsDestroyedPercentage() : deathItemsDestroyedPercentage;
		deathExperienceDroppedPercentage = (deathExperienceDroppedFlat <= 0 && deathExperienceDroppedPercentage <= 0) ? defaultValues.getDeathExperienceDroppedPercentage() : deathExperienceDroppedPercentage;
		deathExperienceDestroyedPercentage = (deathExperienceDestroyedFlat <= 0 && deathExperienceDestroyedPercentage <= 0) ? defaultValues.getDeathExperienceDestroyedPercentage() : deathExperienceDestroyedPercentage;
		deathLevelsDestroyedPercentage = (deathLevelsDestroyedFlat <= 0 && deathLevelsDestroyedPercentage <= 0) ? defaultValues.getDeathLevelsDestroyedPercentage() : deathLevelsDestroyedPercentage;
		String moneyLostBankAccount = (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.MONEY_LOST_BANK_ACCOUNT.getConfigPath())) ? this.mainConfig.getString(worldNamePath + DeathPenaltiesOption.MONEY_LOST_BANK_ACCOUNT.getConfigPath()) : defaultValues.getMoneyLostBankAccount();
		Material[] whitelistedItems = defaultValues.getWhitelistedItems();
		if (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.WHITELISTED_ITEMS.getConfigPath()))
		{
			List<String> whitelistedItemsList = this.mainConfig.getStringList(worldNamePath + DeathPenaltiesOption.WHITELISTED_ITEMS.getConfigPath());
			whitelistedItems = new Material[whitelistedItemsList.size()];
			int position = 0;
			for (String next : whitelistedItemsList)
			{
				try
				{
					whitelistedItems[position++] = Material.valueOf(next);
				}
				catch (IllegalArgumentException exc)
				{
					exc.printStackTrace();
					continue;
				}
			}
		}
		String[] respawnProcessedCommands = defaultValues.getRespawnProcessedCommands();
		if (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.RESPAWN_PROCESSED_COMMANDS.getConfigPath()))
		{
			List<String> respawnProcessedCommandsList = this.mainConfig.getStringList(worldNamePath + DeathPenaltiesOption.RESPAWN_PROCESSED_COMMANDS.getConfigPath());
			respawnProcessedCommands = respawnProcessedCommandsList.toArray(new String[respawnProcessedCommandsList.size()]);
		}
		String[] deathProcessedCommands = defaultValues.getDeathProcessedCommands();
		if (this.mainConfig.contains(worldNamePath + DeathPenaltiesOption.DEATH_PROCESSED_COMMANDS.getConfigPath()))
		{
			List<String> deathProcessedCommandsList = this.mainConfig.getStringList(worldNamePath + DeathPenaltiesOption.DEATH_PROCESSED_COMMANDS.getConfigPath());
			deathProcessedCommands = deathProcessedCommandsList.toArray(new String[deathProcessedCommandsList.size()]);
		}
		this.plugin.setDeathPenaltiesWorld(worldName, new DeathPenaltiesWorld (enabled, respawnHealthFlat, respawnFoodFlat,
				deathMoneyLostFlat, deathItemsDroppedFlat, deathItemsDestroyedFlat,
				deathExperienceDroppedFlat, deathExperienceDestroyedFlat, deathLevelsDestroyedFlat,
				respawnHealthPercentage, respawnHealthChancePercentage, respawnFoodPercentage,
				respawnFoodChancePercentage, deathMoneyLostPercentage, deathMoneyLostChancePercentage,
				deathItemsDroppedPercentage, deathItemsDroppedChancePercentage,
				deathItemsDestroyedPercentage, deathItemsDestroyedChancePercentage,
				deathExperienceDroppedPercentage, deathExperienceDroppedChancePercentage,
				deathExperienceDestroyedPercentage, deathExperienceDestroyedChancePercentage,
				deathLevelsDestroyedPercentage, deathLevelsDestroyedChancePercentage,
				moneyLostBankAccount, whitelistedItems, respawnProcessedCommands,
				deathProcessedCommands));
	}
	
	/**
	 * Load in plugin memory death penalties default values from config
	 * @param durationInSeconds Effects formatting
	 * @param trueLevel Effects formatting
	 */
	public DeathPenaltiesWorld loadDefaultValues ()
	{
		String defaultValuesPath = "default_values.";
		// check if values are set in config otherwise add it can be changed from plugin update so we update config
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.ENABLED.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.ENABLED.getConfigPath(), false);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.HEALTH_PERCENTAGE.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.HEALTH_PERCENTAGE.getConfigPath(), 0.0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.HEALTH_CHANCE_PERCENTAGE.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.HEALTH_CHANCE_PERCENTAGE.getConfigPath(), 1.0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.FOOD_PERCENTAGE.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.FOOD_PERCENTAGE.getConfigPath(), 0.0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.FOOD_CHANCE_PERCENTAGE.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.FOOD_CHANCE_PERCENTAGE.getConfigPath(), 1.0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.MONEY_LOST_PERCENTAGE.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.MONEY_LOST_PERCENTAGE.getConfigPath(), 0.0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.MONEY_LOST_CHANCE_PERCENTAGE.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.MONEY_LOST_CHANCE_PERCENTAGE.getConfigPath(), 1.0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.ITEMS_DROPPED_PERCENTAGE.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.ITEMS_DROPPED_PERCENTAGE.getConfigPath(), 0.0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.ITEMS_DROPPED_CHANCE_PERCENTAGE.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.ITEMS_DROPPED_CHANCE_PERCENTAGE.getConfigPath(), 1.0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.ITEMS_DESTROYED_PERCENTAGE.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.ITEMS_DESTROYED_PERCENTAGE.getConfigPath(), 0.0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.ITEMS_DESTROYED_CHANCE_PERCENTAGE.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.ITEMS_DESTROYED_CHANCE_PERCENTAGE.getConfigPath(), 1.0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.EXPERIENCE_DROPPED_PERCENTAGE.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.EXPERIENCE_DROPPED_PERCENTAGE.getConfigPath(), 0.0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.EXPERIENCE_DROPPED_CHANCE_PERCENTAGE.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.EXPERIENCE_DROPPED_CHANCE_PERCENTAGE.getConfigPath(), 1.0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.EXPERIENCE_DESTROYED_PERCENTAGE.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.EXPERIENCE_DESTROYED_PERCENTAGE.getConfigPath(), 0.0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.EXPERIENCE_DESTROYED_CHANCE_PERCENTAGE.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.EXPERIENCE_DESTROYED_CHANCE_PERCENTAGE.getConfigPath(), 1.0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.LEVELS_DESTROYED_PERCENTAGE.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.LEVELS_DESTROYED_PERCENTAGE.getConfigPath(), 0.0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.LEVELS_DESTROYED_CHANCE_PERCENTAGE.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.LEVELS_DESTROYED_CHANCE_PERCENTAGE.getConfigPath(), 1.0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.HEALTH_FLAT.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.HEALTH_FLAT.getConfigPath(), 0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.FOOD_FLAT.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.FOOD_FLAT.getConfigPath(), 0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.MONEY_LOST_FLAT.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.MONEY_LOST_FLAT.getConfigPath(), 0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.ITEMS_DROPPED_FLAT.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.ITEMS_DROPPED_FLAT.getConfigPath(), 0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.ITEMS_DESTROYED_FLAT.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.ITEMS_DESTROYED_FLAT.getConfigPath(), 0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.EXPERIENCE_DROPPED_FLAT.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.EXPERIENCE_DROPPED_FLAT.getConfigPath(), 0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.EXPERIENCE_DESTROYED_FLAT.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.EXPERIENCE_DESTROYED_FLAT.getConfigPath(), 0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.LEVELS_DESTROYED_FLAT.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.LEVELS_DESTROYED_FLAT.getConfigPath(), 0);
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.MONEY_LOST_BANK_ACCOUNT.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.MONEY_LOST_BANK_ACCOUNT.getConfigPath(), "");
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.WHITELISTED_ITEMS.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.WHITELISTED_ITEMS.getConfigPath(), new ArrayList<String>());
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.RESPAWN_PROCESSED_COMMANDS.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.RESPAWN_PROCESSED_COMMANDS.getConfigPath(), new ArrayList<String>());
		if (!this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.DEATH_PROCESSED_COMMANDS.getConfigPath())) this.mainConfig.set(defaultValuesPath + DeathPenaltiesOption.DEATH_PROCESSED_COMMANDS.getConfigPath(), new ArrayList<String>());
		try
		{
			this.mainConfig.save(this.mainConfigFile);
		}
		catch (IOException exc)
		{
			exc.printStackTrace();
		}
		// check for each value if default values are set in config otherwise set non used values
		boolean enabled = this.mainConfig.getBoolean(defaultValuesPath + DeathPenaltiesOption.ENABLED.getConfigPath(), false);
		double respawnHealthPercentage = this.mainConfig.getDouble(defaultValuesPath + DeathPenaltiesOption.HEALTH_PERCENTAGE.getConfigPath(), 0.0);
		double respawnHealthChancePercentage = this.mainConfig.getDouble(defaultValuesPath + DeathPenaltiesOption.HEALTH_CHANCE_PERCENTAGE.getConfigPath(), 0.0);
		double respawnFoodPercentage = this.mainConfig.getDouble(defaultValuesPath + DeathPenaltiesOption.FOOD_PERCENTAGE.getConfigPath(), 0.0);
		double respawnFoodChancePercentage = this.mainConfig.getDouble(defaultValuesPath + DeathPenaltiesOption.FOOD_CHANCE_PERCENTAGE.getConfigPath(), 0.0);
		double deathMoneyLostPercentage = this.mainConfig.getDouble(defaultValuesPath + DeathPenaltiesOption.MONEY_LOST_PERCENTAGE.getConfigPath(), 0.0);
		double deathMoneyLostChancePercentage = this.mainConfig.getDouble(defaultValuesPath + DeathPenaltiesOption.MONEY_LOST_CHANCE_PERCENTAGE.getConfigPath(), 0.0);
		double deathItemsDroppedPercentage = this.mainConfig.getDouble(defaultValuesPath + DeathPenaltiesOption.ITEMS_DROPPED_PERCENTAGE.getConfigPath(), 0.0);
		double deathItemsDroppedChancePercentage = this.mainConfig.getDouble(defaultValuesPath + DeathPenaltiesOption.ITEMS_DROPPED_CHANCE_PERCENTAGE.getConfigPath(), 1.0);
		double deathItemsDestroyedPercentage = this.mainConfig.getDouble(defaultValuesPath + DeathPenaltiesOption.ITEMS_DESTROYED_PERCENTAGE.getConfigPath(), 0.0);
		double deathItemsDestroyedChancePercentage = this.mainConfig.getDouble(defaultValuesPath + DeathPenaltiesOption.ITEMS_DESTROYED_CHANCE_PERCENTAGE.getConfigPath(), 1.0);
		double deathExperienceDroppedPercentage = this.mainConfig.getDouble(defaultValuesPath + DeathPenaltiesOption.EXPERIENCE_DROPPED_PERCENTAGE.getConfigPath(), 0.0);
		double deathExperienceDroppedChancePercentage = this.mainConfig.getDouble(defaultValuesPath + DeathPenaltiesOption.EXPERIENCE_DROPPED_CHANCE_PERCENTAGE.getConfigPath(), 1.0);
		double deathExperienceDestroyedPercentage = this.mainConfig.getDouble(defaultValuesPath + DeathPenaltiesOption.EXPERIENCE_DESTROYED_PERCENTAGE.getConfigPath(), 0.0);
		double deathExperienceDestroyedChancePercentage = this.mainConfig.getDouble(defaultValuesPath + DeathPenaltiesOption.EXPERIENCE_DESTROYED_CHANCE_PERCENTAGE.getConfigPath(), 1.0);
		double deathLevelsDestroyedPercentage = this.mainConfig.getDouble(defaultValuesPath + DeathPenaltiesOption.LEVELS_DESTROYED_PERCENTAGE.getConfigPath(), 0.0);
		double deathLevelsDestroyedChancePercentage = this.mainConfig.getDouble(defaultValuesPath + DeathPenaltiesOption.LEVELS_DESTROYED_CHANCE_PERCENTAGE.getConfigPath(), 1.0);
		// check config and check if has percentage value set (must ignore default and set 0 then to avoid using default flat)
		double respawnHealthFlat = (respawnHealthPercentage <= 0) ? (this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.HEALTH_FLAT.getConfigPath())) ? this.mainConfig.getDouble(defaultValuesPath + DeathPenaltiesOption.HEALTH_FLAT.getConfigPath()) : 0.0 : 0.0;
		int respawnFoodFlat = (respawnFoodPercentage <= 0) ? (this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.FOOD_FLAT.getConfigPath())) ? this.mainConfig.getInt(defaultValuesPath + DeathPenaltiesOption.FOOD_FLAT.getConfigPath()) : 0 : 0;
		double deathMoneyLostFlat = (deathMoneyLostPercentage <= 0) ? (this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.MONEY_LOST_FLAT.getConfigPath())) ? this.mainConfig.getDouble(defaultValuesPath + DeathPenaltiesOption.MONEY_LOST_FLAT.getConfigPath()) : 0.0 : 0.0;
		int deathItemsDroppedFlat = (deathItemsDroppedPercentage <= 0) ? (this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.ITEMS_DROPPED_FLAT.getConfigPath())) ? this.mainConfig.getInt(defaultValuesPath + DeathPenaltiesOption.ITEMS_DROPPED_FLAT.getConfigPath()) : 0 : 0;
		int deathItemsDestroyedFlat = (deathItemsDestroyedPercentage <= 0) ? (this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.ITEMS_DESTROYED_FLAT.getConfigPath())) ? this.mainConfig.getInt(defaultValuesPath + DeathPenaltiesOption.ITEMS_DESTROYED_FLAT.getConfigPath()) : 0 : 0;
		double deathExperienceDroppedFlat = (deathExperienceDroppedPercentage <= 0) ? (this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.EXPERIENCE_DROPPED_FLAT.getConfigPath())) ? this.mainConfig.getDouble(defaultValuesPath + DeathPenaltiesOption.EXPERIENCE_DROPPED_FLAT.getConfigPath()) : 0.0 : 0.0;
		double deathExperienceDestroyedFlat = (deathExperienceDestroyedPercentage <= 0) ? (this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.EXPERIENCE_DESTROYED_FLAT.getConfigPath())) ? this.mainConfig.getDouble(defaultValuesPath + DeathPenaltiesOption.EXPERIENCE_DESTROYED_FLAT.getConfigPath()) : 0.0 : 0.0;
		int deathLevelsDestroyedFlat = (deathLevelsDestroyedPercentage <= 0) ? (this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.LEVELS_DESTROYED_FLAT.getConfigPath())) ? this.mainConfig.getInt(defaultValuesPath + DeathPenaltiesOption.LEVELS_DESTROYED_FLAT.getConfigPath()) : 0 : 0;
		String moneyLostBankAccount = (this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.MONEY_LOST_BANK_ACCOUNT.getConfigPath())) ? this.mainConfig.getString(defaultValuesPath + DeathPenaltiesOption.MONEY_LOST_BANK_ACCOUNT.getConfigPath()) : "";
		List<String> whitelistedItemsList = (this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.WHITELISTED_ITEMS.getConfigPath())) ? this.mainConfig.getStringList(defaultValuesPath + DeathPenaltiesOption.WHITELISTED_ITEMS.getConfigPath()) : new ArrayList<String>();
		Material[] whitelistedItems = new Material[whitelistedItemsList.size()];
		int position = 0;
		for (String next : whitelistedItemsList)
		{
			try
			{
				whitelistedItems[position++] = Material.valueOf(next);
			}
			catch (IllegalArgumentException exc)
			{
				exc.printStackTrace();
				continue;
			}
		}
		List<String> respawnProcessedCommandsList = (this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.RESPAWN_PROCESSED_COMMANDS.getConfigPath())) ? this.mainConfig.getStringList(defaultValuesPath + DeathPenaltiesOption.RESPAWN_PROCESSED_COMMANDS.getConfigPath()) : new ArrayList<String>();
		String[] respawnProcessedCommands = respawnProcessedCommandsList.toArray(new String[respawnProcessedCommandsList.size()]);
		List<String> deathProcessedCommandsList = (this.mainConfig.contains(defaultValuesPath + DeathPenaltiesOption.DEATH_PROCESSED_COMMANDS.getConfigPath())) ? this.mainConfig.getStringList(defaultValuesPath + DeathPenaltiesOption.DEATH_PROCESSED_COMMANDS.getConfigPath()) : new ArrayList<String>();
		String[] deathProcessedCommands = deathProcessedCommandsList.toArray(new String[deathProcessedCommandsList.size()]);
		DeathPenaltiesWorld defaultValues = new DeathPenaltiesWorld (enabled, respawnHealthFlat, respawnFoodFlat,
				deathMoneyLostFlat, deathItemsDroppedFlat, deathItemsDestroyedFlat,
				deathExperienceDroppedFlat, deathExperienceDestroyedFlat, deathLevelsDestroyedFlat,
				respawnHealthPercentage, respawnHealthChancePercentage, respawnFoodPercentage,
				respawnFoodChancePercentage, deathMoneyLostPercentage, deathMoneyLostChancePercentage,
				deathItemsDroppedPercentage, deathItemsDroppedChancePercentage,
				deathItemsDestroyedPercentage, deathItemsDestroyedChancePercentage,
				deathExperienceDroppedPercentage, deathExperienceDroppedChancePercentage,
				deathExperienceDestroyedPercentage, deathExperienceDestroyedChancePercentage,
				deathLevelsDestroyedPercentage, deathLevelsDestroyedChancePercentage,
				moneyLostBankAccount, whitelistedItems, respawnProcessedCommands,
				deathProcessedCommands);
		this.plugin.setDeathPenaltiesWorld("default_values", defaultValues);
		return defaultValues;
	}

}
