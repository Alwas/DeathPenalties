package be.waslet.dp.commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import be.waslet.dp.main.DeathPenalties;
import be.waslet.dp.main.DeathPenaltiesOption;
import be.waslet.dp.main.DeathPenaltiesWorld;

public class DeathPenaltiesEditCommand implements CommandExecutor
{

	private DeathPenalties plugin;
	
	public DeathPenaltiesEditCommand (DeathPenalties plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand (CommandSender sender, Command command, String label, String[] args)
	{
		// wrong command usage
		if (args.length < 3) return false;
		World senderWorld = sender.getServer().getWorld(args[0]);
		DeathPenaltiesOption option = DeathPenaltiesOption.getOption(args[1]);
		// option does not exist
		if (option == null) sender.sendMessage(ChatColor.RED + "The option \"" + args[1] + "\" does not exist.\nAvaiblable options: ENABLED (true/false); HEALTH_FLAT (value > 0); FOOD_FLAT (integer > 0); MONEY_LOST_FLAT (value > 0); ITEMS_DROPPED_FLAT (integer > 0); ITEMS_DESTROYED_FLAT (integer > 0); HEALTH_PERCENTAGE (value > 0 - max 1.0); FOOD_PERCENTAGE (value > 0 - max 1.0); MONEY_LOST_PERCENTAGE (value > 0 - max 1.0); ITEMS_DROPPED_PERCENTAGE (value > 0 - max 1.0); ITEMS_DROPPED_CHANCE_PERCENTAGE (value > 0 - max 1.0); ITEMS_DESTROYED_PERCENTAGE (value > 0 - max 1.0); ITEMS_DESTROYED_CHANCE_PERCENTAGE (value > 0 - max 1.0);");
		// invalid option value
		else if (!option.isValid(args[2])) sender.sendMessage(ChatColor.RED + "The option value \"" + args[2] + "\" is not valid for option \"" + args[1] + "\".\nAvaiblable options: ENABLED (true/false); HEALTH_FLAT (value > 0); FOOD_FLAT (integer > 0); MONEY_LOST_FLAT (value > 0); ITEMS_DROPPED_FLAT (integer > 0); ITEMS_DESTROYED_FLAT (integer > 0); HEALTH_PERCENTAGE (value > 0 - max 1.0); FOOD_PERCENTAGE (value > 0 - max 1.0); MONEY_LOST_PERCENTAGE (value > 0 - max 1.0); ITEMS_DROPPED_PERCENTAGE (value > 0 - max 1.0); ITEMS_DROPPED_CHANCE_PERCENTAGE (value > 0 - max 1.0); ITEMS_DESTROYED_PERCENTAGE (value > 0 - max 1.0); ITEMS_DESTROYED_CHANCE_PERCENTAGE (value > 0 - max 1.0);");
		// edit all worlds
		else if (args[0].equalsIgnoreCase("all")) for (World world : sender.getServer().getWorlds()) editValue(sender, this.plugin.getDeathPenaltiesWorld(world.getName()), world.getName(), option, args[2]);
		// specific world does not exist
		else if (senderWorld == null) sender.sendMessage(ChatColor.RED + "The world named \"" + args[0] + "\" does not exist");
		// apply editing
		else editValue(sender, this.plugin.getDeathPenaltiesWorld(senderWorld.getName()), senderWorld.getName(), option, args[2]);
		return true;
	}

	private void editValue (CommandSender sender, DeathPenaltiesWorld world, String worldName, DeathPenaltiesOption option, String value)
	{
		if (option.equals(DeathPenaltiesOption.ENABLED))
		{
			boolean enabled = Boolean.parseBoolean(value.toLowerCase());
			world.setEnabled(enabled);
			this.plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, enabled);
		}
		else if (option.equals(DeathPenaltiesOption.HEALTH_FLAT))
		{
			double healthFlat = Double.parseDouble(value);
			world.setRespawnHealthFlat(healthFlat);
			this.plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, healthFlat);
		}
		else if (option.equals(DeathPenaltiesOption.FOOD_FLAT))
		{
			int foodFlat = (int) Double.parseDouble(value);
			world.setRespawnFoodFlat(foodFlat);
			// make sure that config writes an integer
			value = String.valueOf(foodFlat);
			this.plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, foodFlat);
		}
		else if (option.equals(DeathPenaltiesOption.MONEY_LOST_FLAT))
		{
			double moneyLostFlat = Double.parseDouble(value);
			world.setDeathMoneyLostFlat(moneyLostFlat);
			this.plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, moneyLostFlat);
		}
		else if (option.equals(DeathPenaltiesOption.ITEMS_DROPPED_FLAT))
		{
			int itemsDroppedFlat = (int) Double.parseDouble(value);
			world.setDeathItemsDroppedFlat(itemsDroppedFlat);
			// make sure that config writes an integer
			value = String.valueOf(itemsDroppedFlat);
			this.plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, itemsDroppedFlat);
		}
		else if (option.equals(DeathPenaltiesOption.ITEMS_DESTROYED_FLAT))
		{
			int itemsDestroyedFlat = (int) Double.parseDouble(value);
			world.setDeathItemsDestroyedFlat(itemsDestroyedFlat);
			// make sure that config writes an integer
			value = String.valueOf(itemsDestroyedFlat);
			this.plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, itemsDestroyedFlat);
		}
		else if (option.equals(DeathPenaltiesOption.HEALTH_PERCENTAGE))
		{
			double healthPercentage = Double.parseDouble(value);
			world.setRespawnHealthPercentage(healthPercentage);
			this.plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, healthPercentage);
			// remove flat value to use percentage
			world.setRespawnHealthFlat(0.0);
			this.plugin.getDeathPenaltiesConfig().setWorldValue(worldName, DeathPenaltiesOption.HEALTH_FLAT, 0.0);
		}
		else if (option.equals(DeathPenaltiesOption.FOOD_PERCENTAGE))
		{
			double foodPercentage = Double.parseDouble(value);
			world.setRespawnFoodPercentage(foodPercentage);
			this.plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, foodPercentage);
			// remove flat value to use percentage
			world.setRespawnFoodFlat(0);
			this.plugin.getDeathPenaltiesConfig().setWorldValue(worldName, DeathPenaltiesOption.FOOD_FLAT, 0);
		}
		else if (option.equals(DeathPenaltiesOption.MONEY_LOST_PERCENTAGE))
		{
			double moneyLostPercentage = Double.parseDouble(value);
			world.setDeathMoneyLostPercentage(moneyLostPercentage);
			this.plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, moneyLostPercentage);
			// remove flat value to use percentage
			world.setDeathMoneyLostFlat(0.0);
			this.plugin.getDeathPenaltiesConfig().setWorldValue(worldName, DeathPenaltiesOption.MONEY_LOST_FLAT, 0.0);
		}
		else if (option.equals(DeathPenaltiesOption.ITEMS_DROPPED_PERCENTAGE))
		{
			double itemsDroppedPercentage = Double.parseDouble(value);
			world.setDeathItemsDroppedPercentage(itemsDroppedPercentage);
			this.plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, itemsDroppedPercentage);
			// remove flat value to use percentage
			world.setDeathItemsDroppedFlat(0);
			this.plugin.getDeathPenaltiesConfig().setWorldValue(worldName, DeathPenaltiesOption.ITEMS_DROPPED_FLAT, 0);
		}
		else if (option.equals(DeathPenaltiesOption.ITEMS_DROPPED_CHANCE_PERCENTAGE))
		{
			double itemsDroppedChancePercentage = Double.parseDouble(value);
			world.setDeathItemsDroppedChancePercentage(itemsDroppedChancePercentage);
			this.plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, itemsDroppedChancePercentage);
		}
		else if (option.equals(DeathPenaltiesOption.ITEMS_DESTROYED_PERCENTAGE))
		{
			double itemsDestroyedPercentage = Double.parseDouble(value);
			world.setDeathItemsDestroyedPercentage(itemsDestroyedPercentage);
			this.plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, itemsDestroyedPercentage);
			// remove flat value to use percentage
			world.setDeathItemsDestroyedFlat(0);
			this.plugin.getDeathPenaltiesConfig().setWorldValue(worldName, DeathPenaltiesOption.ITEMS_DESTROYED_FLAT, 0);
		}
		else if (option.equals(DeathPenaltiesOption.ITEMS_DESTROYED_CHANCE_PERCENTAGE))
		{
			double itemsDestroyedChancePercentage = Double.parseDouble(value);
			world.setDeathItemsDestroyedChancePercentage(itemsDestroyedChancePercentage);
			this.plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, itemsDestroyedChancePercentage);
		}
		sender.sendMessage(ChatColor.GREEN + "Option " + ChatColor.DARK_GREEN + option.toString() + ChatColor.GREEN + " has been set to " + ChatColor.YELLOW + value + ChatColor.GREEN + " in world: " + ChatColor.YELLOW + worldName);
	}

}
