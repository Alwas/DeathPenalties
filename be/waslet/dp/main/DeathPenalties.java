package be.waslet.dp.main;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;

import be.waslet.dp.commands.DeathPenaltiesEditCommand;
import be.waslet.dp.commands.DeathPenaltiesStateCommand;
import net.milkbowl.vault.economy.Economy;

public class DeathPenalties extends JavaPlugin implements Listener
{

	private final Hashtable<String, DeathPenaltiesWorld> deathPenaltiesWorlds = new Hashtable<String, DeathPenaltiesWorld>();
	private DeathPenaltiesConfig config;
	private DeathPenaltiesParser parser;
	private Economy economy;

	@Override
	public void onEnable ()
	{
		this.parser = new DeathPenaltiesParser();
		// config startup
		this.config = new DeathPenaltiesConfig(this);
		// load default values
		DeathPenaltiesWorld defaultValues = config.loadDefaultValues(config.getPotionEffectsInSeconds(), config.getPotionEffectsTrueLevel());
		Plugin multiverse = getServer().getPluginManager().getPlugin("Multiverse-Core");
		getServer().getLogger().log(Level.INFO, "[" + getName() + "] Loading all worlds");
		// if mv is NOT installed load all bukkit worlds death penalties from config 
		if (multiverse == null || !(multiverse instanceof MultiverseCore)) for (World world : getServer().getWorlds()) config.loadWorldConfiguration(world.getName(), defaultValues, config.getPotionEffectsInSeconds(), config.getPotionEffectsTrueLevel());
		// else if mv is installed load all mv config worlds death penalties from config
		else
		{
			getServer().getLogger().log(Level.INFO, "[" + getName() + "] Multiverse detected looking for mv worlds");
			for (MultiverseWorld world : ((MultiverseCore) multiverse).getCore().getMVWorldManager().getMVWorlds()) config.loadWorldConfiguration(world.getName(), defaultValues, config.getPotionEffectsInSeconds(), config.getPotionEffectsTrueLevel());
		}
		// if vault is installed link it and activate economy death penalties if economy plugin detected
		if (getServer().getPluginManager().getPlugin("Vault") != null)
		{
			getServer().getLogger().log(Level.INFO, "[" + getName() + "] Vault detected enabling economy death penalties");
			RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
	        if (rsp != null) economy = rsp.getProvider();
	        else getServer().getLogger().log(Level.INFO, "[" + getName() + "] No economy plugins are installed economy penalties will not apply");
		}
		// commands executors
		getCommand("dpstate").setExecutor(new DeathPenaltiesStateCommand(this));
		getCommand("dpedit").setExecutor(new DeathPenaltiesEditCommand(this));
		// listener startup
		getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerRespawn (PlayerRespawnEvent event)
	{
		DeathPenaltiesWorld worldValues = getDeathPenaltiesWorld(event.getPlayer().getWorld().getName());
		// apply penalties if we can
		if (!worldValues.isEnabled() || event.getPlayer().hasPermission("dp.ignore")) return;
		// check if money is active and player has account and then apply money penalties
		if (economy != null && economy.hasAccount(event.getPlayer()))
		{
			// if flat value is disabled use percentage and only set value if we have a valid percentage
			if (worldValues.getRespawnMoneyLostFlat() <= 0)
			{
				if (worldValues.getRespawnMoneyLostPercentage() <= 1 && worldValues.getRespawnMoneyLostPercentage() > 0) economy.withdrawPlayer(event.getPlayer(), economy.getBalance(event.getPlayer()) * worldValues.getRespawnMoneyLostPercentage());
			}
			// updating player with flat value
			else economy.withdrawPlayer(event.getPlayer(), worldValues.getRespawnMoneyLostFlat());
		}
		// if flat value is disabled use percentage and only set value if we have a valid percentage
		if (worldValues.getRespawnItemsLostFlat() <= 0)
		{
			if (worldValues.getRespawnItemsLostPercentage() <= 1 && worldValues.getRespawnItemsLostPercentage() > 0) removeItems(event.getPlayer(), worldValues.getRespawnItemsLostPercentage());
		}
		// updating player with flat value
		else removeItems(event.getPlayer(), worldValues.getRespawnItemsLostFlat());
		// apply health and foot penalties 1s later when player has finished respawning
		new DeathPenaltiesRunnable(event.getPlayer(), worldValues).runTaskLater(this, 20);
	}

	private void removeItems (Player player, double percentage)
	{
		if (percentage >= 1.0) player.getInventory().clear();
		else
		{
			Integer[] itemsSlots = getInventoryItemsSlots(player.getInventory());
			int[] randomArray = getShuffledIntArray((int) (itemsSlots.length * percentage));
			for (int i = 0; i < randomArray.length; i++) player.getInventory().setItem(itemsSlots[randomArray[i]], null);
		}
	}
	
	private void removeItems (Player player, int count)
	{
		Integer[] itemsSlots = getInventoryItemsSlots(player.getInventory());
		if (count > itemsSlots.length) player.getInventory().clear();
		else
		{
			int[] randomArray = getShuffledIntArray(count);
			for (int i = 0; i < randomArray.length; i++) player.getInventory().setItem(itemsSlots[randomArray[i]], null);
		}
	}
	
	private Integer[] getInventoryItemsSlots (Inventory inventory)
	{
		ArrayList<Integer> slots = new ArrayList<Integer>();
		ItemStack[] items = inventory.getContents();
		for (int i = 0; i < items.length; i++) if (items[i] != null) slots.add(i);
		return slots.toArray(new Integer[slots.size()]);
	}
	
	private int[] getShuffledIntArray (int arraySize)
    {
        if (arraySize <= 0) return new int[0];
        int[] array = new int[arraySize];
        // Fill array
        for (int i = 0; i < arraySize; i++) array[i] = i;
        // Shuffle array
        for (int i = arraySize - 1; i > 0; i--)
        {
        	int index = (int) (Math.random() * (i + 1));
            int next = array[index];
            array[index] = array[i];
            array[i] = next;
        }
        return array;
    }

	public DeathPenaltiesConfig getDeathPenaltiesConfig ()
	{
		return config;
	}
	
	public DeathPenaltiesParser getParser ()
	{
		return parser;
	}
	
	/**
	 * Set death penalties values for a world
	 * @param worldName Name of the world to set (key in map)
	 * @param world World to add for the key
	 */
	public void setDeathPenaltiesWorld (String worldName, DeathPenaltiesWorld world)
	{
		deathPenaltiesWorlds.put(worldName, world);
	}
	
	/**
	 * Get death penalties values for a world
	 * @param worldName Name of the world to get values from
	 * @return DeathPenaltiesWorld
	 */
	public DeathPenaltiesWorld getDeathPenaltiesWorld (String worldName)
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
