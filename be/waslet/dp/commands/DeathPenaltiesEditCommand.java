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
		if (sender.hasPermission("dp.commands.edit"))
		{
			// wrong command usage
			if (args.length < 3) return false;
			World senderWorld = sender.getServer().getWorld(args[0]);
			DeathPenaltiesOption option = DeathPenaltiesOption.getOption(args[1]);
			// option does not exist
			if (option == null)
			{
				sender.sendMessage(ChatColor.RED + "The option \"" + args[1] + "\" does not exist.\nAvaiblable options: ENABLED (true/false); HEALTH_FLAT (value > 0); FOOD_FLAT (integer > 0); HEALTH_PERCENTAGE (value > 0 - max 1.0); FOOD_PERCENTAGE (value > 0 - max 1.0);");
			}
			// invalid option value
			else if (!option.isValid(args[2]))
			{
				sender.sendMessage(ChatColor.RED + "The option value \"" + args[2] + "\" is not valid for option \"" + args[1] + "\".\\nAvaiblable options: ENABLED (true/false); HEALTH_FLAT (value > 0); FOOD_FLAT (integer > 0); HEALTH_PERCENTAGE (value > 0 - max 1.0); FOOD_PERCENTAGE (value > 0 - max 1.0);");
			}
			// edit all worlds
			else if (args[0].equalsIgnoreCase("all"))
			{
				for (World world : sender.getServer().getWorlds())
				{
					editValue(sender, DeathPenalties.getDeathPenaltiesWorld(world.getName()), world.getName(), option, args[2]);
				}
			}
			// specific world does not exist
			else if (senderWorld == null)
			{
				sender.sendMessage(ChatColor.RED + "The world named \"" + args[0] + "\" does not exist");
			}
			// apply editing
			else
			{
				editValue(sender, DeathPenalties.getDeathPenaltiesWorld(senderWorld.getName()), senderWorld.getName(), option, args[2]);
			}
		}
		return true;
	}

	private void editValue (CommandSender sender, DeathPenaltiesWorld world, String worldName, DeathPenaltiesOption option, String value)
	{
		if (option.equals(DeathPenaltiesOption.ENABLED))
		{
			boolean enabled = Boolean.parseBoolean(value);
			world.setEnabled(worldName, enabled);
		}
		else if (option.equals(DeathPenaltiesOption.HEALTH_FLAT))
		{
			double healthFlat = Double.parseDouble(value);
			world.setRespawnHealthFlat(healthFlat);
		}
		else if (option.equals(DeathPenaltiesOption.FOOD_FLAT))
		{
			int foodFlat = (int) Double.parseDouble(value);
			world.setRespawnFoodFlat(foodFlat);
			// make sure that config writes an integer
			value = String.valueOf(foodFlat);
		}
		else if (option.equals(DeathPenaltiesOption.HEALTH_PERCENTAGE))
		{
			double healthPercentage = Double.parseDouble(value);
			world.setRespawnHealthPercentage(healthPercentage);
			// remove flat value to use percentage
			world.setRespawnHealthFlat(0.0);
			plugin.getConfig().set(worldName + "." + DeathPenaltiesOption.HEALTH_FLAT.getConfigPath(), value);
		}
		else if (option.equals(DeathPenaltiesOption.FOOD_PERCENTAGE))
		{
			double foodPercentage = Double.parseDouble(value);
			world.setRespawnFoodPercentage(foodPercentage);
			// remove flat value to use percentage
			world.setRespawnFoodFlat(0);
			plugin.getConfig().set(worldName + "." + DeathPenaltiesOption.FOOD_FLAT.getConfigPath(), value);
		}
		plugin.getConfig().set(worldName + "." + option.getConfigPath(), value);
		sender.sendMessage(ChatColor.GREEN + "Option " + ChatColor.DARK_GREEN + option.name() + ChatColor.GREEN + " has been set to " + ChatColor.YELLOW + value + ChatColor.GREEN + " in world: " + ChatColor.YELLOW + worldName);
		plugin.saveConfig();
	}

}
