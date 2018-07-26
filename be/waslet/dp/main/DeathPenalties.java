package be.waslet.dp.main;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import be.waslet.dp.commands.DeathPenaltiesEditCommand;
import be.waslet.dp.commands.DeathPenaltiesStateCommand;

public class DeathPenalties extends JavaPlugin implements Listener
{

	private static final Hashtable<String, DeathPenaltiesWorld> deathPenaltiesWorlds = new Hashtable<String, DeathPenaltiesWorld>();
	private boolean deathWorldPenalties;

	@Override
	public void onEnable ()
	{
		// config startup
		saveDefaultConfig();
		deathWorldPenalties = getConfig().getBoolean(DeathPenaltiesOption.APPLY_DEATH_WORLD.getConfigPath());
		boolean durationInSeconds = getConfig().getBoolean(DeathPenaltiesOption.EFFECT_FORMAT_SECONDS.getConfigPath());
		boolean trueLevel = getConfig().getBoolean(DeathPenaltiesOption.EFFECT_FORMAT_LEVELS.getConfigPath());
		// load default values
		DeathPenaltiesWorld defaultValues = loadDefaultValues(durationInSeconds, trueLevel);
		// load all worlds custom values or set it to default
		for (World world : getServer().getWorlds())
		{
			loadWorldConfiguration(world.getName(), defaultValues, durationInSeconds, trueLevel);
		}
		// commands executors
		getCommand("dpstate").setExecutor(new DeathPenaltiesStateCommand());
		getCommand("dpedit").setExecutor(new DeathPenaltiesEditCommand(this));
		// listener startup
		getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerRespawn (PlayerRespawnEvent event)
	{
		DeathPenaltiesWorld worldValues;
		if (deathWorldPenalties) worldValues = getDeathPenaltiesWorld(event.getPlayer().getWorld().getName());
		else worldValues = getDeathPenaltiesWorld(event.getRespawnLocation().getWorld().getName());
		if (!worldValues.isEnabled() || event.getPlayer().hasPermission("dp.ignore")) return;
		new DeathPenaltiesRunnable(event.getPlayer(), worldValues).runTaskLater(this, 20);
	}

	/**
	 * Load in plugin memory death penalties values of a world from config
	 * @param worldName Name of the world values to load
	 * @param durationInSeconds Effects formatting
	 * @param trueLevel Effects formatting
	 */
	private void loadWorldConfiguration (String worldName, DeathPenaltiesWorld defaultValues, boolean durationInSeconds, boolean trueLevel)
	{
		// check if value if is config and save it or default if not found
		boolean enabled = (getConfig().contains(worldName + "." + DeathPenaltiesOption.ENABLED.getConfigPath())) ? getConfig().getBoolean(worldName + "." + DeathPenaltiesOption.ENABLED.getConfigPath(), false) : defaultValues.isEnabled();
		double respawnHealthPercentage = (getConfig().contains(worldName + "." + DeathPenaltiesOption.HEALTH_PERCENTAGE.getConfigPath())) ? getConfig().getDouble(worldName + "." + DeathPenaltiesOption.HEALTH_PERCENTAGE.getConfigPath()) : defaultValues.getRespawnHealthPercentage();
		double respawnFoodPercentage = (getConfig().contains(worldName + "." + DeathPenaltiesOption.FOOD_PERCENTAGE.getConfigPath())) ? getConfig().getDouble(worldName + "." + DeathPenaltiesOption.FOOD_PERCENTAGE.getConfigPath()) : defaultValues.getRespawnFoodPercentage();
		// check config and check if has percentage value set (must ignore default and set 0 then to avoid using default flat)
		double respawnHealthFlat = (respawnHealthPercentage <= 0) ? (getConfig().contains(worldName + "." + DeathPenaltiesOption.HEALTH_FLAT.getConfigPath())) ? getConfig().getDouble(worldName + "." + DeathPenaltiesOption.HEALTH_FLAT.getConfigPath()) : defaultValues.getRespawnHealthFlat() : 0.0;
		int respawnFoodFlat = (respawnFoodPercentage <= 0) ? (getConfig().contains(worldName + "." + DeathPenaltiesOption.FOOD_FLAT.getConfigPath())) ? getConfig().getInt(worldName + "." + DeathPenaltiesOption.FOOD_FLAT.getConfigPath()) : defaultValues.getRespawnFoodFlat() : 0;
		PotionEffect[] respawnEffects = (getConfig().contains(worldName + "." + DeathPenaltiesOption.EFFECTS.getConfigPath())) ? getParsedPotionsEffects(getConfig().getStringList(worldName + "." + DeathPenaltiesOption.EFFECTS.getConfigPath()), durationInSeconds, trueLevel) : defaultValues.getRespawnEffects();
		deathPenaltiesWorlds.put(worldName, new DeathPenaltiesWorld(enabled, respawnHealthFlat, respawnFoodFlat, respawnHealthPercentage, respawnFoodPercentage, respawnEffects));
	}

	private DeathPenaltiesWorld loadDefaultValues (boolean durationInSeconds, boolean trueLevel)
	{
		// check if default values are set in config otherwise set non used values
		boolean enabled = getConfig().getBoolean("default_values." + DeathPenaltiesOption.ENABLED.getConfigPath(), false);
		double respawnHealthPercentage = getConfig().getDouble("default_values." + DeathPenaltiesOption.HEALTH_PERCENTAGE.getConfigPath(), 0.0);
		double respawnFoodPercentage = getConfig().getDouble("default_values." + DeathPenaltiesOption.FOOD_PERCENTAGE.getConfigPath(), 0.0);
		double respawnHealthFlat = (respawnHealthPercentage <= 0) ? (getConfig().contains("default_values." + DeathPenaltiesOption.HEALTH_FLAT.getConfigPath())) ? getConfig().getDouble("default_values." + DeathPenaltiesOption.HEALTH_FLAT.getConfigPath()) : 0.0 : 0.0;
		int respawnFoodFlat = (respawnFoodPercentage <= 0) ? (getConfig().contains("default_values." + DeathPenaltiesOption.FOOD_FLAT.getConfigPath())) ? getConfig().getInt("default_values." + DeathPenaltiesOption.FOOD_FLAT.getConfigPath()) : 0 : 0;
		PotionEffect[] respawnEffects = getParsedPotionsEffects(getConfig().getStringList("default_values." + DeathPenaltiesOption.EFFECTS.getConfigPath()), durationInSeconds, trueLevel);
		DeathPenaltiesWorld defaultValues = new DeathPenaltiesWorld(enabled, respawnHealthFlat, respawnFoodFlat, respawnHealthPercentage, respawnFoodPercentage, respawnEffects);
		deathPenaltiesWorlds.put("default_values", defaultValues);
		return defaultValues;
	}

	private PotionEffect[] getParsedPotionsEffects (List<String> potionEffectsFormats, boolean durationInSeconds, boolean trueLevel)
	{
		ArrayList<PotionEffect> potionsEffects = new ArrayList<PotionEffect>();
		for (String effectFormat : potionEffectsFormats)
		{
			PotionEffect potionEffect = parsePotionEffect(effectFormat, durationInSeconds, trueLevel);
			if (potionEffect != null) potionsEffects.add(potionEffect);
		}
		return potionsEffects.toArray(new PotionEffect[potionsEffects.size()]);
	}

	private PotionEffect parsePotionEffect (String potionEffectFormat, boolean durationInSeconds, boolean trueLevel)
	{
		String[] args = potionEffectFormat.split(":");
		if (args.length != 4)
		{
			getServer().getLogger().log(Level.WARNING, "[" + getName() + "] An effect line is not properly formatted: \"" + potionEffectFormat + "\". Please check it");
			return null;
		}
		PotionEffectType potionEffect = PotionEffectType.getByName(args[0]);
		if (potionEffect == null)
		{
			getServer().getLogger().log(Level.WARNING, "[" + getName() + "] Effect " + args[0] + " does not exist. Please check config");
			return null;
		}
		try
		{
			return new PotionEffect(potionEffect, ((durationInSeconds) ? Integer.parseInt(args[1]) * 20 : Integer.parseInt(args[1])), ((trueLevel) ? Integer.parseInt(args[2]) - 1 : Integer.parseInt(args[2])), false, Boolean.getBoolean(args[3]));
		}
		catch (NumberFormatException e)
		{
			getServer().getLogger().log(Level.WARNING, "[" + getName() + "] An effect line is not properly formatted: " + potionEffectFormat);
			return null;
		}
	}

	/**
	 * Get death penalties values for a world
	 * @param worldName Name of the world to get values from
	 * @return DeathPenaltiesValues
	 */
	public static DeathPenaltiesWorld getDeathPenaltiesWorld (String worldName)
	{
		DeathPenaltiesWorld world = deathPenaltiesWorlds.get(worldName);
		// if world does not exist in death penalties create a new one with default values
		if (world == null)
		{
			world = deathPenaltiesWorlds.get("default_values").getCopy();
			deathPenaltiesWorlds.put(worldName, world);
		}
		return world;
	}

}
