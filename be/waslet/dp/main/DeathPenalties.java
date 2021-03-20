package be.waslet.dp.main;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.garbagemule.MobArena.ArenaPlayer;
import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.events.ArenaPlayerDeathEvent;
import com.garbagemule.MobArena.events.ArenaPlayerJoinEvent;
import com.garbagemule.MobArena.events.ArenaPlayerLeaveEvent;
import com.garbagemule.MobArena.framework.Arena;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;

import be.waslet.dp.commands.DeathPenaltiesEditCommand;
import be.waslet.dp.commands.DeathPenaltiesStateCommand;
import net.milkbowl.vault.economy.Economy;

public class DeathPenalties extends JavaPlugin implements Listener
{

	private final Hashtable<String, DeathPenaltiesWorld> deathPenaltiesWorlds = new Hashtable<>();
	private DeathPenaltiesConfig config;
	private boolean bypassOpPermission;
	private Economy economy;
	private MobArena mobArena;
	private Random random = new Random();
	private ArrayList<String> arenaPlayersUUIDs;

	@Override
	public void onEnable ()
	{
		// config startup
		this.config = new DeathPenaltiesConfig(this);
		this.bypassOpPermission = this.config.getBypassOpPermission();
		// load default values
		DeathPenaltiesWorld defaultValues = this.config.loadDefaultValues();
		Plugin multiverse = getServer().getPluginManager().getPlugin("Multiverse-Core");
		getServer().getLogger().log(Level.INFO, "[" + getName() + "] Loading all worlds");
		// if mv is NOT installed load all bukkit worlds death penalties from config 
		if (multiverse == null || !(multiverse instanceof MultiverseCore)) for (World world : getServer().getWorlds()) this.config.loadWorldConfiguration(world.getName(), defaultValues);
		// else if mv is installed load all mv config worlds death penalties from config
		else
		{
			getServer().getLogger().log(Level.INFO, "[" + getName() + "] Multiverse found: looking for mv worlds");
			for (MultiverseWorld world : ((MultiverseCore) multiverse).getCore().getMVWorldManager().getMVWorlds()) this.config.loadWorldConfiguration(world.getName(), defaultValues);
		}
		getServer().getLogger().log(Level.INFO, "[" + getName() + "] All worlds are loaded. Found " + this.deathPenaltiesWorlds.size() + " worlds configs");
		// if vault is installed link it and activate economy death penalties if economy plugin detected
		if (getServer().getPluginManager().getPlugin("Vault") != null)
		{
			getServer().getLogger().log(Level.INFO, "[" + getName() + "] Vault found: enabling economy death penalties");
			RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
	        if (rsp != null) this.economy = rsp.getProvider();
	        else getServer().getLogger().log(Level.INFO, "[" + getName() + "] No economy plugins are installed economy penalties will not apply");
		}
		// mob arena support
		Plugin mobArenaPlugin = getServer().getPluginManager().getPlugin("MobArena");
		if (mobArenaPlugin != null)
		{
			this.mobArena = (MobArena) mobArenaPlugin;
			this.arenaPlayersUUIDs = new ArrayList<>();
			getServer().getLogger().log(Level.INFO, "[" + getName() + "] MobArena found: disabling death penalties for arenas");
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
		if (worldValues.isEnabled())
		{
			if (this.mobArena != null && this.arenaPlayersUUIDs.contains(event.getPlayer().getUniqueId().toString()))
			{
				return;
			}
			if (event.getPlayer().isOp())
			{
				if (!this.bypassOpPermission) return;
			}
			else if (event.getPlayer().hasPermission("dp.ignore")) return;
		}
		else return;
		// apply health and foot penalties 1s later when player has finished respawning
		new DeathPenaltiesRunnable(event.getPlayer(), worldValues).runTaskLater(this, 20);
	}

	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerDeath (PlayerDeathEvent event)
	{
		DeathPenaltiesWorld worldValues = getDeathPenaltiesWorld(event.getEntity().getWorld().getName());
		// apply penalties if we can
		if (worldValues.isEnabled())
		{
			if (this.mobArena != null && this.arenaPlayersUUIDs.contains(event.getEntity().getUniqueId().toString()))
			{
				return;
			}
			if (event.getEntity().isOp())
			{
				if (!this.bypassOpPermission) return;
			}
			else if (event.getEntity().hasPermission("dp.ignore")) return;
		}
		else return;
		// check if money is active and player has account and then apply money penalties
		if (this.economy != null && this.economy.hasAccount(event.getEntity()))
		{
			// if flat value is disabled use percentage and only set value if we have a valid percentage
			if (worldValues.getDeathMoneyLostFlat() <= 0)
			{
				if (worldValues.getDeathMoneyLostPercentage() <= 1 && worldValues.getDeathMoneyLostPercentage() > 0)
				{
					this.economy.withdrawPlayer(event.getEntity(), this.economy.getBalance(event.getEntity()) * worldValues.getDeathMoneyLostPercentage());
				}
			}
			// updating player with flat value
			else
			{
				this.economy.withdrawPlayer(event.getEntity(), worldValues.getDeathMoneyLostFlat());
			}
		}
		// if keep inventory is not set only destroy items else destroy items then drop
		if (!event.getKeepInventory())
		{
			// first check destroyed chance
			// use destroy items functions with drops list
			// if flat value is disabled use percentage and only set value if we have a valid percentage
			if (worldValues.getDeathItemsDestroyedChancePercentage() > getChance())
			{
				if (worldValues.getDeathItemsDestroyedFlat() <= 0)
				{
					if (worldValues.getDeathItemsDestroyedPercentage() <= 1 && worldValues.getDeathItemsDestroyedPercentage() > 0) destroyItems(event.getDrops(), worldValues.getDeathItemsDestroyedPercentage(), worldValues.getWhitelistedItems());
				}
				else destroyItems(event.getDrops(), worldValues.getDeathItemsDestroyedFlat(), worldValues.getWhitelistedItems());
			}
			// experience
			if (worldValues.getDeathExperienceDestroyedChancePercentage() > getChance())
			{
				if (worldValues.getDeathExperienceDestroyedFlat() <= 0)
				{
					if (worldValues.getDeathExperienceDestroyedPercentage() <= 1 && worldValues.getDeathExperienceDestroyedPercentage() > 0) event.getEntity().setTotalExperience(event.getEntity().getTotalExperience() - (int) (event.getEntity().getTotalExperience() * worldValues.getDeathExperienceDestroyedPercentage()));
				}
				else event.getEntity().setTotalExperience((int) (event.getEntity().getTotalExperience() - worldValues.getDeathExperienceDestroyedFlat()));
			}
			// levels
			if (worldValues.getDeathLevelsDestroyedChancePercentage() > getChance())
			{
				if (worldValues.getDeathLevelsDestroyedFlat() <= 0)
				{
					if (worldValues.getDeathLevelsDestroyedPercentage() <= 1 && worldValues.getDeathLevelsDestroyedPercentage() > 0) event.getEntity().setLevel(event.getEntity().getLevel() - (int) (event.getEntity().getLevel() * worldValues.getDeathLevelsDestroyedPercentage()));
				}
				else event.getEntity().setLevel(event.getEntity().getLevel() - worldValues.getDeathLevelsDestroyedFlat());
			}
		}
		// keep inventory = true
		else
		{
			// first check destroyed chance
			// use destroy items functions with player
			// if flat value is disabled use percentage and only set value if we have a valid percentage
			if (worldValues.getDeathItemsDestroyedChancePercentage() > getChance())
			{
				if (worldValues.getDeathItemsDestroyedFlat() <= 0)
				{
					if (worldValues.getDeathItemsDestroyedPercentage() <= 1 && worldValues.getDeathItemsDestroyedPercentage() > 0) destroyItems(event.getEntity(), worldValues.getDeathItemsDestroyedPercentage(), worldValues.getWhitelistedItems());
				}
				else destroyItems(event.getEntity(), worldValues.getDeathItemsDestroyedFlat(), worldValues.getWhitelistedItems());
			}
			// experience
			if (worldValues.getDeathExperienceDestroyedChancePercentage() > getChance())
			{
				if (worldValues.getDeathExperienceDestroyedFlat() <= 0)
				{
					if (worldValues.getDeathExperienceDestroyedPercentage() <= 1 && worldValues.getDeathExperienceDestroyedPercentage() > 0) event.getEntity().setTotalExperience(event.getEntity().getTotalExperience() - (int) (event.getEntity().getTotalExperience() * worldValues.getDeathExperienceDestroyedPercentage()));
				}
				else event.getEntity().setTotalExperience((int) (event.getEntity().getTotalExperience() - worldValues.getDeathExperienceDestroyedFlat()));
			}
			// levels
			if (worldValues.getDeathLevelsDestroyedChancePercentage() > getChance())
			{
				if (worldValues.getDeathLevelsDestroyedFlat() <= 0)
				{
					if (worldValues.getDeathLevelsDestroyedPercentage() <= 1 && worldValues.getDeathLevelsDestroyedPercentage() > 0) event.getEntity().setLevel(event.getEntity().getLevel() - (int) (event.getEntity().getLevel() * worldValues.getDeathLevelsDestroyedPercentage()));
				}
				else event.getEntity().setLevel(event.getEntity().getLevel() - worldValues.getDeathLevelsDestroyedFlat());
			}
			// first check dropped chance
			// drop items
			// if flat value is disabled use percentage and only set value if we have a valid percentage
			if (worldValues.getDeathItemsDroppedChancePercentage() > getChance())
			{
				if (worldValues.getDeathItemsDroppedFlat() <= 0)
				{
					if (worldValues.getDeathItemsDroppedPercentage() <= 1 && worldValues.getDeathItemsDroppedPercentage() > 0) dropItems(event.getEntity(), worldValues.getDeathItemsDroppedPercentage(), worldValues.getWhitelistedItems());
				}
				else dropItems(event.getEntity(), worldValues.getDeathItemsDroppedFlat(), worldValues.getWhitelistedItems());
			}
			// experience
			if (worldValues.getDeathExperienceDroppedChancePercentage() > getChance())
			{
				if (worldValues.getDeathExperienceDroppedFlat() <= 0)
				{
					if (worldValues.getDeathExperienceDroppedPercentage() <= 1 && worldValues.getDeathExperienceDroppedPercentage() > 0) event.getEntity().setTotalExperience(event.getEntity().getTotalExperience() - (int) (event.getEntity().getTotalExperience() * worldValues.getDeathExperienceDroppedPercentage()));
				}
				else event.getEntity().setTotalExperience((int) (event.getEntity().getTotalExperience() - worldValues.getDeathExperienceDroppedFlat()));
			}
		}
		// process commands at death
		for (String command : worldValues.getDeathProcessedCommands())
		{
			String[] data = command.split(";");
			if (data.length < 2) continue;
			if (data[0].equalsIgnoreCase("server")) event.getEntity().getServer().dispatchCommand(event.getEntity().getServer().getConsoleSender(), data[1].replace("%player%", event.getEntity().getName()));
			else if (data[0].equalsIgnoreCase("player")) event.getEntity().performCommand(data[1].replace("%player%", event.getEntity().getName()));
		}
	}

	/*
	 * MOB ARENA MANAGMENT
	 * since mob arena plugin arena managers players functions does not seem to be working we will use the event system to keep track of in arena players
	 */
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerJoinsArena (ArenaPlayerJoinEvent event)
	{
		this.arenaPlayersUUIDs.add(event.getPlayer().getUniqueId().toString());
	}

	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerLeavesArena (ArenaPlayerLeaveEvent event)
	{
		this.arenaPlayersUUIDs.remove(event.getPlayer().getUniqueId().toString());
	}

	/**
	 * 
	 * @return A chance number between 0.0 and 1.0
	 */
	private double getChance ()
	{
		return this.random.nextDouble();
	}
	
	/**
	 * Drop items with percentage value in a player inventory (used when keep inventory is enabled)
	 * @param player The player that has items that need to be dropped
	 * @param percentage The number of items to drop in percentage value
	 */
	private void dropItems (Player player, double percentage, Material[] whitelist)
	{
		Integer[] itemsSlots = getInventoryItemsSlots(player.getInventory(), whitelist);
		int count = (int) (percentage * itemsSlots.length);
		int[] randomArray = getShuffledIntArray(itemsSlots.length);
		for (int i = 0; i < count && i < itemsSlots.length; i++)
		{
			player.getWorld().dropItemNaturally(player.getLocation(), player.getInventory().getContents()[itemsSlots[randomArray[i]]]);
			player.getInventory().setItem(itemsSlots[randomArray[i]], null);
		}
	}

	/**
	 * Drop items with percentage value in a player inventory (used when keep inventory is enabled)
	 * @param player The player that has items that need to be dropped
	 * @param count The number of items to drop
	 */
	private void dropItems (Player player, int count, Material[] whitelist)
	{
		Integer[] itemsSlots = getInventoryItemsSlots(player.getInventory(), whitelist);
		int[] randomArray = getShuffledIntArray(itemsSlots.length);
		for (int i = 0; i < count && i < itemsSlots.length; i++)
		{
			player.getWorld().dropItemNaturally(player.getLocation(), player.getInventory().getContents()[itemsSlots[randomArray[i]]]);
			player.getInventory().setItem(itemsSlots[randomArray[i]], null);
		}
	}

	/**
	 * Destroy items with percentage value in a player inventory (used when keep inventory is enabled)
	 * @param player The player that has items that need to be removed
	 * @param percentage The number of items to remove in percentage value
	 */
	private void destroyItems (Player player, double percentage, Material[] whitelist)
	{
		Integer[] itemsSlots = getInventoryItemsSlots(player.getInventory(), whitelist);
		int count = (int) (percentage * itemsSlots.length);
		int[] randomArray = getShuffledIntArray(itemsSlots.length);
		for (int i = 0; i < count && i < itemsSlots.length; i++) player.getInventory().setItem(itemsSlots[randomArray[i]], null);
	}

	/**
	 * Destroy items with percentage value in a player inventory (used when keep inventory is enabled)
	 * @param player The player that has items that need to be removed
	 * @param count The number of items to remove
	 */
	private void destroyItems (Player player, int count, Material[] whitelist)
	{
		Integer[] itemsSlots = getInventoryItemsSlots(player.getInventory(), whitelist);
		int[] randomArray = getShuffledIntArray(itemsSlots.length);
		for (int i = 0; i < count && i < itemsSlots.length; i++) player.getInventory().setItem(itemsSlots[randomArray[i]], null);
	}

	/**
	 * Destroy items with percentage value in a list of drops (used when keep inventory is not enabled)
	 * @param drops The drops that has items that need to be removed
	 * @param percentage Number of items to remove in percentage
	 */
	private void destroyItems (List<ItemStack> drops, double percentage, Material[] whitelist)
	{
		LinkedList<ItemStack> whitelistedDrops = new LinkedList<>();
		for (ItemStack drop : drops) if (isWhitelisted(whitelist, drop.getType())) whitelistedDrops.add(drop);
		for (ItemStack drop : whitelistedDrops) drops.remove(drop);
		int count = drops.size() - ((int) (percentage * drops.size()));
		for (int i = drops.size() - 1; i > count && i > 0; i--) drops.remove((int) (Math.random() * (i + 1)));
		for (ItemStack drop : whitelistedDrops) drops.add(drop);
	}

	/**
	 * Destroy items with flat value in a list of drops (used when keep inventory is not enabled)
	 * @param drops The drops that has items that need to be removed
	 * @param count Number of items to remove
	 */
	private void destroyItems (List<ItemStack> drops, int count, Material[] whitelist)
	{
		LinkedList<ItemStack> whitelistedDrops = new LinkedList<>();
		for (ItemStack drop : drops) if (isWhitelisted(whitelist, drop.getType())) whitelistedDrops.add(drop);
		for (ItemStack drop : whitelistedDrops) drops.remove(drop);
		count = drops.size() - count;
		for (int i = drops.size() - 1; i > count && i > 0; i--) drops.remove((int) ((Math.random()* (i + 1))));
		for (ItemStack drop : whitelistedDrops) drops.add(drop);
	}
	
	/**
	 * Get an array of integers containing the list of items slots that are filled from a given inventory
	 * @param inventory The inventory where we want to get filled items slots
	 * @return Integer[]
	 */
	private Integer[] getInventoryItemsSlots (Inventory inventory, Material[] whitelistedItems)
	{
		ArrayList<Integer> slots = new ArrayList<>();
		ItemStack[] items = inventory.getContents();
		for (int i = 0; i < items.length; i++) if (items[i] != null && !(isWhitelisted(whitelistedItems, items[i].getType()))) slots.add(i);
		return slots.toArray(new Integer[slots.size()]);
	}
	
	/**
	 * Get an array of integers from 0 to arraySize that are shuffled
	 * @param arraySize Number of integers to add in the array
	 * @return int[] 
	 */
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

	private boolean isWhitelisted (Material[] whitelist, Material material)
	{
		for (Material next : whitelist) if (next == material) return true;
		return false;
	}
	
	public DeathPenaltiesConfig getDeathPenaltiesConfig ()
	{
		return this.config;
	}
	
	/**
	 * Set death penalties values for a world
	 * @param worldName Name of the world to set (key in map)
	 * @param world World to add for the key
	 */
	public void setDeathPenaltiesWorld (String worldName, DeathPenaltiesWorld world)
	{
		this.deathPenaltiesWorlds.put(worldName, world);
	}
	
	/**
	 * Get death penalties values for a world
	 * @param worldName Name of the world to get values from
	 * @return DeathPenaltiesWorld
	 */
	public DeathPenaltiesWorld getDeathPenaltiesWorld (String worldName)
	{
		DeathPenaltiesWorld world = this.deathPenaltiesWorlds.get(worldName);
		// if world does not exist in death penalties create a new one with default values
		if (world == null)
		{
			world = this.deathPenaltiesWorlds.get("default_values").getCopy();
			this.deathPenaltiesWorlds.put(worldName, world);
		}
		return world;
	}

}
