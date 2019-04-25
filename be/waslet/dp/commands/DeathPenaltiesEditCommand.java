package be.waslet.dp.commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.potion.PotionEffect;

import be.waslet.dp.main.DeathPenalties;
import be.waslet.dp.main.DeathPenaltiesOption;
import be.waslet.dp.main.DeathPenaltiesParserException;
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
		if (option == null) sender.sendMessage(ChatColor.RED + "The option \"" + args[1] + "\" does not exist.\nAvaiblable options: ENABLED (true/false); HEALTH_FLAT (value > 0); FOOD_FLAT (integer > 0); MONEY_LOST_FLAT (value > 0); ITEMS_LOST_FLAT (integer > 0); HEALTH_PERCENTAGE (value > 0 - max 1.0); FOOD_PERCENTAGE (value > 0 - max 1.0); MONEY_LOST_PERCENTAGE (value > 0 - max 1.0); ITEMS_LOST_PERCENTAGE (value > 0 - max 1.0); EFFECTS (see config format, each one seperated by \";\");");
		// invalid option value
		else if (!option.isValid(args[2])) sender.sendMessage(ChatColor.RED + "The option value \"" + args[2] + "\" is not valid for option \"" + args[1] + "\".\\nAvaiblable options: ENABLED (true/false); HEALTH_FLAT (value > 0); FOOD_FLAT (integer > 0); MONEY_LOST_FLAT (value > 0); ITEMS_LOST_FLAT (integer > 0); HEALTH_PERCENTAGE (value > 0 - max 1.0); FOOD_PERCENTAGE (value > 0 - max 1.0); MONEY_LOST_PERCENTAGE (value > 0 - max 1.0); ITEMS_LOST_PERCENTAGE (value > 0 - max 1.0); EFFECTS (see config format, each one seperated by \";\");");
		// edit all worlds
		else if (args[0].equalsIgnoreCase("all")) for (World world : sender.getServer().getWorlds()) editValue(sender, plugin.getDeathPenaltiesWorld(world.getName()), world.getName(), option, args[2]);
		// specific world does not exist
		else if (senderWorld == null) sender.sendMessage(ChatColor.RED + "The world named \"" + args[0] + "\" does not exist");
		// apply editing
		else editValue(sender, plugin.getDeathPenaltiesWorld(senderWorld.getName()), senderWorld.getName(), option, args[2]);
		return true;
	}

	private void editValue (CommandSender sender, DeathPenaltiesWorld world, String worldName, DeathPenaltiesOption option, String value)
	{
		if (option.equals(DeathPenaltiesOption.ENABLED))
		{
			boolean enabled = Boolean.parseBoolean(value.toLowerCase());
			world.setEnabled(worldName, enabled);
			plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, enabled);
		}
		else if (option.equals(DeathPenaltiesOption.HEALTH_FLAT))
		{
			double healthFlat = Double.parseDouble(value);
			world.setRespawnHealthFlat(healthFlat);
			plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, healthFlat);
		}
		else if (option.equals(DeathPenaltiesOption.FOOD_FLAT))
		{
			int foodFlat = (int) Double.parseDouble(value);
			world.setRespawnFoodFlat(foodFlat);
			// make sure that config writes an integer
			value = String.valueOf(foodFlat);
			plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, foodFlat);
		}
		else if (option.equals(DeathPenaltiesOption.MONEY_LOST_FLAT))
		{
			double moneyLostFlat = Double.parseDouble(value);
			world.setRespawnMoneyLostFlat(moneyLostFlat);
			plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, moneyLostFlat);
		}
		else if (option.equals(DeathPenaltiesOption.ITEMS_LOST_FLAT))
		{
			int itemsLostFlat = (int) Double.parseDouble(value);
			world.setRespawnItemsLostFlat(itemsLostFlat);
			// make sure that config writes an integer
			value = String.valueOf(itemsLostFlat);
			plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, itemsLostFlat);
		}
		else if (option.equals(DeathPenaltiesOption.HEALTH_PERCENTAGE))
		{
			double healthPercentage = Double.parseDouble(value);
			world.setRespawnHealthPercentage(healthPercentage);
			plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, healthPercentage);
			// remove flat value to use percentage
			world.setRespawnHealthFlat(0.0);
			plugin.getDeathPenaltiesConfig().setWorldValue(worldName, DeathPenaltiesOption.HEALTH_FLAT, 0.0);
		}
		else if (option.equals(DeathPenaltiesOption.FOOD_PERCENTAGE))
		{
			double foodPercentage = Double.parseDouble(value);
			world.setRespawnFoodPercentage(foodPercentage);
			plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, foodPercentage);
			// remove flat value to use percentage
			world.setRespawnFoodFlat(0);
			plugin.getDeathPenaltiesConfig().setWorldValue(worldName, DeathPenaltiesOption.FOOD_FLAT, 0);
		}
		else if (option.equals(DeathPenaltiesOption.MONEY_LOST_PERCENTAGE))
		{
			double moneyLostPercentage = Double.parseDouble(value);
			world.setRespawnMoneyLostPercentage(moneyLostPercentage);
			plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, moneyLostPercentage);
			// remove flat value to use percentage
			world.setRespawnMoneyLostFlat(0.0);
			plugin.getDeathPenaltiesConfig().setWorldValue(worldName, DeathPenaltiesOption.MONEY_LOST_FLAT, 0.0);
		}
		else if (option.equals(DeathPenaltiesOption.ITEMS_LOST_PERCENTAGE))
		{
			double itemsLostPercentage = Double.parseDouble(value);
			world.setRespawnItemsLostPercentage(itemsLostPercentage);
			plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, itemsLostPercentage);
			// remove flat value to use percentage
			world.setRespawnItemsLostFlat(0);
			plugin.getDeathPenaltiesConfig().setWorldValue(worldName, DeathPenaltiesOption.ITEMS_LOST_FLAT, 0);
		}
		else if (option.equals(DeathPenaltiesOption.EFFECTS))
		{
			PotionEffect[] potionEffects = null;
			try
			{
				potionEffects = plugin.getParser().getParsedPotionsEffects(value.split(";"), plugin.getDeathPenaltiesConfig().getPotionEffectsInSeconds(), plugin.getDeathPenaltiesConfig().getPotionEffectsTrueLevel());
			}
			catch (DeathPenaltiesParserException exc)
			{
				sender.sendMessage("Potion value format is not valid please see format in config (each effect must be separated by ;)");
				return;
			}
			if (potionEffects != null)
			{
				world.setRespawnEffects(potionEffects);
				plugin.getDeathPenaltiesConfig().setWorldValue(worldName, option, value.split(";"));
			}
		}
		sender.sendMessage(ChatColor.GREEN + "Option " + ChatColor.DARK_GREEN + option.toString() + ChatColor.GREEN + " has been set to " + ChatColor.YELLOW + value + ChatColor.GREEN + " in world: " + ChatColor.YELLOW + worldName);
	}

}
